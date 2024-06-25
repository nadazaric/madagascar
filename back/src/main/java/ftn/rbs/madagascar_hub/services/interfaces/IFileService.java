package ftn.rbs.madagascar_hub.services.interfaces;

import ftn.rbs.madagascar_hub.dtos.FileUploadDTO;
import ftn.rbs.madagascar_hub.models.File;

public interface IFileService {
    public void addFile(FileUploadDTO dto);
    public File getFile(Long id);
}
