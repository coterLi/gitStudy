package com.example.demo.mail;

import com.alibaba.fastjson.JSONObject;

import java.io.File;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;

public class Email implements Serializable {

    /**
     * 发送人
     */
    private String mailFrom;

    /**
     * 收件人邮箱
     */
    private String[] recipientMail;

    /**
     * 主题
     */
    private String subject;

    /**
     * 邮件内容(content与template互斥:定义content,则template无效)
     */
    private String content;

    /**
     * freemarker模板(content与template互斥:定义template,则content无效)
     */
    private String template;
    /**
     * 邮件内容的自定义参数
     */
    private HashMap<String, String> replaceParam;

    /**
     * 内嵌图片
     */
    private HashMap<String, File> inlineImage;

    /**
     * 附件
     */
    private Collection<File> attachments;

    public Email() {
    }

    public String getMailFrom() {
        return mailFrom;
    }

    public void setMailFrom(String mailFrom) {
        this.mailFrom = mailFrom;
    }

    public String[] getRecipientMail() {
        return recipientMail;
    }

    public void setRecipientMail(String[] recipientMail) {
        this.recipientMail = recipientMail;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template;
    }

    public HashMap<String, String> getReplaceParam() {
        return replaceParam;
    }

    public void setReplaceParam(HashMap<String, String> replaceParam) {
        this.replaceParam = replaceParam;
    }

    public HashMap<String, File> getInlineImage() {
        return inlineImage;
    }

    public void setInlineImage(HashMap<String, File> inlineImage) {
        this.inlineImage = inlineImage;
    }

    public Collection<File> getAttachments() {
        return attachments;
    }

    public void setAttachments(Collection<File> attachments) {
        this.attachments = attachments;
    }

    @Override
    public String toString() {
        return "Email{" +
                "mailFrom='" + mailFrom + '\'' +
                ", recipientMail=" + Arrays.toString(recipientMail) +
                ", subject='" + subject + '\'' +
                ", content='" + content + '\'' +
                ", template='" + template + '\'' +
                ", replaceParam=" + JSONObject.toJSONString(replaceParam) +
                '}';
    }
}
