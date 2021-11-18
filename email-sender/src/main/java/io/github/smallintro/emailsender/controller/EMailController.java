package io.github.smallintro.emailsender.controller;

import io.github.smallintro.emailsender.model.EmailInfo;
import io.github.smallintro.emailsender.service.EMailService;
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
public class EMailController {

    @Autowired
    private EMailService service;

    @PostMapping("/send")
    public ResponseEntity sendEmail(@Validated @RequestBody EmailInfo mail) {
        try {
            String response = service.sendMail(mail);
            return new ResponseEntity(response, HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }

    }
}
