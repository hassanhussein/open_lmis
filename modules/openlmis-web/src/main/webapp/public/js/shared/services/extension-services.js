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
services.factory('MailingLabels', function ($resource) {
    return $resource('/reports/reportdata/mailingLabels.json', {}, {});
});

services.factory('ConsumptionReport', function ($resource) {
    return $resource('/reports/reportdata/consumption.json', {}, {});
});

services.factory('AverageConsumptionReport', function ($resource) {
    return $resource('/reports/reportdata/averageConsumption.json', {}, {});
});

services.factory('ReportRegimenCategories', function ($resource) {
    return $resource('/reports/regimenCategories.json', {}, {});
});

services.factory('ProductsByCategory', function ($resource) {
    return $resource('/reports/products_by_category.json', {}, {});
});

services.factory('ProductCategoriesByProgram', function ($resource) {
    return $resource('/reports/programs/:programId/productCategories.json', {}, {});
});

services.factory('SummaryReport', function ($resource) {
    return $resource('/reports/reportdata/summary.json', {}, {});
});
services.factory('SupplyStatusReport', function ($resource) {
    return $resource('/reports/reportdata/supply_status.json', {}, {});
});

services.factory('NonReportingFacilities', function ($resource) {
    return $resource('/reports/reportdata/non_reporting.json', {}, {});
});
services.factory('EmergencyRequests', function ($resource) {
    return $resource('/reports/reportdata/emergencyRequest.json', {}, {});
});
services.factory('RequisitionGroupsByProgramSchedule', function ($resource) {
    return $resource('/reports/reporting_groups_by_program_schedule.json', {}, {});
});

services.factory('RequisitionGroupsByProgram', function ($resource) {
    return $resource('/reports/reporting_groups_by_program.json', {}, {});
});

services.factory('AdjustmentSummaryReport', function ($resource) {
    return $resource('/reports/reportdata/adjustmentSummary.json', {}, {});
});

services.factory('OrderReport', function ($resource) {
    return $resource('/reports/reportdata/viewOrders.json', {}, {});
});

services.factory('StockImbalanceReport', function ($resource) {
    return $resource('/reports/reportdata/stockImbalance.json', {}, {});
});

services.factory('AdjustmentTypes', function ($resource) {
    return $resource('/reports/adjustmentTypes.json', {}, {});
});

services.factory('StockedOutReport', function ($resource) {
    return $resource('/reports/reportdata/stockedOut.json', {}, {});
});

services.factory('DistrictConsumptionReport', function ($resource) {
    return $resource('/reports/reportdata/districtConsumption.json', {}, {});
});

services.factory('AggregateConsumptionReport', function ($resource) {
    return $resource('/reports/reportdata/aggregateConsumption.json', {}, {});
});

services.factory('RnRFeedbackReport', function ($resource) {
    return $resource('/reports/reportdata/rnr_feedback.json', {}, {});
});

services.factory('OperationYears', function ($resource) {
    return $resource('/reports/operationYears.json', {}, {});
});
services.factory('Months', function ($resource) {
    return $resource('/reports/months.json', {}, {});
});

services.factory('ReportPrograms', function ($resource) {
    return $resource('/reports/programs.json', {}, {});
});

services.factory('ReportUserPrograms', function ($resource) {
    return $resource('/reports/user-programs.json', {}, {});
});


services.factory('ReportSchedules', function ($resource) {
    return $resource('/reports/schedules.json', {}, {});
});

services.factory('ReportProgramSchedules', function ($resource) {
    return $resource('/reports/schedules-by-program.json', {}, {});
});

services.factory('ReportFacilityTypes', function ($resource) {
    return $resource('/reports/facilityTypes.json', {}, {});
});

services.factory('ReportFacilityTypesByProgram', function ($resource) {
    return $resource('/reports/facilityTypesForProgram.json', {}, {});
});

services.factory('ReportFacilityLevels', function ($resource) {
    return $resource('/reports/facility-levels.json', {}, {});
});

services.factory('ReportRegimenCategories', function ($resource) {
    return $resource('/reports/regimenCategories.json', {}, {});
});

services.factory('ReportRegimensByCategory', function ($resource) {
    return $resource('/reports/regimenCategories/:regimenCategoryId/regimens.json', {}, {});
});

services.factory('GeographicLevels', function ($resource) {
    return $resource('/geographicLevels.json', {}, {});
});

services.factory('ReportGeographicZonesByLevel', function ($resource) {
    return $resource('/reports/geographicLevels/:geographicLevelId/zones.json', {}, {});
});

services.factory('FlatGeographicZoneList', function ($resource) {
    return $resource('/reports//geographic-zones/flat.json', {}, {});
});

services.factory('TreeGeographicZoneList', function ($resource) {
    return $resource('/reports//geographic-zones/tree.json', {}, {});
});

services.factory('TreeGeographicZoneListByProgram', function ($resource) {
    return $resource('/reports//geographic-zones/tree-program.json', {}, {});
});
services.factory('TreeGeographicTreeByProgramNoZones', function ($resource) {
    return $resource('/reports/geographic-zones/tree-no-zones.json', {}, {});
});
services.factory('ReportRegimens', function ($resource) {
    return $resource('/reports/regiments.json', {}, {});
});

services.factory('ColdChainEquipmentService', function ($resource) {
    return $resource('/reports/reportdata/coldChainEquipment.json', {}, {});
});

services.factory('FacilityList', function ($resource) {
    return $resource('/reports/reportdata/facilitylist.json', {}, {});
});

services.factory('ReportPeriods', function ($resource) {
    return $resource('/reports/schedules/:scheduleId/periods.json', {}, {});
});

services.factory('ReportPeriodsByScheduleAndYear', function ($resource) {
    return $resource('/reports/schedules/:scheduleId/year/:year/periods.json', {}, {});
});

services.factory('ReportGeographicZones', function ($resource) {
    return $resource('/reports/geographicZones.json', {}, {});
});

services.factory('GetFacilityByFacilityType', function ($resource) {
    return $resource('/facilities/facilityType/:facilityTypeId.json', {}, {});
});
services.factory('FacilityByFacilityType', function ($resource) {
    return $resource('/reports/facilitiesByType/:facilityTypeId.json', {}, {});
});
services.factory('FacilityByProgramByFacilityType', function ($resource) {
    return $resource('/reports/facilitiesByType.json', {}, {});
});
services.factory('SaveRequisitionGroupMember', function ($resource) {
    return $resource('/requisitionGroupMember/insert.json', {}, {});
});

services.factory('RemoveRequisitionGroupMember', function ($resource) {
    return $resource('/requisitionGroupMember/remove/:rgId/:facId.json', {}, {});
});

services.factory("FacilitiesByProgramParams", function ($resource) {
    return $resource('/reports/facilities.json', {}, {});
});

services.factory('Settings', function ($resource) {
    return $resource('/settings.json', {}, {});
});

