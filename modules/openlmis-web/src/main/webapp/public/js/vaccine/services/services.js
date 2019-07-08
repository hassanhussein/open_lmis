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

services.factory('VaccineDiseases', function ($resource) {
    return $resource('/vaccine/disease/all.json', {}, {});
});

services.factory('VaccineDisease', function ($resource) {
    return $resource('/vaccine/disease/get/:id.json', {id: '@id'}, {});
});

services.factory('ColdTraceStatus', function ($resource) {
    return $resource('/equipment/cold-trace/status.json', {}, {});
});

services.factory('ColdTraceAlarms', function ($resource) {
    return $resource('/equipments/cold-trace/:facility/:program/:period/alarms.json', {
        facility: '@facility',
        program: '@program',
        period: '@period'
    }, {});
});

services.factory('SaveVaccineDisease', function ($resource) {
    return $resource('/vaccine/disease/save.json', {}, update);
});


services.factory('SaveVaccineProductDose', function ($resource) {
    return $resource('/vaccine/product-dose/save.json', {}, update);
});
services.factory('SaveVaccineProductDoseAgeGroup', function ($resource) {
    return $resource('/vaccine/product-dose/saveProduct.json', {}, update);
});

services.factory('VaccineProductDose', function ($resource) {
    return $resource('/vaccine/product-dose/get/:programId.json', {productId: '@programId'}, {});
});
services.factory('VaccineProductDoseAgeGroup', function ($resource) {
    return $resource('/vaccine/product-dose/get_vaccine/:programId.json', {productId: '@programId'}, {});
});

services.factory('VaccineIvdTabConfigs', function ($resource) {
    return $resource('/vaccine/config/tab-visibility/:programId.json', {productId: '@programId'}, {});
});

services.factory('SaveVaccineIvdTabConfigs', function ($resource) {
    return $resource('/vaccine/config/save-tab-visibility.json', {}, update);
});


services.factory('VaccineReportConfigurablePrograms', function ($resource) {
    return $resource('/vaccine/report/programs.json', {}, {});
});

services.factory('VaccineSupervisedIvdPrograms', function ($resource) {
    return $resource('/vaccine/report/ivd-form/supervised-programs.json', {}, {});
});
services.factory('VimsVaccineSupervisedIvdPrograms', function ($resource) {
    return $resource('/vaccine/report/ivd-form/vims-supervised-programs.json', {}, {});
});
services.factory('VaccineHomeFacilityIvdPrograms', function ($resource) {
    return $resource('/vaccine/report/ivd-form/programs.json', {}, {});
});

services.factory('VaccineReportPrograms', function ($resource) {
    return $resource('/vaccine/report/programs.json', {}, {});
});

services.factory('VaccineReportFacilities', function ($resource) {
    return $resource('/vaccine/report/ivd-form/facilities/:programId.json', {programId: '@programId'}, {});
});

services.factory('VaccineReportPeriods', function ($resource) {
    return $resource('/vaccine/report/periods/:facilityId/:programId.json', {
        facilityId: '@facilityId',
        programId: '@programId'
    }, {});
});

services.factory('ViewVaccineReportPeriods', function ($resource) {
    return $resource('/vaccine/report/view-periods/:facilityId/:programId.json', {
        facilityId: '@facilityId',
        programId: '@programId'
    }, {});
});

services.factory('ApprovalPendingIvds', function ($resource) {
    return $resource('/vaccine/report/approval-pending.json', {}, {});
});

services.factory('VaccineReportInitiate', function ($resource) {
    return $resource('/vaccine/report/initialize/:facilityId/:programId/:periodId.json', {
        facilityId: '@facilityId',
        programId: '@programId',
        periodId: '@periodId'
    }, {});
});

services.factory('VaccineReport', function ($resource) {
    return $resource('/vaccine/report/get/:id.json', {id: '@id'}, {});
});

services.factory('VaccineReportSave', function ($resource) {
    return $resource('/vaccine/report/save.json', {}, update);
});

services.factory('VaccineReportSubmit', function ($resource) {
    return $resource('/vaccine/report/submit.json', {}, update);
});


services.factory('VaccineReportApprove', function ($resource) {
    return $resource('/vaccine/report/approve.json', {}, update);
});

services.factory('VaccineReportReject', function ($resource) {
    return $resource('/vaccine/report/reject.json', {}, update);
});


services.factory('VaccineColumnTemplate', function ($resource) {
    return $resource('/vaccine/columns/get/:id.json', {id: '@id'}, {});
});

services.factory('VaccineColumnTemplateSave', function ($resource) {
    return $resource('/vaccine/columns/save.json', {}, update);
});

services.factory('VaccineDiscardingReasons', function ($resource) {
    return $resource('/vaccine/discarding/reasons/all.json', {}, {});
});

services.factory('StockCards', function ($resource) {
    return $resource('/api/v2/facilities/:facilityId/stockCards?includeEmptyLots=false', {facilityId: '@facilityId'}, {});
});

services.factory('Forecast', function ($resource) {
    return $resource('/rest-api/facility/:facilityId/program/:programId/stockRequirements', {
        facilityId: '@facilityId',
        programId: '@programId'
    }, {});
});

services.factory('ProgramProducts', function ($resource) {
    return $resource('/programProducts/programId/:programId.json', {programId: '@programId'}, {});
});

services.factory('VaccineInventoryPrograms', function ($resource) {
    return $resource('/vaccine/inventory/programs.json', {}, {});
});

services.factory('SaveVaccineInventoryAdjustment', function ($resource) {
    return $resource('/vaccine/inventory/stock/adjustment.json', {}, {update: {method: 'PUT'}});
});

services.factory('VaccineAdjustmentReasons', function ($resource) {
    return $resource('/api/v2/stockManagement/adjustmentReasons.json', {programId: '@programId'}, {});
});

services.factory('VaccineProgramProducts', function ($resource) {
    return $resource('/vaccine/inventory/programProducts/programId/:programId.json', {}, {});
});

services.factory('ProductLots', function ($resource) {
    return $resource('/vaccine/inventory/lots/byProduct/:productId.json', {productId: '@productId'}, {});
});

services.factory('SaveVaccineInventoryReceived', function ($resource) {
    return $resource('/vaccine/inventory/stock/credit.json', {}, {update: {method: 'PUT'}});
});

services.factory('SaveVaccineInventoryConfigurations', function ($resource) {
    return $resource('/vaccine/inventory/configuration/save.json', {}, {update: {method: 'PUT'}});
});

services.factory('VaccineInventoryConfigurations', function ($resource) {
    return $resource('/vaccine/inventory/configuration/getProductConfigurations.json', {}, {});
});

services.factory('ManufacturerList', function ($resource) {
    return $resource('/vaccine/manufacturers.json', {}, {});
});


services.factory('StockEvent', function ($resource) {
    return $resource('/api/v2/facilities/:facilityId/stockCards', {facilityId: '@facilityId'}, {update: {method: "POST"}});
});


