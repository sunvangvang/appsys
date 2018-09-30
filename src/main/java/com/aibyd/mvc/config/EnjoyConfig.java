package com.aibyd.mvc.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.jfinal.template.source.ClassPathSourceFactory;

@Configuration
public class EnjoyConfig {

    @Bean(name = "enjoyViewResolver")
    public EnjoyViewResolver getJFinalViewResolver() {
        EnjoyViewResolver jf = new EnjoyViewResolver();
        jf.setDevMode(true);

        jf.setSourceFactory(new ClassPathSourceFactory());
        jf.setPrefix("/templates/");
        jf.setSuffix(".html");
        jf.setContentType("text/html;charset=UTF-8");
        jf.setOrder(0);
        return jf;
    }

}