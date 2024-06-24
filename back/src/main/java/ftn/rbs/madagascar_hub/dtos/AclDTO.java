package ftn.rbs.madagascar_hub.dtos;

public class AclDTO {
    private String user;
    private String relation;
    private String object;
    public AclDTO() {}
    public AclDTO(String user, String filename, String role) {
        this.user = user;
        this.relation = role;
        this.object = filename;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getRelation() {
        return relation;
    }

    public void setRelation(String relation) {
        this.relation = relation;
    }

    public String getObject() {
        return object;
    }

    public void setObject(String object) {
        this.object = object;
    }
}