services.factory('VaccineReportPrograms', function ($resource) {
    return $resource('/vaccine/orderRequisition/programs.json', {}, {});
});


services.factory('VaccineOrderRequisitionReportPeriods', function ($resource) {
    return $resource('/vaccine/orderRequisition/periods/:facilityId/:programId.json', {
        facilityId: '@facilityId',
        programId: '@programId'
    }, {});
});

services.factory('ViewOrderRequisitionVaccineReportPeriods', function ($resource) {
    return $resource('/vaccine/orderRequisition/view-periods/:facilityId/:programId.json', {
        facilityId: '@facilityId',
        programId: '@programId'
    }, {});
});


services.factory('VaccineOrderRequisitionReportInitiate', function ($resource) {
    return $resource('/vaccine/orderRequisition/initialize/:periodId/:programId/:facilityId.json', {
        facilityId: '@facilityId',
        programId: '@programId',
        periodId: '@periodId'
    }, {});
});

services.factory('VaccineOrderRequisitionReportInitiateEmergency', function ($resource) {
    return $resource('/vaccine/orderRequisition/initializeEmergency/:periodId/:programId/:facilityId.json', {
        facilityId: '@facilityId',
        programId: '@programId',
        periodId: '@periodId'
    }, {});
});


services.factory('VaccineOrderRequisitionReport', function ($resource) {
    return $resource('/vaccine/orderRequisition/get/:id.json', {id: '@id'}, {});
});

services.factory('UserHomeFacility', function ($resource) {
    return $resource('/vaccine/orderRequisition/userHomeFacility.json', {}, {});
});


services.factory('UserPrograms', function ($resource) {
    return $resource('/reports/user-programs.json', {}, {});
});

services.factory('VaccineOrderRequisitionSubmit', function ($resource) {
    return $resource('/vaccine/orderRequisition/submit.json', {}, update);
});


services.factory('VaccineOrderRequisitionColumns', function ($resource) {
    return $resource('/vaccine/columns/get/columns.json', {}, {});
});

services.factory('VaccinePendingRequisitions', function ($resource) {
    return $resource('/vaccine/orderRequisition/getPendingRequest/:facilityId.json', {}, {});
});


services.factory('LoggedInUserDetails', function ($resource) {
    return $resource('/vaccine/orderRequisition/loggedInUserDetails.json', {}, {});
});

services.factory('ProgramForUserHomeFacility', function ($resource) {
    return $resource('/vaccine/orderRequisition/order-requisition/programs.json', {}, {});
});

services.factory('VaccineOrderRequisitionInsert', function ($resource) {
    return $resource('/vaccine/orderRequisition/initialize/:programId/:facilityId.json', {
        programId: '@programId',
        facilityId: '@facilityId'
    }, {});
});

services.factory('VaccineOrderRequisitionLastReport', function ($resource) {
    return $resource('/vaccine/orderRequisition/lastReport/:facilityId/:programId.json', {
        facilityId: '@facilityId',
        programId: '@programId'
    }, {});
});

services.factory('VaccineOrderRequisitionSave', function ($resource) {
    return $resource('/vaccine/orderRequisition/save.json', {}, update);
});

services.factory('VaccineOrderRequisitionSubmit', function ($resource) {
    return $resource('/vaccine/orderRequisition/submit.json', {}, update);
});

services.factory('VaccineHomeFacilityPrograms', function ($resource) {
    return $resource('/vaccine/orderRequisition/programs.json', {}, {});
});

services.factory('UpdateOrderRequisitionStatus', function ($resource) {
    return $resource('/vaccine/orderRequisition/updateOrderRequest/:orderId.json', {orderId: '@orderId'}, {update: {method: 'PUT'}});
});

services.factory('VaccineLastStockMovement', function ($resource) {
    return $resource('/vaccine/inventory/stock/lastReport.json', {}, {});
});

services.factory('SaveForecastConfiguration', function ($resource) {
    return $resource('/vaccine/inventory/configuration/saveForecastConfiguration.json', {}, {update: {method: 'PUT'}});
});

services.factory('VaccineForecastConfigurations', function ($resource) {
    return $resource('/vaccine/inventory/configuration/getAllForecastConfigurations.json', {}, {});
});

services.factory('FacilityDistributed', function ($resource) {
    return $resource('/vaccine/inventory/distribution/get-distributed.json', {}, {});
});

services.factory('SaveDistribution', function ($resource) {
    return $resource('/vaccine/inventory/distribution/save.json', {}, {save: {method: 'POST'}});
});

services.factory('DistributedFacilities', function ($resource) {
    return $resource('/vaccine/inventory/distribution/get-distributed.json', {}, {});
});

services.factory('EquipmentNonFunctional', function ($resource) {
    return $resource('/vaccine/inventory/dashboard/get-equipment-alerts', {}, {});
});

services.factory('OneLevelSupervisedFacilities', function ($resource) {
    return $resource('/vaccine/inventory/distribution/supervised-facilities/:programId.json', {programId: '@programId'}, {});
});
services.factory('ViewBundledDistributionVaccinationSupplies', function ($resource) {

    return $resource('/vaccine/report/view-bundled-distribution-vaccination-supplies/:year/:productId.json', {
        year: '@year',
        productId: '@productId'
    }, {});
});
services.factory('PerformanceByDropoutRateByDistrict', function ($resource) {
    return $resource('/vaccine/report/performanceByDropoutRateByDistrict.json', {}, {});
});
services.factory('TrendOfMinMasColdRange', function ($resource) {

    return $resource('/vaccine/report/trendOfMinMaxColdRange.json', {}, {});
});
services.factory('DropoutProducts', function ($resource) {
    return $resource('/vaccine/report/dropoutProducts.json', {}, {});
});

services.factory('PerformanceCoverage', function ($resource) {
    return $resource('/vaccine/report/performanceCoverage.json', {}, {});
});
services.factory('DenominatorName', function ($resource) {
    return $resource('/vaccine/report/denominatorName.json', {}, {});
});

services.factory('VaccineDashboardSummary', function ($resource) {

    return $resource('/vaccine/dashboard/summary.json', {}, {});
});

services.factory('VaccineDashboardMonthlyCoverage', function ($resource) {
    return $resource('/vaccine/dashboard/monthly-coverage.json', {}, {});
});

services.factory('repairingDetail', function ($resource) {
    return $resource('/vaccine/dashboard/repairing-details.json', {}, {});
});
services.factory('reportingDetail', function ($resource) {
    return $resource('/vaccine/dashboard/reporting-details.json', {}, {});
});
services.factory('InvestigatingDetails', function ($resource) {
    return $resource('/vaccine/dashboard/investigating-details.json', {}, {});
});
services.factory('VaccineDashboardMonthlyDropout', function ($resource) {
    return $resource('/vaccine/dashboard/monthly-dropout.json', {}, {});
});

services.factory('VaccineDashboardDistrictDropout', function ($resource) {
    return $resource('/vaccine/dashboard/district-dropout.json', {}, {});
});

