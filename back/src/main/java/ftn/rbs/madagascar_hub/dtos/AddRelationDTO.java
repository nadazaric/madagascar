package ftn.rbs.madagascar_hub.dtos;

import java.util.List;

public class AddRelationDTO {
    private List<String> union;

    public AddRelationDTO() {}
    public AddRelationDTO(List<String> union) {
        this.union = union;
    }

    public List<String> getUnion() {
        return union;
    }

    public void setUnion(List<String> union) {
        this.union = union;
    }
}
