package com.itheima.controller;

import com.itheima.pojo.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping
public class UploadController {

    @Value("${tlias.upload.dir}")
    private String uploadDir;

    @Value("${tlias.upload.base-url}")
    private String baseUrl;

    /**
     * 文件上传，返回可访问的图片路径（相对路径，前端拼接 API 前缀）
     */
    @PostMapping("/upload")
    public Result upload(MultipartFile file) throws IOException {
        if (file == null || file.isEmpty()) {
            return Result.error("上传文件为空");
        }
        File dir = new File(uploadDir);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        String originalFilename = file.getOriginalFilename();
        String suffix = (originalFilename != null && originalFilename.contains("."))
                ? originalFilename.substring(originalFilename.lastIndexOf(".")) : "";
        String newName = UUID.randomUUID().toString().replace("-", "") + suffix;

        file.transferTo(new File(dir, newName));
        log.info("文件上传成功: {}", newName);
        return Result.success(baseUrl + newName);
    }
}
