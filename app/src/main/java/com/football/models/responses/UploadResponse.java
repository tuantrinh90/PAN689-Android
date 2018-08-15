package com.football.models.responses;

import com.fasterxml.jackson.annotation.JsonProperty;

public class UploadResponse {

    @JsonProperty("url")
    private String url;
    @JsonProperty("file_name")
    private String fileName;
    @JsonProperty("file_machine_name")
    private String fileMachineName;
    @JsonProperty("file_type")
    private String fileType;
    @JsonProperty("file_mime_type")
    private String fileMimeType;
    @JsonProperty("file_size")
    private Integer fileSize;

    public UploadResponse() {
    }

    public String getUrl() {
        return url;
    }

    public String getFileName() {
        return fileName;
    }

    public String getFileMachineName() {
        return fileMachineName;
    }

    public String getFileType() {
        return fileType;
    }

    public String getFileMimeType() {
        return fileMimeType;
    }

    public Integer getFileSize() {
        return fileSize;
    }

    @Override
    public String toString() {
        return "UploadResponse{" +
                "url='" + url + '\'' +
                ", fileName='" + fileName + '\'' +
                ", fileMachineName='" + fileMachineName + '\'' +
                ", fileType='" + fileType + '\'' +
                ", fileMimeType='" + fileMimeType + '\'' +
                ", fileSize=" + fileSize +
                '}';
    }
}
