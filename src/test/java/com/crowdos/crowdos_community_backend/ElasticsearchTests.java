package com.crowdos.crowdos_community_backend;

import com.crowdos.crowdos_community_backend.mapper.BmsTopicMapper;
import com.crowdos.crowdos_community_backend.mapper.elasticsearch.DiscussPostRepository;
import com.crowdos.crowdos_community_backend.model.entity.BmsPost;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class ElasticsearchTests {

    @Autowired
    private BmsTopicMapper bmsTopicMapper;
    @Autowired
    private DiscussPostRepository discussPostRepository;
    @Autowired
    private ElasticsearchRestTemplate elasticsearchTemplate;
    @Autowired
    private ElasticsearchOperations elasticsearchOperations;

    //往ES服务器中添加数据
    @Test
    public void testInsert(){
        discussPostRepository.save(bmsTopicMapper.selectById("1869317597376409601"));
    }

    @Test
    public void testInsertList(){
        discussPostRepository.saveAll(bmsTopicMapper.selectList(null));
    }

    @Test
    public void testUpdate(){
        BmsPost post = bmsTopicMapper.selectById("1869317597376409601");
        post.setContent("更改一下，检查ES服务器是否更改");
        discussPostRepository.save(post);
    }

    @Test
    public void testDelete(){
        discussPostRepository.deleteById("1869317597376409601");

    }

    @Test
    public void testSearchByRepository(){
        NativeSearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(
                        QueryBuilders.boolQuery()
                                .must(QueryBuilders.matchQuery("content", "ES"))//content中必须包含“ES”
                                .filter(QueryBuilders.termQuery("top", false))//filter中子句必须满足，但是不影响相关性得分
                                .should(QueryBuilders.matchQuery("title","社区"))
                                .minimumShouldMatch(1)//至少满足一个，match条件
                )
                .withSort(SortBuilders.scoreSort())
                .withPageable(PageRequest.of(0,10))
                .withHighlightFields(
                        new HighlightBuilder.Field("title").preTags("<em>").postTags("</em>"),
                        new HighlightBuilder.Field("content").preTags("<em>").postTags("</em>")
                )
                .build();

        // elasticsearch底层获取到了高亮显示的值，但是没有返回,因此需要自己实现返回高亮标签的数据
        Page<BmsPost> page = searchPosts(searchQuery, PageRequest.of(0, 10));
        System.out.println(page.getTotalElements());
        System.out.println(page.getTotalPages());
        System.out.println(page.getContent());
        System.out.println(page.getSize());
    }

    public Page<BmsPost> searchPosts(NativeSearchQuery searchQuery, Pageable pageable) {
        //添加分页参数到查询
        searchQuery.setPageable(pageable);

        //执行查询（返回SearchHits）
        SearchHits<BmsPost> searchHits = elasticsearchOperations.search(
                searchQuery,
                BmsPost.class
        );

        //将SearchHits转换为List<BmsPost>
        List<BmsPost> posts = searchHits.stream()
                .map(hit -> {
                    BmsPost post = hit.getContent();
                    Map<String,List<String>> highlightFields = hit.getHighlightFields();

                    //处理标题高亮
                    if(highlightFields.containsKey("title")){
                        String highlightTttle = highlightFields.get("title").get(0);
                        post.setTitle(highlightTttle);
                    }

                    //处理高亮内容
                    if(highlightFields.containsKey("content")){
                        String highlightContent = highlightFields.get("content").get(0);
                        post.setContent(highlightContent);
                    }

                    return post;
                })
                .collect(Collectors.toList());

        //手动构建Page对象
        return new PageImpl<>(posts, pageable, searchHits.getTotalHits());
    }

    @Test
    public void testSearchByTemplate() {
        NativeSearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(
                        QueryBuilders.boolQuery()
                                .must(QueryBuilders.matchQuery("content", "ES"))//content中必须包含“ES”
                                .filter(QueryBuilders.termQuery("top", false))//filter中子句必须满足，但是不影响相关性得分
                                .should(QueryBuilders.matchQuery("title","社区"))
                                .minimumShouldMatch(1)//至少满足一个，match条件
                )
                .withSort(SortBuilders.scoreSort())
                .withPageable(PageRequest.of(0,10))
                .withHighlightFields(
                        new HighlightBuilder.Field("title").preTags("<em>").postTags("</em>"),
                        new HighlightBuilder.Field("content").preTags("<em>").postTags("</em>")
                )
                .build();

    }






}
