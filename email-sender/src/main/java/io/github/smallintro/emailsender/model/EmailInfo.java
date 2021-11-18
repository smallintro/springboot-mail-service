package io.github.smallintro.emailsender.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Data
@Validated
public class EmailInfo {
    @JsonProperty("subject")
    private String mailSubject;

    @JsonProperty("body")
    private String mailBody;

    @JsonProperty("to")
    private List<String> mailTo;

    @JsonProperty("cc")
    private List<String> mailCc;

    @JsonProperty("bcc")
    private List<String> mailBcc;

    @JsonProperty("has_attachment")
    private boolean isAttachment;
}
