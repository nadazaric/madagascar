package ftn.rbs.madagascar_hub.dtos;

public class FrontAclDTO {
    private String user;
    private String relation;
    private Long fileId;

    public FrontAclDTO() {
    }

    public FrontAclDTO(String user, String relation, Long fileId) {
        this.user = user;
        this.relation = relation;
        this.fileId = fileId;
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

    public Long getFileId() {
        return fileId;
    }

    public void setFileId(Long fileId) {
        this.fileId = fileId;
    }
}