services.factory('VaccineDashboardDistrictCoverage', function ($resource) {
    return $resource('/vaccine/dashboard/district-coverage.json', {}, {});
});

services.factory('VaccineDashboardMonthlyWastage', function ($resource) {
    return $resource('/vaccine/dashboard/monthly-wastage.json', {}, {});
});
services.factory('VaccineDashboardDistrictWastage', function ($resource) {
    return $resource('/vaccine/dashboard/district-wastage.json', {}, {});
});
services.factory('VaccineDashboardMonthlyStock', function ($resource) {
    return $resource('/vaccine/dashboard/monthly-stock.json', {}, {});
});
services.factory('VaccineDashboardDistrictStock', function ($resource) {
    return $resource('/vaccine/dashboard/district-stock.json', {}, {});
});
services.factory('VaccineDashboardFacilityStock', function ($resource) {
    return $resource('/vaccine/dashboard/facility-stock.json', {}, {});
});

services.factory('VaccineDashboardBundle', function ($resource) {
    return $resource('/vaccine/dashboard/bundle.json', {}, {});
});
services.factory('VaccineDashboardFacilityCoverage', function ($resource) {
    return $resource('/vaccine/dashboard/facility-coverage.json', {}, {});
});


services.factory('VaccineDashboardFacilityCoverageDetails', function ($resource) {
    return $resource('/vaccine/dashboard/facility-coverage-details.json', {}, {});
});

services.factory('VaccineDashboardFacilityWastage', function ($resource) {
    return $resource('/vaccine/dashboard/facility-wastage.json', {}, {});
});


services.factory('VaccineDashboardFacilityWastageDetails', function ($resource) {
    return $resource('/vaccine/dashboard/facility-wastage-details.json', {}, {});
});

services.factory('VaccineDashboardFacilitySessions', function ($resource) {
    return $resource('/vaccine/dashboard/facility-sessions.json', {}, {});
});


services.factory('VaccineDashboardFacilitySessionsDetails', function ($resource) {
    return $resource('/vaccine/dashboard/facility-sessions-details.json', {}, {});
});

services.factory('VaccineDashboardFacilityDropout', function ($resource) {
    return $resource('/vaccine/dashboard/facility-dropout.json', {}, {});
});


services.factory('VaccineDashboardFacilityDropoutDetails', function ($resource) {
    return $resource('/vaccine/dashboard/facility-dropout-details.json', {}, {});
});

services.factory('VaccineDashboardFacilityTrend', function ($resource) {
    return $resource('/vaccine/dashboard/facility-coverage.json', {},
        {
            coverage: {method: 'GET', params: {}, url: '/vaccine/dashboard/facility-coverage.json'},
            coverageDetails: {method: 'GET', params: {}, url: '/vaccine/dashboard/facility-coverage-details.json'},
            sessions: {method: 'GET', params: {}, url: '/vaccine/dashboard/facility-sessions.json'},
            sessionsDetails: {method: 'GET', params: {}, url: '/vaccine/dashboard/facility-sessions-details.json'},
            wastage: {method: 'GET', params: {}, url: '/vaccine/dashboard/facility-wastage.json'},
            wastageDetails: {method: 'GET', params: {}, url: '/vaccine/dashboard/facility-wastage-details.json'},
            dropout: {method: 'GET', params: {}, url: '/vaccine/dashboard/facility-dropout.json'},
            dropoutDetails: {method: 'GET', params: {}, url: '/vaccine/dashboard/facility-dropout-details.json'},
            stockDetails: {method: 'GET', params: {}, url: '/vaccine/dashboard/facility-stock-detail.json'}
        }
    );
});


services.factory('Distribution', function ($resource) {
    return $resource('/vaccine/inventory/distribution/by-voucher-number/', {}, {});
});


services.factory('ColdChainOperationalStatus', function ($resource) {
    return $resource('/equipment/type/operational-status.json', {}, {});
});

services.factory('VaccineDashboardSessions', function ($resource) {
    return $resource('/vaccine/dashboard/sessions.json', {}, {});
});

services.factory('VaccineDashboardDistrictSessions', function ($resource) {
    return $resource('/vaccine/dashboard/district-sessions.json', {}, {});
});

services.factory('CompletenessAndTimeliness', function ($resource) {
    return $resource('/reports/reportdata/completenessAndTimeliness.json', {}, {});
});

services.factory('AdequacyLevelOfSupply', function ($resource) {
    return $resource('/vaccine/report/adequaceyLevel.json', {}, {});
});

services.factory('StatuVaccinationSupply', function ($resource) {
    return $resource('/vaccine/report/statusOfVaccinationSupplyReceive.json', {}, {});
});

services.factory("SendVaccineMessages", function ($resource) {
    return $resource('/vaccine/messages/send.json', {}, {post: {method: 'POST'}});
});

services.factory('SupervisoryNodeByFacilityAndRequisition', function ($resource) {
    return $resource('/vaccine/orderRequisition/supervisoryNodeByFacilityAndRequisition/:facilityId.json', {facilityId: '@facilityId'}, {});
});


services.factory('ConsolidatedOrdersList', function ($resource) {
    return $resource('/vaccine/orderRequisition/getConsolidatedOrderList/:program/:facilityId.json', {
        program: '@program',
        facilityId: '@facilityId'
    }, {});
});

services.factory('PrintConsolidatedList', function ($resource) {
    return $resource('/vaccine/orderRequisition/consolidate/print/:facilityId.json', {facilityId: '@facilityId'}, {});
});

services.factory('SaveDistributionList', function ($resource) {
    return $resource('/vaccine/inventory/distribution/saveConsolidatedDistributionList.json', {}, {save: {method: 'POST'}});
});

services.factory('GetDistributionNotification', function ($resource) {
    return $resource('/vaccine/inventory/distribution/getAllDistributionsForNotification.json', {}, {});
});

services.factory('UpdateDistributionsForNotification', function ($resource) {
    return $resource('/vaccine/inventory/distribution/UpdateDistributionsForNotification/:id.json', {id: '@id'}, {});
});

services.factory("SendVaccineMessages", function ($resource) {
    return $resource('/vaccine/messages/send.json', {}, {post: {method: 'POST'}});
});

services.factory('RefreshStockRequirements', function ($resource) {
    return $resource('/rest-api/facility/:facilityId/program/:programId/refreshStockRequirements', {
        facilityId: '@facilityId',
        programId: '@programId'
    }, {});
});

services.factory('AllVaccineInventoryConfigurations', function ($resource) {
    return $resource('/vaccine/inventory/configuration/getAll.json', {}, {});
});

services.factory('Lot', function ($resource) {
    return $resource('/vaccine/inventory/lot/create.json', {}, {create: {method: 'PUT'}});
});

services.factory('FacilityTypeAndProgramProducts', function ($resource) {
    return $resource('/vaccine/inventory/programProducts/facilityId/:facilityId/programId/:programId.json', {
        facilityId: '@facilityId',
        programId: '@programId'
    }, {});
});

