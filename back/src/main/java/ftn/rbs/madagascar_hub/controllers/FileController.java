package ftn.rbs.madagascar_hub.controllers;


import ftn.rbs.madagascar_hub.dtos.*;
import ftn.rbs.madagascar_hub.models.File;
import ftn.rbs.madagascar_hub.services.interfaces.IAclService;
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

    @Autowired
    private  IAclService aclService;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> add(@Valid @RequestBody FileUploadDTO fileDTO){
        File file = fileService.addFile(fileDTO);
        aclService.add(new FrontAclDTO(
                file.getOwner().getUsername(),
                "owner",
                file.getId()
        ));
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
