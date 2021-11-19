package io.github.smallintro.emailsender.util;

import io.github.smallintro.emailsender.model.MailInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class ValidatorUtil {

    public static void validateMailIds(MailInfo mail) {

        if (CollectionUtils.isEmpty(mail.getMailTo())
                && CollectionUtils.isEmpty(mail.getMailCc())
                && CollectionUtils.isEmpty(mail.getMailBcc())) {
            log.error("Email must have at least one recipient");
            throw new IllegalArgumentException("Email must have at least one recipient");
        }

        EmailValidator validator = EmailValidator.getInstance();
        List<String> invalidEmailIds = new ArrayList<>();
        if (null != mail.getMailTo()) {
            for (String emailId : mail.getMailTo()) {
                if (!validator.isValid(emailId)) {
                    invalidEmailIds.add(emailId);
                }
            }
        }

        if (null != mail.getMailCc()) {
            for (String emailId : mail.getMailCc()) {
                if (!validator.isValid(emailId)) {
                    invalidEmailIds.add(emailId);
                }
            }
        }

        if (null != mail.getMailBcc()) {
            for (String emailId : mail.getMailBcc()) {
                if (!validator.isValid(emailId)) {
                    invalidEmailIds.add(emailId);
                }
            }
        }

        if (invalidEmailIds.size() > 0) {
            log.error("Invalid EmailIds: {}", invalidEmailIds);
            throw new IllegalArgumentException("Invalid EmailIds: " + invalidEmailIds);
        }
    }

    public static void validateMailIAttachment(MailInfo mail) {
        if (mail.isAttachment() && !StringUtils.hasLength(mail.getMailSubject())) {
            log.error("Email with attachment must have a Subject");
            throw new IllegalArgumentException("Email with attachment must have a Subject");
        }
    }

    public static void validateFile(String name, long size, String contentType) {
        // file name length
        // validate file extentions
        // validate file size
    }
}
