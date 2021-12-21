package com.example.demo.mail;

import java.io.Serializable;
import java.util.Date;

/**
 * 冗余irenshi的公司邮箱数据
 */
public class SendMessageEmailSettingPo implements Serializable {
    private static final long serialVersionUID = -5350730726200483454L;

    protected String id;

    protected Date createdDate = new Date();

    /**
     * 是否删除，默认是未删除
     */
    private Boolean isDeleted = false;

    /**
     * 公司Id
     */
    private String companyId;

    /**
     * 邮箱名称
     */
    private String emailAddress;
    /**
     * 邮箱密码
     */
    private String emailPassword;
    /**
     * 发信箱服务
     */
    private String emailSmtpServer;
    /**
     * 邮箱是否连接成功
     */
    private Boolean isConnect = false;

    /**
     * 发信箱端口
     */
    private String port;
    /**
     * 是否ssl
     */
    private Boolean useSsl = false;

    public Boolean getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Boolean getUseSsl() {
        return useSsl;
    }

    public void setUseSsl(Boolean useSsl) {
        this.useSsl = useSsl;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public Boolean getIsConnect() {
        return isConnect;
    }

    public void setIsConnect(Boolean isConnect) {
        this.isConnect = isConnect;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getEmailPassword() {
        return emailPassword;
    }

    public void setEmailPassword(String emailPassword) {
        this.emailPassword = emailPassword;
    }

    public String getEmailSmtpServer() {
        return emailSmtpServer;
    }

    public void setEmailSmtpServer(String emailSmtpServer) {
        this.emailSmtpServer = emailSmtpServer;
    }

}
