package ftn.rbs.madagascar_hub.dtos;
import java.util.Date;

public class FileDTO {
    private double size;
    private Date createdAt;
    private Long id;
    private Date lastModified;
    private Long ownerId;
    private String description;
    private String name;

    public FileDTO(){

    }

    public FileDTO(double size, Date createdAt, Date lastModified, Long ownerId, String description, String name) {
        this.size = size;
        this.createdAt = createdAt;
        this.lastModified = lastModified;
        this.ownerId = ownerId;
        this.description = description;
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getLastModified() {
        return lastModified;
    }

    public void setLastModified(Date lastModified) {
        this.lastModified = lastModified;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

