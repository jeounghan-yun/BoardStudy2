package com.example.boardstudy2.Utils;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

//HandlerMethodArgumentResolver클래스를 쓰기 위한 클래스

@Configuration
public class Webconfig implements WebMvcConfigurer {

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers){
        resolvers.add(new CommandMapArgumentResolver());
    }
}
