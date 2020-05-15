package com.fleet.mail;

import com.fleet.mail.util.MailUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MailApplicationTests {

    @Test
    public void simpleMail() throws Exception {
        Map<String, String> map = new HashMap<>();
        map.put("host", "smtp.sina.com");
        map.put("port", "25");
        map.put("protocol", "smtp");
        map.put("username", "fleetsoft@sina.com");
        map.put("password", "9a052dc39292b0ab");
        map.put("default-encoding", "UTF-8");
        map.put("from", "fleetsoft@sina.com");
        map.put("personal", "fleetsoft");
        MailUtil mailUtil = new MailUtil(map);
        mailUtil.simpleMail("aprilhan1992@foxmail.com", "测试", "这是测试");
    }

    @Test
    public void textMail() throws Exception {
        Map<String, String> map = new HashMap<>();
        map.put("host", "smtp.sina.com");
        map.put("port", "25");
        map.put("protocol", "smtp");
        map.put("username", "fleetsoft@sina.com");
        map.put("password", "9a052dc39292b0ab");
        map.put("default-encoding", "UTF-8");
        map.put("from", "fleetsoft@sina.com");
        map.put("personal", "fleetsoft");
        MailUtil mailUtil = new MailUtil(map);
        mailUtil.simpleMail("aprilhan1992@foxmail.com", "测试", "这是测试");
    }

    @Test
    public void htmlMail() throws Exception {
        Map<String, String> map = new HashMap<>();
        map.put("host", "smtp.sina.com");
        map.put("port", "25");
        map.put("protocol", "smtp");
        map.put("username", "fleetsoft@sina.com");
        map.put("password", "9a052dc39292b0ab");
        map.put("default-encoding", "UTF-8");
        map.put("from", "fleetsoft@sina.com");
        map.put("personal", "fleetsoft");
        MailUtil mailUtil = new MailUtil(map);
        mailUtil.htmlMail("aprilhan1992@foxmail.com", "测试", "<P style=\"color:red\">这是测试</P>");
    }

    @Test
    public void attachmentMail() throws Exception {
        Map<String, String> map = new HashMap<>();
        map.put("host", "smtp.sina.com");
        map.put("port", "25");
        map.put("protocol", "smtp");
        map.put("username", "fleetsoft@sina.com");
        map.put("password", "9a052dc39292b0ab");
        map.put("default-encoding", "UTF-8");
        map.put("from", "fleetsoft@sina.com");
        map.put("personal", "fleetsoft");
        MailUtil mailUtil = new MailUtil(map);

        Map<String, File> files = new HashMap<>();
        File file = new File("D:\\test.xls");
        File file1 = new File("D:\\test1.xls");
        files.put("test.xls", file);
        files.put("test1.xls", file1);
        mailUtil.attachmentMail("aprilhan1992@foxmail.com", "测试", "<P style=\"color:red\">这是测试</P>", files);
    }

    @Test
    public void inlineMail() throws Exception {
        Map<String, String> map = new HashMap<>();
        map.put("host", "smtp.sina.com");
        map.put("port", "25");
        map.put("protocol", "smtp");
        map.put("username", "fleetsoft@sina.com");
        map.put("password", "9a052dc39292b0ab");
        map.put("default-encoding", "UTF-8");
        map.put("from", "fleetsoft@sina.com");
        map.put("personal", "fleetsoft");
        MailUtil mailUtil = new MailUtil(map);

        Map<String, File> files = new HashMap<>();
        File file = new File("D:\\Chrysanthemum.jpg");
        files.put("file", file);
        mailUtil.inlineMail("aprilhan1992@foxmail.com", "测试", "<body><p style='color:red;'>Hello Html Email</p><img src='cid:file'/></body>", files);
    }
}
