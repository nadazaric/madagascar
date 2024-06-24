package ftn.rbs.madagascar_hub.controllers;

import ftn.rbs.madagascar_hub.dtos.AclDTO;
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
    private RestTemplate restTemplate;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> add(@Valid @RequestBody AclDTO dto) {
        //TODO: add validation check based on namespace config

        String url = "http://localhost:4000/acl";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<AclDTO> requestEntity = new HttpEntity<>(dto, headers);
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);
        return new ResponseEntity<>(response.getBody(), headers, HttpStatus.CREATED);
    }

    @PostMapping(path = "/check", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> check(@Valid @RequestBody AclDTO dto) {
        //TODO: add validation check based on namespace config

        String url = "http://localhost:4000/acl/check";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<AclDTO> requestEntity = new HttpEntity<>(dto, headers);
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);
        return new ResponseEntity<>(response.getBody(), headers, HttpStatus.OK);
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> delete(@Valid @RequestBody AclDTO dto) {
        //TODO: add validation check based on namespace config

        String url = "http://localhost:4000/acl";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<AclDTO> requestEntity = new HttpEntity<>(dto, headers);
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.DELETE, requestEntity, String.class);
        return new ResponseEntity<>(response.getBody(), headers, HttpStatus.OK);
    }
}
