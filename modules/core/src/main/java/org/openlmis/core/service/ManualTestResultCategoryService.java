/*
 * This program is part of the OpenLMIS logistics management information system platform software.
 * Copyright © 2013 VillageReach
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU Affero General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *  
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Affero General Public License for more details.
 * You should have received a copy of the GNU Affero General Public License along with this program.  If not, see http://www.gnu.org/licenses.  For additional information contact info@OpenLMIS.org. 
 */
package org.openlmis.core.service;


import org.openlmis.core.domain.ManualTestResultCategory;
import org.openlmis.core.domain.ManualTestResultType;
import org.openlmis.core.exception.DataException;
import org.openlmis.core.logging.Loggable;
import org.openlmis.core.logging.TableActionEnum;
import org.openlmis.core.repository.ManualTestResultCategoryRepository;
import org.openlmis.core.repository.ManualTestResultTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ManualTestResultCategoryService {

    @Autowired
    private ManualTestResultCategoryRepository repository;

    public List<ManualTestResultCategory> getAll(){
        return repository.getAll();
    }

    public ManualTestResultCategory getById(Long id){
        return repository.getById(id);
    }
    @Loggable(action = TableActionEnum.INSERT_ACTION)
    public void insert(ManualTestResultCategory type){
        repository.insert(type);
    }
    @Loggable(action = TableActionEnum.INSERT_ACTION)
    public void  update(ManualTestResultCategory type){
        repository.update(type);
    }
    @Loggable(action = TableActionEnum.INSERT_ACTION)
    public void save(ManualTestResultCategory testResultType) {
        try
        {
            if(testResultType.getId() == null)
                repository.insert(testResultType);
            else
                repository.update(testResultType);
        } catch (DuplicateKeyException e) {
                throw new DataException("Invalid code, the provided Result Category code already exists");
        }
    }
    @Loggable(action = TableActionEnum.INSERT_ACTION)
    public void remove(Long id) {
        repository.remove(id);
    }
}
