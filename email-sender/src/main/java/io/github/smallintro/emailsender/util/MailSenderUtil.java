package io.github.smallintro.emailsender.util;

import io.github.smallintro.emailsender.config.AppConstants;
import io.github.smallintro.emailsender.model.MailInfo;
import lombok.extern.slf4j.Slf4j;
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

@Component
@Slf4j
public class MailSenderUtil {

    @Value("${spring.mail.username}")
    private String sendFrom;

    @Autowired
    private JavaMailSender emailSender;

    public void sendMailWithAttachment(MailInfo mail) throws MessagingException {
        MimeMessage mimeMessage = emailSender.createMimeMessage();
        // pass 'true' to the constructor to create a multipart message
        MimeMessageHelper message = new MimeMessageHelper(mimeMessage, true);
        message.setFrom(sendFrom);
        if (!CollectionUtils.isEmpty(mail.getMailTo())) {
            message.setTo(mail.getMailTo().toArray(new String[mail.getMailTo().size()]));
        }
        if (!CollectionUtils.isEmpty(mail.getMailCc())) {
            message.setCc(mail.getMailCc().toArray(new String[mail.getMailTo().size()]));
        }
        if (!CollectionUtils.isEmpty(mail.getMailBcc())) {
            message.setBcc(mail.getMailBcc().toArray(new String[mail.getMailTo().size()]));
        }
        message.setSubject(mail.getMailSubject());
        message.setText(mail.getMailBody());

        mail.getAttachments().forEach(attachment -> {
            FileSystemResource file = new FileSystemResource(new File(AppConstants.UPLOAD_PATH + attachment));
            try {
                message.addAttachment(file.getFilename(), file);
            } catch (MessagingException e) {
                log.error("Failed to attach {} in mail. Error: {}", file.getFilename(), e.getMessage());
            }
        });

        emailSender.send(mimeMessage);
    }


    public void sendMailWithoutAttachment(MailInfo mail) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(sendFrom);
        if (!CollectionUtils.isEmpty(mail.getMailTo())) {
            message.setTo(mail.getMailTo().toArray(new String[mail.getMailTo().size()]));
        }
        if (!CollectionUtils.isEmpty(mail.getMailCc())) {
            message.setCc(mail.getMailCc().toArray(new String[mail.getMailTo().size()]));
        }
        if (!CollectionUtils.isEmpty(mail.getMailBcc())) {
            message.setBcc(mail.getMailBcc().toArray(new String[mail.getMailTo().size()]));
        }
        message.setSubject(mail.getMailSubject());
        message.setText(mail.getMailBody());
        emailSender.send(message);
    }

}
