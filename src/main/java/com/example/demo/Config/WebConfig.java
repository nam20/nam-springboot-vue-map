package com.example.demo.Config;

import com.example.demo.Config.interceptor.JwtAuthInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;


@Configuration
public class WebConfig implements WebMvcConfigurer {


    private JwtAuthInterceptor jwtAuthInterceptor;

    public WebConfig(JwtAuthInterceptor jwtAuthInterceptor) {
        this.jwtAuthInterceptor = jwtAuthInterceptor;
    }


    @Override
    public void addInterceptors(InterceptorRegistry registry) {
            registry.addInterceptor(jwtAuthInterceptor)
                    .addPathPatterns("/user/auth","/comment","/board","/board/**","/user/profile");
    }






}
