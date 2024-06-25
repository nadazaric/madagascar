package ftn.rbs.madagascar_hub.repositories;

import ftn.rbs.madagascar_hub.models.File;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FileRepository extends JpaRepository<File, Long> {
}
