package io.github.smallintro.emailsender.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

@Service
@Slf4j
public class FileStorageService {

    public final Path rootPath = Paths.get("/file");
    public final Path uploadPath = Paths.get("/file/uploads");
    public final Path attachmentPath = Paths.get("/file/attachments");

    private final int maxDepth = 1;

    public void init(Path dirPath) {
        try {
            Files.createDirectory(dirPath);
        } catch (FileAlreadyExistsException e) {
            log.warn("Directory {} already exists ", e.getMessage());
        } catch (IOException ex) {
            throw new RuntimeException("Could not initialize folder for upload!" + ex.getMessage());
        }
    }

    public String saveFile(MultipartFile file) {
        try {
            Files.copy(file.getInputStream(), uploadPath.resolve(file.getOriginalFilename()));
        } catch (Exception ex) {
            throw new RuntimeException("Could not save the file. Error: " + ex.getMessage());
        }
        return String.format("{} Uploaded", file.getOriginalFilename());
    }

    public Resource getFile(String filename) {
        try {
            Path file = uploadPath.resolve(filename);
            Resource resource = new UrlResource(file.toUri());

            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new RuntimeException("Not able to read file: " + file.getFileName());
            }
        } catch (MalformedURLException ex) {
            throw new RuntimeException("Could not read the file! Error: " + ex.getMessage());
        }
    }

    public Stream<Path> listFiles() {
        try {
            // scan the upload directory and get list of files
            return Files.walk(uploadPath, maxDepth).filter(path -> !path.equals(uploadPath)).map(uploadPath::relativize);
        } catch (IOException ex) {
            throw new RuntimeException("Could not load the files! Error: " + ex.getMessage());
        }
    }

    public void deleteAllFiles(Path dirPath) {
        try {
            FileSystemUtils.deleteRecursively(dirPath);
        } catch (IOException ex) {
            throw new RuntimeException("Could not clear files! Error: " + ex.getMessage());
        }

    }
}
