package com.lyears.projects.bookstore.config;

import com.lyears.projects.bookstore.interceptor.ModifyInterceptor;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.web.servlet.ErrorPage;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.Formatter;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

/**
 * @author fzm
 * @date 2018/9/28
 **/
@Configuration
public class ConfigurerAdapter extends WebMvcConfigurerAdapter {

    /*@Bean
    public HandlerInterceptor getAdminInterceptor() {
        return new AdminInterceptor();
    }*/

    @Bean
    public HandlerInterceptor getModifyInterceptor() {
        return new ModifyInterceptor();
    }

    @Bean
    public EmbeddedServletContainerCustomizer containerCustomizer() {
        return container -> container.addErrorPages(new ErrorPage(HttpStatus.NOT_FOUND, "/404"));
    }

    @Bean
    public Formatter<LocalDate> localDateFormatter() {
        return new Formatter<LocalDate>() {
            @Override
            public LocalDate parse(String text, Locale locale) {
                return LocalDate.parse(text, DateTimeFormatter.ISO_LOCAL_DATE);
            }

            @Override
            public String print(LocalDate object, Locale locale) {
                return DateTimeFormatter.ISO_LOCAL_DATE.format(object);
            }
        };
    }

    @Bean
    public Formatter<LocalDateTime> localDateTimeFormatter() {
        return new Formatter<LocalDateTime>() {
            @Override
            public LocalDateTime parse(String text, Locale locale) {
                return LocalDateTime.parse(text, DateTimeFormatter.ISO_LOCAL_DATE);
            }

            @Override
            public String print(LocalDateTime object, Locale locale) {
                return DateTimeFormatter.ISO_LOCAL_DATE.format(object);
            }
        };
    }


    /**
     * 添加拦截器
     *
     * @param registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //registry.addInterceptor(getAdminInterceptor()).addPathPatterns("/admin", "/admin.*");
        registry.addInterceptor(getModifyInterceptor()).addPathPatterns("/**").excludePathPatterns("/logout", "/login/**");
        super.addInterceptors(registry);
    }

    @Override
    public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
        configurer.favorPathExtension(false);
    }
}
