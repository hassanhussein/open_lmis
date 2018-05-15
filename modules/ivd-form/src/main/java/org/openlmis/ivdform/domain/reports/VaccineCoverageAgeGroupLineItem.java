package org.openlmis.ivdform.domain.reports;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.openlmis.core.domain.BaseModel;
import org.openlmis.core.domain.Product;
import org.openlmis.ivdform.domain.VaccineProductDose;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VaccineCoverageAgeGroupLineItem extends BaseModel {

    private Boolean skipped = false;

    private Long reportId;
    private Long productId;

    private Product product;

    private Boolean trackMale;
    private Boolean trackFemale;
    private Long doseId;

    // this is needed to make the calculations of coverage on the front end.
    private VaccineProductDose vaccineProductDose;

    private Long displayOrder;
    private String displayName;
    private String productName;

    private Long regularMale;
    private Long regularFemale;

    private Long outreachMale;
    private Long outreachFemale;

    private Long campaignMale;
    private Long campaignFemale;

    private Long previousRegular;
    private Long previousOutreach;

    private Long ageGroupId;
    private String ageGroupName;

    public void copyValuesFrom(VaccineCoverageAgeGroupLineItem source){
        this.setRegularMale(source.getRegularMale());
        this.setRegularFemale(source.getRegularFemale());
        this.setCampaignMale(source.getCampaignMale());
        this.setCampaignFemale(source.getCampaignFemale());
        this.setOutreachMale(source.getOutreachMale());
        this.setOutreachFemale(source.getOutreachFemale());
        this.setAgeGroupId(source.getAgeGroupId());
        this.setAgeGroupName(source.getAgeGroupName());
    }


}