services.factory('SettingsByKey', function ($resource) {
    return $resource('/settings/:key.json', {}, {});
});

services.factory('SettingsByGroup', function ($resource) {
    return $resource('/settings/group/:name.json', {}, {});
});

services.factory('SettingUpdator', function ($resource) {
    return $resource('/saveSettings.json', {}, {post: {method: 'POST'}});
});

services.factory('ProductDetail', function ($resource) {
    return $resource('/productDetail/:id.json', {}, {post: {method: 'GET'}});
});

services.factory('PriceHistory', function ($resource) {
    return $resource('/priceHistory/:productId.json', {}, {});
});

services.factory('ProgramCompleteList', function ($resource) {
    return $resource('/programs.json', {}, {});
});

services.factory('ScheduleCompleteList', function ($resource) {
    return $resource('/schedules.json', {}, {});
});

services.factory('LoadSchedulesForRequisitionGroupProgram', function ($resource) {
    return $resource('/requisitionGroupProgramSchedule/getDetails/:rgId/:pgId.json', {}, {});
});

services.factory('SaveRequisitionGroupProgramSchedule', function ($resource) {
    return $resource('/requisitionGroupProgramSchedule/insert.json', {}, {});
});

services.factory('RemoveRequisitionGroupProgramSchedule', function ($resource) {
    return $resource('/requisitionGroupProgramSchedule/remove/:id', {}, {});
});

services.factory('GetProgramsForAFacilityCompleteList', function ($resource) {
    return $resource('/facilities/:facilityId/programsList.json', {}, {});
});

services.factory('GetFacilityApprovedProductsCompleteList', function ($resource) {
    return $resource('/facilityApprovedProducts/facility/:facilityId/program/:programId/all.json', {}, {});
});

services.factory('GetFacilityProgramProductAlreadyAllowedList', function ($resource) {
    return $resource('/facility/:facilityId/program/:programId/programProductList.json', {}, {});
});

services.factory('GetFacilityTypeApprovedProductsCompleteList', function ($resource) {
    return $resource('/facilityApprovedProducts/facilityType/:facilityTypeId/program/:programId/all.json', {}, {});
});

services.factory('GetFacilityTypeProgramProductAlreadyAllowedList', function ($resource) {
    return $resource('/facilityApprovedProducts/:facilityTypeId/program/:programId/programProductList.json', {}, {});
});

services.factory('GetProductsCompleteListForAProgram', function ($resource) {
    return $resource('/programProducts/program/:programId/all.json', {}, {});
});

services.factory('ReportProductsByProgram', function ($resource) {
    return $resource('/reports/program-products/:programId.json', {}, {});
});
services.factory('VaccineProducts', function ($resource) {
    return $resource('/vaccine/report/vaccine_products.json', {}, {});
});
services.factory('GetApprovedProductForFacilityTypeDetail', function ($resource) {
    return $resource('/facilityApprovedProducts/facilityType/:facilityTypeId/program/:programId/product/:productId', {}, {});
});

services.factory('SaveApprovedProductForFacilityType', function ($resource) {
    return $resource('/facilityApprovedProducts/insert.json', {}, {});
});

services.factory('RemoveApprovedProductForFacilityType', function ($resource) {
    return $resource('/facilityApprovedProducts/remove/facilityType/:facilityTypeId/program/:programId/product/:productId', {}, {});
});

services.factory("SupplyingFacilities", function ($resource) {
    return $resource('/facility/supplyingFacilities.json', {}, {});
});

services.factory("GetRequisitionGroupsForSupervisoryNode", function ($resource) {
    return $resource('/requisitionGroup/getForSupervisoryNode/:supervisoryNodeId.json', {}, {});
});

services.factory('OrderFillRateReport', function ($resource) {
    return $resource('/reports/reportdata/orderFillRate.json', {}, {});
});

services.factory('RegimenSummaryReport', function ($resource) {
    return $resource('/reports/reportdata/regimenSummary.json', {}, {});
});

services.factory('AggregateRegimenSummaryReport', function ($resource) {
    return $resource('/reports/reportdata/aggregateRegimenSummary.json', {}, {});
});

//It populate all programs with regimens
services.factory('ReportRegimenPrograms', function ($resource) {
    return $resource('/reports/programs-supporting-regimen.json', {}, {});
});

services.factory('DistrictFinancialSummaryReport', function ($resource) {
    return $resource('/reports/reportdata/districtFinancialSummary.json', {}, {});
});

services.factory('SaveGeographicInfo', function ($resource) {
    return $resource('/geographic-zone/save-gis.json', {}, {post: {method: 'POST'}});
});


services.factory('ReportingFacilityList', function ($resource) {
    return $resource('/gis/reporting-facilities.json', {}, {});
});

services.factory('NonReportingFacilityList', function ($resource) {
    return $resource('/gis/non-reporting-facilities.json', {}, {});
});

services.factory('ContactList', function ($resource) {
    return $resource('/facility-contacts', {}, {});
});

/* Dashboard data factories */
services.factory('UserSupervisoryNodes', function ($resource) {
    return $resource('/reports/user/supervisory-nodes.json', {}, {});
});
services.factory('UserGeographicZoneTree', function ($resource) {
    return $resource('/reports/geographic-zones/tree.json', {}, {});
});

services.factory('UserSupervisedActivePrograms', function ($resource) {
    return $resource('/reports/user/programs.json', {}, {});
});
services.factory("FacilitiesByGeographicZoneTree", function ($resource) {
    return $resource('/reports/geographic-zone/facilities.json', {}, {});
});

services.factory("FacilitiesByGeographicZone", function ($resource) {
    return $resource('/reports/geographic-zone/:geoId/facilities.json', {}, {});
});

services.factory("FacilitiesForNotifications", function ($resource) {
    return $resource('/reports/notification/facilities.json', {}, {});
});

services.factory('OrderFillRate', function ($resource) {
    return $resource('/dashboard/orderFillRate.json', {}, {});
});

services.factory('ItemFillRate', function ($resource) {
    return $resource('/dashboard/itemFillRate.json', {}, {});
});

services.factory('ShipmentLeadTime', function ($resource) {
    return $resource('/dashboard/shipmentLeadTime.json', {}, {});
});

services.factory('StockEfficiency', function ($resource) {
    return $resource('/dashboard/stockEfficiency.json', {}, {});
});

services.factory('StockEfficiencyDetail', function ($resource) {
    return $resource('/dashboard/stockEfficiencyDetail.json', {}, {});
});

services.factory('StockedOutFacilities', function ($resource) {
    return $resource('/dashboard/stockedOutFacilities.json', {}, {});
});

services.factory('ReportProgramsBySupervisoryNode', function ($resource) {
    return $resource('/reports/supervisory-node/:supervisoryNodeId/programs.json', {}, {});
});
services.factory('ReportAllProgramsBySupervisoryNode', function ($resource) {
    return $resource('/reports/supervisory-node/:supervisoryNodeId/allPrograms.json', {}, {});
});

