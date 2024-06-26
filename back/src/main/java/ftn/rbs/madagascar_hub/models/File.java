package ftn.rbs.madagascar_hub.models;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Base64;

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
    private Date createdAt;
    private double size;

    @Lob
    @Transient
    private byte[] content;
    
    @ManyToOne(cascade = {})
    private User owner;

    public File() {}

    public File(String name, String description, Date lastModified, Date createdAt, double size) {
        this.name = name;
        this.description = description;
        this.lastModified = lastModified;
        this.createdAt = createdAt;
        this.size = size;
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
    public User getOwner() {
        return owner;
    }
    public void setOwner(User owner) {
        this.owner = owner;
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

    public String getContent() {
        if (this.content == null) 
			return null;
		try {
            return Base64.getEncoder().encodeToString(this.content);
// 			s = "data:image/jpeg;base64, ";
// 			s = s + new String(this.content, "UTF-8");
// //					Base64.getEncoder().encodeToString(this.profilePicture, "UTF-8");
// 			return s;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
    }

    public void setContent(String content) {
        String[] fileContent = content.split(",");
		if (fileContent.length >= 2) {
			// byte[] decoded = Base64.getDecoder().decode(picture[1], "-8");
			byte[] decoded;
			try {
				decoded = fileContent[1].getBytes("UTF-8");
				this.content = decoded;
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
    }

    
}
