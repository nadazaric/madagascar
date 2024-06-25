package ftn.rbs.madagascar_hub.services.interfaces;

import ftn.rbs.madagascar_hub.dtos.AclDTO;
import org.springframework.http.ResponseEntity;

public interface IAclService {
    public ResponseEntity<?> add(AclDTO dto);
    public ResponseEntity<?> update(AclDTO dto);
    public ResponseEntity<?> delete(AclDTO dto);
    public ResponseEntity<?> check(AclDTO dto);
}
