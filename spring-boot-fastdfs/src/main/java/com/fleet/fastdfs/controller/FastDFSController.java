package com.fleet.fastdfs.controller;

import com.fleet.fastdfs.util.FastDFSUtil;
import org.apache.commons.io.IOUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;

@RestController
@RequestMapping("/fdfs")
public class FastDFSController {

    @Resource
    private FastDFSUtil fastDFSUtil;

    /**
     * 上传文件
     */
    @RequestMapping("/uploadFile")
    public String uploadFile(MultipartFile file) throws IOException {
        return fastDFSUtil.uploadFile(file);
    }

    /**
     * 上传图片文件
     */
    @RequestMapping("/uploadImageFile")
    public String uploadImageFile(MultipartFile file) throws IOException {
        return fastDFSUtil.uploadImageFile(file);
    }

    /**
     * 下载文件
     */
    @RequestMapping("/downloadFile")
    public void downloadFile(String filePath, HttpServletResponse response) throws Exception {
        byte[] data = fastDFSUtil.downloadFile(filePath);

        response.setCharacterEncoding("UTF-8");
        response.setContentType("image/jpeg;charset=utf-8");
        response.setHeader("Content-disposition", "attachment;filename=\"" + URLEncoder.encode("test.jpg", "UTF-8") + "\"");

        OutputStream out = response.getOutputStream();
        IOUtils.write(data, out);
    }
}