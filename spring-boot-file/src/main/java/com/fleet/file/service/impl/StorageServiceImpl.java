package com.fleet.file.service.impl;

import com.fleet.file.config.FileConfig;
import com.fleet.file.entity.MultipartFileParam;
import com.fleet.file.service.StorageService;
import com.fleet.file.util.FileUtil;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.File;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

@Service
public class StorageServiceImpl implements StorageService {

    private final static Logger logger = LoggerFactory.getLogger(StorageServiceImpl.class);

    @Resource
    FileConfig fileConfig;

    @Override
    public void uploadFileByRandomAccessFile(MultipartFileParam param) throws Exception {
        String filePath = fileConfig.getBigFilePath() + param.getMd5();
        String fileName = param.getName();
        String tempFileName = fileName + "_tmp";

        File fileDir = new File(filePath);
        File tempFile = new File(filePath, tempFileName);
        if (!fileDir.exists()) {
            fileDir.mkdirs();
        }

        RandomAccessFile raf = new RandomAccessFile(tempFile, "rw");
        long offset = param.getChunk() * param.getChunkSize();
        //定位到该分片的偏移量
        raf.seek(offset);
        //写入该分片数据
        raf.write(param.getFile().getBytes());
        // 释放
        raf.close();

        boolean isComplete = checkUploadStatus(param);
        if (isComplete) {
            renameFile(tempFile, fileName);
        }
    }

    /**
     * 分块上传
     * 第一步：获取 RandomAccessFile ，随机访问文件类的对象
     * 第二步：调用 RandomAccessFile 的 getChannel()方法，打开文件通道 FileChannel
     * 第三步：获取当前是第几个分块，计算文件的最后偏移量
     * 第四步：获取当前文件分块的字节数组，用于获取文件字节长度
     * 第五步：使用文件通道 FileChannel 类的 map() 方法创建直接字节缓冲器 MappedByteBuffer
     * 第六步：将分块的字节数组放入到当前位置的缓冲区内 mappedByteBuffer.put(byte[] b);
     * 第七步：释放缓冲区
     * 第八步：检查文件是否全部完成上传
     */
    @Override
    public void uploadFileByMappedByteBuffer(MultipartFileParam param) throws Exception {
        String filePath = fileConfig.getBigFilePath() + param.getMd5();
        String fileName = param.getName();
        String tempFileName = fileName + "_tmp";

        File fileDir = new File(filePath);
        File tempFile = new File(filePath, tempFileName);
        if (!fileDir.exists()) {
            fileDir.mkdirs();
        }

        //第一步
        RandomAccessFile raf = new RandomAccessFile(tempFile, "rw");
        //第二步
        FileChannel fileChannel = raf.getChannel();
        //第三步
        long offset = param.getChunk() * param.getChunkSize();
        //第四步
        byte[] fileData = param.getFile().getBytes();
        //第五步
        MappedByteBuffer mappedByteBuffer = fileChannel.map(FileChannel.MapMode.READ_WRITE, offset, fileData.length);
        //第六步
        mappedByteBuffer.put(fileData);
        //第七步
        FileUtil.mappedByteBuffer(mappedByteBuffer);
        fileChannel.close();
        raf.close();
        //第八步
        boolean isComplete = checkUploadStatus(param);
        if (isComplete) {
            renameFile(tempFile, fileName);
        }
    }


    /**
     * 检查文件上传进度
     *
     * @return
     */
    public boolean checkUploadStatus(MultipartFileParam param) throws Exception {
        File confFile = new File(fileConfig.getBigFilePath() + param.getMd5(), "status");
        RandomAccessFile raf = new RandomAccessFile(confFile, "rw");
        //设置文件长度
        raf.setLength(param.getChunks());
        //设置起始偏移量
        raf.seek(param.getChunk());
        //将指定的一个字节写入文件中 127
        raf.write(Byte.MAX_VALUE);
        byte[] completeStatusList = FileUtils.readFileToByteArray(confFile);
        byte isComplete = Byte.MAX_VALUE;
        //这一段逻辑有点复杂，看的时候思考了好久，创建conf文件文件长度为总分片数，每上传一个分块即向conf文件中写入一个127，那么没上传的位置就是默认的0,已上传的就是Byte.MAX_VALUE 127
        for (int i = 0; i < completeStatusList.length && isComplete == Byte.MAX_VALUE; i++) {
            // 按位与运算，将&两边的数转为二进制进行比较，有一个为0结果为0，全为1结果为1  eg.3&5  即 0000 0011 & 0000 0101 = 0000 0001   因此，3&5的值得1。
            isComplete = (byte) (isComplete & completeStatusList[i]);
            System.out.println("check part " + i + " complete?:" + completeStatusList[i]);
        }
        raf.close();
        if (isComplete == Byte.MAX_VALUE) {
            return true;
        }
        return false;
    }

    /**
     * 文件重命名
     *
     * @param file    将要修改名字的文件
     * @param newName 新的名字
     * @return
     */
    private boolean renameFile(File file, String newName) {
        // 检查要重命名的文件是否存在，是否是文件
        if (!file.exists() || file.isDirectory()) {
            logger.info("File does not exist: " + file.getName());
            return false;
        }
        String p = file.getParent();
        File newFile = new File(p + File.separatorChar + newName);
        // 修改文件名
        return file.renameTo(newFile);
    }
}
