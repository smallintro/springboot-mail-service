package io.github.smallintro.emailsender;

import io.github.smallintro.emailsender.service.FileStorageService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import java.io.FileNotFoundException;
import java.nio.file.Paths;
import java.util.stream.Stream;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
class EmailSenderApplicationTests {

    @Test
    void contextLoads() {
    }

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FileStorageService fileStorageService;

    @Test
    public void shouldListAllFiles() throws Exception {
        given(this.fileStorageService.listFiles())
                .willReturn(Stream.of(Paths.get("first.txt"), Paths.get("second.txt")));

        this.mockMvc.perform(get("/")).andExpect(status().isOk())
                .andExpect(model().attribute("files",
                        Matchers.contains("http://localhost/files/first.txt",
                                "http://localhost/files/second.txt")));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void should404WhenMissingFile() throws Exception {
        given(this.fileStorageService.getFile("test.txt"))
                .willThrow(FileNotFoundException.class);
        this.mockMvc.perform(get("/files/test.txt")).andExpect(status().isNotFound());
    }

    @Test
    public void shouldSaveUploadedFile() throws Exception {
        MockMultipartFile multipartFile = new MockMultipartFile("file", "test.txt",
                "text/plain", "Spring Framework".getBytes());
        this.mockMvc.perform(multipart("/").file(multipartFile))
                .andExpect(status().isFound())
                .andExpect(header().string("Location", "/"));

        then(this.fileStorageService).should().saveFile(multipartFile);
    }
}
