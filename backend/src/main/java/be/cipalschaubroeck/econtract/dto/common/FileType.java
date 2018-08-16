package be.cipalschaubroeck.econtract.dto.common;

import com.fasterxml.jackson.annotation.JsonValue;
import org.springframework.http.MediaType;

public enum FileType {
    PDF(MediaType.APPLICATION_PDF),
    XML(MediaType.TEXT_XML);

    private final MediaType mediaType;

    FileType(MediaType mediaType) {
        this.mediaType = mediaType;
    }

    @JsonValue
    public String getTextValue() {
        return mediaType.getType();
    }

    @JsonValue
    public MediaType getMediaType() {
        return mediaType;
    }
}
