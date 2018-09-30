package com.aibyd.mvc.config;

import com.jfinal.template.ext.spring.JFinalViewResolver;

import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

public class EnjoyViewResolver extends JFinalViewResolver implements ApplicationListener<ContextRefreshedEvent> {

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        getEngine().addSharedObject("ctx", getServletContext().getContextPath());
    }
    
}