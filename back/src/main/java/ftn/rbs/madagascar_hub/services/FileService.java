package ftn.rbs.madagascar_hub.services;

import ftn.rbs.madagascar_hub.dtos.FileDTO;
import ftn.rbs.madagascar_hub.dtos.FileUploadDTO;
import ftn.rbs.madagascar_hub.models.File;
import ftn.rbs.madagascar_hub.repositories.FileRepository;
import ftn.rbs.madagascar_hub.services.interfaces.IFileService;
import ftn.rbs.madagascar_hub.services.interfaces.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class FileService implements IFileService {

    @Autowired
    private IUserService userService;

    @Autowired
    private FileRepository allFiles;

    @Override
    public void addFile(FileUploadDTO dto) {
        File file = new File(dto.getName(), dto.getDescription(), dto.getLastModified(), dto.getCreatedAt(), dto.getSize());
        file.setOwner(userService.getCurrentUser());
        file.setContent(dto.getContent());
        allFiles.save(file);
        allFiles.flush();
    }

    @Override
    public File getFile(Long id) {
        Optional<File> foundFile = allFiles.findById(id);

        if (foundFile.isEmpty())
            throw new RuntimeException("File doesn't exist");

        return foundFile.get();
    }

    @Override
    public List<FileDTO> getAllFilesByUser() {
        List<File> files = allFiles.findByOwnerId(userService.getCurrentUser().getId());  // Method to be implemented in the repository
        return files.stream().map(this::convertToFileDTO).collect(Collectors.toList());
    }

    private FileDTO convertToFileDTO(File file) {
        FileDTO fileDTO = new FileDTO();
        fileDTO.setSize(file.getSize());
        fileDTO.setCreatedAt(file.getCreatedAt());
        fileDTO.setId(file.getId());
        fileDTO.setLastModified(file.getLastModified());
        fileDTO.setOwnerId(file.getOwner().getId());
        fileDTO.setDescription(file.getDescription());
        fileDTO.setName(file.getName());
        return fileDTO;
    }
}
