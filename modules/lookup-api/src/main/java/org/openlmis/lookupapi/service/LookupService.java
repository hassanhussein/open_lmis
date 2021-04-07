/*
 * Electronic Logistics Management Information System (eLMIS) is a supply chain management system for health commodities in a developing country setting.
 *
 * Copyright (C) 2015  John Snow, Inc (JSI). This program was produced for the U.S. Agency for International Development (USAID). It was prepared under the USAID | DELIVER PROJECT, Task Order 4.
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU Affero General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.openlmis.lookupapi.service;

import lombok.NoArgsConstructor;
import org.apache.ibatis.session.RowBounds;
import org.openlmis.core.domain.*;
import org.openlmis.core.domain.FacilityTypeApprovedProduct;
import org.openlmis.core.domain.GeographicLevel;
import org.openlmis.core.domain.ProcessingSchedule;
import org.openlmis.core.domain.ProgramProduct;
import org.openlmis.core.domain.ProgramSupported;
import org.openlmis.core.domain.RegimenCombinationConstituent;
import org.openlmis.core.domain.RegimenConstituentDosage;
import org.openlmis.core.domain.RegimenProductCombination;
import org.openlmis.core.repository.ManualTestTypeRepository;
import org.openlmis.core.repository.RegimenRepository;
import org.openlmis.core.repository.mapper.FacilityApprovedProductMapper;
import org.openlmis.core.repository.mapper.ProcessingScheduleMapper;
import org.openlmis.core.repository.mapper.ProgramProductMapper;
import org.openlmis.core.service.ProgramSupportedService;
import org.openlmis.equipment.domain.*;
import org.openlmis.equipment.domain.ColdChainEquipmentDesignation;
import org.openlmis.equipment.domain.ColdChainEquipmentPqsStatus;
import org.openlmis.equipment.domain.Equipment;
import org.openlmis.equipment.domain.EquipmentCategory;
import org.openlmis.equipment.domain.EquipmentEnergyType;
import org.openlmis.equipment.domain.EquipmentFunctionalTestTypes;
import org.openlmis.equipment.domain.EquipmentInventory;
import org.openlmis.equipment.domain.EquipmentModel;
import org.openlmis.equipment.domain.EquipmentOperationalStatus;
import org.openlmis.equipment.domain.EquipmentTestItems;
import org.openlmis.equipment.domain.EquipmentType;
import org.openlmis.equipment.domain.ProgramEquipmentType;
import org.openlmis.equipment.domain.ServiceType;
import org.openlmis.equipment.domain.Vendor;
import org.openlmis.equipment.repository.*;
import org.openlmis.lookupapi.mapper.DosageUnitReportMapper;
import org.openlmis.lookupapi.mapper.GeographicLevelReportMapper;
import org.openlmis.lookupapi.mapper.ILInterfaceMapper;
import org.openlmis.lookupapi.model.HealthFacilityDTO;
import org.openlmis.lookupapi.model.ProgramReferenceData;
import org.openlmis.report.mapper.lookup.*;


import org.openlmis.report.model.dto.*;
import org.openlmis.report.model.dto.DosageUnit;
import org.openlmis.report.model.dto.Facility;
import org.openlmis.report.model.dto.FacilityType;
import org.openlmis.report.model.dto.GeographicZone;
import org.openlmis.report.model.dto.ProcessingPeriod;
import org.openlmis.report.model.dto.Product;
import org.openlmis.report.model.dto.ProductCategory;
import org.openlmis.report.model.dto.Program;
import org.openlmis.report.model.dto.Regimen;
import org.openlmis.report.model.dto.RegimenCategory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;

@Service
@NoArgsConstructor
public class LookupService {

    public static final String FACILITY_CODE = "code";
    public static final String FACILITY_ID = "id";

    @Autowired
    private ProgramReportMapper programMapper;

    @Autowired
    private ProgramProductMapper programProductMapper;

    @Autowired
    private FacilityApprovedProductMapper facilityApprovedProductMapper;

    @Autowired
    private ProcessingScheduleMapper processingScheduleMapper;

    @Autowired
    private DosageUnitReportMapper dosageUnitMapper;

    @Autowired
    private RegimenRepository regimenRepository;

    @Autowired
    private GeographicLevelReportMapper geographicLevelMapper;

    @Autowired
    private FacilityLookupReportMapper facilityReportMapper;

    @Autowired
    private ProcessingPeriodReportMapper processingPeriodMapper;

    @Autowired
    private RegimenCategoryReportMapper regimenCategoryReportMapper;

    @Autowired
    private ProductCategoryReportMapper productCategoryMapper;

    @Autowired
    private AdjustmentTypeReportMapper adjustmentTypeReportMapper;

    @Autowired
    private ProductReportMapper productMapper;

    @Autowired
    private FacilityTypeReportMapper facilityTypeMapper;

    @Autowired
    private GeographicZoneReportMapper geographicZoneMapper;

    @Autowired
    private RegimenReportMapper regimenReportMapper;

    @Autowired
    private ILInterfaceMapper interfaceMapper;
    @Autowired
    private EquipmentTypeRepository equipmentTypeRepository;
    @Autowired
    private EquipmentFunctionalTestTypesRepository functionalTestTypesRepository;
    @Autowired
    private EquipmentCategoryRepository categoryRepository;
    @Autowired
    private ProgramEquipmentTypeRepository programEquipmentTypeRepository;
    @Autowired
    private EquipmentEnergyTypeRepository equipmentEnergyTypeRepository;
    @Autowired
    private EquipmentModelRepository modelRepository;
    @Autowired
    private EquipmentProductRepository equipmentProductRepository;
    @Autowired
    private EquipmentRepository equipmentRepository;
    @Autowired
    private VendorRepository vendorRepository;
    @Autowired
    private VendorUserRepository userRepository;
    @Autowired
    private ServiceTypeRepository serviceTypeRepository;
    @Autowired
    private EquipmentInventoryRepository inventoryRepository;
    @Autowired
    private ManualTestTypeRepository manualTestTypeRepository;
    @Autowired
    EquipmentOperationalStatusRepository statusRepository;
    @Autowired
    ColdChainEquipmentDesignationRepository designationRepository;
    @Autowired
    ColdChainEquipmentPqsStatusRepository equipmentPqsStatusRepository;
    @Autowired
    private DonorRepository donorRepository;
    @Autowired
    private EquipmentTestItemsRepository equipmentTestItemsRepository;
    @Autowired
    private ProgramSupportedService programSupportedService;

    public List<Program> getAllPrograms() {
        return programMapper.getAll();
    }

    public Program getProgramByCode(String code) {
        return programMapper.getProgramByCode(code);
    }

    public List<RegimenCategory> getAllRegimenCategories() {
        return regimenCategoryReportMapper.getAll();
    }

    public List<DosageFrequency> getAllDosageFrequencies() {
        return regimenRepository.getAllDosageFrequencies();
    }

    public List<RegimenProductCombination> getAllRegimenProductCombinations() {
        return regimenRepository.getAllRegimenProductCombinations();
    }

    public List<RegimenCombinationConstituent> getAllRegimenCombinationConstituents() {
        return regimenRepository.getAllRegimenCombinationConstituents();
    }

    public List<ProcessingSchedule> getAllProcessingSchedules() {
        return processingScheduleMapper.getAll();
    }

    public List<ProcessingPeriod> getAllProcessingPeriods() {
        return processingPeriodMapper.getAll();
    }

    public Product getProductByCode(String code) {
        return productMapper.getProductByCode(code);
    }

    public List<GeographicLevel> getAllGeographicLevels() {
        return geographicLevelMapper.getAll();
    }

    public List<org.openlmis.report.model.dto.GeographicZone> getAllZones() {
        return geographicZoneMapper.getAll();
    }

    public List<ProductCategory> getAllProductCategories() {
        return this.productCategoryMapper.getAll();
    }

    public List<org.openlmis.core.domain.Product> getFullProductList(RowBounds rowBounds) {
        return productMapper.getFullProductList(rowBounds);
    }

    public List<RegimenConstituentDosage> getAllRegimenConstituentDosages() {
        return regimenRepository.getAllRegimenConstituentsDosages();
    }

    public List<Regimen> getAllRegimens() {
        return regimenReportMapper.getAll();
    }

    public List<DosageUnit> getDosageUnits() {
        return dosageUnitMapper.getAll();
    }

    public List<FacilityType> getAllFacilityTypes() {
        return facilityTypeMapper.getAllFacilityTypes();
    }

    public List<Facility> getAllFacilities(RowBounds bounds) {
        return facilityReportMapper.getAll(bounds);
    }

    public Facility getFacilityByCode(String code) {
        return facilityReportMapper.getFacilityByCode(code);
    }

    public List<ProgramProduct> getAllProgramProducts() {
        List<ProgramProduct> programProductList=programProductMapper.getAll();
        return programProductList;
    }

    public List<FacilityTypeApprovedProduct> getAllFacilityTypeApprovedProducts() {
        return facilityApprovedProductMapper.getAll();
    }

    public List<AdjustmentType> getAllAdjustmentTypes() {
        return adjustmentTypeReportMapper.getAll();
    }

    @Transactional
    public void saveHFR(HealthFacilityDTO dto) {

        if (dto != null) {
            if (!dto.getIlIDNumber().isEmpty()) {
                HealthFacilityDTO hfr = interfaceMapper.getByTransactionId(dto.getIlIDNumber());
                //HealthFacilityDTO facilityDTO = interfaceMapper.getByFacilityCode(dto.getFacIDNumber());
                if (hfr == null) {
                    //if(facilityDTO == null) {
                    interfaceMapper.insert(dto);
        /*}else
          interfaceMapper.update(dto);*/

                } else {
                    // if (hfr.getFacIDNumber() != null){
                    interfaceMapper.update(dto);
                    // }
                }
            }

        }

    }


    public ProgramReferenceData getProgramReferenceData(String code, String facilityCode) {
        Facility facility = null;
        Program program = null;
        List<FacilityTypeApprovedProduct> facilityTypeApprovedProductList = null;
        List<ProcessingPeriod> processingPeriodList = null;
        ProgramReferenceData programReferenceData = new ProgramReferenceData();
        program = programMapper.getProgramByCode(code);
        facility = facilityReportMapper.getFacilityByCode(facilityCode);
        processingPeriodList = processingPeriodMapper.getPeriodsByProgramCode(code);
        if (facility != null && program != null) {
            facilityTypeApprovedProductList = facilityApprovedProductMapper.getAllByFacilityAndProgramId(Long.valueOf(facility.getId()), Long.valueOf(program.getId()));

            programReferenceData.setFacility(facility);
            programReferenceData.setProgram(program);
            programReferenceData.setFacilityTypeApprovedProductList(facilityTypeApprovedProductList);
            programReferenceData.setProcessingPeriodList(processingPeriodList);
        }
        return programReferenceData;
    }

    public List<HashMap<String, Object>> getAllHFRFacilities() {
        return interfaceMapper.getAllHFRFacilities();
    }

    public List<EquipmentType> getAllEquipmentTypes() {
        return this.equipmentTypeRepository.getAll();
    }

    public List<EquipmentFunctionalTestTypes> getAllEquipmentFunctionalTestTypesTypes() {
        return this.functionalTestTypesRepository.getAllEquipmentFunctionalTestTypeList();
    }

    public List<EquipmentCategory> getAllEquipmentCategories() {
        return this.categoryRepository.getAllEquipmentCategories();
    }

    public List<EquipmentEnergyType> getAllEquipmentEnergyTypes() {
        return this.equipmentEnergyTypeRepository.getAll();
    }

    public List<EquipmentModel> getAllEquipmentEquipmentModels() {
        return this.modelRepository.getAll();
    }


    public List<EquipmentProduct> getAllEquipmentProducts() {
        return this.equipmentProductRepository.getByProgramEquipmentId(4l);
    }

    public List<Equipment> getAllEquipmentsEquipments() {
        return this.equipmentRepository.getAllEquipments();
    }

    public List<Equipment> getAllEquipments() {
        return this.equipmentRepository.getAll();
    }

    public List<Vendor> getAllEquipmentServiceVendors() {
        return this.vendorRepository.getAll();
    }

    public List<VendorUser> getAllEquipmentServiceVendorUsers() {
        return this.userRepository.getAllVendorUsers();
    }

    public List<ServiceType> getAllEquipmentServiceTypes() {
        return this.serviceTypeRepository.getAll();

    }

    public List<ProgramEquipmentType> getAllEquipmentTypePrograms() {
        return this.programEquipmentTypeRepository.getAllEquipmentTypePrograms();
    }

    public List<EquipmentInventory> getAllEquipmentInventories() {
        return inventoryRepository.getAllEquipmentInventories();
    }

    public List<ManualTestType> getAllManualTestTypes() {
        return manualTestTypeRepository.getAll();
    }

    public ManualTestType getAllManualTestType(Long id) {
        return manualTestTypeRepository.getById(id);
    }

    public List<EquipmentOperationalStatus> getAllEquipmentOprationalStatuses() {
        return statusRepository.getAll();
    }

    public List<ColdChainEquipmentDesignation> getAllColdChainEquipmentDesignations() {
        return designationRepository.getAll();
    }

    public List<ColdChainEquipmentPqsStatus> getAllColdChainEquipmentPqsStatuses() {
        return equipmentPqsStatusRepository.getAll();
    }

    public List<Donor> getAllDonors() {
        return donorRepository.getAll();
    }


    public List<EquipmentInventory> getEquipmentInventoriesForFacility(String searchFacilityValue, String facilityInfo) {
        if (searchFacilityValue != null && !searchFacilityValue.trim().equals("")) {
            if (searchFacilityValue.equals(FACILITY_CODE)) {
                return inventoryRepository.getEquipmentInventoriesForFacilityByCode(facilityInfo);
            } else if (searchFacilityValue.equals(FACILITY_ID)) {
                Long facilityId = Long.parseLong(facilityInfo);
                return inventoryRepository.getEquipmentInventoriesForFacilityId(facilityId);

            }
        }
        return null;
    }

    public List<EquipmentTestItems> getAllEquipmentTestItems() {
        return equipmentTestItemsRepository.getAllEquipmentTestItems();
    }

    public List<ProgramSupported> getAllProgramsSupportedByFacilityCode(String facilityCode) {
        return programSupportedService.getAllByFacilityCode(facilityCode);
    }

    public List<ProgramSupported> getAllProgramsSupported() {
        return programSupportedService.getAll();
    }
}
