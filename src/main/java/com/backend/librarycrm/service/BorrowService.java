package com.backend.librarycrm.service;


import com.backend.librarycrm.model.BorrowRecord;

import java.util.List;

public interface BorrowService {
    BorrowRecord borrowBooks(Integer readerId, Integer librarianId, List<String> barcodes);

    BorrowRecord returnBooks(Integer recordId, List<String> returnedBarcodes, List<Double> damageFines);
}