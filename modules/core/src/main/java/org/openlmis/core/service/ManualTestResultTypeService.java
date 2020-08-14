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


import org.openlmis.core.domain.ManualTestResultType;
import org.openlmis.core.domain.ManualTestType;
import org.openlmis.core.exception.DataException;
import org.openlmis.core.repository.ManualTestResultTypeRepository;
import org.openlmis.core.repository.ManualTestTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ManualTestResultTypeService {

    @Autowired
    private ManualTestResultTypeRepository repository;

    public List<ManualTestResultType> getAll(){
        return repository.getAll();
    }

    public ManualTestResultType getById(Long id){
        return repository.getById(id);
    }

    public void insert(ManualTestResultType type){
        repository.insert(type);
    }

    public void  update(ManualTestResultType type){
        repository.update(type);
    }

    public void save(ManualTestResultType testResultType) {
        try
        {
            if(testResultType.getId() == null)
                repository.insert(testResultType);
            else
                repository.update(testResultType);
        } catch (DuplicateKeyException e) {
                throw new DataException("Invalid code, the provided Result Type code already exists");
        }
    }

    public void remove(Long id) {
        repository.remove(id);
    }
}
