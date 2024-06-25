package ftn.rbs.madagascar_hub.services;

import ftn.rbs.madagascar_hub.dtos.AclDTO;
import ftn.rbs.madagascar_hub.models.User;
import ftn.rbs.madagascar_hub.repositories.UserRepository;
import ftn.rbs.madagascar_hub.services.interfaces.IAclService;
import org.springframework.aop.scope.ScopedProxyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Service
public class AclService implements IAclService {

    @Value("${zanzibar.path}")
    public String zanzibarPath;

    @Value("${zanzibar.namespace}")
    public String zanzibarNamespace;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private UserRepository allUsers;

    private boolean doesFileExist(String filename) {
        //TODO: implement
        return true;
    }
    private boolean doesUserExist(String username) {
        Optional<User> foundUser = allUsers.findByUsername(username);
        if (foundUser.isEmpty()) {
            return false;
        }

        return true;
    }
    private boolean isRequesterOwner(String filename) {
        //TODO: implement
        return true;
    }

    private ResponseEntity<?> sendRequestToZanzibar(AclDTO dto, String endpoint, HttpMethod httpMethod) {
        String url = String.format("%s/%s", zanzibarPath, endpoint);

        dto.setUser("user:" + dto.getUser());
        dto.setObject(zanzibarNamespace + ":" + dto.getObject());

        System.out.println(url);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<AclDTO> requestEntity = new HttpEntity<>(dto, headers);
        return restTemplate.exchange(url, httpMethod, requestEntity, String.class);
    }

    public ResponseEntity<?> add(AclDTO dto) {
        if (!doesFileExist(dto.getObject()) || !isRequesterOwner(dto.getObject()) || !doesUserExist(dto.getUser()))
            throw new RuntimeException("ACL not valid.");

        return sendRequestToZanzibar(dto, "acl", HttpMethod.POST);
    }

    @Override
    public ResponseEntity<?> update(AclDTO dto) {
        if (!doesFileExist(dto.getObject()) || !isRequesterOwner(dto.getObject()) || !doesUserExist(dto.getUser()))
            throw new RuntimeException("ACL not valid.");

        return sendRequestToZanzibar(dto, "acl", HttpMethod.PUT);
    }

    @Override
    public ResponseEntity<?> delete(AclDTO dto) {
        if (!doesFileExist(dto.getObject()) || !isRequesterOwner(dto.getObject()) || !doesUserExist(dto.getUser()))
            throw new RuntimeException("ACL not valid.");

        return sendRequestToZanzibar(dto, "acl", HttpMethod.DELETE);
    }

    @Override
    public ResponseEntity<?> check(AclDTO dto) {
        if (!doesFileExist(dto.getObject()) || !isRequesterOwner(dto.getObject()) || !doesUserExist(dto.getUser()))
            throw new RuntimeException("ACL not valid.");

        return sendRequestToZanzibar(dto, "acl/check", HttpMethod.POST);
    }
}