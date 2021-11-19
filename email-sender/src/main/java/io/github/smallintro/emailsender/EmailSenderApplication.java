package io.github.smallintro.emailsender;

import io.github.smallintro.emailsender.config.AppConstants;
import io.github.smallintro.emailsender.service.FileStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * MultipartConfigElement bean will be created by spring boot
 */
@SpringBootApplication
public class EmailSenderApplication implements CommandLineRunner {

    @Autowired
    FileStorageService fileStorageService;

    public static void main(String[] args) {
        SpringApplication.run(EmailSenderApplication.class, args);
    }


    @Override
    public void run(String... arg) {
        fileStorageService.init(AppConstants.ROOT_FILE_PATH);
        fileStorageService.init(AppConstants.UPLOAD_PATH);
        // fileStorageService.deleteAllFiles(AppConstants.UPLOAD_PATH);
    }

}