services.factory('GetFacilityForVaccineOrderRequisition', function ($resource) {
    return $resource('/vaccine/orderRequisition/facilities/:facilityId.json', {facilityId: '@facilityId'}, {});
});

services.factory('ClassificationVaccineUtilizationPerformance', function ($resource) {
    return $resource('/vaccine/report/classificationVaccineUtilizationPerformance.json', {}, {});
});
services.factory('CategorizationVaccineUtilizationPerformance', function ($resource) {
    return $resource('/vaccine/report/categorizationVaccineUtilizationPerformance.json', {}, {});
});

services.factory('QuantityRequired', function ($resource) {
    return $resource('/rest-api/ivd/facility-stock-status', {
        facilityCode: '@facilityCode',
        programCode: '@programCode',
        periodId: '@periodId'
    }, {});
});

services.factory('FacilityDistributionForecastAndLastPeriod', function ($resource) {
    return $resource('/vaccine/inventory/distribution/facility-distribution-forecast-lastPeriod/:facilityId/:programId.json', {
        facilityCode: '@facilityCode',
        programCode: '@programCode',
        periodId: '@periodId'
    }, {});
});

services.factory('DistributionWithSupervisorId', function ($resource) {
    return $resource('/vaccine/inventory/distribution/distribution-supervisorid/:facilityId', {facilityId: '@facilityId'}, {});
});


services.factory('PendingConsignmentNotification', function ($resource) {
    return $resource('/vaccine/inventory/distribution/pendingConsignmentNotification.json', {}, {});
});

services.factory('PendingNotificationForLowerLevel', function ($resource) {
    return $resource('/vaccine/inventory/distribution/pendingConsignmentNotificationForLowerLevel.json', {}, {});
});

services.factory('DistributionWithSupervisorId', function ($resource) {
    return $resource('/vaccine/inventory/distribution/distribution-supervisorid/:facilityId', {facilityId: '@facilityId'}, {});
});

services.factory('IsDistrictUser', function ($resource) {
    return $resource('/vaccine/dashboard/isDistrictUser.json', {}, {});
});

services.factory('verifyDistribution', function ($resource) {
    return $resource('/vaccine/orderRequisition/updateVerify/:orderId.json', {orderId: '@orderId'}, {update: {method: 'PUT'}});
});

services.factory('FacilitiesSameType', function ($resource) {
    return $resource('/vaccine/inventory/distribution/facilities/same-type/:facilityId/:query', {
        facilityId: '@facilityId',
        query: '@query'
    }, {});
});

services.factory('DistributionsByDate', function ($resource) {
    return $resource('/vaccine/inventory/distribution/get-by-date/:facilityId', {facilityId: '@facilityId'}, {});
});

services.factory('DistributionsByDateRange', function ($resource) {
    return $resource('/vaccine/inventory/distribution/get-by-date-range/:facilityId', {facilityId: '@facilityId'}, {});
});

services.factory('VaccineDashboardMonthlyStockStatus', function ($resource) {
    return $resource('/vaccine/dashboard/monthly-stock-status.json', {}, {});
});
services.factory('VaccineDashboardDistrictStockStatus', function ($resource) {
    return $resource('/vaccine/dashboard/district-stock-status.json', {}, {});
});
services.factory('VaccineDashboardFacilityStockStatus', function ($resource) {
    return $resource('/vaccine/dashboard/facility-stock-status.json', {}, {});
});
services.factory('VaccineDashboardFacilityStockStatusDetails', function ($resource) {
    return $resource('/vaccine/dashboard/facility-stock-status-details.json', {}, {});
});
services.factory('VaccineCurrentPeriod', function ($resource) {
    return $resource('/vaccine/dashboard/vaccine-current-period.json', {}, {});
});
services.factory('UserGeographicZonePereference', function ($resource) {
    return $resource('/vaccine/dashboard/user-geographic-zone-preference.json', {}, {});
});

services.factory('VaccineProductDoseList', function ($resource) {
    return $resource('/vaccine/product-dose/get/:programId/:productId.json', {}, {});
});

services.factory('VaccineDashboardFacilityInventoryStockStatus', function ($resource) {
    return $resource('/vaccine/dashboard/facility-inventory-stock-status.json', {}, {});
});

services.factory('VaccineDashboardSupervisedFacilityInventoryStockStatus', function ($resource) {
    return $resource('/vaccine/dashboard/supervised-facilities-inventory-stock-status.json', {}, {});
});

services.factory('VaccineTotalPendingRequisitions', function ($resource) {
    return $resource('/vaccine/orderRequisition/getTotalPendingRequest/:facilityId.json', {}, {});
});

services.factory('ExistingDistribution', function ($resource) {
    return $resource('/vaccine/inventory/distribution/get-if-exist/', {}, {});
});


services.factory('BatchExpiryNotification', function ($resource) {
    return $resource('/vaccine/inventory/distribution/getBatchExpiryNotification/', {}, {});
});

services.factory('CoefficientValues', function ($resource) {
    return $resource('/vaccine/report/coverageAndDropoutCoefficient.json', {}, {});
});

services.factory('SendIssueNotification', function ($resource) {
    return $resource('/vaccine/orderRequisition/sendNotification/:distributionId.json', {distributionId: '@distributionId'}, {});
});

services.factory('GetAllOneLevelFacilities', function ($resource) {
    return $resource('/vaccine/inventory/distribution/getOneLevelSuperVisedFacility.json', {}, {});
});

services.factory('GetSameLevelFacilities', function ($resource) {
    return $resource('/vaccine/inventory/distribution/getSameLevelFacilities.json', {}, {});
});

services.factory('GetDistributionsByDateRangeAndFacility', function ($resource) {
    return $resource('/vaccine/inventory/distribution/getDistributionsByDateRangeAndFacility.json', {}, {});
});

services.factory('DistributionByVoucherNumber', function ($resource) {
    return $resource('/vaccine/inventory/distribution/get-all-by-voucher-number/', {}, {});
});

services.factory('VaccineDistributionCompletenessReport', function ($resource) {
    return $resource('/vaccine/inventory/report/distributionCompleteness.json', {}, {});
});

services.factory('VaccineDistributedFacilitiesReport', function ($resource) {
    return $resource('/vaccine/inventory/report/getDistributedFacilities.json', {}, {});
});

services.factory('VaccineDistributionProgramProduct', function ($resource) {
    return $resource('/vaccine/orderRequisition/:programId.json', {programId: '@programId'}, {});
});


services.factory('searchDistributionsByDateRange', function ($resource) {
    return $resource('/vaccine/inventory/distribution/searh-by-date-range/:facilityId', {facilityId: '@facilityId'}, {});
});

services.factory('GetDistributionsByDateRangeForFacility', function ($resource) {
    return $resource('/vaccine/inventory/distribution/getDistributionsByDateRangeForFacility.json', {}, {});
});


