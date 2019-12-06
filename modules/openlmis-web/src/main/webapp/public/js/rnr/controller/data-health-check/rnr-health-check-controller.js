/*
 * This program is part of the OpenLMIS logistics management information system platform software.
 * Copyright © 2013 VillageReach
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU Affero General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *  
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Affero General Public License for more details.
 * You should have received a copy of the GNU Affero General Public License along with this program.  If not, see http://www.gnu.org/licenses.  For additional information contact info@OpenLMIS.org. 
 */
function RnrHealthCheckController($scope, requisitionData, rnrColumns, regimenTemplate, equipmentOperationalStatus, showMaxStock, ReOpenRequisition, RejectRequisition, $dialog, $location, pageSize, $routeParams, requisitionService, patientTemplate, RunDataHealthCheck, $timeout) {

    var PATIENT = 'patient';
    $scope.dataHealthCheckInfoModal = false;
    $scope.showHealthCheckStatus = false;
    $scope.healthCheckMessage = [];
    $scope.rnrColumns = rnrColumns;
    $scope.pageSize = pageSize;
    $scope.rnr = new Rnr(requisitionData.rnr, rnrColumns, requisitionData.numberOfMonths);
    $scope.regimenColumns = regimenTemplate ? regimenTemplate.columns : [];
    $scope.patientColumns = patientTemplate ? patientTemplate.columns : [];
    $scope.viewSourceOfFund = true;
    $scope.healthCheckResults;
    $scope.fullSupplyTabError = false;
    $scope.summary;
    $scope.showSummary=false;

    $scope.showMaxStock = showMaxStock;

    if (!($scope.rnr.status == "APPROVED" || $scope.rnr.status == "RELEASED")) {
        rnrColumns = _.filter(rnrColumns, function(column) {
            return column.name != "quantityApproved";
        });
    }


    $scope.runHealthCheck = function() {
        $timeout(function() {
            RunDataHealthCheck.get({
                rnrid: $scope.rnr.id
            }, function(data) {
                $scope.healthCheckResults = data.healthCheck;
                createSummary ();
            });
        }, 100);
    };

$scope.dismiss = function ()
{
 $scope.dataHealthCheckInfoModal = false;
};
    $scope.viewinformation = function(productCode, productName) {
        $scope.dataHealthCheckInfoModal = true;
        $scope.productCode = productCode;
        $scope.productName = productName;
        $scope.issues_arry = _.where($scope.healthCheckMessage, {
            productCode: productCode
        });
    };

function createSummary (){
var summary=[];
for(var key in $scope.healthCheckResults)
{
    var sof_objs = _.where($scope.healthCheckResults[key], {status: false});
   for(var x in sof_objs)
   {
   summary.push(sof_objs[x]);
   }
}

$scope.summary = _.countBy(summary, function(obj){
                                    return obj.rule;
                                });
$scope.showSummary=true;
}

    $scope.checkHealthStatus = function(productCode) {
        if ($scope.healthCheckResults) {
            $scope.showHealthCheckStatus = true;
            $scope.fullSupplyTabError = true;
            var check_status = true;
            var prd_arry = $scope.healthCheckResults[productCode];


            for (var x in prd_arry) {
                if (!prd_arry[x].status) {
                    check_status = false;
                    var prod_arry = _.where($scope.healthCheckMessage, {
                        productCode: productCode,
                        rule: prd_arry[x].rule
                    });
                    if (prod_arry.length === 0) {
                        var message = {
                            productCode: productCode,
                            rule: prd_arry[x].rule,
                            message: prd_arry[x].message
                        };
                        $scope.healthCheckMessage.push(message);
                    }


                }
            }
            return check_status;
        }
        $scope.healthCheckMessage = [];
        $scope.showHealthCheckStatus = false;
    };




    $scope.reOpenRnR = function() {
        var callBack = function(result) {
            if (result) {
                ReOpenRequisition.post({
                    id: $scope.rnr.id
                }, function() {
                    OpenLmisDialog.newDialog({
                        id: "confirmDialog",
                        header: "label.confirm.action",
                        body: 'msg.rnr.reopened'
                    }, function() {
                        $location.url('/public/pages/logistics/rnr/index.html#/init-rnr');
                    }, $dialog);
                });
            }
        };


        var options = {
            id: "confirmDialog",
            header: "label.confirm.action",
            body: "label.rnr.confirm.reopen"
        };
        OpenLmisDialog.newDialog(options, callBack, $dialog);
    };

    $scope.rejectRnR = function() {
        var callBack = function(result) {

            if (result) {
                RejectRequisition.post({
                    id: $scope.rnr.id
                }, function() {
                    OpenLmisDialog.newDialog({
                        id: "confirmDialog",
                        header: "label.confirm.action",
                        body: 'msg.rnr.returned'
                    }, function() {
                        $location.url('/public/pages/logistics/rnr/index.html#/init-rnr');
                    }, $dialog);
                });
            }
        };

        var options = {
            id: "confirmDialog",
            header: "label.confirm.action",
            body: "label.rnr.confirm.return"
        };

        OpenLmisDialog.newDialog(options, callBack, $dialog);
    };
    var columnsAllowedToDisplay;
    if ($scope.rnr.period.enableOrder) {

        columnsAllowedToDisplay = requisitionService.getMappedVisibleColumns(rnrColumns, RegularRnrLineItem.frozenColumns,
            ['quantityApproved', 'remarks'], !$scope.rnr.period.enableOrder);

    } else {

        columnsAllowedToDisplay = requisitionService.getMappedVisibleColumns(rnrColumns, RegularRnrLineItem.frozenColumns,
            ['quantityApproved', 'lossesAndAdjustments', 'remarks', 'quantityReceived', 'beginningBalance', 'quantityDispensed', 'normalizedConsumption', 'maxStockQuantity', 'calculatedOrderQuantity', 'quantityRequested', 'packsToShip', 'reasonForRequestedQuantity', 'price', 'cost'], !$scope.rnr.period.enableOrder);

    }


    $scope.visibleColumns = columnsAllowedToDisplay;
    $scope.regimenCount = $scope.rnr.regimenLineItems.length;
    $scope.equipmentCount = $scope.rnr.equipmentLineItems.length;
    $scope.manualTestCount = $scope.rnr.manualTestLineItems.length;
    $scope.patientCount = $scope.rnr.patientLineItems.length;

    $scope.equipmentOperationalStatus = equipmentOperationalStatus;

    if ($scope.patientCount > 0) $routeParams.supplyType = PATIENT;

    requisitionService.populateScope($scope, $location, $routeParams);

    $scope.requisitionType = $scope.rnr.emergency ? "requisition.type.emergency" : "requisition.type.regular";

    $scope.$on('$routeUpdate', function() {
        requisitionService.refreshGrid($scope, $location, $routeParams, false);
    });

    requisitionService.refreshGrid($scope, $location, $routeParams, false);
    $scope.$watch("currentPage", function() {
        $location.search("page", $scope.currentPage);
    });

    $scope.getId = function(prefix, parent) {
        return prefix + "_" + parent.$parent.$index;
    };
}

