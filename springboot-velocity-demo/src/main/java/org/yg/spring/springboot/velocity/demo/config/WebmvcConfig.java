package org.yg.spring.springboot.velocity.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.ui.velocity.SpringResourceLoader;
import org.springframework.web.servlet.config.annotation.ViewResolverRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.view.velocity.VelocityConfigurer;
import org.springframework.web.servlet.view.velocity.VelocityLayoutViewResolver;

import java.util.Properties;

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

        velocityConfigurer.setPreferFileSystemAccess(false);

        // 自定义 velocity 属性
        Properties properties = new Properties();
        // 关闭 velocity 层面的缓存（注意线上开启）
        properties.setProperty(SpringResourceLoader.SPRING_RESOURCE_LOADER_CACHE, "false");
        velocityConfigurer.setVelocityProperties(properties);

        return velocityConfigurer;
    }

    @Override
    public void configureViewResolvers(ViewResolverRegistry registry) {
        // 创建 velocity 视图解析器
        VelocityLayoutViewResolver velocityLayoutViewResolver = new VelocityLayoutViewResolver();
        // 模板文件后缀
        velocityLayoutViewResolver.setSuffix(".vm");
        // 模板文件前缀
        velocityLayoutViewResolver.setPrefix("");
        // 布局模板文件的 key
        velocityLayoutViewResolver.setLayoutKey("layout");
        // 关闭 spring 层的视图缓存（注意线上开启）
        velocityLayoutViewResolver.setCache(false);
        // 默认布局模板文件
//        velocityLayoutViewResolver.setLayoutUrl("layout.vm");
        // 工具集合配置
        velocityLayoutViewResolver.setToolboxConfigLocation("toolbox.xml");
        // 设置 Content-Type
        velocityLayoutViewResolver.setContentType("text/html;charset=UTF-8");

        // 注册 velocity 视图解析器
        registry.viewResolver(velocityLayoutViewResolver);
    }
}
