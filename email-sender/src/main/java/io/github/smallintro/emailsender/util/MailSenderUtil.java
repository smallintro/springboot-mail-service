package io.github.smallintro.emailsender.util;

import io.github.smallintro.emailsender.model.MailInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.util.List;

@Component
public class MailSenderUtil {

    @Value("${spring.mail.username}")
    private String sendFrom;

    @Autowired
    private JavaMailSender emailSender;

    public void sendMailWithAttachment(MailInfo mail, List<String> attachments) throws MessagingException {
        MimeMessage mimeMessage = emailSender.createMimeMessage();
        FileSystemResource file = new FileSystemResource(new File(attachments.get(0)));
        // pass 'true' to the constructor to create a multipart message
        MimeMessageHelper message = new MimeMessageHelper(mimeMessage, true);
        message.setFrom(sendFrom);
        if (!CollectionUtils.isEmpty(mail.getMailTo())) {
            message.setTo(mail.getMailTo().get(0));
        }
        if (!CollectionUtils.isEmpty(mail.getMailCc())) {
            message.setCc(mail.getMailCc().get(0));
        }
        if (!CollectionUtils.isEmpty(mail.getMailBcc())) {
            message.setBcc(mail.getMailBcc().get(0));
        }
        message.setSubject(mail.getMailSubject());
        message.setText(mail.getMailBody());
        message.addAttachment(file.getFilename(), file);
        emailSender.send(mimeMessage);
    }

    public void sendMailWithoutAttachment(MailInfo mail) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(sendFrom);
        if (!CollectionUtils.isEmpty(mail.getMailTo())) {
            message.setTo(mail.getMailTo().get(0));
        }
        if (!CollectionUtils.isEmpty(mail.getMailCc())) {
            message.setCc(mail.getMailCc().get(0));
        }
        if (!CollectionUtils.isEmpty(mail.getMailBcc())) {
            message.setBcc(mail.getMailBcc().get(0));
        }
        message.setSubject(mail.getMailSubject());
        message.setText(mail.getMailBody());
        emailSender.send(message);
    }

}
