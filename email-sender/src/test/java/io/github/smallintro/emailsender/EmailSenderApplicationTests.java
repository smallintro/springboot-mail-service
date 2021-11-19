package io.github.smallintro.emailsender;

import io.github.smallintro.emailsender.model.FileInfo;
import io.github.smallintro.emailsender.service.FileStorageService;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.io.FileNotFoundException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = EmailSenderApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
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

        MvcResult result = this.mockMvc.perform(get("/file")).andExpect(status().isOk()).andReturn();
        List<FileInfo> model = (List<FileInfo>) result.getModelAndView().getModel().get("files");

        List<String> fileUrls = new ArrayList<>();
        fileUrls.add("http://localhost/file/download/first.txt");
        fileUrls.add("http://localhost/file/download/second.txt");

        model.forEach(fileInfo -> assertThat(fileInfo.getUrl()).isIn(fileUrls));
    }


    @Test
    public void should404WhenMissingFile() throws Exception {
        given(this.fileStorageService.getFile("test.txt"))
                .willThrow(FileNotFoundException.class);
        this.mockMvc.perform(get("/file/download/test.txt")).andExpect(status().isBadRequest());
    }

    @Test
    public void shouldSaveUploadedFile() throws Exception {
        MockMultipartFile multipartFile = new MockMultipartFile("file", "test.txt",
                "text/plain", "Spring Framework".getBytes());
        this.mockMvc.perform(multipart("/file/upload").file(multipartFile))
                .andExpect(status().isFound())
                .andExpect(header().string("Location", "/file"));

        then(this.fileStorageService).should().saveFile(multipartFile);
    }
}
