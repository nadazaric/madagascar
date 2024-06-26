package ftn.rbs.madagascar_hub.controllers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import ftn.rbs.madagascar_hub.dtos.AclDTO;
import ftn.rbs.madagascar_hub.dtos.FrontAclDTO;
import ftn.rbs.madagascar_hub.dtos.SharedUserDTO;
import ftn.rbs.madagascar_hub.services.interfaces.IAclService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/api/acl")
public class AclController {

    @Autowired
    private IAclService aclService;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> add(@Valid @RequestBody FrontAclDTO dto) throws IOException {
        try {
            ResponseEntity<?> response = aclService.add(dto);
            String responseBody = (String) response.getBody();
            SharedUserDTO sharedUserDTO = aclService.getSharedInfoJsonAclString(responseBody);
            return new ResponseEntity<>(sharedUserDTO, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }


    @PostMapping(path = "/check", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> check(@Valid @RequestBody FrontAclDTO dto) {
        try {
            ResponseEntity<?> response = aclService.check(dto);
            return new ResponseEntity<>(response.getBody(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping(path = "/delete", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> delete(@Valid @RequestBody FrontAclDTO dto) {
        try{
            ResponseEntity<?> response = aclService.delete(dto);
            return new ResponseEntity<>(response.getBody(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping(path = "/update", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> update(@Valid @RequestBody FrontAclDTO dto) {
        try {
            ResponseEntity<?> response = aclService.update(dto);
            return new ResponseEntity<>(response.getBody(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/shared-with/{fileId}")
    public ResponseEntity<?> sharedWith(@PathVariable Long fileId) {
        try {
            List<SharedUserDTO> aclDTOS = aclService.getSharedWith(fileId);
            return new ResponseEntity<>(aclDTOS, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
