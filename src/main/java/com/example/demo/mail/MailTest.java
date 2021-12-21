package com.example.demo.mail;

import org.apache.commons.codec.Charsets;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

/**
 * @author nelson.li
 * @date 2021/12/20
 **/
@Configuration
public class MailTest {
    private static final String OUTLOOK_PORT = "587";
    private static final String MAIL_FROM = "huohuo5234@outlook.sg";
    private static final String MAIL_FROM_PASSWORD = "gmail5234";
    private static final String MAIL_TO = "nelson.li@ihr360.com";

//    private static final String MAIL_FROM = "xiaonan.li1992@outlook.com";
//    private static final String MAIL_FROM_PASSWORD = "Xiao32784794!";
//    private static final String MAIL_TO = "nelson.li@ihr360.com";

    @Bean
    public void testMail() {
        Email email = new Email();
        email.setContent("123");
        email.setSubject("enders");
        email.setRecipientMail(new String[]{MAIL_TO});
        email.setMailFrom(MAIL_FROM);
        List<Email> emails = new ArrayList<>();
        emails.add(email);
        sendEmail(emails, "123");
    }

    public static void sendEmail(List<Email> emails, String companyId) {
        if (CollectionUtils.isEmpty(emails)) {
            return;
        }
        SendMessageEmailSettingPo po = findMailDetail(companyId);
        if (po == null || !Boolean.TRUE.equals(po.getIsConnect())) {
//            log.info("公司未设置邮箱 {}", companyId);
            return;
        }
        Session session = getJavaMailSender2(po);
        try {
            Transport transport = session.getTransport(Boolean.TRUE.equals(po.getUseSsl()) ? "smtps" : "smtp");
            transport.connect(po.getEmailSmtpServer(), po.getEmailAddress(), po.getEmailPassword());
            for (Email email : emails) {
                MimeMessage message = createMineMessage(session, email.getMailFrom(), InternetAddress.parse(email.getRecipientMail()[0]));
                transport.sendMessage(message, message.getAllRecipients());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static SendMessageEmailSettingPo findMailDetail(String companyId) {
        SendMessageEmailSettingPo po = new SendMessageEmailSettingPo();
        po.setEmailAddress(MAIL_FROM);
        po.setEmailPassword(MAIL_FROM_PASSWORD);
        po.setEmailSmtpServer("smtp.office365.com");
        po.setPort("587");
        po.setUseSsl(false);
        po.setIsConnect(true);

        return po;
    }

    private static Session getJavaMailSender2(SendMessageEmailSettingPo po) {
        Properties prop = new Properties();
        String emailSmtpServer = po.getEmailSmtpServer();
        String port = po.getPort();
        Boolean useSsl = po.getUseSsl();
        if (Boolean.TRUE.equals(useSsl)) {
            prop.setProperty("mail.smtps.ssl.trust", emailSmtpServer);
            prop.setProperty("mail.smtps.auth", "true");
            prop.setProperty("mail.smtps.host", emailSmtpServer);
            prop.setProperty("mail.smtps.port", port);
            prop.setProperty("mail.smtps.ssl.protocols", "TLSv1 TLSv1.1 TLSv1.2");
            prop.setProperty("mail.smtps.ssl.ciphersuites", "TLS_ECDHE_ECDSA_WITH_AES_128_CBC_SHA TLS_ECDHE_RSA_WITH_AES_128_CBC_SHA TLS_RSA_WITH_AES_128_CBC_SHA TLS_ECDH_ECDSA_WITH_AES_128_CBC_SHA TLS_ECDH_RSA_WITH_AES_128_CBC_SHA TLS_DHE_RSA_WITH_AES_128_CBC_SHA TLS_DHE_DSS_WITH_AES_128_CBC_SHA TLS_ECDHE_ECDSA_WITH_RC4_128_SHA TLS_ECDHE_RSA_WITH_RC4_128_SHA SSL_RSA_WITH_RC4_128_SHA TLS_ECDH_ECDSA_WITH_RC4_128_SHA TLS_ECDH_RSA_WITH_RC4_128_SHA TLS_ECDHE_ECDSA_WITH_3DES_EDE_CBC_SHA TLS_ECDHE_RSA_WITH_3DES_EDE_CBC_SHA SSL_RSA_WITH_3DES_EDE_CBC_SHA TLS_ECDH_ECDSA_WITH_3DES_EDE_CBC_SHA TLS_ECDH_RSA_WITH_3DES_EDE_CBC_SHA SSL_DHE_RSA_WITH_3DES_EDE_CBC_SHA SSL_DHE_DSS_WITH_3DES_EDE_CBC_SHA SSL_RSA_WITH_RC4_128_MD5 TLS_EMPTY_RENEGOTIATION_INFO_SCSV");
            prop.setProperty("mail.smtps.connectiontimeout", "12000");
            prop.setProperty("mail.smtps.timeout", "12000");
        } else if (OUTLOOK_PORT.equals(port)) {
            prop.setProperty("mail.smtp.auth", "true");
            prop.setProperty("mail.smtp.starttls.enable", "true");
//            prop.setProperty("mail.smtp.ssl.protocols", "TLSv1 TLSv1.1 TLSv1.2");
            prop.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
            prop.setProperty("mail.smtp.host", emailSmtpServer);
            prop.setProperty("mail.smtp.port", port);
            prop.setProperty("mail.smtp.connectiontimeout", "12000");
            prop.setProperty("mail.smtp.timeout", "12000");
        } else {
            //兼容SMTP服务器不支持smtps但是开启了ssl的
            prop.setProperty("mail.smtp.ssl.protocols", "TLSv1 TLSv1.1 TLSv1.2");
            prop.setProperty("mail.smtp.ssl.trust", emailSmtpServer);
            prop.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
            prop.setProperty("mail.smtp.auth", "true");
            prop.setProperty("mail.smtp.host", emailSmtpServer);
            prop.setProperty("mail.smtp.port", port);
            prop.setProperty("mail.smtp.connectiontimeout", "12000");
            prop.setProperty("mail.smtp.timeout", "12000");
        }
        prop.put("mail.debug", "true");

        return Session.getInstance(prop);
    }

    /**
     * 构造邮件内容
     *
     * @param session            session
     * @param sendMailAddress    发件邮箱地址
     * @param receiveMailAddress 收件邮箱地址
     * @return
     * @throws Exception
     */
    private static MimeMessage createMineMessage(Session session, String sendMailAddress, InternetAddress[] receiveMailAddress) throws Exception {
        InternetAddress fromAddress = new InternetAddress(sendMailAddress);
        //创建邮件
        MimeMessage message = new MimeMessage(session);
        //设置发件人
        message.setFrom(fromAddress);
        //设置收件人
        message.addRecipients(Message.RecipientType.TO, receiveMailAddress);
        message.setSentDate(new Date());
        //设置主题
        message.setSubject(MimeUtility.encodeText("test mail", Charsets.UTF_8.toString(), "B"));
        //构建内容/附件
        MimeMultipart allPart = new MimeMultipart("mixed");
        //构建邮件内容
        MimeBodyPart body = createContent("test mail content");
        allPart.addBodyPart(body);
        message.setContent(allPart);
        message.saveChanges();
        return message;
    }

    /**
     * 根据传入的邮件正文body和文件路径创建图文并茂的正文部分
     */
    private static MimeBodyPart createContent(String body) throws Exception {
        // 用于保存最终正文部分
        MimeBodyPart contentBody = new MimeBodyPart();
        // 用于组合文本和图片，"related"型的MimeMultipart对象
        MimeMultipart contentMulti = new MimeMultipart("related");
        // 正文的文本部分
        MimeBodyPart textBody = new MimeBodyPart();
        textBody.setContent(body, "text/html;charset=gbk");
        contentMulti.addBodyPart(textBody);

        // 将上面"related"型的 MimeMultipart 对象作为邮件的正文
        contentBody.setContent(contentMulti);
        return contentBody;
    }

}
