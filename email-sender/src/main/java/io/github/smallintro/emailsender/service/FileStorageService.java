package io.github.smallintro.emailsender.service;

import io.github.smallintro.emailsender.config.AppConstants;
import io.github.smallintro.emailsender.util.ValidatorUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
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

    private final Path uploadPath = Paths.get(AppConstants.UPLOAD_PATH);

    private final int maxDepth = 1;

    public void createUploadPath(String dirPath) {
        try {
            Path filePath = StringUtils.hasLength(dirPath) ? Paths.get(dirPath) : uploadPath;
            Files.createDirectories(filePath);
        } catch (FileAlreadyExistsException e) {
            log.warn("Directory {} already exists ", e.getMessage());
        } catch (IOException ex) {
            log.error("Could not initialize folder for upload!\nError: " + ex.getMessage());
            // throw new RuntimeException("Could not initialize folder for upload!\nError: " + ex.getMessage());
        }
    }

    public String saveFile(MultipartFile file) {
        try {
            ValidatorUtil.validateFile(file.getName(), file.getSize(), file.getContentType());
            Files.copy(file.getInputStream(), uploadPath.resolve(file.getOriginalFilename()));
        } catch (Exception ex) {
            throw new RuntimeException("Could not save the file!\nError: " + ex.getMessage());
        }
        return String.format("{} Uploaded", file.getOriginalFilename());
    }

    public Resource getFile(String filename) throws FileNotFoundException {
        try {
            Path file = uploadPath.resolve(filename);
            Resource resource = new UrlResource(file.toUri());

            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new RuntimeException("Not able to read file: " + file.getFileName());
            }
        } catch (MalformedURLException ex) {
            throw new FileNotFoundException("Could not read the file!\nError: " + ex.getMessage());
        }
    }

    public Stream<Path> listFiles() {
        try {
            // scan the upload directory and get list of files
            return Files.walk(uploadPath, maxDepth).filter(path -> !path.equals(uploadPath)).map(uploadPath::relativize);
        } catch (IOException ex) {
            throw new RuntimeException("Could not load the files!\nError: " + ex.getMessage());
        }
    }

    public void deleteAllFiles(String dirPath) {
        try {
            FileSystemUtils.deleteRecursively(Paths.get(dirPath));
        } catch (IOException ex) {
            throw new RuntimeException("Could not clear files!\nError: " + ex.getMessage());
        }
    }
}
