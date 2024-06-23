package ftn.rbs.madagascar_hub.models;

import java.sql.Date;

import jakarta.persistence.*;

@Entity
@Table(name="files")
public class File {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;
    private Date lastModified;
    
    public File(){}

    public File(String name, String description, Date lastModified) {
        this.name = name;
        this.description = description;
        this.lastModified = lastModified;
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public Date getLastModified() {
        return lastModified;
    }
    public void setLastModified(Date lastModified) {
        this.lastModified = lastModified;
    }

    
}
