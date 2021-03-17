/*
 * This program is part of the OpenLMIS logistics management information system platform software.
 * Copyright © 2013 VillageReach
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU Affero General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *  
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Affero General Public License for more details.
 * You should have received a copy of the GNU Affero General Public License along with this program.  If not, see http://www.gnu.org/licenses.  For additional information contact info@OpenLMIS.org. 
 */
package org.openlmis.logging.repository;

import org.apache.ibatis.session.RowBounds;
import org.openlmis.logging.domain.TransactionBatch;
import org.openlmis.logging.domain.TransactionHistory;
import org.openlmis.logging.domain.params.DataTransactionSearchParameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class DataChangeLogRepository {
    @Autowired
    private org.openlmis.logging.repository.mapper.DataChangeLogMapper mapper;

    public Integer logDataChangeTransaction(TransactionBatch dataChangeLog) {
        return mapper.insert(dataChangeLog);
    }

    public List<TransactionHistory> loadRecentUnBatchedTransactions() {

        return this.mapper.loadRecentUnBatchedTransactions();
    }

    public void attachBatchToTransaction(TransactionHistory t) {
        this.mapper.updateTransactionHistory(t);
    }

    public List<TransactionBatch> searchTransactionBacthList(DataTransactionSearchParameter parameter,Map<String, String[]> sortCriteria,
                                                              RowBounds rowBounds,
                                                             Long userId) {
        return this.mapper.searchTransactionBacthList( parameter);
    }
    public TransactionBatch searchTransactionBacthDate(DataTransactionSearchParameter parameter){
      return   this.mapper.searchTransactionBacthDate(parameter);
    }
}
