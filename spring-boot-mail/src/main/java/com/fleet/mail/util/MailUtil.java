package com.fleet.mail.util;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;

import javax.mail.internet.MimeMessage;
import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * 邮件工具类
 */
public class MailUtil {

    private static final Logger logger = LoggerFactory.getLogger(MailUtil.class);

    private JavaMailSenderImpl javaMailSender;

    private Map<String, String> map;

    public MailUtil(Map<String, String> map) {
        if (map == null) {
            logger.error("邮件服务错误：配置缺失");
            return;
        }
        this.map = map;
        javaMailSender = new JavaMailSenderImpl();
        javaMailSender.setHost(map.get("host"));
        javaMailSender.setPort(StringUtils.isNotEmpty(map.get("port")) ? Integer.parseInt(map.get("port")) : 25);
        javaMailSender.setProtocol(map.get("protocol"));
        javaMailSender.setUsername(map.get("username"));
        javaMailSender.setPassword(map.get("password"));
        javaMailSender.setDefaultEncoding(StringUtils.isNotEmpty(map.get("default-encoding")) ? map.get("default-encoding") : "UTF-8");
        Properties javaMailProperties = new Properties();
        javaMailProperties.setProperty("mail.smtp.auth", "true");
        javaMailProperties.setProperty("mail.smtp.timeout", "10000");
        javaMailProperties.setProperty("mail.smtp.starttls.enable", "true");
        javaMailProperties.setProperty("mail.smtp.starttls.required", "true");
        javaMailSender.setJavaMailProperties(javaMailProperties);
    }

    /**
     * 发送文本邮件
     */
    public void textMail(String to, String subject, String text) throws Exception {
        simpleMail(to, subject, text);
    }

    /**
     * 发送文本邮件
     */
    public void simpleMail(String to, String subject, String text) throws Exception {
        String[] tos = {to};
        simpleMail(tos, subject, text);
    }

    /**
     * 发送文本邮件
     */
    public void textMail(List<String> toList, String subject, String text) throws Exception {
        simpleMail(toList, subject, text);
    }

    /**
     * 发送文本邮件
     */
    public void simpleMail(List<String> toList, String subject, String text) throws Exception {
        String[] tos = toList.toArray(new String[0]);
        simpleMail(tos, subject, text);
    }

    /**
     * 发送文本邮件
     */
    public void textMail(String[] tos, String subject, String text) throws Exception {
        simpleMail(tos, subject, text);
    }

    /**
     * 发送文本邮件
     */
    public void simpleMail(String[] tos, String subject, String text) throws Exception {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        if (StringUtils.isNotEmpty(this.map.get("personal"))) {
            helper.setFrom(this.map.get("from"), this.map.get("personal"));
        } else {
            helper.setFrom(this.map.get("from"));
        }
        helper.setTo(tos);
        helper.setSubject(subject);
        helper.setText(text);
        javaMailSender.send(message);
    }


    /**
     * 发送html邮件
     */
    public void htmlMail(String to, String subject, String text) throws Exception {
        String[] tos = {to};
        htmlMail(tos, subject, text);
    }

    /**
     * 发送html邮件
     */
    public void htmlMail(List<String> toList, String subject, String text) throws Exception {
        String[] tos = toList.toArray(new String[0]);
        htmlMail(tos, subject, text);
    }

    /**
     * 发送html邮件
     */
    public void htmlMail(String[] tos, String subject, String text) throws Exception {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        if (StringUtils.isNotEmpty(this.map.get("personal"))) {
            helper.setFrom(this.map.get("from"), this.map.get("personal"));
        } else {
            helper.setFrom(this.map.get("from"));
        }
        helper.setTo(tos);
        helper.setSubject(subject);
        helper.setText(text, true);
        javaMailSender.send(message);
    }

    /**
     * 发送带附件的邮件
     */
    public void attachmentMail(String to, String subject, String text, Map<String, File> files) throws Exception {
        String[] tos = {to};
        attachmentMail(tos, subject, text, files);
    }

    /**
     * 发送带附件的邮件
     */
    public void attachmentMail(List<String> toList, String subject, String text, Map<String, File> files) throws Exception {
        String[] tos = toList.toArray(new String[0]);
        attachmentMail(tos, subject, text, files);
    }

    /**
     * 发送带附件的邮件
     */
    public void attachmentMail(String[] tos, String subject, String text, Map<String, File> files) throws Exception {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        if (StringUtils.isNotEmpty(this.map.get("personal"))) {
            helper.setFrom(this.map.get("from"), this.map.get("personal"));
        } else {
            helper.setFrom(this.map.get("from"));
        }
        helper.setTo(tos);
        helper.setSubject(subject);
        helper.setText(text, true);
        if (files != null) {
            files.forEach((k, v) -> {
                // 加载文件资源，作为附件
                FileSystemResource fileSystemResource = new FileSystemResource(v);
                try {
                    // 添加附件
                    helper.addAttachment(k, fileSystemResource);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }
        javaMailSender.send(message);
    }

    /**
     * 发送正文中有静态资源（图片）的邮件
     */
    public void inlineMail(String to, String subject, String text, Map<String, File> files) throws Exception {
        String[] tos = {to};
        inlineMail(tos, subject, text, files);
    }

    /**
     * 发送正文中有静态资源（图片）的邮件
     */
    public void inlineMail(List<String> toList, String subject, String text, Map<String, File> files) throws Exception {
        String[] tos = toList.toArray(new String[0]);
        inlineMail(tos, subject, text, files);
    }

    /**
     * 发送正文中有静态资源（图片）的邮件
     */
    public void inlineMail(String[] tos, String subject, String text, Map<String, File> files) throws Exception {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        if (StringUtils.isNotEmpty(this.map.get("personal"))) {
            helper.setFrom(this.map.get("from"), this.map.get("personal"));
        } else {
            helper.setFrom(this.map.get("from"));
        }
        helper.setTo(tos);
        helper.setSubject(subject);
        helper.setText(text, true);
        if (files != null) {
            files.forEach((k, v) -> {
                FileSystemResource fileSystemResource = new FileSystemResource(v);
                try {
                    // 添加静态资源
                    helper.addInline(k, fileSystemResource);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }
        javaMailSender.send(message);
    }
}
