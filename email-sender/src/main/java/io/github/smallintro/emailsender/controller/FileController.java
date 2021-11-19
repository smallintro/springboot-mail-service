package io.github.smallintro.emailsender.controller;

import io.github.smallintro.emailsender.model.FileInfo;
import io.github.smallintro.emailsender.service.FileStorageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@Slf4j
public class FileController {

    @Autowired
    private FileStorageService fileStorageService;

    @PostMapping(value = "/file/upload")
    public String uploadFile(@RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes) {
        log.info("Received uploadFile request for filename: {}", file.getOriginalFilename());
        try {
            String response = fileStorageService.saveFile(file);
            redirectAttributes.addFlashAttribute("message",
                    "You successfully uploaded " + file.getOriginalFilename() + "!");
        } catch (Exception ex) {
            log.error("Failed to upload file: {}. Error {}", file.getOriginalFilename(), ex.getMessage());
            redirectAttributes.addFlashAttribute("message",
                    "Failed to upload file " + file.getOriginalFilename() + "!");
        }
        return "redirect:/file";
    }

    @GetMapping(value = "/file/download/{filename:.+}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String filename) {
        log.info("Received downloadFile request for filename: {}", filename);
        try {
            Resource file = fileStorageService.getFile(filename);
            return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                    "attachment; filename=\"" + file.getFilename() + "\"").body(file);
        } catch (Exception ex) {
            log.error("Failed to download file: {}. Error {}", filename, ex.getMessage());
            return new ResponseEntity(filename + " not downloaded.\nError: " + ex.getMessage(),
                    HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(value = "/file")
    public String listFiles(Model model) {
        log.info("Received listFiles request");
        try {
            /*model.addAttribute("files", fileStorageService.listFiles().map(
                            path -> MvcUriComponentsBuilder.fromMethodName(FileController.class,
                                    "listFiles", path.getFileName().toString()).build().toUri().toString())
                    .collect(Collectors.toList()));*/
            List<FileInfo> files = fileStorageService.listFiles().map(path -> {
                String filename = path.getFileName().toString();
                String url = MvcUriComponentsBuilder
                        .fromMethodName(FileController.class, "downloadFile", path.getFileName().toString()).build().toString();
                return new FileInfo(filename, url);
            }).collect(Collectors.toList());
            model.addAttribute("files", files);
        } catch (Exception ex) {
            log.error("Failed to list files. Error {}", ex.getMessage());
            model.addAttribute("message", "Failed to get files. " + ex.getMessage());
        }
        return "file-manager";
    }

}