services.factory('StockedOutFacilitiesByDistrict', function ($resource) {
    return $resource('/dashboard/geographic-zone/:zoneId/program/:programId/period/:periodId/product/:productId/stockedOutFacilities.json', {}, {});

});
services.factory('Alerts', function ($resource) {
    return $resource('/dashboard/alerts.json', {}, {});
});

services.factory('StockedOutAlerts', function ($resource) {
    return $resource('/dashboard/stocked-out/alerts.json', {}, {});
});

services.factory('NotificationAlerts', function ($resource) {
    return $resource('/dashboard/notification/alerts.json', {}, {});
});

services.factory('DashboardNotificationsDetail', function ($resource) {
    return $resource('/dashboard/notifications/:programId/:periodId/:zoneId/:productId/:detailTable.json', {}, {});
});
services.factory('SendNotification', function ($resource) {
    return $resource('/dashboard/notification/send.json', {}, {});
});
services.factory('GetPeriod', function ($resource) {
    return $resource('/dashboard/period/:id.json', {}, {});
});
services.factory('GetProduct', function ($resource) {
    return $resource('/dashboard/productDetail/:id/:periodId.json', {}, {});
});
services.factory('ReportingPerformance', function ($resource) {
    return $resource('/dashboard/reportingPerformance.json', {}, {});
});
services.factory('ReportingPerformanceDetail', function ($resource) {
    return $resource('/dashboard/reportingPerformance-detail.json', {}, {});
});

services.factory('ReportingPerformanceDetail', function ($resource) {
    return $resource('/dashboard/reportingPerformance-detail.json', {}, {});
});

/* End Dashboard data factories */

services.factory('SMSCompleteList', function ($resource) {
    return $resource('/sms/MessageList.json', {}, {});
});

services.factory('GetSMSInfo', function ($resource) {
    return $resource('/sms/setDetails', {}, {get: {method: 'GET'}});
});

services.factory('GetMessagesForMobile', function ($resource) {
    return $resource('/sms/MessagesForMobile', {}, {get: {method: 'GET'}});
});

services.factory('GetReplyMessages', function ($resource) {
    return $resource('/sms/getSMS', {}, {get: {method: 'GET'}});
});

/*End SMS data Factories*/
services.factory('UserSummaryReport', function ($resource) {
    return $resource('/reports/reportdata/userSummary.json', {}, {});
});
services.factory('GetAllRoles', function ($resource) {
    return $resource('/roles/getList.json', {}, {});
});
services.factory('GetAllRolesForReport', function ($resource) {
    return $resource('/reports/roles/getList.json', {}, {});
});
services.factory('UserRoleAssignmentsSummary', function ($resource) {
    return $resource('/reports/UserRoleAssignments/getUserRoleAssignments', {}, {});
});

services.factory("UserRoleAssignmentsSummary1", function ($resource) {
    return $resource('reports/reportdata/userRoleAssignmentSummary');

});


services.factory("totalRnRCreatedByRequisitionGroup", function ($resource) {
    return $resource('/dashboard//RnRCreateForRequisitionGroup', {}, {});
});
services.factory('RnRStatusSummary', function ($resource) {
    return $resource('/dashboard/RnRStatus/:zoneId/:periodId/:programId/rnrStatus.json', {}, {});
});

services.factory("EmergencyRnRStatusSummary", function ($resource) {
    return $resource('/dashboard/EmergencyRnRStatus/:zoneId/:periodId/:programId/rnrStatus.json', {}, {});
});

services.factory("SendMessages", function ($resource) {
    return $resource('/messages/send.json', {}, {post: {method: 'POST'}});
});


services.factory('RnRStatusDetail', function ($resource) {
    return $resource('/dashboard/rnrStatus-detail.json', {}, {});
});


services.factory('GetLabEquipmentList', function ($resource) {
    return $resource('/dashboard/notification/alerts.json', {}, {}); // just for mock
});

services.factory('ReportEquipmentTypes', function ($resource) {
    return $resource('/reports/equipmentTypes.json', {}, {}); // just for mock
});

services.factory('LabEquipmentListReport', function ($resource) {
    return $resource('/reports/reportdata/labEquipmentList.json', {}, {});
});

services.factory('CCEStorageCapacityReport', function ($resource) {
    return $resource('/reports/reportdata/cceStorageCapacity.json', {}, {});
});

services.factory("PipelineExportReport", function ($resource) {
    return $resource('/reports/reportdata/pipelineExport.json', {}, {});

});

services.factory('ReportProgramsWithBudgeting', function ($resource) {
    return $resource('/reports/programs-supporting-budget.json', {}, {});

});
services.factory('RegimenDistributionReport', function ($resource) {
    return $resource('/reports/reportdata/getRegimenDistribution.json', {}, {});
});

services.factory('LabEquipmentListByDonorReport', function ($resource) {
    return $resource('/reports/reportdata/labEquipmentsByFundingSource.json', {}, {});
});

services.factory('GetAllUsers', function ($resource) {
    return $resource('/user/getAll.json', {}, {});
});

services.factory('GetPushedProductList', function ($resource) {
    return $resource('/reports/reportdata/pushedProductList.json', {}, {});
});

services.factory('GetUserUnassignedSupervisoryNode', function ($resource) {
    return $resource('/reports/supervisory-node/user-unassigned-node.json', {}, {});
});
services.factory('GetProductCategoryProductByProgramTree', function ($resource) {
    return $resource('/reports/productProgramCategoryTree/:programId', {}, {});
});

services.factory('GetYearSchedulePeriodTree', function ($resource) {
    return $resource('/reports/yearSchedulePeriod', {}, {});
});

services.factory('OrderFillRateSummaryReport', function ($resource) {
    return $resource('/reports/reportdata/orderFillRateReportSummary.json', {}, {});
});

services.factory('GetOrderFillRateSummary', function ($resource) {
    return $resource('/reports/OrderFillRateSummary/program/:programId/period/:periodId/schedule/:scheduleId/facilityTypeId/:facilityTypeId/zone/:zoneId/status/:status/orderFillRateSummary.json', {}, {});
});

services.factory('StockedOutFacilityList', function ($resource) {
    return $resource('/gis/stocked-out-facilities.json', {}, {});
});

services.factory('OverStockedFacilityList', function ($resource) {
    return $resource('/gis/over-stocked-facilities.json', {}, {});
});

services.factory('UnderStockedFacilityList', function ($resource) {
    return $resource('/gis/under-stocked-facilities.json', {}, {});
});

services.factory('AdequatelyStockedFacilityList', function ($resource) {
    return $resource('/gis/adequately-stocked-facilities.json', {}, {});
});

services.factory('StockStatusProductList', function ($resource) {
    return $resource('/gis/stock-status-products.json', {}, {});
});

services.factory('StockedOutFacilityByProductList', function ($resource) {
    return $resource('/gis/stocked-out-products.json', {}, {});
});

