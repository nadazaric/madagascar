package ftn.rbs.madagascar_hub.services.interfaces;

import ftn.rbs.madagascar_hub.dtos.FileDTO;
import ftn.rbs.madagascar_hub.dtos.FileUploadDTO;
import ftn.rbs.madagascar_hub.models.File;

import java.util.List;

public interface IFileService {
    public void addFile(FileUploadDTO dto);
    public File getFile(Long id);
    public List<FileDTO> getAllFilesByUser();
}
