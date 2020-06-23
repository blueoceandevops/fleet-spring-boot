package com.fleet.aliyunoss.util;

import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.model.*;
import com.fleet.aliyunoss.config.AliyunOSSConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public class AliyunOSSUtil {

    private static final Logger logger = LoggerFactory.getLogger(AliyunOSSUtil.class);

    @Resource
    private AliyunOSSConfig aliyunOSSConfig;

    /**
     * 上传
     */
    public void upload(File file) {
        String endpoint = aliyunOSSConfig.getEndpoint();
        String accessKeyId = aliyunOSSConfig.getAccessKeyId();
        String accessKeySecret = aliyunOSSConfig.getAccessKeySecret();
        String bucketName = aliyunOSSConfig.getBucketName();
        String fileHost = aliyunOSSConfig.getFileHost();

        OSSClient ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);
        try {
            if (!ossClient.doesBucketExist(bucketName)) {
                ossClient.createBucket(bucketName);
                CreateBucketRequest createBucketRequest = new CreateBucketRequest(bucketName);
                createBucketRequest.setCannedACL(CannedAccessControlList.PublicRead);
                ossClient.createBucket(createBucketRequest);
            }
            String key = fileHost + "/" + file.getName();
            ossClient.putObject(bucketName, key, file);
            ossClient.setBucketAcl(bucketName, CannedAccessControlList.PublicRead);
        } catch (OSSException oe) {
            logger.error(oe.getMessage());
        } catch (ClientException ce) {
            logger.error(ce.getMessage());
        } finally {
            //关闭
            ossClient.shutdown();
        }
    }

    /**
     * 通过文件名下载文件
     *
     * @param objectName    要下载的文件名
     * @param localFileName 本地要创建的文件名
     */
    public void downloadFile(String fileName, String localFileName, HttpServletResponse response) throws IOException {
        String endpoint = aliyunOSSConfig.getEndpoint();
        String accessKeyId = aliyunOSSConfig.getAccessKeyId();
        String accessKeySecret = aliyunOSSConfig.getAccessKeySecret();
        String bucketName = aliyunOSSConfig.getBucketName();
        String fileHost = aliyunOSSConfig.getFileHost();

        OSSClient ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);
        String key = fileHost + "/" + fileName;

        GetObjectRequest getObjectRequest = new GetObjectRequest(bucketName, key);
        File file = new File(fileName);
        ossClient.getObject(getObjectRequest, file);
        ossClient.shutdown();

        InputStream is = new FileInputStream(file);

        response.reset();
        response.setContentType("bin");
        response.addHeader("Content-Disposition", "attachment;filename=" + new String(fileName.getBytes(), StandardCharsets.ISO_8859_1));
        OutputStream os = response.getOutputStream();
        byte[] b = new byte[1024];
        int len;
        while ((len = is.read(b)) > 0) {
            os.write(b, 0, len);
        }
        os.flush();
        os.close();
        is.close();
    }

    /**
     * 删除
     *
     * @param fileKey
     * @return
     */
    public String deleteBlog(String fileKey) {
        logger.info("=========>OSS文件删除开始");
        String endpoint = aliyunOSSConfig.getEndpoint();
        String accessKeyId = aliyunOSSConfig.getAccessKeyId();
        String accessKeySecret = aliyunOSSConfig.getAccessKeySecret();
        String bucketName = aliyunOSSConfig.getBucketName();
        String fileHost = aliyunOSSConfig.getFileHost();
        try {
            OSSClient ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);

            if (!ossClient.doesBucketExist(bucketName)) {
                logger.info("==============>您的Bucket不存在");
                return "您的Bucket不存在";
            } else {
                logger.info("==============>开始删除Object");
                ossClient.deleteObject(bucketName, fileKey);
                logger.info("==============>Object删除成功：" + fileKey);
                return "==============>Object删除成功：" + fileKey;
            }
        } catch (Exception ex) {
            logger.info("删除Object失败", ex);
            return "删除Object失败";
        }
    }

    /**
     * 查询文件名列表
     *
     * @param bucketName
     * @return
     */
    public List<String> getObjectList(String bucketName) {
        List<String> listRe = new ArrayList<>();
        String endpoint = aliyunOSSConfig.getEndpoint();
        String accessKeyId = aliyunOSSConfig.getAccessKeyId();
        String accessKeySecret = aliyunOSSConfig.getAccessKeySecret();
        try {
            logger.info("===========>查询文件名列表");
            OSSClient ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);
            ListObjectsRequest listObjectsRequest = new ListObjectsRequest(bucketName);
            //列出11111目录下今天所有文件
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            listObjectsRequest.setPrefix("11111/" + sdf.format(new Date()) + "/");
            ObjectListing list = ossClient.listObjects(listObjectsRequest);
            for (OSSObjectSummary objectSummary : list.getObjectSummaries()) {
                System.out.println(objectSummary.getKey());
                listRe.add(objectSummary.getKey());
            }
            return listRe;
        } catch (Exception ex) {
            logger.info("==========>查询列表失败", ex);
            return new ArrayList<>();
        }
    }
}
