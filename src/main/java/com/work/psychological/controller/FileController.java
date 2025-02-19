package com.work.psychological.controller;

import com.work.psychological.common.api.ApiResult;
import com.work.psychological.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/files")
@RequiredArgsConstructor
public class FileController {

    private final FileService fileService;

    @PostMapping("/avatar")
    public ApiResult<String> uploadAvatar(@RequestParam("file") MultipartFile file) {
        String avatarUrl = fileService.uploadAvatar(file);
        return ApiResult.success(avatarUrl);
    }
} 