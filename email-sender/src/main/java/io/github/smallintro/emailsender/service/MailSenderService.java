package io.github.smallintro.emailsender.service;

import io.github.smallintro.emailsender.model.MailInfo;
import io.github.smallintro.emailsender.util.MailSenderUtil;
import io.github.smallintro.emailsender.util.ValidatorUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

@Service
@Slf4j
public class MailSenderService {
    @Autowired
    private MailSenderUtil sender;

    public String sendMail(MailInfo mail) {
        ValidatorUtil.validateMailIds(mail);
        ValidatorUtil.validateMailIAttachment(mail);
        try {
            if (CollectionUtils.isEmpty(mail.getAttachments())) {
                sender.sendMailWithoutAttachment(mail);
            } else {
                // check attachment exists
                ValidatorUtil.checkAttachmentExists(mail.getAttachments());
                sender.sendMailWithAttachment(mail);
            }
            return "Mail is sent successfully";
        } catch (Exception ex) {
            log.error("Exception during sendMail: ", ex.getMessage());
            return "Mail is not sent: " + ex.getMessage();
        }
    }

}
