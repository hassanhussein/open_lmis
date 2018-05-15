package org.openlmis.ivdform.service;

import org.openlmis.core.domain.Product;
import org.openlmis.core.domain.ProgramProduct;
import org.openlmis.core.repository.ProgramProductRepository;
import org.openlmis.ivdform.domain.CoverageLineItemAgeGroup;
import org.openlmis.ivdform.domain.VaccineProductDose;
import org.openlmis.ivdform.domain.VaccineProductDoseAgeGroup;
import org.openlmis.ivdform.dto.ProductDoseDTO;
import org.openlmis.ivdform.dto.VaccineProductDoseAgeGroupDTO;
import org.openlmis.ivdform.dto.VaccineServiceConfigDTO;
import org.openlmis.ivdform.repository.CoverageLineItemAgeGroupRepository;
import org.openlmis.ivdform.repository.ProductDoseAgeGroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ProductDoseAgeGroupService {

    @Autowired
    private ProductDoseAgeGroupRepository repository;

    @Autowired
    private ProgramProductRepository programProductRepository;

    @Autowired
    private CoverageLineItemAgeGroupRepository ageGroupRepository;


    public VaccineServiceConfigDTO getProductDoseForProgram(Long programId) {
        VaccineServiceConfigDTO dto = new VaccineServiceConfigDTO();

        List<VaccineProductDoseAgeGroupDTO> productDoseDTOs = new ArrayList<>();
        List<ProgramProduct> pp = programProductRepository.getActiveByProgram(programId);
        List<Product> products = new ArrayList<>();
        for (ProgramProduct p : pp) {
            if (!p.getProduct().getFullSupply()) {
                continue;
            }
            List<VaccineProductDoseAgeGroup> doses = repository.getDosesForProduct(programId, p.getProduct().getId());
            if (!doses.isEmpty()) {
                VaccineProductDoseAgeGroupDTO productDose = new VaccineProductDoseAgeGroupDTO();
                Long maxDisplayOrder = doses
                        .stream()
                        .map(VaccineProductDoseAgeGroup::getDisplayOrder)
                        .collect(Collectors.toList())
                        .stream().max(Comparator.naturalOrder())
                        .orElse(0L);

              //  productDose.setDisplayOrder(maxDisplayOrder);
                productDose.setProductId(p.getProduct().getId());
                productDose.setProductName(p.getProduct().getPrimaryName());
                productDose.setDoses(doses);
                productDoseDTOs.add(productDose);
            } else {
                //these are the possible other products.
                products.add(p.getProduct());
            }
        }
        dto.setPossibleDoses(repository.getAllDoses());
        dto.setPossibleProducts(products);
        dto.setVaccineProtocols(productDoseDTOs);

        return dto;
    }

    public void save(List<VaccineProductDoseAgeGroupDTO> productDoseDTOs,Long userId) {
       repository.deleteAllByProgram(productDoseDTOs.get(0).getDoses().get(0).getProgramId());
        for (VaccineProductDoseAgeGroupDTO productDoseDTO : productDoseDTOs) {
            for (VaccineProductDoseAgeGroup dose : productDoseDTO.getDoses()) {
                String age = (dose.getAgeGroupId() == null)?null:getById(dose.getAgeGroupId()).getName();
                dose.setAgeGroupName(age);
                dose.setCreatedBy(userId);
                dose.setModifiedBy(userId);
                repository.insert(dose);
            }
        }
    }

    public List<VaccineProductDoseAgeGroup> getProductDosesListByProgramProduct(Long programId, Long productId) {
        return repository.getDosesForProduct(programId, productId);
    }

    public List<CoverageLineItemAgeGroup>getAllAgeGroups(){
        return ageGroupRepository.getAll();
    }
    public CoverageLineItemAgeGroup getById(Long id){
        return ageGroupRepository.getBy(id);
    }

    public List<VaccineProductDoseAgeGroup> getProgramProductDoses(Long programId) {
        return repository.getProgramProductDoses(programId);
    }

}
