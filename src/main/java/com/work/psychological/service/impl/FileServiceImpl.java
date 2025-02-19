package com.work.psychological.service.impl;

import com.work.psychological.common.exception.BusinessException;
import com.work.psychological.config.FileUploadConfig;
import com.work.psychological.service.FileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {

    private final FileUploadConfig fileUploadConfig;

    @Override
    public String uploadAvatar(MultipartFile file) {
        // 检查文件是否为空
        if (file.isEmpty()) {
            throw new BusinessException("请选择要上传的文件");
        }

        // 检查文件大小
        if (file.getSize() > fileUploadConfig.getMaxSize()) {
            throw new BusinessException("文件大小超过限制");
        }

        // 检查文件类型
        String contentType = file.getContentType();
        if (!fileUploadConfig.getAllowedTypesList().contains(contentType)) {
            throw new BusinessException("不支持的文件类型");
        }

        try {
            // 获取项目根目录
            String projectRoot = System.getProperty("user.dir");
            // 创建保存目录
            File uploadDir = new File(projectRoot, fileUploadConfig.getAvatarPath());
            if (!uploadDir.exists()) {
                if (!uploadDir.mkdirs()) {
                    throw new BusinessException("创建上传目录失败");
                }
                log.info("创建上传目录: {}", uploadDir.getAbsolutePath());
            }

            // 生成文件名
            String originalFilename = file.getOriginalFilename();
            String extension = StringUtils.getFilenameExtension(originalFilename);
            String newFilename = UUID.randomUUID().toString() + "." + extension;

            // 保存文件
            File destFile = new File(uploadDir, newFilename);
            file.transferTo(destFile);
            log.info("文件已保存: {}", destFile.getAbsolutePath());

            // 返回文件访问路径，不需要/api前缀
            return "/uploads/avatars/" + newFilename;

        } catch (IOException e) {
            log.error("文件上传失败", e);
            throw new BusinessException("文件上传失败");
        }
    }
}