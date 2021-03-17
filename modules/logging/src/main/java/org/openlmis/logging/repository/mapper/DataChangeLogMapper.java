/*
 * This program is part of the OpenLMIS logistics management information system platform software.
 * Copyright © 2013 VillageReach
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU Affero General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *  
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Affero General Public License for more details.
 * You should have received a copy of the GNU Affero General Public License along with this program.  If not, see http://www.gnu.org/licenses.  For additional information contact info@OpenLMIS.org. 
 */

package org.openlmis.logging.repository.mapper;

import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.ResultSetType;
import org.openlmis.logging.domain.TransactionBatch;
import org.openlmis.logging.domain.TransactionHistory;
import org.openlmis.logging.domain.params.DataTransactionSearchParameter;
import org.openlmis.logging.repository.builder.DataChangeLogQueryBuilder;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface DataChangeLogMapper {

    @Insert("insert into logging.t_history_batch " +
            "(uuid, " +
            "description," +
            " createdby," +
            "createddate," +
            " modifiedby," +
            "modifieddate) " +
            "values" +
            "(#{uuid, typeHandler=org.openlmis.core.utils.UUIDTypeHandler}::UUID, " +
            "#{description}," +
            " #{createdBy}, " +
            "#{createdDate}," +
            "#{modifiedBy}," +
            "#{modifiedDate})")
    @Options(useGeneratedKeys = true)
    public Integer insert(TransactionBatch dataChangeLog);


    @Select(" select * from logging.t_history t\n" +
            "where t.data_change_batch_id is null;")
    @Results(value = {
            @Result(property = "tstamp", column = "tstamp"),
            @Result(property = "schemaName", column = "equipmentTypeId"),
            @Result(property = "tabname", column = "programName"),
            @Result(property = "operation", column = "programName"),
            @Result(property = "who", column = "newVal"),
            @Result(property = "oldVal", column = "oldVal"),
            @Result(property = "oldVal", column = "sql")
    })
    public List<TransactionHistory> loadRecentUnBatchedTransactions();

    @Update("UPDATE logging.t_history\n" +
            "\tSET data_change_batch_uuid=#{transactionBatch.uuid, typeHandler=org.openlmis.core.utils.UUIDTypeHandler}::UUID, " +
            "data_change_batch_id= #{transactionBatch.id}" +
            "\tWHERE id= #{id};")
    void updateTransactionHistory(TransactionHistory t);

    @SelectProvider(type = DataChangeLogQueryBuilder.class, method = "getTransactionDate")
    @Options(resultSetType = ResultSetType.SCROLL_SENSITIVE, fetchSize = 10, timeout = 0, useCache = true, flushCache = true)
    @Results({
            @Result(property = "uuid", column = "uuid" , javaType = UUID.class, typeHandler = org.openlmis.core.utils.UUIDTypeHandler.class),
            @Result(property = "description", column = "description")
    })
    TransactionBatch searchTransactionBacthDate(@Param("filterCriteria") DataTransactionSearchParameter filterCriteria);

    @SelectProvider(type = DataChangeLogQueryBuilder.class, method = "getSearchQuery")
    @Options(resultSetType = ResultSetType.SCROLL_SENSITIVE, fetchSize = 10, timeout = 0, useCache = true, flushCache = true)
    @Results({
            @Result(property = "uuid", column = "uuid" , javaType = UUID.class, typeHandler = org.openlmis.core.utils.UUIDTypeHandler.class),
            @Result(property = "description", column = "description"),
            @Result(property = "transactionHistoryList", column = "uuid", javaType = List.class,
                    many = @Many(select = "getBachTransactions"))
    })
    List<TransactionBatch> searchTransactionBacthList(@Param("filterCriteria") DataTransactionSearchParameter filterCriteria);

    @SelectProvider(type = DataChangeLogQueryBuilder.class, method = "getBachTransactions")
    @Options(resultSetType = ResultSetType.SCROLL_SENSITIVE, fetchSize = 10, timeout = 0, useCache = true, flushCache = true)
    @Results(value = {
            @Result(property = "tstamp", column = "tstamp"),
            @Result(property = "schemaName", column = "equipmentTypeId"),
            @Result(property = "tabname", column = "programName"),
            @Result(property = "operation", column = "programName"),
            @Result(property = "newVal", column = "new_val"),
            @Result(property = "oldVal", column = "old_val"),
            @Result(property = "sql", column = "sql")
    })
    List<TransactionHistory> getBachTransactions(UUID filterCriteria);
}
