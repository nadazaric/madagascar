package ftn.rbs.madagascar_hub.dtos;

import java.util.Map;

public class AddNamespaceDTO {
    private String namespace;
    private Map<String, AddRelationDTO> relations;

    public AddNamespaceDTO() {}

    public AddNamespaceDTO(String namespace, Map<String, AddRelationDTO> relations) {
        this.namespace = namespace;
        this.relations = relations;
    }

    public String getNamespace() {
        return namespace;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    public Map<String, AddRelationDTO> getRelations() {
        return relations;
    }

    public void setRelations(Map<String, AddRelationDTO> relations) {
        this.relations = relations;
    }
}