services.factory('OverStockedFacilityByProductList', function ($resource) {
    return $resource('/gis/over-stocked-products.json', {}, {});
});

services.factory('UnderStockedFacilityByProductList', function ($resource) {
    return $resource('/gis/under-stocked-products.json', {}, {});
});

services.factory('AdequatelyStockedFacilityByProductList', function ($resource) {
    return $resource('/gis/adequately-stocked-products.json', {}, {});
});

services.factory('GetFacilitiesByEquipmentStatus', function ($resource) {
    return $resource('/gis/facilitiesByEquipmentOperationalStatus.json', {}, {});
});

services.factory('GetFacilitiesEquipmentStatusSummary', function ($resource) {
    return $resource('/gis/facilitiesEquipmentStatusSummary.json', {}, {});
});

services.factory('ReportEquipments', function ($resource) {
    return $resource('/reports/equipmentsByType/:equipmentType', {}, {});
});

services.factory('NonFunctioningLabEquipment', function ($resource) {
    return $resource('/reports/reportdata/nonFunctioningLabEquipment.json', {}, {});
});

services.factory('FunctioningLabEquipment', function ($resource) {
    return $resource('/reports/reportdata/functioningLabEquipment.json', {}, {});
});

services.factory('StockStatusProductConsumptionGraph', function ($resource) {
    return $resource('/gis/stock-status-product-consumption.json', {}, {});
});

services.factory('GetDonors', function ($resource) {
    return $resource('/reports/donors', {}, {});
});

services.factory('GetFacilitySupervisors', function ($resource) {
    return $resource('/facility-supervisors.json', {}, {});
});

services.factory("SendMessagesReportAttachment", function ($resource) {
    return $resource('/messages/send/report.json', {}, {post: {method: 'POST'}});

});
services.factory('UserPrograms', function ($resource) {
    return $resource('/reports/users/:userId/programs.json', {}, {});
});
services.factory('UserFacilitiesForProgram', function ($resource) {
    return $resource('/users/program/supervised/facilities.json', {}, {});
});

services.factory('UserPreferences', function ($resource) {
    return $resource('/users/:userId/preferences.json', {}, {});
});
services.factory('UpdateUserPreference', function ($resource) {
    return $resource('/users/:userId/preferences.json', {}, update);
});
services.factory('UserProfile', function ($resource) {
    return $resource('/preference/users/:id.json', {}, {});
});

services.factory('SyncDashboard', function ($resource) {
    return $resource('/dashboard/sync.json', {}, update);
});

services.factory('SupervisoryNodesList', function ($resource) {
    return $resource('/supervisory-nodes/list.json', {}, {});
});

services.factory('RolesList', function ($resource) {
    return $resource('/roles/list.json', {}, {});
});
/* help modudule services as updated on october 16

 */
//CreateHelp topic
//load helptopic intialize
services.factory('IntializeHelpTopic', function ($resource) {
    return $resource('/helpTopicForCreate.json', {}, {post: {method: 'GET'}});
});

services.factory('CreateHelpTopic', function ($resource) {
    return $resource('/createHelpTopic.json', {}, {post: {method: 'POST'}});
});
//load helptopic detail
services.factory('HelpTopicDetail', function ($resource) {
    return $resource('/helpTopicDetail/:id.json', {}, {post: {method: 'GET'}});
});
//update help topic
services.factory('UpdateHelpTopic', function ($resource) {
    return $resource('/updateHelpTopic.json', {}, {post: {method: 'POST'}});
});
//load help topic list
services.factory('HelpTopicList', function ($resource) {
    return $resource('/helpTopicList.json', {}, {});
});

services.factory('CreateHelpContent', function ($resource) {
    return $resource('/createHelpContent.json', {}, {post: {method: 'POST'}});
});

services.factory('UpdateHelpContent', function ($resource) {
    return $resource('/updateHelpContent.json', {}, {post: {method: 'POST'}});
});

services.factory('HelpContentList', function ($resource) {
    return $resource('/helpContentList.json', {}, {});
});

services.factory('HelpContentDetail', function ($resource) {
    return $resource('/helpContentDetail/:id.json', {}, {post: {method: 'GET'}});
});

services.factory('HelpUsertopicList', function ($resource) {
    return $resource('/userHelpTopicList.json', {}, {});
});
//load helptopic detail
services.factory('SiteContent', function ($resource) {
    return $resource('/site_content/:content_name.json', {}, {post: {method: 'GET'}});
});
services.factory('HelpContentByKey', function ($resource) {
    return $resource('/general_content/:content_key.json', {}, {post: {method: 'GET'}});
});
services.factory('DashboardDashletHelpContent', function ($resource) {
    return $resource('/dashboard_dashlet_help.json', {}, {post: {method: 'GET'}});
});
services.factory('VaccineTargetUpdate', function ($resource) {
    return $resource('/vaccine/target/create.json', {}, {post: {method: 'POST'}});
});

services.factory('VaccineTargetList', function ($resource) {
    return $resource('/vaccine/target/list.json', {}, {});
});

services.factory('GetVaccineTarget', function ($resource) {
    return $resource('/vaccine/target/get/:id.json', {}, {});
});

services.factory('DeleteVaccineTarget', function ($resource) {
    return $resource('/vaccine/target/delete/:id.json', {}, {});
});

//vaccine storage service
services.factory('CreateVaccineStorage', function ($resource) {

    return $resource('/createVaccineStorage.json', {}, {post: {method: 'POST'}});
});

services.factory('UpdateVaccineStorage', function ($resource) {
    return $resource('/updateVaccineStorage.json', {}, {post: {method: 'POST'}});
});

services.factory('VaccineStorageList', function ($resource) {
    return $resource('/vaccineStorageList.json', {}, {});
});

services.factory('VaccineStorageDetail', function ($resource) {
    return $resource('/vaccineStorageDetail/:id.json', {}, {post: {method: 'GET'}});
});
services.factory('DeleteVaccineStorage', function ($resource) {
    return $resource('/deleteVaccineStorage.json', {}, {post: {method: 'POST'}});
});
services.factory('StorageTypeList', function ($resource) {
    return $resource('/storageTypeList.json', {}, {});
});

services.factory('TempratureList', function ($resource) {
    return $resource('/tempratureList.json', {}, {});
});

services.factory('VaccineQuantificationUpdate', function ($resource) {
    return $resource('/vaccine/quantification/create.json', {}, {post: {method: 'POST'}});
});

services.factory('VaccineQuantificationList', function ($resource) {
    return $resource('/vaccine/quantification/list.json', {}, {});
});

services.factory('GetVaccineQuantification', function ($resource) {
    return $resource('/vaccine/quantification/get/:id.json', {}, {});
});

services.factory('DeleteVaccineQuantification', function ($resource) {
    return $resource('/vaccine/quantification/delete/:id.json', {}, {});
});

services.factory('VaccineQuantificationFormLookUps', function ($resource) {
    return $resource('/vaccine/quantification/formLookups.json', {}, {});
});

