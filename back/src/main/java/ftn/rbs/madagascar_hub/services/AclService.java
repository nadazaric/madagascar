package ftn.rbs.madagascar_hub.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import ftn.rbs.madagascar_hub.dtos.AclDTO;
import ftn.rbs.madagascar_hub.dtos.FrontAclDTO;
import ftn.rbs.madagascar_hub.dtos.SharedUserDTO;
import ftn.rbs.madagascar_hub.exceptions.NotValidAclException;
import ftn.rbs.madagascar_hub.models.User;
import ftn.rbs.madagascar_hub.models.File;
import ftn.rbs.madagascar_hub.repositories.UserRepository;
import ftn.rbs.madagascar_hub.services.interfaces.IAclService;
import ftn.rbs.madagascar_hub.services.interfaces.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class AclService implements IAclService {

    @Value("${zanzibar.path}")
    public String zanzibarPath;

    @Value("${zanzibar.namespace}")
    public String zanzibarNamespace;

    @Value("${madagascar.apikey.field}")
    public String madagascarApiKeyField;

    @Value("${madagascar.apikey}")
    public String madagascarApiKey;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private UserRepository allUsers;

    @Autowired
    private IUserService userService;

    @Autowired
    private FileService fileService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private boolean doesUserExist(String username) {
        Optional<User> foundUser = allUsers.findByUsername(username);
        return foundUser.isPresent();
    }
    private boolean isRequesterOwner(File file) {
        Long loggedUserId = userService.getCurrentUser().getId();
        return Objects.equals(file.getOwner().getId(), loggedUserId);
    }

    private void validateAclDto(FrontAclDTO dto, File file) {
        if (!isRequesterOwner(file) || !doesUserExist(dto.getUser()))
            throw new NotValidAclException();
    }

    private ResponseEntity<?> sendRequestToZanzibar(AclDTO dto, String endpoint, HttpMethod httpMethod) {
        String url = String.format("%s/%s", zanzibarPath, endpoint);

        System.out.println(url);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set(madagascarApiKeyField, madagascarApiKey);

        HttpEntity<AclDTO> requestEntity = new HttpEntity<>(dto, headers);
        return restTemplate.exchange(url, httpMethod, requestEntity, String.class);
    }

    private String formatObjectName(File file) {
        return zanzibarNamespace + ":" + file.getName() + file.getId();
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

    @Override
    public List<SharedUserDTO> getSharedWith(Long id) {
        File file = fileService.getFile(id);
        List<AclDTO> acls = getSharedAcls(file);
        if (acls == null) return new ArrayList<>();
        List<SharedUserDTO> shared = new ArrayList<>();
        for(AclDTO acl : acls) {
            SharedUserDTO sharedUserDTO = convertAclToSharedUserDTO(acl);
            shared.add(sharedUserDTO);
        }
        return shared;
    }

    @Override
    public SharedUserDTO getSharedInfoJsonAclString(String json) throws JsonProcessingException {
        JsonNode jsonNode = objectMapper.readTree(json);
        JsonNode entryNode = jsonNode.get("entry");
        AclDTO aclDto = objectMapper.treeToValue(entryNode, AclDTO.class);
        return convertAclToSharedUserDTO(aclDto);
    }

    private SharedUserDTO convertAclToSharedUserDTO(AclDTO acl) {
        String[] parts = acl.getUser().split(":");
        String username = parts[1];
        User user = userService.getUserByUsername(username);
        return new SharedUserDTO(
                String.format("%s %s", user.getName(), user.getSurname()),
                parts[1],
                acl.getRelation()
        );
    }
    
    private List<AclDTO> getSharedAcls(File file){
        String url = String.format("%s/%s/%s", zanzibarPath, "shared", formatObjectName(file));
        HttpHeaders headers = new HttpHeaders();
        headers.set(madagascarApiKeyField, madagascarApiKey);
        HttpEntity<AclDTO> requestEntity = new HttpEntity<>(headers);
        ResponseEntity<List<AclDTO>> response = restTemplate.exchange(url, HttpMethod.GET, requestEntity, new ParameterizedTypeReference<>() {});
        return response.getBody();
    }
}
