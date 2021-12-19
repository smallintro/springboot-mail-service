package io.github.smallintro.emailsender.config;

import org.springframework.beans.factory.annotation.Value;

public interface AppConstants {
    String UPLOAD_PATH = "/home/spring/files/";

    int MAX_FILE_NAME_LENGTH = 5;
    long MAX_FILE_SIZE = 1024*1024*10; // 10 MB
}
