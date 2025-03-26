package com.crowdos.crowdos_community_backend.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.Date;


@Document(indexName = "discusspost",shards = 6,replicas = 3)
@Data
@Builder
@Accessors(chain = true)
@TableName("bms_post")
@NoArgsConstructor
@AllArgsConstructor
public class BmsPost implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 主键
     */
    @Id
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;
    /**
     * 帖子标题
     */
    //互联网校招 -> 大量拆分成词汇 ->
    //analyzer表示拆分词表
    @Field(type = FieldType.Text, analyzer = "ik_max_word", searchAnalyzer = "ik_smart")
    @NotEmpty(message = "标题不仅可以为空")
    @TableField("title")
    private String title;
    /**
     * 帖子内容
     */
    @Field(type = FieldType.Text, analyzer = "ik_max_word", searchAnalyzer = "ik_smart")
    @NotEmpty(message = "内容不可以为空")
    @TableField("content")
    private String content;
    /**
     * 作者id
     */
    @Field(type = FieldType.Text)
    @TableField("user_id")
    private String userId;
    /**
     * 评论数
     */
    @Field(type = FieldType.Integer)
    @TableField("comments")
    @Builder.Default
    private Integer comments = 0;
    /**
     * 收藏数
     */
    @Field(type = FieldType.Integer)
    @TableField("collects")
    @Builder.Default
    private Integer collects = 0;
    /**
     * 浏览数
     */
    @Field(type = FieldType.Integer)
    @TableField("view")
    @Builder.Default
    private Integer view = 0;
    /**
     * 是否置顶，默认不置顶
     */
    @Field(type = FieldType.Boolean)
    @TableField("top")
    @Builder.Default
    private Boolean top = false;
    /**
     * 专栏ID，默认不分栏
     */
    @Field(type = FieldType.Integer)
    @TableField("section_id")
    @Builder.Default
    private Integer sectionId = 0;
    /**
     * 加精
     */
    @Field(type = FieldType.Boolean)
    @TableField("essence")
    @Builder.Default
    private Boolean essence = false;
    /**
     * 创建时间
     */
    @Field(type = FieldType.Date, format = DateFormat.date)
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private Date createTime;
    /**
     * 修改时间
     */
    @Field(type = FieldType.Date, format = DateFormat.date)
    @TableField(value = "modify_time", fill = FieldFill.UPDATE)
    private Date modifyTime;
}