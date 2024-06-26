package ftn.rbs.madagascar_hub.dtos;

public class SharedUserDTO {
    private String fullName;
    private String username;
    private String relation;

    public SharedUserDTO(String fullName, String username, String relation) {
        this.fullName = fullName;
        this.username = username;
        this.relation = relation;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRelation() {
        return relation;
    }

    public void setRelation(String relation) {
        this.relation = relation;
    }
}
