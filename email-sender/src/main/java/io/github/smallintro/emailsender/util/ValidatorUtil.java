package io.github.smallintro.emailsender.util;

import io.github.smallintro.emailsender.config.AppConstants;
import io.github.smallintro.emailsender.model.MailInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.core.io.FileSystemResource;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.io.File;
import java.security.InvalidParameterException;
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
        if (!CollectionUtils.isEmpty(mail.getAttachments()) && !StringUtils.hasLength(mail.getMailSubject())) {
            log.error("Email with attachment must have a Subject");
            throw new IllegalArgumentException("Email with attachment must have a Subject");
        }
    }

    public static void validateFile(String name, long size, String contentType) {
        // validate file name
        if (null != name && name.length() > AppConstants.MAX_FILE_NAME_LENGTH) {
            log.error("File name too big {}", name);
            throw new IllegalArgumentException("File name is too big for mail attachment");
        }
        // validate file size
        if (size > AppConstants.MAX_FILE_SIZE) {
            log.error("File size too big {}", size);
            throw new IllegalArgumentException("File size is too big for mail attachment");
        }
        // validate file type
        if ("application/octet-stream".equals(contentType) || "application/x-msdownload".equals(contentType)) {
            log.error("File type not supported {}", contentType);
            throw new IllegalArgumentException("Unsupported File type. File can not be send via mail");
        }
    }

    public static void checkAttachmentExists(List<String> files) {
        StringBuffer fileNotFound = new StringBuffer();
        files.forEach(fileName -> {
            FileSystemResource file = new FileSystemResource(new File(AppConstants.UPLOAD_PATH+fileName));
            if (!file.exists()) {
                fileNotFound.append(fileName).append(", ");
            }
        });

        if (fileNotFound.length() > 0) {
            throw new InvalidParameterException("Please upload the these missing files and try again.\n" + fileNotFound);
        }
    }
}
