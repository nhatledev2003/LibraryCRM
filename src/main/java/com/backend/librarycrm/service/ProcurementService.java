package com.backend.librarycrm.service;

import com.backend.librarycrm.model.PurchaseOrder;

import java.util.List;

public interface ProcurementService {
    PurchaseOrder receiveOrder(Integer orderId);

    PurchaseOrder createOrder(Integer supplierId, List<Integer> bookIds, List<Integer> quantities, List<Double> unitPrices);
}