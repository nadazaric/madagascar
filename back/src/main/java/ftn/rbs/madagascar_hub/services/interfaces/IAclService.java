package ftn.rbs.madagascar_hub.services.interfaces;

import com.fasterxml.jackson.core.JsonProcessingException;
import ftn.rbs.madagascar_hub.dtos.AclDTO;
import ftn.rbs.madagascar_hub.dtos.FileDTO;
import ftn.rbs.madagascar_hub.dtos.FrontAclDTO;
import ftn.rbs.madagascar_hub.dtos.SharedUserDTO;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface IAclService {
    public ResponseEntity<?> add(FrontAclDTO dto);
    public ResponseEntity<?> update(FrontAclDTO dto);
    public ResponseEntity<?> delete(FrontAclDTO dto);
    public ResponseEntity<?> check(FrontAclDTO dto);
    public List<SharedUserDTO> getSharedWith(Long file);
    public SharedUserDTO getSharedInfoJsonAclString(String json) throws JsonProcessingException;
}
