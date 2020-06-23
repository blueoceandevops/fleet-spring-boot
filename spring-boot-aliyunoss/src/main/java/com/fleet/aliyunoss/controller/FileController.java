package com.fleet.aliyunoss.controller;

import com.fleet.aliyunoss.config.AliyunOSSConfig;
import com.fleet.aliyunoss.util.AliyunOSSUtil;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/file")
public class FileController {

    @Resource
    private AliyunOSSConfig aliyunOSSConfig;

    @Resource
    private AliyunOSSUtil aliyunOSSUtil;

    @RequestMapping("/uploadFile")
    public void uploadFile(@RequestParam(value = "file") MultipartFile file) throws IOException {
        File dest = new File(Objects.requireNonNull(file.getOriginalFilename()));
        OutputStream os = new FileOutputStream(dest);
        os.write(file.getBytes());
        os.close();
        file.transferTo(dest);
        aliyunOSSUtil.upload(dest);
    }

    @GetMapping("/getObjectList")
    public List<String> getObjectList() {
        String bucketName = aliyunOSSConfig.getBucketName();
        System.out.println(bucketName);
        List<String> objectList = aliyunOSSUtil.getObjectList(bucketName);
        return objectList;
    }
}
