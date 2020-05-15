package com.fleet.file.controller;

import com.fleet.file.config.FileConfig;
import com.fleet.file.entity.MultipartFileParam;
import com.fleet.file.service.StorageService;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.io.File;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/bigFile")
public class BigFileController {

    private Logger logger = LoggerFactory.getLogger(BigFileController.class);

    @Resource
    private StorageService storageService;

    @Resource
    FileConfig fileConfig;

    /**
     * 秒传判断，断点判断
     */
    @RequestMapping(value = "checkFileMd5", method = RequestMethod.POST)
    public Map<Integer, Object> checkFileMd5(String md5) throws Exception {
        Map<Integer, Object> map = new HashMap<>();
        File md5Dir = new File(fileConfig.getBigFilePath() + md5);
        if (!md5Dir.exists()) {
            map.put(101, "文件不存在");
            return map;
        }
        // status文件记录文件上传进度
        File confFile = new File(fileConfig.getBigFilePath() + md5 + File.separatorChar + "status");
        byte[] completeList = FileUtils.readFileToByteArray(confFile);
        List<String> missChunkList = new LinkedList<>();
        for (int i = 0; i < completeList.length; i++) {
            if (completeList[i] != Byte.MAX_VALUE) {
                missChunkList.add(i + "");
            }
        }
        if (missChunkList.isEmpty()) {
            map.put(100, "文件已存在");
            return map;
        }
        map.put(102, missChunkList);
        return map;
    }


    /**
     * 上传文件
     */
    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public String upload(MultipartFileParam param) {
        try {
            // 方法1
            // storageService.uploadFileByRandomAccessFile(param);
            // 方法2 这个更快点
            storageService.uploadFileByMappedByteBuffer(param);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("文件上传失败。{}", param.toString());
            return "失败";
        }
        return "成功";
    }
}
