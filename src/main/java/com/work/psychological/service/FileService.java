package com.work.psychological.service;

import org.springframework.web.multipart.MultipartFile;

public interface FileService {
    
    /**
     * 上传头像
     * @param file 头像文件
     * @return 头像访问URL
     */
    String uploadAvatar(MultipartFile file);
} 