services.factory('VaccineInventorySummary', function ($resource) {

    return $resource('/vaccine/dashboard/stock-status-over-view.json', {}, {});
});

services.factory('VaccineInventorySummaryDetails', function ($resource) {

    return $resource('/vaccine/dashboard/vaccineInventoryStockDetails.json', {}, {});
});
services.factory('GetVaccineInventoryDetails', function ($resource) {

    return $resource('/vaccine/dashboard/vaccineInventoryDetails.json', {}, {});
});

services.factory('GetVaccineInventoryFacilityDetails', function ($resource) {

    return $resource('/vaccine/dashboard/vaccineInventoryFacilityDetails.json', {}, {});
});

//Log Tag API
services.factory('GetAllLogTagTemperature', function ($resource) {

    return $resource('/log-tag-api/getLogTagTemps.json', {}, {});
});

services.factory('GetAllLogTagTemperatureById', function ($resource) {
    return $resource('/log-tag-api/byId/:id', {id: '@id'}, {});
});

services.factory('SaveLogTagTemperatureInfo', function ($resource) {
    return $resource('/log-tag-api/save', {}, {});
});

services.factory('ReceiveNotification', function ($resource) {
    return $resource('/vaccine/orderRequisition/receiveNotification', {}, {});
});

services.factory('ReceiveDistributionAlert', function ($resource) {
    return $resource('/vaccine/orderRequisition/receiveDistributionAlert', {}, {});
});

services.factory('MinimumStockNotification', function ($resource) {
    return $resource('/vaccine/orderRequisition/getMinimumStock', {}, {});
});

services.factory('AvailableStockDashboard', function ($resource) {

    return $resource('/vaccine/dashboard/availableStock.json', {}, {});
});


//Dashboard API

services.factory('NationalVaccineCoverageData', function ($q, $timeout, $resource, GetVaccineNationalCoverage) {

    function get(params) {
        var deferred = $q.defer();

        $timeout(function () {
            GetVaccineNationalCoverage.get({
                product: parseInt(params.product, 10),
                doseId: parseInt(params.dose, 10),
                periodId: parseInt(params.period, 10),
                year: parseInt(params.year, 10)
            }, function (data) {
                var coverage = [];
                if (data !== undefined) {
                    coverage = data.natioanl_coverage;
                }
                deferred.resolve(coverage);

            });

        }, 100);
        return deferred.promise;
    }

    return {
        get: get
    };

});


services.factory('Categorization', function ($q, $timeout, $resource, GetDistrictCategorization) {

    function get(param) {
        var deferred = $q.defer();
        $timeout(function () {
            GetDistrictCategorization.get({
                product: parseInt(param.product, 10),
                doseId: parseInt(param.dose, 10),
                periodId: parseInt(param.period, 10),
                year: parseInt(param.year, 10)
            }, function (data) {
                var coverage = [];
                if (data !== undefined) {
                    coverage = data.categories;
                }
                deferred.resolve(coverage);

            });

        }, 100);
        return deferred.promise;
    }

    return {
        get: get
    };

});

services.factory('ProductService', function ($q, $timeout, $resource, GetProductBy) {

    function get(product) {
        var deferred = $q.defer();
        $timeout(function () {
            GetProductBy.get({id: parseInt(product, 10)}, function (data) {
                var product = [];
                if (data !== undefined) {
                    product = data.productDTO.product.primaryName;
                }
                deferred.resolve(product);

            });

        }, 100);
        return deferred.promise;
    }

    return {
        get: get
    };

});
services.factory('GetFullStockAvailability', function ($q, $timeout, $resource, FullStockAvailableForDashboard) {

    function get(params) {
        var deferred = $q.defer();
        $timeout(function () {
            FullStockAvailableForDashboard.get({
                period: parseInt(params.period, 10),
                year: parseInt(params.year, 10)
            }, function (data) {
                var product = [];
                if (data !== undefined) {
                    product = data.fullStocks;
                }
                deferred.resolve(product);

            });

        }, 100);
        return deferred.promise;
    }

    return {
        get: get
    };

});


services.factory('AvailableStockData', function ($q, $timeout, $resource, AvailableStockDashboard) {

    function get(params) {
        var deferred = $q.defer();
        $timeout(function () {
            AvailableStockDashboard.get({
                product: parseInt(params.product, 10),
                period: parseInt(params.period, 10),
                year: parseInt(params.year, 10)
            }, function (data) {
                var product = [];
                if (data !== undefined) {
                    product = data.availableStock;
                }
                deferred.resolve(product);

            });

        }, 100);
        return deferred.promise;
    }

    return {
        get: get
    };

});


services.factory('GetAggregateFacilityPerformanceData', function ($q, $timeout, $resource, AggregateFacilityPerformance) {

    function get(params) {
        var deferred = $q.defer();
        $timeout(function () {
            AggregateFacilityPerformance.get({
                productId: parseInt(params.product, 10),
                periodId: parseInt(params.period, 10),
                year: parseInt(params.year, 10)
            }, function (data) {
                var product = [];
                if (data !== undefined) {
                    product = data.performance;
                }
                deferred.resolve(product);

            });

        }, 100);
        return deferred.promise;
    }

    return {
        get: get
    };

});


services.factory('VaccineCoverageByProductData', function ($q, $timeout, $resource, GetVaccineCoverageByRegionAndProduct) {

    function get(params) {
        var deferred = $q.defer();
        $timeout(function () {
            GetVaccineCoverageByRegionAndProduct.get({
                productId: parseInt(params.product, 10),
                periodId: parseInt(params.period, 10),
                year: parseInt(params.year, 10),
                doseId: parseInt(params.dose, 10)
            }, function (data) {
                var coverage = [];
                if (data !== undefined) {
                    coverage = data.coverage;
                }
                deferred.resolve(coverage);

            });

        }, 100);
        return deferred.promise;
    }

    return {
        get: get
    };

});


services.factory('GetPeriodForDashboard', function ($q, $timeout, $resource, ReportPeriodsByYear) {

    function get(params) {
        var deferred = $q.defer();
        $timeout(function () {
            ReportPeriodsByYear.get({year: parseInt(params, 10)}, function (data) {

                var period = {};
                if (data !== undefined) {
                    period = data.periods[0];
                }
                deferred.resolve(period);

            });

        }, 100);
        return deferred.promise;
    }

    return {
        get: get
    };

});

services.factory('FacilityInventoryStockStatusData', function ($q, $timeout, $resource, VaccineDashboardFacilityInventoryStockStatus) {

    function get(params) {
        var deferred = $q.defer();
        $timeout(function () {
            VaccineDashboardFacilityInventoryStockStatus.get({
                facilityId: parseInt(params.facilityId, 10),
                date: params.date
            }, function (data) {

                var stocks = {};
                if (data !== undefined) {
                    stocks = data.facilityStockStatus;
                }
                deferred.resolve(stocks);

            });

        }, 100);
        return deferred.promise;
    }

    return {
        get: get
    };

});

