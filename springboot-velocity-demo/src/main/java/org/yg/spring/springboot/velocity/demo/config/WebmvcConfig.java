package org.yg.spring.springboot.velocity.demo.config;

import org.springframework.context.ApplicationContext;
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
    public VelocityConfigurer velocityConfigurer(ApplicationContext context) {
        VelocityConfigurer velocityConfigurer = new VelocityConfigurer();
        // 模板加载路径
        velocityConfigurer.setResourceLoaderPath("classpath:templates");
        // 设置文件系统加载器 "不" 优先（即 spring 的资源加载器优先）
        velocityConfigurer.setPreferFileSystemAccess(false);
        // 使用 spring 的资源加载器
        velocityConfigurer.setResourceLoader(context);
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
//        // 默认布局模板文件
//        velocityLayoutViewResolver.setLayoutUrl("layout.vm");
        // 工具集合配置
        velocityLayoutViewResolver.setToolboxConfigLocation("toolbox.xml");
        velocityLayoutViewResolver.setContentType("text/html;charset=UTF-8");
        registry.viewResolver(velocityLayoutViewResolver);
    }
}
