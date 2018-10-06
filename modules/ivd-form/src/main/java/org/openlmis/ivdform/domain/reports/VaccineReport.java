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

package org.openlmis.ivdform.domain.reports;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.openlmis.core.domain.BaseModel;
import org.openlmis.core.domain.Facility;
import org.openlmis.core.domain.ProcessingPeriod;
import org.openlmis.core.domain.ProgramProduct;
import org.openlmis.core.exception.DataException;
import org.openlmis.core.serializer.DateDeserializer;
import org.openlmis.demographics.domain.AnnualFacilityEstimateEntry;
import org.openlmis.ivdform.domain.*;
import org.openlmis.ivdform.domain.config.VaccineIvdTabVisibility;

import java.util.*;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VaccineReport extends BaseModel {

    private Long periodId;
    private Long programId;
    private Long facilityId;
    private ReportStatus status;
    private Long supervisoryNodeId;
    private ProcessingPeriod period;
    private Facility facility;
    private String majorImmunizationActivities;

    private Long fixedImmunizationSessions;
    private Long plannedOutreachImmunizationSessions;
    private Long outreachImmunizationSessions;
    private Long outreachImmunizationSessionsCanceled;

    @JsonDeserialize(using = DateDeserializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date submissionDate;

    private List<VaccineIvdTabVisibility> tabVisibilitySettings;

    private List<LogisticsLineItem> logisticsLineItems;
    @Deprecated
    private List<LogisticsLineItem> vaccineProductsLogisticsLineItems;
    private List<AdverseEffectLineItem> adverseEffectLineItems;
    private List<CampaignLineItem> campaignLineItems;

    private List<AnnualFacilityEstimateEntry> facilityDemographicEstimates;

    private List<VitaminSupplementationLineItem> vitaminSupplementationLineItems;

    private List<LogisticsColumn> columnTemplate;

    private List<VaccineCoverageItem> coverageLineItems;
    private List<DiseaseLineItem> diseaseLineItems;
    private List<ColdChainLineItem> coldChainLineItems;
    private List<ReportStatusChange> reportStatusChanges;

    private List<VaccineCoverageAgeGroupLineItem> coverageAgeGroupLineItems;

    public String rejectionComment;

    public String source;

    /*
      AdditionaL Line Items
    */
    private List<PmtctLineItem> pmtctLineItems;
    private List<ChildVisitLineItem> childVisitLineItems;
    private List<LLINLineItem> llInLineItemLists;
    private List<TtStatusLineItem> ttStatusLineItems;
    private List<BreastFeedingLineItem> breastFeedingLineItems;
    private List<WeightAgeRatioLineItem> weightAgeRatioLineItems;
    private List<HEIDLineItem> heidLineItems;


    public void initializeLogisticsLineItems(List<ProgramProduct> programProducts, VaccineReport previousReport, Boolean defaultFieldsToZero) {
        logisticsLineItems = new ArrayList<>();
        Map<String, LogisticsLineItem> previousLineItemMap = new HashMap<>();
        if (previousReport != null) {
            for (LogisticsLineItem lineItem : previousReport.getLogisticsLineItems()) {
                previousLineItemMap.put(lineItem.getProductCode(), lineItem);
            }
        }
        for (ProgramProduct pp : programProducts) {
            LogisticsLineItem item = new LogisticsLineItem();

            item.setReportId(id);

            item.setProductId(pp.getProduct().getId());
            item.setProductCode(pp.getProduct().getCode());
            item.setProductName(pp.getProduct().getPrimaryName());
            item.setProductCategory(pp.getProductCategory().getName());
            item.setDosageUnit(pp.getProduct().getDosageUnit().getCode());
            item.setDisplayOrder(pp.getDisplayOrder());

            if (previousReport != null) {
                LogisticsLineItem lineitem = previousLineItemMap.get(item.getProductCode());
                if (lineitem != null) {
                    item.setOpeningBalance(lineitem.getClosingBalance());
                    item.setOpeningBalanceFromPreviousPeriod(true);
                    item.setClosingBalance(item.getOpeningBalance());
                }
            }
            if (defaultFieldsToZero) {
                if (item.getOpeningBalance() == null) {
                    item.setOpeningBalance(0L);
                }
                item.setClosingBalance(item.getOpeningBalance());
                item.setQuantityReceived(0L);
                item.setQuantityIssued(0L);
                item.setQuantityVvmAlerted(0L);
                item.setQuantityExpired(0L);
                item.setQuantityDiscardedOpened(0L);
                item.setQuantityDiscardedUnopened(0L);
                item.setQuantityWastedOther(0L);
                item.setDaysStockedOut(0L);
                item.setTotalAdjustedQuantity(0L);
                item.setTransferInQuantity(0L);
                item.setTransferOutQuantity(0L);
            }

            logisticsLineItems.add(item);
        }

    }

    public void initializeDiseaseLineItems(List<VaccineDisease> diseases, Boolean defaultFieldsToZero) {
        diseaseLineItems = new ArrayList<>();
        for (VaccineDisease disease : diseases) {
            DiseaseLineItem lineItem = new DiseaseLineItem();
            lineItem.setReportId(id);
            lineItem.setDiseaseId(disease.getId());
            lineItem.setDiseaseName(disease.getName());
            lineItem.setDisplayOrder(disease.getDisplayOrder());

            if (defaultFieldsToZero) {
                lineItem.setCases(0L);
                lineItem.setDeath(0L);
            }

            diseaseLineItems.add(lineItem);
        }
    }

    public void initializeCoverageLineItems(List<VaccineProductDose> dosesToCover, Boolean defaultFieldsToZero) {
        coverageLineItems = new ArrayList<>();
        for (VaccineProductDose dose : dosesToCover) {

            VaccineCoverageItem item = new VaccineCoverageItem();
            item.setReportId(id);
            item.setDoseId(dose.getDoseId());
            item.setTrackMale(dose.getTrackMale());
            item.setTrackFemale(dose.getTrackFemale());
            item.setDisplayOrder(dose.getDisplayOrder());
            item.setDisplayName(dose.getDisplayName());
            item.setProductName(dose.getProductName());
            item.setProductId(dose.getProductId());

            if (defaultFieldsToZero) {
                item.setRegularMale(0L);
                item.setRegularFemale(0L);
                item.setCampaignMale(0L);
                item.setCampaignFemale(0L);
                item.setOutreachMale(0L);
                item.setOutreachFemale(0L);
                item.setRegularOutReachFeMale(0L);
                item.setRegularOutReachMale(0L);
            }

            coverageLineItems.add(item);
        }


    }


    public void initializeColdChainLineItems(List<ColdChainLineItem> lineItems, Boolean defaultFieldsToZero) {
        if (defaultFieldsToZero) {
            for (ColdChainLineItem lineItem : lineItems) {
                lineItem.setMinTemp(0F);
                lineItem.setMaxTemp(0F);

                lineItem.setMinEpisodeTemp(0F);
                lineItem.setMaxEpisodeTemp(0F);
            }
        }
        coldChainLineItems = lineItems;
    }

    public void initializeVitaminLineItems(List<Vitamin> vitamins, List<VitaminSupplementationAgeGroup> ageGroups, Boolean defaultFieldsToZero) {
        this.vitaminSupplementationLineItems = new ArrayList<>();
        Long displayOrder = 1L;
        for (Vitamin vitamin : vitamins) {
            for (VitaminSupplementationAgeGroup ageGroup : ageGroups) {
                VitaminSupplementationLineItem item = new VitaminSupplementationLineItem();
                item.setVitaminAgeGroupId(ageGroup.getId());
                item.setDisplayOrder(displayOrder);
                item.setVitaminName(vitamin.getName());
                item.setVaccineVitaminId(vitamin.getId());
                item.setAgeGroup(ageGroup.getName());
                if (defaultFieldsToZero) {
                    item.setMaleValue(0L);
                    item.setFemaleValue(0L);
                }

                this.vitaminSupplementationLineItems.add(item);
                displayOrder++;
            }
        }
    }

    public void validateBasicHeaders() {
        if (this.getPeriodId() == null || this.getFacilityId() == null || this.getProgramId() == null) {
            throw new DataException("error.ivd.form.submitted.misses.required.fields");
        }
    }

    public void copyValuesFrom(VaccineReport report) {
        this.setRejectionComment(report.rejectionComment);
        this.setFixedImmunizationSessions(report.fixedImmunizationSessions);
        this.setMajorImmunizationActivities(report.majorImmunizationActivities);
        this.setPlannedOutreachImmunizationSessions(report.plannedOutreachImmunizationSessions);
        this.setOutreachImmunizationSessions(report.outreachImmunizationSessions);
        this.setOutreachImmunizationSessionsCanceled(report.outreachImmunizationSessionsCanceled);
        this.setSubmissionDate(report.getSubmissionDate());
    }

    public void initializePmctLineItems(List<PmtctCategory> categoryList, Boolean defaultFieldsToZero) {
        this.pmtctLineItems = new ArrayList<>();

        Long displayOrder = 1L;
        for (PmtctCategory pmtctCategory : categoryList) {
            PmtctLineItem item = new PmtctLineItem();
            item.setCategoryId(pmtctCategory.getId());
            item.setCategory(pmtctCategory.getName());
            if (defaultFieldsToZero) {
                item.setMaleValue(0L);
                item.setFemaleValue(0L);
            }

            this.pmtctLineItems.add(item);
            displayOrder++;

        }
    }

    public void initializeChildVisitLineItems(List<ChildVisitAgeGroup> ageGroups, Boolean defaultFieldsToZero) {
        this.childVisitLineItems = new ArrayList<>();
        Long displayOrder = 1L;

        for (ChildVisitAgeGroup ageGroup : ageGroups) {
            ChildVisitLineItem item = new ChildVisitLineItem();
            item.setChildVisitAgeGroupId(ageGroup.getId());
            item.setChildVisitAgeGroupId(ageGroup.getId());
            item.setAgeGroup(ageGroup.getName());
            if (defaultFieldsToZero) {
                item.setMaleValue(0L);
                item.setFemaleValue(0L);
            }
            this.childVisitLineItems.add(item);
            displayOrder++;
        }
    }

    public void initializeLLINLineItems(Boolean defaultFieldsToZero) {
        this.llInLineItemLists = new ArrayList<>();
        LLINLineItem lineItem = new LLINLineItem();

            if (defaultFieldsToZero) {
                lineItem.setMaleValue(0L);
                lineItem.setFemaleValue(0L);
            }
            this.llInLineItemLists.add(lineItem);

    }

    public void initializeTtStatusLineItems(List<TtStatusCategory> categories, Boolean defaultFieldsToZero) {
        this.ttStatusLineItems = new ArrayList<>();
        Long displayOrder = 1L;
        for (TtStatusCategory statusCategory : categories) {

            TtStatusLineItem item = new TtStatusLineItem();

            item.setCategory(statusCategory.getName());
            item.setCategoryId(statusCategory.getId());

            if (defaultFieldsToZero) {
                item.setMaleValue(0L);
                item.setFemaleValue(0L);
            }
            this.ttStatusLineItems.add(item);
            displayOrder++;

        }
    }

    public void initializeBreastFeedingLineItems(List<BreastFeedingCategory> categoryList, List<BreastFeedingAgeGroup> ageGroups, Boolean defaultFieldsToZero) {
        this.breastFeedingLineItems = new ArrayList<>();

        Long displayOrder = 1L;
        for (BreastFeedingCategory feedingCategory : categoryList) {
            for (BreastFeedingAgeGroup ageGroup : ageGroups) {
                BreastFeedingLineItem item = new BreastFeedingLineItem();
                item.setAgeGroupId(ageGroup.getId());
                item.setAgeGroup(ageGroup.getName());
                item.setCategoryId(feedingCategory.getId());
                item.setCategory(feedingCategory.getName());
                if (defaultFieldsToZero) {
                    item.setMaleValue(0L);
                    item.setFemaleValue(0L);
                }

                this.breastFeedingLineItems.add(item);
                displayOrder++;
            }
        }
    }

    public void initializeWeightAgeRatioLineItems(List<WeightAgeRatioAgeGroup> ageGroups, List<WeightCategory> weightCategories, Boolean defaultFieldsToZero) {
        this.weightAgeRatioLineItems = new ArrayList<>();
        Long displayOrder = 1L;

        for (WeightCategory weightCategory : weightCategories) {

            for (WeightAgeRatioAgeGroup ageGroup : ageGroups) {

                WeightAgeRatioLineItem item = new WeightAgeRatioLineItem();
                item.setAgeGroupId(ageGroup.getId());
                item.setAgeGroup(ageGroup.getName());
                item.setCategory(weightCategory.getName());
                item.setCategoryId(weightCategory.getId());

                if (defaultFieldsToZero) {
                    item.setMaleValue(0L);
                    item.setFemaleValue(0L);
                }
                this.weightAgeRatioLineItems.add(item);
                displayOrder++;
            }
        }
    }


    public void initializeCoverageAgeGroupLineItems(List<VaccineProductDose> dosesToCover, List<VaccineProductDoseAgeGroup> ageGroups, Boolean defaultFieldsToZero) {
        coverageAgeGroupLineItems = new ArrayList<>();
        // for (VaccineProductDose dose : dosesToCover) {
        for (VaccineProductDoseAgeGroup ageGroup : ageGroups) {
            for (VaccineProductDose dose : dosesToCover) {
                VaccineCoverageAgeGroupLineItem item = new VaccineCoverageAgeGroupLineItem();

                if (dose.getDoseId().equals(ageGroup.getDoseId()) && dose.getProductId().equals(ageGroup.getProductId())) {

                    item.setReportId(id);
                    item.setDoseId(ageGroup.getDoseId());
                    item.setTrackMale(true);
                    item.setTrackFemale(true);
                    item.setDisplayOrder(ageGroup.getDisplayOrder());
                    item.setDisplayName(dose.getDisplayName());
                    item.setProductName(ageGroup.getProductName());
                    item.setProductId(ageGroup.getProductId());
                    item.setAgeGroupName(ageGroup.getAgeGroupName());
                    item.setAgeGroupId(ageGroup.getAgeGroupId());

                    if (defaultFieldsToZero) {
                        item.setRegularMale(0L);
                        item.setRegularFemale(0L);
                        item.setCampaignMale(0L);
                        item.setCampaignFemale(0L);
                        item.setOutreachMale(0L);
                        item.setOutreachFemale(0L);
                    }


                    coverageAgeGroupLineItems.add(item);
                }
            }
        }

        //  }
    }

    public void initializeHEIDLineItems(Boolean defaultFieldsToZero) {
        this.heidLineItems = new ArrayList<>();
        HEIDLineItem lineItem = new HEIDLineItem();
        if (defaultFieldsToZero) {
                lineItem.setMaleValue(0L);
                lineItem.setFemaleValue(0L);
        }
        this.heidLineItems.add(lineItem);
    }
}