services.factory('GetCoverageByProductAndDoseData', function ($q, $timeout, $resource, GetVaccineNationalCoverageByProductAndDose) {

    function get(params) {
        var deferred = $q.defer();
        $timeout(function () {
            GetVaccineNationalCoverageByProductAndDose.get({
                periodId: parseInt(params.period, 10),
                year: params.year,
                product: parseInt(params.product, 10)
            }, function (data) {

                var stocks = {};
                if (data !== undefined) {
                    stocks = data.natioanl_coverage;
                }
                deferred.resolve(stocks);

            });

        }, 100);
        return deferred.promise;
    }

    return {
        get: get
    };

});

services.factory('GetRegionInventorySummaryData', function ($q, $timeout, $resource, GetVaccineRegionInventorySummary) {

    function get(params) {
        var deferred = $q.defer();
        $timeout(function () {
            GetVaccineRegionInventorySummary.get({}, function (data) {

                var stocks = {};
                if (data !== undefined) {
                    stocks = data.region_summary;
                }
                deferred.resolve(stocks);

            });

        }, 100);
        return deferred.promise;
    }

    return {
        get: get
    };

});

services.factory('GetDistrictInventorySummaryData', function ($q, $timeout, $resource, GetVaccineDistrictInventorySummary) {

    function get(params) {
        var deferred = $q.defer();
        $timeout(function () {
            GetVaccineDistrictInventorySummary.get({}, function (data) {

                var stocks = {};
                if (data !== undefined) {
                    stocks = data.district_summary;
                }
                deferred.resolve(stocks);

            });

        }, 100);
        return deferred.promise;
    }

    return {
        get: get
    };

});

services.factory('GetInventorySummaryByLocationData', function ($q, $timeout, $resource, GetInventorySummaryByLocation) {

    function get(params) {
        var deferred = $q.defer();
        $timeout(function () {
            GetInventorySummaryByLocation.get({facilityLevel: params.level, status: params.status}, function (data) {

                var stocks = {};
                if (data !== undefined) {
                    stocks = data.inventory_summary;
                }
                deferred.resolve(stocks);

            });

        }, 100);
        return deferred.promise;
    }

    return {
        get: get
    };

});

services.factory('GetInventorySummaryByMaterialData', function ($q, $timeout, $resource, GetInventorySummaryByMaterial) {

    function get(params) {
        var deferred = $q.defer();
        $timeout(function () {
            GetInventorySummaryByMaterial.get({facilityLevel: params.level, status: params.status}, function (data) {

                var stocks = {};
                if (data !== undefined) {
                    stocks = data.inventory_summary;
                }
                deferred.resolve(stocks);

            });

        }, 100);
        return deferred.promise;
    }

    return {
        get: get
    };

});

services.factory('GetGeoMapInfo', function ($q, $timeout, $resource, GetGeoMapData) {

    function get(params) {
        var deferred = $q.defer();
        $timeout(function () {
            GetGeoMapData.get({}, function (data) {

                var stocks = {};
                if (data !== undefined) {
                    stocks = data.geoZoneMapDTOList;
                }
                deferred.resolve(stocks);

            });

        }, 100);
        return deferred.promise;
    }

    return {
        get: get
    };

});


services.factory('GetCoverageMapInfo', function ($q, $timeout, $resource, GetCoverageForMap) {

    function get(params) {
        var deferred = $q.defer();
        $timeout(function () {
            GetCoverageForMap.get({
                product: parseInt(params.product, 10),
                periodId: parseInt(params.period, 10),
                year: parseInt(params.year, 10),
                doseId: parseInt(params.dose, 10)
            }, function (data) {

                var stocks = {};
                if (data !== undefined) {
                    stocks = data.map_data;
                }
                deferred.resolve(stocks);

            });

        }, 100);
        return deferred.promise;
    }

    return {
        get: get
    };

});

services.factory('GetInventoryByMaterialFacilityListData', function ($q, $timeout, $resource, GetInventorySummaryByMaterialFacilityList) {

    function get(params) {
        var deferred = $q.defer();
        $timeout(function () {
            GetInventorySummaryByMaterialFacilityList.get({
                product: params.product,
                facilityLevel: params.level,
                indicator: params.color
            }, function (data) {

                var stocks = {};
                if (data !== undefined) {
                    stocks = data.facility_list;
                }
                deferred.resolve(stocks);

            });

        }, 100);
        return deferred.promise;
    }

    return {
        get: get
    };

});

services.factory('GetCoverageByDistrictData', function ($q, $timeout, $resource, GetCoverageByDistrict) {
    function get(params) {

        var deferred = $q.defer();
        $timeout(function () {
            GetCoverageByDistrict.get({
                product: parseInt(params.product, 10),
                period: parseInt(params.period, 10),
                year: parseInt(params.year, 10),
                doseId: parseInt(params.dose, 10)
            }, function (data) {

                var stocks = {};
                if (data !== undefined) {
                    stocks = data.district_coverage;
                }
                deferred.resolve(stocks);

            });

        }, 100);
        return deferred.promise;
    }

    return {
        get: get
    };

});

services.factory('GetCoverageByFacilityData', function ($q, $timeout, $resource, GetCoverageByFacility) {
    function get(params) {

        var deferred = $q.defer();
        $timeout(function () {
            GetCoverageByFacility.get({
                product: parseInt(params.product, 10),
                period: parseInt(params.period, 10),
                year: parseInt(params.year, 10),
                doseId: parseInt(params.dose, 10)
            }, function (data) {

                var stocks = {};
                if (data !== undefined) {
                    stocks = data.district_coverage;
                }
                deferred.resolve(stocks);

            });

        }, 100);
        return deferred.promise;
    }

    return {
        get: get
    };

});

services.factory('GetCoverageByRegionSummary', function ($q, $timeout, $resource, GetCoverageByRegionSummaryData) {
    function get(params) {

        var deferred = $q.defer();
        $timeout(function () {
            GetCoverageByRegionSummaryData.get({
                period: parseInt(params.period, 10),
                year: parseInt(params.year, 10),
                doseId: parseInt(params.dose, 10),
                product: parseInt(params.product, 10)
            }, function (data) {

                var stocks = {};
                if (data !== undefined) {
                    stocks = data.region_coverage;
                }
                deferred.resolve(stocks);

            });

        }, 100);
        return deferred.promise;
    }

    return {
        get: get
    };

});

services.factory('GetCategorizationByDistrictData', function ($q, $timeout, $resource, GetCategorizationByDistrictSummary) {
    function get(params) {

        var deferred = $q.defer();
        $timeout(function () {
            GetCategorizationByDistrictSummary.get({year: parseInt(params.year, 10)}, function (data) {

                var stocks = {};
                if (data !== undefined) {
                    stocks = data.district_categorization;
                }
                deferred.resolve(stocks);

            });

        }, 100);
        return deferred.promise;
    }

    return {
        get: get
    };

});

