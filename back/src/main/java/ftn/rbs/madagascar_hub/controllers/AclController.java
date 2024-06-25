package ftn.rbs.madagascar_hub.controllers;

import ftn.rbs.madagascar_hub.dtos.AclDTO;
import ftn.rbs.madagascar_hub.dtos.FrontAclDTO;
import ftn.rbs.madagascar_hub.services.interfaces.IAclService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/api/acl")
public class AclController {

    @Autowired
    private IAclService aclService;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> add(@Valid @RequestBody FrontAclDTO dto) {
        ResponseEntity<?> response = aclService.add(dto);
        return new ResponseEntity<>(response.getBody(), HttpStatus.CREATED);
    }

    @PostMapping(path = "/check", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> check(@Valid @RequestBody FrontAclDTO dto) {
        ResponseEntity<?> response = aclService.check(dto);
        return new ResponseEntity<>(response.getBody(), HttpStatus.OK);
    }

    @PutMapping(path = "/delete", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> delete(@Valid @RequestBody FrontAclDTO dto) {
        ResponseEntity<?> response = aclService.delete(dto);
        return new ResponseEntity<>(response.getBody(), HttpStatus.OK);
    }

    @PutMapping(path = "/update", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> update(@Valid @RequestBody FrontAclDTO dto) {
        ResponseEntity<?> response = aclService.update(dto);
        return new ResponseEntity<>(response.getBody(), HttpStatus.OK);
    }
}
