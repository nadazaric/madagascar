package ftn.rbs.madagascar_hub.services.interfaces;

import ftn.rbs.madagascar_hub.dtos.AclDTO;
import ftn.rbs.madagascar_hub.dtos.FileDTO;
import ftn.rbs.madagascar_hub.dtos.FrontAclDTO;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface IAclService {
    public ResponseEntity<?> add(FrontAclDTO dto);
    public ResponseEntity<?> update(FrontAclDTO dto);
    public ResponseEntity<?> delete(FrontAclDTO dto);
    public ResponseEntity<?> check(FrontAclDTO dto);
    public List<AclDTO> getSharedWith(Long file);
}