services.factory('GetCategorizationByDistrictDrillDownData', function ($q, $timeout, $resource, GetCategorizationByDistrictDrillDown) {
    function get(params) {

        var deferred = $q.defer();
        $timeout(function () {
            GetCategorizationByDistrictDrillDown.get(params, function (data) {

                var stocks = {};
                if (data !== undefined) {
                    stocks = data.district_categorization_summary;
                }
                deferred.resolve(stocks);

            });

        }, 100);
        return deferred.promise;
    }

    return {
        get: get
    };

});

services.factory('GetDistrictClassificationSummaryData', function ($q, $timeout, $resource, GetDistrictClassificationSummary) {
    function get(params) {

        var deferred = $q.defer();
        $timeout(function () {
            GetDistrictClassificationSummary.get(params, function (data) {

                var stocks = {};
                if (data !== undefined) {
                    stocks = data.district_classification_summary;
                }
                deferred.resolve(stocks);

            });

        }, 100);
        return deferred.promise;
    }

    return {
        get: get
    };

});

services.factory('GetClassificationDistrictDrillDownData', function ($q, $timeout, $resource, GetDistrictClassificationDistrictDrillDown) {
    function get(params) {

        var deferred = $q.defer();
        $timeout(function () {
            GetDistrictClassificationDistrictDrillDown.get(params, function (data) {

                var stocks = {};
                if (data !== undefined) {
                    stocks = data.district_classification_summary;
                }
                deferred.resolve(stocks);

            });

        }, 100);
        return deferred.promise;
    }

    return {
        get: get
    };

});

services.factory('GetDistributionOfDistrictPerPerformanceData', function ($q, $timeout, $resource, GetDistributionOfDistrictPerPerformance) {
    function get(params) {

        var deferred = $q.defer();
        $timeout(function () {
            GetDistributionOfDistrictPerPerformance.get(params, function (data) {

                var stocks = {};
                if (data !== undefined) {
                    stocks = data.distribution_per_district;
                }
                deferred.resolve(stocks);

            });

        }, 100);
        return deferred.promise;
    }

    return {
        get: get
    };

});

services.factory('GetPerformanceMonitoringData', function ($q, $timeout, $resource, GetPerformanceMonitoring) {
    function get(params) {

        var deferred = $q.defer();
        $timeout(function () {
            GetPerformanceMonitoring.get(params, function (data) {

                var stocks = {};
                if (data !== undefined) {
                    stocks = data.performance;
                }
                deferred.resolve(stocks);

            });

        }, 100);
        return deferred.promise;
    }

    return {
        get: get
    };

});


services.factory('GetCategorizationByFacilityData', function ($q, $timeout, $resource, GetCategorizationByFacilitySummary) {
    function get(params) {

        var deferred = $q.defer();
        $timeout(function () {
            GetCategorizationByFacilitySummary.get({year: parseInt(params.year, 10)}, function (data) {

                var stocks = {};
                if (data !== undefined) {
                    stocks = data.facility_categorization;
                }
                deferred.resolve(stocks);

            });

        }, 100);
        return deferred.promise;
    }

    return {
        get: get
    };

});

services.factory('GetIVDReportingSummaryData', function ($q, $timeout, $resource, GetIVDReportingSummary) {
    function get(params) {

        var deferred = $q.defer();
        $timeout(function () {
            GetIVDReportingSummary.get({period: parseInt(params.period, 10)}, function (data) {

                var stocks = {};
                if (data !== undefined) {
                    stocks = data.reporting_summary;
                }
                deferred.resolve(stocks);

            });

        }, 100);
        return deferred.promise;
    }

    return {
        get: get
    };

});

services.factory('GetFacilityClassificationSummaryData', function ($q, $timeout, $resource, GetFacilityClassificationSummary) {
    function get(params) {

        var deferred = $q.defer();
        $timeout(function () {
            GetFacilityClassificationSummary.get(params, function (data) {

                var stocks = {};
                if (data !== undefined) {
                    stocks = data.facility_classification_summary;
                }
                deferred.resolve(stocks);

            });

        }, 100);
        return deferred.promise;
    }

    return {
        get: get
    };

});

services.factory('GetFacilityClassificationDrillDownData', function ($q, $timeout, $resource, GetFacilityClassificationDrillDownSummary) {
    function get(params) {

        var deferred = $q.defer();
        $timeout(function () {
            GetFacilityClassificationDrillDownSummary.get(params, function (data) {

                var stocks = {};
                if (data !== undefined) {
                    stocks = data.district_classification_summary;
                }
                deferred.resolve(stocks);

            });

        }, 100);
        return deferred.promise;
    }

    return {
        get: get
    };

});

services.factory('GetImmunizationSessionSummaryData', function ($q, $timeout, $resource, GetImmunizationSessionSummary) {
    function get(params) {

        var deferred = $q.defer();
        $timeout(function () {
            GetImmunizationSessionSummary.get(params, function (data) {

                var stocks = {};
                if (data !== undefined) {
                    stocks = data.session_summary;
                }
                deferred.resolve(stocks);

            });

        }, 100);
        return deferred.promise;
    }

    return {
        get: get
    };

});
services.factory('GetClassificationByDistrictSummaryData', function ($q, $timeout, $resource, GetClassificationByDistrictSummary) {
    function get(params) {

        var deferred = $q.defer();
        $timeout(function () {
            GetClassificationByDistrictSummary.get(params, function (data) {

                var stocks = {};
                if (data !== undefined) {
                    stocks = data.classification_summary;
                }
                deferred.resolve(stocks);

            });

        }, 100);
        return deferred.promise;
    }

    return {
        get: get
    };

});
services.factory('GetClassificationByDistrictDrillDownData', function ($q, $timeout, $resource, GetClassificationByDistrictDrillDownSummary) {
    function get(params) {

        var deferred = $q.defer();
        $timeout(function () {
            GetClassificationByDistrictDrillDownSummary.get(params, function (data) {

                var stocks = {};
                if (data !== undefined) {
                    stocks = data.classification_summary;
                }
                deferred.resolve(stocks);

            });

        }, 100);
        return deferred.promise;
    }

    return {
        get: get
    };

});

services.factory('GetDistributionByIdData', function ($q, $timeout, $resource, GetDistributionById) {
    function get(params) {

        var deferred = $q.defer();
        $timeout(function () {
            GetDistributionById.get(params, function (data) {

                var stocks = {};
                if (data !== undefined) {
                    stocks = data.distribution;
                }
                deferred.resolve(stocks);

            });

        }, 100);
        return deferred.promise;
    }

    return {
        get: get
    };

});

services.factory('GetFacilityStockStatusSummaryData', function ($q, $timeout, $resource, GetFacilityStockStatusSummary) {
    function get(params) {

        var deferred = $q.defer();
        $timeout(function () {
            GetFacilityStockStatusSummary.get(params, function (data) {

                var stocks = {};
                if (data !== undefined) {
                    stocks = data.stocks;
                }
                deferred.resolve(stocks);

            });

        }, 100);
        return deferred.promise;
    }

    return {
        get: get
    };

});

