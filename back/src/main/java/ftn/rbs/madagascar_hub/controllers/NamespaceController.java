package ftn.rbs.madagascar_hub.controllers;

import ftn.rbs.madagascar_hub.dtos.AddNamespaceDTO;
import ftn.rbs.madagascar_hub.dtos.CredentialsDTO;
import ftn.rbs.madagascar_hub.dtos.RegisterUserDTO;
import ftn.rbs.madagascar_hub.dtos.TokenDTO;
import ftn.rbs.madagascar_hub.models.User;
import ftn.rbs.madagascar_hub.security.jwt.IJWTTokenService;
import ftn.rbs.madagascar_hub.security.jwt.TokenUtils;
import ftn.rbs.madagascar_hub.services.interfaces.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;

import jakarta.validation.Valid;
import org.springframework.web.client.RestTemplate;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/api/namespace")
public class NamespaceController {

    @Value("${madagascar.apikey.field}")
    public String madagascarApiKeyField;

    @Value("${madagascar.apikey}")
    public String madagascarApiKey;

    @Autowired
    private RestTemplate restTemplate;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> add(@Valid @RequestBody AddNamespaceDTO namespaceDTO) {
        String url = "http://localhost:4000/namespace";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set(madagascarApiKeyField, madagascarApiKey);

        HttpEntity<AddNamespaceDTO> requestEntity = new HttpEntity<>(namespaceDTO, headers);
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);

        return new ResponseEntity<>(response.getBody(), headers, HttpStatus.CREATED);
    }

    @GetMapping()
    public ResponseEntity<?> get(@RequestParam String namespace) {
//        String url = "http://localhost:4000/namespace?namespace=" + namespace;
//        System.out.println(url);
//        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        String url = "http://localhost:4000/namespace?namespace=" + namespace;
        System.out.println(url);

        HttpHeaders headers = new HttpHeaders();
        headers.set(madagascarApiKeyField, madagascarApiKey);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

        return new ResponseEntity<>(response.getBody(), HttpStatus.OK);
    }
}
