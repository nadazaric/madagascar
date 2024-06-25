package ftn.rbs.madagascar_hub.controllers;


import ftn.rbs.madagascar_hub.dtos.CredentialsDTO;
import ftn.rbs.madagascar_hub.dtos.FileUploadDTO;
import ftn.rbs.madagascar_hub.services.interfaces.IFileService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
}
