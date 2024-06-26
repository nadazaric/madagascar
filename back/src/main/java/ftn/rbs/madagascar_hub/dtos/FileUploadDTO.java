package ftn.rbs.madagascar_hub.dtos;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;

import java.util.Date;

public class FileUploadDTO {
    @NotEmpty(message="is required")
    @Pattern(regexp = "^([a-zA-Zčćđžš0-9 ._-]*)$", message="format is not valid")
    private String name;

    private double size;
    private Date createdAt;
    private Date lastModified;

    @NotEmpty(message="is required")
    @Pattern(regexp = "^([a-zA-Zčćđžš0-9 ._]*)$", message="format is not valid")
    private String description;
    
    private String content;

    public FileUploadDTO(){}
    
    public FileUploadDTO(String name, double size, Date createdAt, Date lastModified, String description,
            String content) {
        this.name = name;
        this.size = size;
        this.createdAt = createdAt;
        this.lastModified = lastModified;
        this.description = description;
        this.content = content;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public double getSize() {
        return size;
    }
    public void setSize(double size) {
        this.size = size;
    }
    public Date getCreatedAt() {
        return createdAt;
    }
    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
    public Date getLastModified() {
        return lastModified;
    }
    public void setLastModified(Date lastModified) {
        this.lastModified = lastModified;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }

    
}
