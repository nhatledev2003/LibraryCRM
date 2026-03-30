package com.backend.librarycrm.repository;

import com.backend.librarycrm.model.BorrowDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BorrowDetailRepository extends JpaRepository<BorrowDetail, Integer> {}