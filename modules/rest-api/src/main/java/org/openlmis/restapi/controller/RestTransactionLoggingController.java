/*
 * This program is part of the OpenLMIS logistics management information system platform software.
 * Copyright © 2013 VillageReach
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU Affero General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *  
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Affero General Public License for more details.
 * You should have received a copy of the GNU Affero General Public License along with this program.  If not, see http://www.gnu.org/licenses.  For additional information contact info@OpenLMIS.org. 
 */
package org.openlmis.restapi.controller;

import org.openlmis.logging.domain.TransactionBatch;
import org.openlmis.logging.domain.params.DataTransactionSearchParameter;
import org.openlmis.restapi.response.RestResponse;
import org.openlmis.restapi.service.RestTransactionLoggingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.openlmis.restapi.response.RestResponse.response;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
public class RestTransactionLoggingController extends BaseController {
    public static final String DATA_TRANACTION_LOGS = "dataTransactionLogs";
    @Autowired
    private RestTransactionLoggingService service;

    @RequestMapping(value = "/rest-api/data-transaction-change-logs", method = POST, headers = BaseController.ACCEPT_JSON)
    public ResponseEntity<RestResponse> dataTransactionChangeLogs(@RequestBody DataTransactionSearchParameter parameter) {
        List<TransactionBatch> transactionBatchList = service.loadTransactionBatchHistory(parameter);
        return response(DATA_TRANACTION_LOGS, transactionBatchList);


    }

}