services.factory('GetFacilityStockStatusSummaryDataByPeriod', function ($q, $timeout, $resource, GetFacilityStockStatusSummaryByPeriodData) {
    function get(params) {

        var deferred = $q.defer();
        $timeout(function () {
            GetFacilityStockStatusSummaryByPeriodData.get(params, function (data) {

                var stocks = {};
                if (data !== undefined) {
                    stocks = data.stocks;
                }
                deferred.resolve(stocks);

            });

        }, 100);
        return deferred.promise;
    }

    return {
        get: get
    };

});

services.factory('FullStockAvailableForDashboard', function ($resource) {
    return $resource('/vaccine/dashboard/fullStockAvailability.json', {}, {});
});

services.factory('AggregateFacilityPerformance', function ($resource) {
    return $resource('/vaccine/dashboard/getNationalPerformance.json', {}, {});
});

services.factory('ReportingTarget', function ($resource) {
    return $resource('/vaccine/dashboard/reportingTarget.json', {}, {});
});

services.factory('GetDistrictCategorization', function ($resource) {
    return $resource('/vaccine/dashboard/categorization.json', {}, {});
});
services.factory('GetVaccineCoverageByRegionAndProduct', function ($resource) {
    return $resource('/vaccine/dashboard/VaccineCoverageByRegionAndProduct.json', {}, {});
});
services.factory('GetRejectedRnR', function ($resource) {
    return $resource('/reports/getRejectedRnR.json', {}, {});
});
services.factory('GetVaccineNationalCoverage', function ($resource) {
    return $resource('/vaccine/dashboard/VaccineNationalCoverage.json', {}, {});
});

services.factory('GetProductBy', function ($resource) {
    return $resource('/products/product/:id.json', {id: '@id'}, {});
});


services.factory('SaveSupervisoryDistribution', function ($resource) {
    return $resource('/vaccine/inventory/distribution/saveSupervisoryDistribution.json', {}, {save: {method: 'POST'}});
});

services.factory('GetVaccineNationalCoverageByProductAndDose', function ($resource) {
    return $resource('/vaccine/dashboard/VaccineNationalCoverageByProductAndDose.json', {}, {});
});

services.factory('GetVaccineRegionInventorySummary', function ($resource) {
    return $resource('/vaccine/dashboard/VaccineRegionInvetorySummary.json', {}, {});
});

services.factory('GetVaccineDistrictInventorySummary', function ($resource) {
    return $resource('/vaccine/dashboard/VaccineDistrictInvetorySummary.json', {}, {});
});

services.factory('GetInventorySummaryByLocation', function ($resource) {
    return $resource('/vaccine/dashboard/InventorySummaryByLocation.json', {}, {});
});
services.factory('GetInventorySummaryByMaterial', function ($resource) {
    return $resource('/vaccine/dashboard/InventorySummaryByMaterial.json', {}, {});
});

services.factory('GetGeoMapData', function ($resource) {
    return $resource('/geo-zone-map-data.json', {}, {});
});

services.factory('GetCoverageForMap', function ($resource) {
    return $resource('/vaccine/dashboard/GetCoverageForMap.json', {}, {});
});

services.factory('GetInventorySummaryByMaterialFacilityList', function ($resource) {
    return $resource('/vaccine/dashboard/getInventorySummaryByMaterialFacilityList.json', {}, {});
});
services.factory('GetCoverageByDistrict', function ($resource) {
    return $resource('/vaccine/dashboard/GetCoverageByDistrictSummary.json', {}, {});
});
services.factory('GetCoverageByFacility', function ($resource) {
    return $resource('/vaccine/dashboard/GetCoverageByFacility.json', {}, {});
});
services.factory('GetCategorizationByDistrictSummary', function ($resource) {
    return $resource('/vaccine/dashboard/GetCategorizationByDistrictSummary.json', {}, {});
});
services.factory('GetCategorizationByDistrictDrillDown', function ($resource) {
    return $resource('/vaccine/dashboard/GetCategorizationByDistrictDrillDown.json', {}, {});
});
services.factory('GetDistrictClassificationSummary', function ($resource) {
    return $resource('/vaccine/dashboard/GetDistrict_classification_summary.json', {}, {});
});

services.factory('GetDistrictClassificationDistrictDrillDown', function ($resource) {
    return $resource('/vaccine/dashboard/GetDistrict_classification_drill_down.json', {}, {});
});

services.factory('GetDistributionOfDistrictPerPerformance', function ($resource) {
    return $resource('/vaccine/dashboard/GetDistributionOfDistrictPerPerformance.json', {}, {});
});

services.factory('GetPerformanceMonitoring', function ($resource) {
    return $resource('/vaccine/dashboard/GetPerformanceMonitoring.json', {}, {});
});

services.factory('GetCoverageByRegionSummaryData', function ($resource) {
    return $resource('/vaccine/dashboard/GetCoverageByRegionSummary.json', {}, {});
});

services.factory('GetCategorizationByFacilitySummary', function ($resource) {
    return $resource('/vaccine/dashboard/GetCategorizationByFacilitySummary.json', {}, {});
});

services.factory('GetIVDReportingSummary', function ($resource) {
    return $resource('/vaccine/dashboard/GetIVDReportingSummary.json', {}, {});
});

services.factory('GetFacilityClassificationSummary', function ($resource) {
    return $resource('/vaccine/dashboard/GetFacilityClassification.json', {}, {});
});

services.factory('GetImmunizationSessionSummary', function ($resource) {
    return $resource('/vaccine/dashboard/GetImmunizationSessionSummary.json', {}, {});
});

services.factory('CoverageAgeGroups', function ($resource) {
    return $resource('/vaccine/product-dose/getAllAgeGroups.json', {}, {});
});


services.factory('GetClassificationByDistrictSummary', function ($resource) {
    return $resource('/vaccine/dashboard/GetClassificationByDistrictSummary.json', {}, {});
});

services.factory('GetClassificationByDistrictDrillDownSummary', function ($resource) {
    return $resource('/vaccine/dashboard/GetClassificationByDistrictDrillDown.json', {}, {});
});
services.factory('GetFacilityClassificationDrillDownSummary', function ($resource) {
    return $resource('/vaccine/dashboard/GetFacilityClassificationDrillDown.json', {}, {});
});
services.factory('GetFacilityDistributionNotifications', function ($resource) {
    return $resource('/vaccine/orderRequisition/getDistributionNotifications.json',{},{});
});

services.factory('GetDistributionById', function ($resource) {
    return $resource('/vaccine/inventory/distribution/distribution.json', {}, {});
});

services.factory('GetFacilityStockStatusSummary', function ($resource) {
    return $resource('/vaccine/dashboard/GetFacilityStockStatusSummary.json', {}, {});
});

services.factory('GetFacilityStockStatusSummaryByPeriodData', function ($resource) {
    return $resource('/vaccine/dashboard/GetFacilityStockStatusSummaryData.json', {}, {});
});