//storage type
services.factory('CreateStorageType', function ($resource) {

    return $resource('/createStorageType.json', {}, {post: {method: 'POST'}});
});

services.factory('UpdateStorageType', function ($resource) {
    return $resource('/updateStorageType.json', {}, {post: {method: 'POST'}});
});

services.factory('StorageTypeDetail', function ($resource) {
    return $resource('/storageTypeDetail/:id.json', {}, {post: {method: 'GET'}});
});
services.factory('DeleteStorageType', function ($resource) {
    return $resource('/deleteStorageType.json', {}, {post: {method: 'POST'}});
});
services.factory('StorageTypes', function ($resource) {
    var resource = $resource('/storageTypes/:id.json', {id: '@id'}, update);

    resource.disable = function (pathParams, success, error) {
        $resource('/storageTypes/:id.json', {}, {update: {method: 'DELETE'}}).update(pathParams, {}, success, error);
    };

    return resource;
});
//temprature
services.factory('CreateTemprature', function ($resource) {

    return $resource('/createTemprature.json', {}, {post: {method: 'POST'}});
});

services.factory('UpdateTemprature', function ($resource) {
    return $resource('/updateTemprature.json', {}, {post: {method: 'POST'}});
});
services.factory('TempratureDetail', function ($resource) {
    return $resource('/tempratureDetail/:id.json', {}, {post: {method: 'GET'}});
});
services.factory('DeleteTemprature', function ($resource) {
    return $resource('/deleteTemprature.json', {}, {post: {method: 'POST'}});
});
services.factory('Tempratures', function ($resource) {
    var resource = $resource('/tempratures/:id.json', {id: '@id'}, update);

    resource.disable = function (pathParams, success, error) {
        $resource('/tempratures/:id.json', {}, {update: {method: 'DELETE'}}).update(pathParams, {}, success, error);
    };

    return resource;
});
services.factory('Countries', function ($resource) {
    var resource = $resource('/countries/:id.json', {}, {
        update: {
            method: 'PUT', params: {id: '@id'}
        },
        remove: {method: 'DELETE'}
    });

    return resource;
});
services.factory('DeleteCountries', function ($resource) {
    return $resource('/countries_remove.json', {}, {post: {method: 'POST'}});
});
services.factory('StorageFacilityList', function ($resource) {
    return $resource('/facilityList.json', {}, {});
});

/* Begin: Vaccine Supply Line */

services.factory('Manufacturers', function ($resource) {
    return $resource('/vaccine/manufacturers.json', {}, {});
});

services.factory('VaccineDistributionStatus', function ($resource) {
    return $resource('/vaccine/status.json', {}, {});
});

services.factory('VaccineStorageByFacility', function ($resource) {
    return $resource('/vaccine-storage/facility/:facilityId.json', {}, {});
});
services.factory('UserSupervisedFacilities', function ($resource) {
    return $resource('/reports/user/supervised/facilities.json', {}, {});
});

services.factory('ReceiveVaccines', function ($resource) {
    return $resource('/vaccine/receive-vaccine/:id.json', {id: '@id'}, update);
});

services.factory('UsableBatches', function ($resource) {
    return $resource('/vaccine/usable-batches/product/:productId.json', {}, {});
});

services.factory('DistributeVaccines', function ($resource) {
    return $resource('/vaccine/distribute-vaccine.json', {}, {});
});

services.factory('GeoZoneFacilityTrees', function ($resource) {
    return $resource('/vaccine/geographic-zone-facility/tree.json', {}, {});
});

services.factory('PushProgramProducts', function ($resource) {
    return $resource('/reports/push-program/products.json', {}, {});
});


/* End: Vaccine Supply Line */


services.factory('VaccineManufacturerUpdate', function ($resource) {
    return $resource('/vaccine/manufacturer/create.json', {}, {post: {method: 'POST'}});
});

services.factory('VaccineManufacturerList', function ($resource) {
    return $resource('/vaccine/manufacturer/list.json', {}, {});
});

services.factory('GetVaccineManufacturer', function ($resource) {
    return $resource('/vaccine/manufacturer/get/:id.json', {}, {});
});

services.factory('DeleteVaccineManufacturer', function ($resource) {
    return $resource('/vaccine/manufacturer/delete/:id.json', {}, {});
});

services.factory('GetVaccineManufacturerProductMapping', function ($resource) {
    return $resource('/vaccine/manufacturer/getManufacturerProducts/:id.json', {}, {});
});

services.factory('VaccineManufacturerProductUpdate', function ($resource) {
    return $resource('/vaccine/manufacturer/createProduct.json', {}, {post: {method: 'POST'}});
});

services.factory('GetManufacturerProduct', function ($resource) {
    return $resource('/vaccine/manufacturer/getProduct/:id.json', {}, {});
});

services.factory('DeleteManufacturerProductMapping', function ($resource) {
    return $resource('/vaccine/manufacturer/deleteProduct/:id.json', {}, {});
});

services.factory('VaccineTransactionTypeList', function ($resource) {
    return $resource('/vaccine/transaction-type/list.json', {}, {});
});

services.factory('GetVaccineTransactionType', function ($resource) {
    return $resource('/vaccine/transaction-type/get/:id.json', {}, {});
});

services.factory('VaccineTransactionTypeSave', function ($resource) {
    return $resource('/vaccine/transaction-type/save.json', {}, {post: {method: 'POST'}});
});

services.factory('DeleteVaccineTransactionType', function ($resource) {
    return $resource('/vaccine/transaction-type/delete/:id.json', {}, {});
});

services.factory('SearchVaccineTransactionType', function ($resource) {
    return $resource('/vaccine/transaction-type/search.json', {}, {});
});


services.factory('VaccineReceivedStatusList', function ($resource) {
    return $resource('/vaccine/received-status/list.json', {}, {});
});

services.factory('GetVaccineReceivedStatus', function ($resource) {
    return $resource('/vaccine/received-status/get/:id.json', {}, {});
});

services.factory('VaccineReceivedStatusSave', function ($resource) {
    return $resource('/vaccine/received-status/save.json', {}, {post: {method: 'POST'}});
});

services.factory('DeleteVaccineReceivedStatus', function ($resource) {
    return $resource('/vaccine/received-status/delete/:id.json', {}, {});
});

services.factory('SearchVaccineReceivedStatus', function ($resource) {
    return $resource('/vaccine/received-status/search.json', {}, {});
});
services.factory('HelpDocumentList', function ($resource) {
    return $resource('/loadDocumentList.json', {}, {});
});


services.factory('ExtraAnalyticDataForRnRStatus', function ($resource) {
    return $resource('/dashboard/extraAnalyticsRnRStatus/:zoneId/:periodId/:programId/statusData.json', {}, {});
});


