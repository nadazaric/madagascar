package ftn.rbs.madagascar_hub.controllers;


import ftn.rbs.madagascar_hub.dtos.AclDTO;
import ftn.rbs.madagascar_hub.dtos.CredentialsDTO;
import ftn.rbs.madagascar_hub.dtos.FileDTO;
import ftn.rbs.madagascar_hub.dtos.FileUploadDTO;
import ftn.rbs.madagascar_hub.services.interfaces.IFileService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/api/file")
public class FileController {

    @Autowired
    private IFileService fileService;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> add(@Valid @RequestBody FileUploadDTO fileDTO){
        fileService.addFile(fileDTO);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/user")
    public ResponseEntity<List<FileDTO>> getAllFilesByUser() {
        List<FileDTO> files = fileService.getAllFilesByUser();
        return ResponseEntity.ok(files);
    }

    @GetMapping("/shared-with")
    public ResponseEntity<List<AclDTO>> getSharedWith() {
        List<AclDTO> acls = new ArrayList<AclDTO>();
        // TODO: call zanzibar
        return ResponseEntity.ok(acls);
    }

    @PostMapping(value="/share",consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> share(@Valid @RequestBody AclDTO aclDTO){
        // TODO: call zanzibar
//        fileService.share(aclDTO);
        return ResponseEntity.ok().build();
    }

    @PutMapping(value="/share",consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateShare(@Valid @RequestBody AclDTO aclDTO){
        // TODO: call zanzibar
//        fileService.share(aclDTO);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping(value="/share",consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> deleteShare(@Valid @RequestBody AclDTO aclDTO){
        // TODO: call zanzibar
//        fileService.share(aclDTO);
        return ResponseEntity.ok().build();
    }
}
