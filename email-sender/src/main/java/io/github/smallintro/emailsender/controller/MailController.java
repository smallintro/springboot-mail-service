package io.github.smallintro.emailsender.controller;

import io.github.smallintro.emailsender.model.MailInfo;
import io.github.smallintro.emailsender.service.MailSenderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/mail")
@Slf4j
public class MailController {

    @Autowired
    private MailSenderService mailSenderService;

    @PostMapping("/send")
    public ResponseEntity sendEmail(@Validated @RequestBody MailInfo mail) {
        log.info("Received sendEmail request");
        try {
            String response = mailSenderService.sendMail(mail);
            return new ResponseEntity(response, HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

}
