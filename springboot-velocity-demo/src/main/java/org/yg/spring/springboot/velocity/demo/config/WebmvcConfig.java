package org.yg.spring.springboot.velocity.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewResolverRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.view.velocity.VelocityConfigurer;
import org.springframework.web.servlet.view.velocity.VelocityLayoutViewResolver;

/**
 * @author 小天
 * @date 2020/6/14 14:32
 */
@Configuration
public class WebmvcConfig implements WebMvcConfigurer {

    @Bean
    public VelocityConfigurer velocityConfigurer() {
        VelocityConfigurer velocityConfigurer = new VelocityConfigurer();
        // 模板加载路径
        velocityConfigurer.setResourceLoaderPath("classpath:templates");
        return velocityConfigurer;
    }

    @Override
    public void configureViewResolvers(ViewResolverRegistry registry) {
        VelocityLayoutViewResolver velocityLayoutViewResolver = new VelocityLayoutViewResolver();
        // 模板文件后缀
        velocityLayoutViewResolver.setSuffix(".vm");
        // 模板文件前缀
        velocityLayoutViewResolver.setPrefix("");
        // 布局模板文件的 key
        velocityLayoutViewResolver.setLayoutKey("layout");
        // 默认布局模板文件
        velocityLayoutViewResolver.setLayoutUrl("layout.vm");
        // 工具集合配置
        velocityLayoutViewResolver.setToolboxConfigLocation("toolbox.xml");
        velocityLayoutViewResolver.setContentType("text/html;charset=UTF-8");
        registry.viewResolver(velocityLayoutViewResolver);
    }
}
