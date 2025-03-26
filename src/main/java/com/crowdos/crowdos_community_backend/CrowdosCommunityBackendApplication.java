package com.crowdos.crowdos_community_backend;

import com.crowdos.crowdos_community_backend.jwt.JwtAuthenticationFilter;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;

import javax.annotation.PostConstruct;

@MapperScan("com.crowdos.crowdos_community_backend.mapper")
@SpringBootApplication
public class CrowdosCommunityBackendApplication extends SpringBootServletInitializer {

    @PostConstruct
    public void init() {
        //解决netty启动冲突问题
        //Netty4Utils.setAvailableProcessors()
        System.setProperty("es.set.netty.runtime.available.processors", "false");
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(CrowdosCommunityBackendApplication.class);
    }

    @Bean
    public FilterRegistrationBean jwtFilter() {
        final FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
        JwtAuthenticationFilter filter = new JwtAuthenticationFilter();
        filterRegistrationBean.setFilter(filter);
        return filterRegistrationBean;
    }



    public static void main(String[] args) {
        SpringApplication.run(CrowdosCommunityBackendApplication.class, args);
    }

}
