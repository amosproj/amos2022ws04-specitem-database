package amos.specitemdatabase.repo;

import amos.specitemdatabase.model.SpecItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;


public interface SpecItemRepo extends JpaRepository<SpecItemEntity, Long> {
}
