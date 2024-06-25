package ftn.rbs.madagascar_hub.services;

import ftn.rbs.madagascar_hub.dtos.AclDTO;
import ftn.rbs.madagascar_hub.dtos.FrontAclDTO;
import ftn.rbs.madagascar_hub.models.User;
import ftn.rbs.madagascar_hub.models.File;
import ftn.rbs.madagascar_hub.repositories.FileRepository;
import ftn.rbs.madagascar_hub.repositories.UserRepository;
import ftn.rbs.madagascar_hub.services.interfaces.IAclService;
import ftn.rbs.madagascar_hub.services.interfaces.IUserService;
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

    @Autowired
    private IUserService userService;

    @Autowired
    private FileService fileService;

    private boolean doesUserExist(String username) {
        Optional<User> foundUser = allUsers.findByUsername(username);
        if (foundUser.isEmpty()) {
            return false;
        }

        return true;
    }
    private boolean isRequesterOwner(File file) {
        Long loggedUserId = userService.getCurrentUser().getId();
        return file.getOwner().getId() == loggedUserId;
    }

    private void validateAclDto(FrontAclDTO dto, File file) {
        if (!isRequesterOwner(file) || !doesUserExist(dto.getUser()))
            throw new RuntimeException("ACL not valid.");
    }

    private ResponseEntity<?> sendRequestToZanzibar(AclDTO dto, String endpoint, HttpMethod httpMethod) {
        String url = String.format("%s/%s", zanzibarPath, endpoint);

        System.out.println(url);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<AclDTO> requestEntity = new HttpEntity<>(dto, headers);
        return restTemplate.exchange(url, httpMethod, requestEntity, String.class);
    }

    private String formatObjectName(File file) {
        return file.getName() + file.getId();
    }

    public ResponseEntity<?> add(FrontAclDTO dto) {
        File file = fileService.getFile(dto.getFileId());
        AclDTO zanzibarDto = new AclDTO("user:" + dto.getUser(), dto.getRelation(), formatObjectName(file));
        validateAclDto(dto, file);

        return sendRequestToZanzibar(zanzibarDto, "acl", HttpMethod.POST);
    }

    @Override
    public ResponseEntity<?> update(FrontAclDTO dto) {
        File file = fileService.getFile(dto.getFileId());
        AclDTO zanzibarDto = new AclDTO("user:" + dto.getUser(), dto.getRelation(), formatObjectName(file));
        validateAclDto(dto, file);

        return sendRequestToZanzibar(zanzibarDto, "acl", HttpMethod.PUT);
    }

    @Override
    public ResponseEntity<?> delete(FrontAclDTO dto) {
        File file = fileService.getFile(dto.getFileId());
        AclDTO zanzibarDto = new AclDTO("user:" + dto.getUser(), dto.getRelation(), formatObjectName(file));
        validateAclDto(dto, file);

        return sendRequestToZanzibar(zanzibarDto, "acl", HttpMethod.DELETE);
    }

    @Override
    public ResponseEntity<?> check(FrontAclDTO dto) {
        File file = fileService.getFile(dto.getFileId());
        AclDTO zanzibarDto = new AclDTO("user:" + dto.getUser(), dto.getRelation(), formatObjectName(file));

        if (!doesUserExist(dto.getUser()))
            throw new RuntimeException("ACL not valid.");

        return sendRequestToZanzibar(zanzibarDto, "acl/check", HttpMethod.POST);
    }
}