services.factory('SeasonalityRationingReport', function ($resource) {
    return $resource('/reports/reportdata/seasonalityRationing.json', {}, {});
});
services.factory('SeasonalityRationingTypes', function ($resource) {

    var resource = $resource('/season-rationing/seasonalityRationingTypes/:id.json', {}, {

        update: {
            method: 'PUT', params: {id: '@id'}
        },
        delete: {
            method: 'DELETE',
            params: {id: '@id'}
        }
    });

    return resource;
});
services.factory('SeasonalityRationingTypeList', function ($resource) {
    return $resource('/season-rationing/seasonalityRationingTypeList.json', {}, {});
});

services.factory('AdjustmentFactors', function ($resource) {

    var resource = $resource('/season-rationing/adjustmentFactors/:id.json', {}, {

        update: {
            method: 'PUT', params: {id: '@id'}
        },
        delete: {
            method: 'DELETE',
            params: {id: '@id'}
        }
    });
    return resource;
});
services.factory('AdjustmentFactorList', function ($resource) {
    return $resource('/season-rationing/adjustmentFactorList.json', {}, {});
});

services.factory('FacilityByTypeAndRequisition', function ($resource) {
    return $resource('/facilityType/:facilityTypeId/requisitionGroup/:requisitionGroupId/facilities.json', {}, {});
});

services.factory('AdjustmentProducts', function ($resource) {
    return $resource('/season-rationing/adjustmentProducts.json', {id: '@id'}, update);
});

services.factory('AdjustmentProductSearch', function ($resource) {
    return $resource('/season-rationing/search.json', {}, {});
});


services.factory('getTimelinessReport', function ($resource) {
    return $resource('/reports/reportdata/timeliness.json', {}, {});
});

services.factory("getTimelinessStatusData", function ($resource) {
    return $resource('/reports/timelinessStatusData/timelinessData.json', {}, {});
});

services.factory("getFacilityRnRTimelinessReportData", function ($resource) {
    return $resource('/reports/timelinessStatusData/getFacilityRnRStatusData.json', {}, {});
});

services.factory("getTimelinessReportingDates", function ($resource) {
    return $resource('/reports/reportingDates/getTimelinessReportingDates.json', {}, {});
});

/* RMNCH report POC*/

services.factory('RmnchStockedOutFacilityList', function ($resource) {
    return $resource('/rmnch/stocked-out-facilities.json', {}, {});
});

services.factory('RmnchOverStockedFacilityList', function ($resource) {
    return $resource('/rmnch/over-stocked-facilities.json', {}, {});
});

services.factory('RmnchUnderStockedFacilityList', function ($resource) {
    return $resource('/rmnch/under-stocked-facilities.json', {}, {});
});

services.factory('RmnchAdequatelyStockedFacilityList', function ($resource) {
    return $resource('/rmnch/adequately-stocked-facilities.json', {}, {});
});

services.factory('RmnchStockStatusProductList', function ($resource) {
    return $resource('/rmnch/stock-status-products.json', {}, {});
});

services.factory('RmnchStockedOutFacilityByProductList', function ($resource) {
    return $resource('/rmnch/stocked-out-products.json', {}, {});
});

services.factory('RmnchOverStockedFacilityByProductList', function ($resource) {
    return $resource('/rmnch/over-stocked-products.json', {}, {});
});

services.factory('RmnchUnderStockedFacilityByProductList', function ($resource) {
    return $resource('/rmnch/under-stocked-products.json', {}, {});
});

services.factory('RmnchAdequatelyStockedFacilityByProductList', function ($resource) {
    return $resource('/rmnch/adequately-stocked-products.json', {}, {});
});
services.factory('RmnchStockStatusProductConsumptionGraph', function ($resource) {
    return $resource('/rmnch/stock-status-product-consumption.json', {}, {});
});

services.factory('RmnchProducts', function ($resource) {
    return $resource('/reports/rmnch-products.json', {}, {});
});


services.factory('CustomReportFullList', function ($resource) {
    return $resource('/report-api/full-list.json', {}, {});
});

services.factory('SaveCustomReport', function ($resource) {
    return $resource('/report-api/save.json', {}, {method: 'POST'});
});

services.factory('CustomReportValue', function ($resource) {
    return $resource('/report-api/report.json', {}, {});
});

services.factory('PriceScheduleCategories', function ($resource) {
    return $resource('/priceScheduleCategories.json', {}, {});
});

services.factory('CCERepairManagement', function ($resource) {
    return $resource('/reports/reportdata/cceRepairManagement.json', {}, {});
});

services.factory('CCERepairManagementEquipmentList', function ($resource) {
    return $resource('/reports/reportdata/cceRepairManagementEquipmentList.json', {}, {});
});

services.factory('ReplacementPlanSummaryReport', function ($resource) {
    return $resource('/reports/reportdata/replacementPlanSummary.json', {}, {});
});

services.factory('EquipmentsInNeedForReplacement', function ($resource) {
    return $resource('/reports/reportdata/equipmentsInNeedForReplacement.json', {}, {});
});

services.factory('VaccineMonthlyReport', function ($resource) {
    return $resource('/vaccine/report/vaccine-monthly-report.json', {}, {});
});


services.factory('VaccineUsageTrend', function ($resource) {
    return $resource('/vaccine/report/vaccine-usage-trend.json', {}, {});
});

services.factory('VaccineReportLegendContent', function ($resource) {
    return $resource('/report_legend.json', {}, {});
});

services.factory("FacilityGeoTree", function ($resource) {
    return $resource('/geoFacilityTree.json', {}, {});
});

services.factory('GetLastPeriods', function ($resource) {
    return $resource('/reports/last-periods.json', {}, {});
});

services.factory('GetProgramPeriodTracerProductsTrend', function ($resource) {
    return $resource('/dashboard/program/:programId/period/:periodId/tracer-products-trend.json', {}, {});
});

services.factory('DashboardReportingPerformance', function ($resource) {
    return $resource('/dashboard/program/:programId/period/:periodId/reporting-performance.json', {}, {});
});

services.factory('DashboardDistrictStockSummary', function ($resource) {
    return $resource('/dashboard/program/:programId/period/:periodId/district-stock-summary.json', {}, {});
});

services.factory('DashboardFacilityStockSummary', function ($resource) {
    return $resource('/dashboard/program/:programId/period/:periodId/facility-stock-summary.json', {}, {});
});

services.factory('GetStockOutFacilitiesForProgramPeriodAndProductCode', function ($resource) {
    return $resource('/dashboard/program/:programId/period/:periodId/product/:productCode/stocked-out-facilities.json', {}, {});
});


services.factory('GetVaccineReportPeriodTree', function ($resource) {
    return $resource('/reports/vaccineYearSchedulePeriod', {}, {});
});
services.factory('GetVaccineReportPeriodFlat', function ($resource) {
    return $resource('/reports/vaccineYearSchedulePeriodFlat', {}, {});
});
services.factory("ELMISInterface", function ($resource) {
    return {
        getInterface: function () {
            return $resource('/ELMISInterface/:id.json', {}, {});
        },

        getInterfacesReference: function () {
            return $resource('/ELMISAllActiveInterfaces.json', {}, {});
        },

        getFacilityMapping: function () {
            return $resource('/ELMISInterfacesMapping/{facilityId}.json', {}, {});
        },

        getAllinterfaces: function () {
            return $resource('/ELMISAllInterfaces.json');
        }

    };
});

