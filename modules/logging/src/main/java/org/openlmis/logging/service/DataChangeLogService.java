/*
 * This program is part of the OpenLMIS logistics management information system platform software.
 * Copyright © 2013 VillageReach
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU Affero General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *  
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Affero General Public License for more details.
 * You should have received a copy of the GNU Affero General Public License along with this program.  If not, see http://www.gnu.org/licenses.  For additional information contact info@OpenLMIS.org. 
 */
package org.openlmis.logging.service;


import org.apache.ibatis.session.RowBounds;
import org.openlmis.logging.converter.EntityConverterImp;
import org.openlmis.logging.domain.TransactionBatch;
import org.openlmis.logging.domain.TransactionHistory;
import org.openlmis.logging.domain.params.DataTransactionSearchParameter;
import org.openlmis.logging.repository.DataChangeLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class DataChangeLogService {
    @Autowired
    private DataChangeLogRepository repository;

    @Autowired
    private EntityConverterImp converterImp;

    public void logDataChangeTransaction() {
        TransactionBatch transactionBatch = prepareBatchTransactionBatch();
        if (transactionBatch != null && transactionBatch.getTransactionHistoryList() != null && !transactionBatch.getTransactionHistoryList().isEmpty()) {
            repository.logDataChangeTransaction(transactionBatch);
            transactionBatch.getTransactionHistoryList().stream().forEach(t -> {
                t.setTransactionBatch(transactionBatch);
                repository.attachBatchToTransaction(t);
            });
        }

    }

    public TransactionBatch prepareBatchTransactionBatch() {
        List<TransactionHistory> transactionHistoryList = this.repository.loadRecentUnBatchedTransactions();
        TransactionBatch batch = null;
        if (transactionHistoryList != null && !transactionHistoryList.isEmpty()) {
            batch = new TransactionBatch();
            batch.setCreatedDate(new Date());
            batch.setModifiedDate(new Date());
            UUID uuid = batch.generateUUID();
            batch.setUuid(uuid);
            batch.setTransactionHistoryList(transactionHistoryList);
        }
        return batch;
    }

    public List<TransactionBatch> loadTransactionBacthList(DataTransactionSearchParameter parameter, Map<String, String[]> sortCriteria,
                                                           RowBounds rowBounds,
                                                           Long userId) {
        List<TransactionBatch> transactionBatchList = null;
        if (parameter.getUuid() != null) {
            TransactionBatch batch = repository.searchTransactionBacthDate(parameter);
            if (batch != null) {
                parameter.setStartDate(batch.getCreatedDate());
            }
        }
        transactionBatchList = this.repository.searchTransactionBacthList(parameter, sortCriteria, rowBounds, userId);
        if (transactionBatchList != null && !transactionBatchList.isEmpty()) {
            transactionBatchList.forEach(tb -> {
                if(tb.getTransactionHistoryList()!=null && !tb.getTransactionHistoryList().isEmpty()){
                    tb.getTransactionHistoryList().forEach(t->converterImp.convert(t));
                }

            });
        }
        return transactionBatchList;
    }
}
