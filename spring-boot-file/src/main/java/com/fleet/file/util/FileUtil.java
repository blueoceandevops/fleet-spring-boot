package com.fleet.file.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.lang.reflect.Method;
import java.nio.MappedByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * 文件工具类
 */
public class FileUtil {

    private final static Logger logger = LoggerFactory.getLogger(FileUtil.class);

    /**
     * 文件上传
     */
    public static void upload(byte[] file, String path, String fileName) throws Exception {
        File fileDir = new File(path);
        if (!fileDir.exists() && !fileDir.isDirectory()) {
            fileDir.mkdirs();
        }
        FileOutputStream fos = new FileOutputStream(path + fileName);
        fos.write(file);
        fos.flush();
        fos.close();
    }

    /**
     * 文件上传
     */
    public static void upload(InputStream is, String path, String fileName) throws Exception {
        File fileDir = new File(path);
        if (!fileDir.exists() && !fileDir.isDirectory()) {
            fileDir.mkdirs();
        }
        FileOutputStream fos = new FileOutputStream(path + fileName);
        byte[] b = new byte[1024];
        int len;
        while ((len = is.read(b)) > 0) {
            fos.write(b, 0, len);
        }
        is.close();
        fos.flush();
        fos.close();
    }

    /**
     * resource 文件下载
     */
    public static void downloadResource(String filePath, String fileName, HttpServletResponse response) throws Exception {
        InputStream is = FileUtil.class.getResourceAsStream(filePath);

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
     * 文件下载
     */
    public static void download(String filePath, String fileName, HttpServletResponse response) throws Exception {
        InputStream is = new FileInputStream(filePath);

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
     * 批量文件打包下载
     *
     * @param files    文件
     * @param path     缓存Zip包存放位置
     * @param fileName 文件名
     * @param response
     * @throws Exception
     */
    public static void download(File[] files, String path, String fileName, HttpServletResponse response) throws Exception {
        String zipFileName = UUIDUtil.getUUID() + ".zip";
        File zipfile = new File(path + zipFileName);
        if (!zipfile.exists()) {
            zipfile.createNewFile();
        }

        FileOutputStream fos = new FileOutputStream(zipfile);
        ZipOutputStream zos = new ZipOutputStream(fos);
        zipFile(files, zos);
        zos.finish();
        zos.close();
        fos.flush();
        fos.close();
        downloadZip(zipfile, fileName, response);
    }

    /**
     * 图片在线查看
     */
    public static void image(String path, String fileName, HttpServletResponse response) throws Exception {
        InputStream is = new FileInputStream(path + fileName);

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

    public static String rename(String fileName) {
        return UUIDUtil.getUUID() + "." + fileName.substring(fileName.lastIndexOf(".") + 1);
    }

    /**
     * 全部文件打包
     */
    private static void zipFile(File[] files, ZipOutputStream zos) {
        if (files != null) {
            for (int i = 0; i < files.length; i++) {
                File file = files[i];
                zipFile(file, zos);
            }
        }
    }

    /**
     * 对文件打包
     */
    private static void zipFile(File file, ZipOutputStream zos) {
        try {
            if (file.exists()) {
                if (file.isFile()) {
                    FileInputStream fis = new FileInputStream(file);
                    ZipEntry entry = new ZipEntry(file.getName());
                    zos.putNextEntry(entry);

                    // 向压缩文件中输出数据
                    byte[] b = new byte[1024];
                    int len;
                    while ((len = fis.read(b)) > 0) {
                        zos.write(b, 0, len);
                    }
                    zos.closeEntry();
                    // 关闭创建的流对象
                    fis.close();
                } else {
                    try {
                        File[] files = file.listFiles();
                        if (files != null) {
                            for (int i = 0; i < files.length; i++) {
                                zipFile(files[i], zos);
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 下载Zip文件
     */
    private static void downloadZip(File file, String fileName, HttpServletResponse response) throws Exception {
        FileInputStream fis = new FileInputStream(file);

        response.reset();
        response.setContentType("application/octet-stream");
        response.addHeader("Content-Disposition", "attachment;filename=" + new String(fileName.getBytes(), StandardCharsets.ISO_8859_1));
        OutputStream os = response.getOutputStream();
        byte[] b = new byte[1024];
        int len;
        while ((len = fis.read(b)) > 0) {
            os.write(b, 0, len);
        }
        os.flush();
        os.close();
        fis.close();
    }

    /**
     * 在MappedByteBuffer释放后再对它进行读操作的话就会引发jvm crash，在并发情况下很容易发生
     * 正在释放时另一个线程正开始读取，于是crash就发生了。所以为了系统稳定性释放前一般需要检 查是否还有线程在读或写
     */
    public static void mappedByteBuffer(final MappedByteBuffer mappedByteBuffer) {
        try {
            if (mappedByteBuffer == null) {
                return;
            }

            mappedByteBuffer.force();
            AccessController.doPrivileged(new PrivilegedAction<Object>() {
                @Override
                public Object run() {
                    try {
                        Method cleanerMethod = mappedByteBuffer.getClass().getMethod("cleaner");
                        cleanerMethod.setAccessible(true);
                        Object cleaner = cleanerMethod.invoke(mappedByteBuffer);
                        Method cleanMethod = cleaner.getClass().getMethod("clean");
                        cleanMethod.setAccessible(true);
                        cleanMethod.invoke(cleaner);
                    } catch (Exception e) {
                        logger.error("clean MappedByteBuffer error!!!", e);
                    }
                    logger.info("clean MappedByteBuffer completed!!!");
                    return null;
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
