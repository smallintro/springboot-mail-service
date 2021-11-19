package io.github.smallintro.emailsender.service;

import io.github.smallintro.emailsender.model.MailInfo;
import io.github.smallintro.emailsender.util.MailSenderUtil;
import io.github.smallintro.emailsender.util.ValidatorUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class MailSenderService {
    @Autowired
    private MailSenderUtil sender;

    @Value("${attachment.path:/attachments}")
    private String attachments;

    public String sendMail(MailInfo mail) {
        ValidatorUtil.validateMailIds(mail);
        ValidatorUtil.validateMailIAttachment(mail);
        try {
            if (mail.isAttachment()) {
                List<String> attachments = new ArrayList<>();
                sender.sendMailWithAttachment(mail, attachments);
            } else {
                sender.sendMailWithoutAttachment(mail);
            }
            return "Mail is sent successfully";
        } catch (Exception ex) {
            log.error("Exception during sendMail: ", ex.getMessage());
            return "Mail is not sent: " + ex.getMessage();
        }
    }

}