services.factory('ELMISInterfaceSave', function ($resource) {
    return $resource('/ELMISInterface.json', {}, {save: {method: 'POST'}});
});

services.factory('FacilitiesByLevel', function ($resource) {
    return $resource('/reports/facility-By-level.json', {}, {});
});


services.factory('RequisitionReportService', function ($resource) {
    return $resource('/reports/requisition-report.json', {}, {});
});

services.factory('ProductReportService', function ($resource) {
    return {
        loadAllProducts: function () {
            return $resource('/rest-api/lookup/products', {pageSize: 2000}, {});
        },
        loadProductReport: function () {
            return $resource('/reports/single-product-report', {}, {save: {method: 'POST'}});
        },
        loadFacilityReport: function () {
            return $resource('/reports/all-products-report', {}, {save: {method: 'POST'}});
        }
    };
});

services.factory('FacilityService', function ($resource) {
    return {
        allFacilities: function () {
            return $resource('/rest-api/lookup/facilities', {page: 0, pageSize: 2000}, {});
        },
        facilityTypes: function () {
            return $resource('/rest-api/lookup/facility-types', {}, {});
        }
    };
});

services.factory('ColdChainTemperaturesReport', function ($resource) {
    return $resource('/reports/reportdata/vaccineTemperature.json', {}, {});
});

services.factory('GeographicZoneService', function ($resource) {
    return {
        loadGeographicZone: function () {
            return $resource('/rest-api/lookup/geographic-zones', {}, {});
        },
        loadGeographicLevel: function () {
            return $resource('/rest-api/lookup/geographic-levels', {}, {});
        }
    };
});


services.factory('VaccineStockStatusReport', function ($resource) {
    return $resource('/reports/reportdata/vaccineStockStatus.json', {}, {});
});


services.factory('StockLedgerReport', function ($resource) {
    return $resource('/reports/reportdata/stockLedgerReport.json', {}, {});
});


services.factory('ReportProductsByProgramWithoutDescriptions', function ($resource) {
    return $resource('/reports/program-products-with-no-descriptions/:programId.json', {}, {});
});

services.factory('ReportProductsByProgramWithoutDescriptionsAndSyringes', function ($resource) {
    return $resource('/reports/program-products-with-no-descriptions-and-program-and-syringes.json', {}, {});
});

services.factory('StaticYears', function ($resource) {
    return $resource('/vaccine/report/staticYearList.json', {}, {});
});

services.factory('GetFacilitySupervisorsByProgram', function ($resource) {
    return $resource('/get-facility-supervisors-by-program.json', {}, {});
});

services.factory('GetAllFacilityOperators', function ($resource) {
    return $resource('/reports/allFacilityOperators.json', {}, {});
});

services.factory('VaccineOnTimeInFullReporting', function ($resource) {
    return $resource('/vaccine/orderRequisition/searchOnTimeReporting.json', {}, {});
});

services.factory('GetVaccineOnTimeInFullList', function ($resource) {
    return $resource('/vaccine/orderRequisition/getOnTimeInFull.json', {}, {});
});


services.factory('OnTimeInFullReport', function ($resource) {
    return $resource('/reports/reportdata/onTimeInFullReport.json', {}, {});
});

services.factory('MinMaxStockReport', function ($resource) {
    return $resource('/reports/reportdata/getMinMaxVaccineData.json', {}, {});
});

services.factory('DistributionSummaryReport', function ($resource) {
    return $resource('/reports/reportdata/getDistributionSummaryData.json', {}, {});
});

services.factory('VaccineReceivedSummarReport', function ($resource) {
    return $resource('/reports/reportdata/getConsignmentReceivedData.json', {}, {});
});

services.factory('ReportFacilityLevelWithoutProgram', function ($resource) {
    return $resource('/reports/facility-levelWithNoProgram.json', {}, {});
});

services.factory('ProductCategoriesWithoutProgram', function ($resource) {
    return $resource('/reports/productCategoriesWithoutProgram.json', {}, {});
});

services.factory('ReportProductsByProgramWithoutDescriptionsAndProgram', function ($resource) {
    return $resource('/reports/program-products-with-no-descriptions-and-program.json', {}, {});
});

services.factory('ReportProductsWithoutDescriptionsAndWithoutProgram', function ($resource) {
    return $resource('/reports/program-products-with-no-descriptions-and-without-program.json', {}, {});
});


services.factory('ReportPeriodsByYear', function ($resource) {
    return $resource('/reports/year/:year/periods.json', {}, {});
});

services.factory('StockEventReport', function ($resource) {
    return $resource('/reports/reportdata/stock-event.json', {}, {});
});

services.factory('LogTagInfo', function ($resource) {
    return $resource('/reports/reportdata/log-tag.json', {}, {});
});
services.factory('FacilityOnwerList', function ($resource) {
    return $resource('/reports/facility_owners.json', {}, {});
});

services.factory('ManualTestType', function ($resource) {
    return $resource('/manualTestTypes/types/:tid', {tid: '@tid'}, {});
});
services.factory('ManualTestResultCategory', function ($resource) {
    return $resource('/manualTestResultCategories/types/:tid', {tid: '@tid'}, {});
});
services.factory('ManualTestResultType', function ($resource) {
    return $resource('/manualTestResultTypes/types/:tid', {tid: '@tid'}, {});
});

services.factory('GetProductById', function ($resource) {
    return $resource('/products/product/:id.json', {id: '@id'}, {});
});

services.factory('GetRejectedRnRReport', function ($resource) {
    return $resource('/reports/reportdata/rejectedRnR', {}, {});
});
services.factory('DetailRejectedRnRReport', function ($resource) {
    return $resource('/reports/reportdata/detail-rejected-rnrs', {}, {});
});
services.factory('GetMsdStockStatusReport', function ($resource) {
    return $resource('/dashboard/msdStock.json', {}, {});
});
services.factory('GetMsdStockStatus', function ($resource) {
    return $resource('/dashboard/msdStockStatus.json', {}, {});
});

services.factory('GetMsdStockStatusColor', function ($resource) {
    return $resource('/dashboard/getStockColor.json', {}, {});
});
services.factory('GetGoZoneByLevelCode', function ($resource) {
    return $resource('/geographic-zone-by/:geoLevelCode.json', {geoLevelCode: '@geoLevelCode'}, {});
});

services.factory('GetHFRFacilities', function ($resource) {
    return $resource('/rest-api/lookup/hfr-facilities', {}, {});
});

services.factory('GetHfrFacilityMappingList', function ($resource) {
    return $resource('/hfrFacilityMappingList', {}, {});
});