RnrHealthCheckController.resolve = {
    requisitionData: function($q, $timeout, Requisitions, $route) {
        var deferred = $q.defer();
        $timeout(function() {
            Requisitions.get({
                id: $route.current.params.rnr
            }, function(data) {
                deferred.resolve(data);
            }, {});
        }, 100);
        return deferred.promise;
    },

    rnrColumns: function($q, $timeout, ProgramRnRColumnList, $route) {
        var deferred = $q.defer();
        $timeout(function() {
            ProgramRnRColumnList.get({
                programId: $route.current.params.program
            }, function(data) {
                deferred.resolve(data.rnrColumnList);
            }, {});
        }, 100);
        return deferred.promise;
    },
    showMaxStock: function($q, $timeout, ConfigSettingsByKey) {
        var deferred = $q.defer();
        $timeout(function() {
            ConfigSettingsByKey.get({
                key: 'USE_GLOBAL_MAX_MOS_ON_DISPLAY'
            }, function(data) {
                deferred.resolve(data.settings.value === 'true');
            });
        }, 100);
        return deferred.promise;
    },
    pageSize: function($q, $timeout, LineItemsPerPage) {
        var deferred = $q.defer();
        $timeout(function() {
            LineItemsPerPage.get({}, function(data) {
                deferred.resolve(data.pageSize);
            }, {});
        }, 100);
        return deferred.promise;
    },
    equipmentOperationalStatus: function($q, $timeout, EquipmentOperationalStatus) {
        var deferred = $q.defer();
        $timeout(function() {
            EquipmentOperationalStatus.get({}, function(data) {
                deferred.resolve(data.status);
            }, {});
        }, 100);
        return deferred.promise;
    },
    regimenTemplate: function($q, $timeout, $route, ProgramRegimenTemplate) {
        var deferred = $q.defer();
        $timeout(function() {
            ProgramRegimenTemplate.get({
                programId: $route.current.params.program
            }, function(data) {
                deferred.resolve(data.template);
            }, {});
        }, 100);
        return deferred.promise;
    },
    patientTemplate: function($q, $timeout, $route, ProgramPatientTemplate) {
        var deferred = $q.defer();
        $timeout(function() {
            ProgramPatientTemplate.get({
                programId: $route.current.params.program
            }, function(data) {
                deferred.resolve(data.template);
            }, {});
        }, 100);
        return deferred.promise;
    }
};