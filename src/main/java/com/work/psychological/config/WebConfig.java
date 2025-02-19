package com.work.psychological.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.File;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private final FileUploadConfig fileUploadConfig;
    private static final Logger log = LoggerFactory.getLogger(WebConfig.class);

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 获取项目根目录的绝对路径
        String projectRoot = System.getProperty("user.dir");
        String uploadPath = projectRoot + File.separator + fileUploadConfig.getAvatarPath();
        log.info("配置静态资源路径: {}", uploadPath);

        // 确保上传目录存在
        File uploadDir = new File(uploadPath);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
            log.info("创建上传目录: {}", uploadPath);
        }

        // 配置静态资源映射，注意这里不需要/api前缀，因为在application.yml中已经配置了context-path
        registry.addResourceHandler("/uploads/avatars/**")
                .addResourceLocations("file:" + uploadPath + File.separator)
                .setCachePeriod(3600);

        log.info("静态资源映射配置完成: pattern=/uploads/avatars/**, location=file:{}", uploadPath);
    }
} 