services.factory('HfrFacilityMapping', function ($resource) {
    return $resource('/hfrFacilityMapping.json', {}, {post: {method: 'POST'}});
});

services.factory('GetHfrFacilityMappingById', function ($resource) {
    return $resource('/hfrFacilityMappingBy/:id.json', {id: '@id'}, {});
});
services.factory('FacilityConsumptionReport', function ($resource) {
    return $resource('/reports/reportdata/facilityConsumption.json', {}, {});
});


services.factory('DailyConsumption', function ($resource) {
    return $resource('/reports/reportdata/dailyConsumption.json', {}, {});
});
services.factory('GeoZoneLevel', function ($resource) {
    return $resource('/reports/geographic-zone/:zone.json', {zone: '@zone'}, {});
});

services.factory('RejectionCount', function ($resource) {
    return $resource('/dashboard/getRejectionCount.json', {}, {});
});


//API for Zambia Dashboard
services.factory('GetNumberOfEmergencyData', function ($q, $timeout, $resource, GetNumberOfEmergency) {
    function get(params) {

        var deferred = $q.defer();
        $timeout(function () {
            GetNumberOfEmergency.get(params, function (data) {

                var stocks = {};
                if (data !== undefined) {
                    stocks = data.emergency;
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
services.factory('GetPercentageOfEmergencyOrderByProgramData', function ($q, $timeout, $resource, GetPercentageOfEmergencyOrderByProgram) {
    function get(params) {

        var deferred = $q.defer();
        $timeout(function () {
            GetPercentageOfEmergencyOrderByProgram.get(params, function (data) {

                var stocks = {};
                if (data !== undefined) {
                    stocks = data.emergency;
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
services.factory('GetEmergencyOrderByProgramData', function ($q, $timeout, $resource, GetEmergencyOrderByProgram) {
    function get(params) {

        var deferred = $q.defer();
        $timeout(function () {
            GetEmergencyOrderByProgram.get(params, function (data) {

                var stocks = {};
                if (data !== undefined) {
                    stocks = data.emergency;
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
services.factory('GetTrendOfEmergencyOrdersSubmittedPerMonthData', function ($q, $timeout, $resource, GetTrendOfEmergencyOrdersSubmittedPerMonth) {
    function get(params) {

        var deferred = $q.defer();
        $timeout(function () {
            GetTrendOfEmergencyOrdersSubmittedPerMonth.get(params, function (data) {

                var stocks = {};
                if (data !== undefined) {
                    stocks = data.emergency;
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
services.factory('GetEmergencyOrderTrendsData', function ($q, $timeout, $resource, GetEmergencyOrderTrends) {
    function get(params) {

        var deferred = $q.defer();
        $timeout(function () {
            GetEmergencyOrderTrends.get(params, function (data) {

                var stocks = {};
                if (data !== undefined) {
                    stocks = data.emergency;
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

services.factory('GetNumberOfEmergency', function ($resource) {
    return $resource('/dashboard/getNumberOfEmergency.json', {}, {});
});
services.factory('GetPercentageOfEmergencyOrderByProgram', function ($resource) {
    return $resource('/dashboard/getPercentageOfEmergencyOrderByProgram.json', {}, {});
});
services.factory('GetEmergencyOrderByProgram', function ($resource) {
    return $resource('/dashboard/getEmergencyOrderByProgram.json', {}, {});
});
services.factory('GetTrendOfEmergencyOrdersSubmittedPerMonth', function ($resource) {
    return $resource('/dashboard/getTrendOfEmergencyOrdersSubmittedPerMonth.json', {}, {});
});
services.factory('GetEmergencyOrderTrends', function ($resource) {
    return $resource('/dashboard/emergencyOrderTrends.json', {}, {});
});

services.factory('ReportingRate', function ($resource) {
    return $resource('/dashboard/reporting-rate.json', {}, {});
});
services.factory('StockStatusAvailaiblity', function ($resource) {
    return $resource('/dashboard/stock-staus-availablity.json', {}, {});
});
services.factory('ItemFillRate', function ($resource) {
    return $resource('/dashboard/item-fill-rate.json', {}, {});
});
services.factory('DailyStockStatus', function ($resource) {
    return $resource('/dashboard/daily-stock-status.json', {}, {});
});
services.factory('DashboardCommodityStatus', function ($resource) {
    return $resource('/dashboard/commodity-status.json', {}, {});
});
services.factory('DashboardFacilityCommodityStatus', function ($resource) {
    return $resource('/dashboard/facility-commodity-status.json', {}, {});
});
services.factory('DashboardProductExpired', function ($resource) {
    return $resource('/dashboard/expired_products.json', {}, {});
});
services.factory('DashboardRnrTypes', function ($resource) {
    return $resource('/dashboard/rnr-emergency-regular-types.json', {}, {});
});

services.factory('ShipmentInterfaces', function ($resource) {
    return $resource('/dashboard/shipment-interface.json', {}, {});
});
services.factory('VitalStates', function ($resource) {
    return $resource('/dashboard/vital_status.json', {}, {});
});
services.factory('RnRTimeLines', function ($resource) {
    return $resource('/dashboard/rnr_status_change_timeline.json', {}, {});
});
services.factory('UserInThreeMonths', function ($resource) {
    return $resource('/dashboard/user_in_three_months.json', {}, {});
});

services.factory('EmergencyOrderFrequentAppearingProducts', function ($resource) {
    return $resource('/dashboard/getEmergencyOrderFrequentAppearingProducts.json', {}, {});
});

services.factory('FacilitiesReportingThroughFEAndCE', function ($resource) {
    return $resource('/dashboard/getFacilitiesReportingThroughFEAndCE.json', {}, {});
});
services.factory('DashboardAggregateProductExpired', function ($resource) {
    return $resource('/dashboard/aggregateExpiry', {}, {});
});

services.factory('ReportingRateGis', function ($resource) {
    return $resource('/gis/reporting-rate.json', {}, {});
});
services.factory('UserDashboardPreference', function ($resource) {
    return $resource('/dashboard/dashboard-preferences.json', {}, {post: {method: 'POST'}});
});

services.factory('UserDashboardPreferences', function ($resource) {
    return $resource('/dashboard/dashboard-preferences.json', {}, {});
});

services.factory('PeriodInfo', function ($resource) {
    return $resource('/reports/period-info.json', {}, {});
});

services.factory('AuditReport', function ($resource) {
    return $resource('/reports/reportdata/audit_trail.json', {}, {});
});

services.factory('AuditActions', function ($resource) {
    return $resource('/reports/audit_actions.json', {}, {});
});

services.factory('DataRangeConfigurations', function ($resource) {
    return $resource('/data_configuration/:tid', {tid: '@tid'}, {});
});

services.factory('FacilityNotification', function ($resource) {
    return $resource('/notification/notifications/:nid', {nid: '@nid'}, {});
});


