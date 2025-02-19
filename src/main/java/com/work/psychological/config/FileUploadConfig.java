package com.work.psychological.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Arrays;

@Data
@Configuration
@ConfigurationProperties(prefix = "file.upload")
public class FileUploadConfig {
    
    private String avatarPath;
    private String allowedTypes;
    private Long maxSize;
    
    public List<String> getAllowedTypesList() {
        return Arrays.asList(allowedTypes.split(","));
    }
} 