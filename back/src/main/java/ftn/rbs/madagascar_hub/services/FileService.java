package ftn.rbs.madagascar_hub.services;

import ftn.rbs.madagascar_hub.dtos.FileUploadDTO;
import ftn.rbs.madagascar_hub.models.File;
import ftn.rbs.madagascar_hub.repositories.FileRepository;
import ftn.rbs.madagascar_hub.services.interfaces.IFileService;
import ftn.rbs.madagascar_hub.services.interfaces.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

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
}
