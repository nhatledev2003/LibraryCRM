package com.backend.librarycrm.repository;


import com.backend.librarycrm.model.PoDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PoDetailRepository extends JpaRepository<PoDetail, Integer> {
}
