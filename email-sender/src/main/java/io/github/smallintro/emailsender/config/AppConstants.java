package io.github.smallintro.emailsender.config;

public interface AppConstants {
    String ROOT_FILE_PATH = "/file";
    String UPLOAD_PATH = "/file/uploads/";

    int MAX_FILE_NAME_LENGTH = 5;
    long MAX_FILE_SIZE = 1024*1024*10; // 10 MB
}
