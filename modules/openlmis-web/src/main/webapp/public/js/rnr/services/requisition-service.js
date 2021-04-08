/*
 * This program is part of the OpenLMIS logistics management information system platform software.
 * Copyright © 2013 VillageReach
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU Affero General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Affero General Public License for more details.
 * You should have received a copy of the GNU Affero General Public License along with this program.  If not, see http://www.gnu.org/licenses.  For additional information contact info@OpenLMIS.org.
 */
services.factory('requisitionService', function(messageService) {

    var NON_FULL_SUPPLY = 'nonFullSupply';
    var FULL_SUPPLY = 'fullSupply';
    var REGIMEN = 'regimen';
    var EQUIPMENT = 'equipment';
    var MANUAL_TEST = 'manualTest';
    var PATIENT = 'patient';
    var patientLineItem;

    var populateScope = function($scope, $location, $routeParams) {
        $scope.visibleTab = $routeParams.supplyType;
        $scope.currency = messageService.get('label.currency.symbol');
        $scope.requisitionType = $scope.rnr.emergency ? "requisition.type.emergency" : "requisition.type.regular";

        $scope.switchSupplyType = function(supplyType) {
            if (supplyType === $scope.visibleTab)
                return;
            $location.search({
                page: 1,
                supplyType: supplyType
            });
        };

        $scope.showCategory = function(index) {
            return !((index > 0) && ($scope.page[$scope.visibleTab][index].productCategory == $scope.page[$scope.visibleTab][index - 1].productCategory));
        };

        $scope.goToPage = function(page, event) {
            angular.element(event.target).parents(".dropdown").click();
            $location.search('page', page);
        };

        $scope.highlightRequired = function(showError, value, skipped) {
            if (showError && isUndefined(value) && !skipped) {
                return "required-error";
            }
            return null;
        };

        $scope.highlightPatientRequired = function(showError, value, disabled, skipped) {
            if (showError && isUndefined(value) && !disabled && !skipped) {
                return "required-error";
            }
            return null;
        };

        $scope.highlightWarningBasedOnField = function(showError, value, field, skipped) {
            if (showError && (isUndefined(value) || value === false) && field && skipped === false) {
                return "warning-error";
            }
            return null;
        };

    };


    var setErrorPages = function($scope) {
        $scope.errorPages = $scope.rnr.getErrorPages($scope.pageSize);
        $scope.patientErrorPagesCount = $scope.errorPages.patient.length;
        $scope.fullSupplyErrorPagesCount = $scope.errorPages.fullSupply.length;
        $scope.nonFullSupplyErrorPagesCount = $scope.errorPages.nonFullSupply.length;
        $scope.regimenErrorPagesCount = $scope.errorPages.regimen.length;
        $scope.patientErrorPagesCount = $scope.errorPages.patient.length;

    };

    var resetErrorPages = function($scope) {
        $scope.errorPages = {
            fullSupply: [],
            nonFullSupply: [],
            regimen: [],
            patient: []
        };
    };

    var refreshGrid = function($scope, $location, $routeParams, save) {
        var lineItemMap = {
            'nonFullSupply': $scope.rnr.nonFullSupplyLineItems,
            'fullSupply': $scope.rnr.fullSupplyLineItems,
            'regimen': $scope.rnr.regimenLineItems,
            'equipment': $scope.rnr.equipmentLineItems,
            'manualTest': $scope.rnr.manualTestLineItems,
            'patient': $scope.rnr.patientLineItems
        };

        if (save) $scope.saveRnr();

        $scope.page = {
            fullSupply: [],
            nonFullSupply: [],
            regimen: [],
            equipment: [],
            manualTest: [],
            patient: []
        };
        $scope.visibleTab = ($routeParams.supplyType === NON_FULL_SUPPLY) ? NON_FULL_SUPPLY : ($routeParams.supplyType === REGIMEN && $scope.regimenCount) ? REGIMEN : ($routeParams.supplyType === EQUIPMENT && $scope.equipmentCount) ? EQUIPMENT : ($routeParams.supplyType === MANUAL_TEST && $scope.manualTestCount) ? MANUAL_TEST : ($routeParams.supplyType === PATIENT) ? PATIENT : FULL_SUPPLY;

        $location.search('supplyType', $scope.visibleTab);


        $scope.numberOfPages = Math.ceil(lineItemMap[$scope.visibleTab].length / $scope.pageSize) || 1;

        $scope.currentPage = (utils.isValidPage($routeParams.page, $scope.numberOfPages)) ? parseInt($routeParams.page,
            10) : 1;

        $scope.page[$scope.visibleTab] = lineItemMap[$scope.visibleTab].slice($scope.pageSize * ($scope.currentPage - 1),
            $scope.pageSize * $scope.currentPage);

        if ($scope.rnr.patientLineItems.length > 0 && $scope.rnr.program.code !='TB-MDR')
            updateCalculatedColumn($scope.page[$scope.visibleTab], $scope.rnr.patientLineItems);

    };

    function updateCalculatedColumn(fullSupplyLineItems, patientLineItems) {

       angular.forEach(fullSupplyLineItems, function(fullSupplyLineItem, key) {
            switch (fullSupplyLineItem.patientCalculationFormula) {
                case 'ADULT_NEW_INTENSIVE_PHASE':
                    patientLineItem = patientLineItems.filter(function(item){return item.code === "Number of adult patients(New)";})[0];
                    fullSupplyLineItem.nextMonthPatient = parseInt(patientLineItem.firstMonth, messageService.get("radix.no")) + parseInt(patientLineItem.secondMonth,messageService.get("radix.no"));
                    if(isNaN(fullSupplyLineItem.nextMonthPatient)) fullSupplyLineItem.nextMonthPatient=0;
                    break;
                case 'ADULT_NEW_CONTINUATION_PHASE':
                    patientLineItem = patientLineItems.filter(function(item){return item.code === "Number of adult patients(New)";})[0];
                    fullSupplyLineItem.nextMonthPatient = parseInt(patientLineItem.thirdMonth,messageService.get("radix.no")) + parseInt(patientLineItem.fourthMonth,messageService.get("radix.no")) +
                        parseInt(patientLineItem.fifthMonth,messageService.get("radix.no")) + parseInt(patientLineItem.sixthMonth,messageService.get("radix.no"));
                    if(isNaN(fullSupplyLineItem.nextMonthPatient)) fullSupplyLineItem.nextMonthPatient=0;
                    break;

                case 'CHILD_NEW_INTENSIVE_PHASE':
                    patientLineItem = patientLineItems.filter(function(item){return item.code === "Number of children(New)";})[0];
                    fullSupplyLineItem.nextMonthPatient = parseInt(patientLineItem.firstMonth,messageService.get("radix.no")) + parseInt(patientLineItem.secondMonth,messageService.get("radix.no"));
                    if(isNaN(fullSupplyLineItem.nextMonthPatient)) fullSupplyLineItem.nextMonthPatient=0;
                    break;
                case 'CHILD_NEW_CONTINUATION_PHASE':
                    patientLineItem = patientLineItems.filter(function(item){return item.code === "Number of children(New)";})[0];
                    fullSupplyLineItem.nextMonthPatient = parseInt(patientLineItem.thirdMonth,messageService.get("radix.no")) + parseInt(patientLineItem.fourthMonth,messageService.get("radix.no")) +
                        parseInt(patientLineItem.fifthMonth,messageService.get("radix.no")) + parseInt(patientLineItem.sixthMonth,messageService.get("radix.no"));
                    if(isNaN(fullSupplyLineItem.nextMonthPatient)) fullSupplyLineItem.nextMonthPatient=0;
                    break;
                case 'MB_ADULT':
                    patientLineItem = patientLineItems.filter(function(item){return item.code === "Number of adult on MB regimen";})[0];
                    fullSupplyLineItem.nextMonthPatient = parseInt(patientLineItem.firstMonth,messageService.get("radix.no")) + parseInt(patientLineItem.secondMonth,messageService.get("radix.no")) +
                        parseInt(patientLineItem.thirdMonth,messageService.get("radix.no")) + parseInt(patientLineItem.fourthMonth,messageService.get("radix.no")) + parseInt(patientLineItem.fifthMonth,messageService.get("radix.no")) +
                        parseInt(patientLineItem.sixthMonth,messageService.get("radix.no")) + parseInt(patientLineItem.seventhMonth,messageService.get("radix.no")) + parseInt(patientLineItem.eighthMonth,messageService.get("radix.no")) +
                        parseInt(patientLineItem.ninthMonth,messageService.get("radix.no")) + parseInt(patientLineItem.tenthMonth,messageService.get("radix.no")) + parseInt(patientLineItem.eleventhMonth,messageService.get("radix.no")) +
                            parseInt(patientLineItem.twelfthMonth,messageService.get("radix.no"));
                    if(isNaN(fullSupplyLineItem.nextMonthPatient)) fullSupplyLineItem.nextMonthPatient=0;
                    break;
                case 'PB_ADULT':
                    patientLineItem = patientLineItems.filter(function(item){return item.code === "Number of adult on PB regimen";})[0];
                    fullSupplyLineItem.nextMonthPatient = parseInt(patientLineItem.firstMonth,messageService.get("radix.no")) + parseInt(patientLineItem.secondMonth,messageService.get("radix.no")) +
                        parseInt(patientLineItem.thirdMonth,messageService.get("radix.no")) + parseInt(patientLineItem.fourthMonth,messageService.get("radix.no")) + parseInt(patientLineItem.fifthMonth,messageService.get("radix.no")) +
                        parseInt(patientLineItem.sixthMonth,messageService.get("radix.no"));
                    if(isNaN(fullSupplyLineItem.nextMonthPatient)) fullSupplyLineItem.nextMonthPatient=0;
                    break;
                case 'MB_PEDIATRIC':
                    patientLineItem = patientLineItems.filter(function(item){return item.code === "Number of children on MB regimen";})[0];
                    fullSupplyLineItem.nextMonthPatient = parseInt(patientLineItem.firstMonth, messageService.get("radix.no")) + parseInt(patientLineItems[6].secondMonth, messageService.get("radix.no")) +
                        parseInt(patientLineItem.thirdMonth, messageService.get("radix.no")) + parseInt(patientLineItem.fourthMonth, messageService.get("radix.no")) + parseInt(patientLineItem.fifthMonth, messageService.get("radix.no")) +
                        parseInt(patientLineItem.sixthMonth, messageService.get("radix.no")) + parseInt(patientLineItem.seventhMonth, messageService.get("radix.no")) + parseInt(patientLineItem.eighthMonth, messageService.get("radix.no")) +
                            parseInt(patientLineItem.ninthMonth, messageService.get("radix.no")) + parseInt(patientLineItem.tenthMonth, messageService.get("radix.no")) + parseInt(patientLineItem.eleventhMonth, messageService.get("radix.no")) +
                            parseInt(patientLineItem.twelfthMonth, messageService.get("radix.no"));
                    if(isNaN(fullSupplyLineItem.nextMonthPatient)) fullSupplyLineItem.nextMonthPatient=0;
                    break;
                case 'PB_PEDIATRIC':
                    patientLineItem = patientLineItems.filter(function(item){return item.code === "Number of children on PB regimen";})[0];
                    fullSupplyLineItem.nextMonthPatient = parseInt(patientLineItem.firstMonth, messageService.get("radix.no")) + parseInt(patientLineItem.secondMonth, messageService.get("radix.no")) +
                        parseInt(patientLineItem.thirdMonth, messageService.get("radix.no")) + parseInt(patientLineItem.fourthMonth, messageService.get("radix.no")) + parseInt(patientLineItem.fifthMonth, messageService.get("radix.no")) +
                        parseInt(patientLineItem.sixthMonth, messageService.get("radix.no"));
                    if(isNaN(fullSupplyLineItem.nextMonthPatient)) fullSupplyLineItem.nextMonthPatient=0;
                    break;

                case 'IPT_PEDIATRIC':
                    patientLineItem = patientLineItems.filter(function(item){return item.code === "Number of Children on IPT";})[0];
                    fullSupplyLineItem.nextMonthPatient = parseInt(patientLineItem.firstMonth, messageService.get("radix.no")) + parseInt(patientLineItem.secondMonth, messageService.get("radix.no")) +
                        parseInt(patientLineItem.thirdMonth, messageService.get("radix.no")) + parseInt(patientLineItem.fourthMonth, messageService.get("radix.no")) + parseInt(patientLineItem.fifthMonth, messageService.get("radix.no")) +
                        parseInt(patientLineItem.sixthMonth, messageService.get("radix.no"));
                    if(isNaN(fullSupplyLineItem.nextMonthPatient)) fullSupplyLineItem.nextMonthPatient=0;
                    break;
                case 'IPT_ADULT':
                    patientLineItem = patientLineItems.filter(function(item){return item.code === "Number of Children on IPT";})[0];
                    fullSupplyLineItem.nextMonthPatient = parseInt(patientLineItem.firstMonth, messageService.get("radix.no")) + parseInt(patientLineItem.secondMonth, messageService.get("radix.no")) +
                        parseInt(patientLineItem.thirdMonth, messageService.get("radix.no")) + parseInt(patientLineItem.fourthMonth, messageService.get("radix.no")) + parseInt(patientLineItem.fifthMonth, messageService.get("radix.no")) +
                        parseInt(patientLineItem.sixthMonth, messageService.get("radix.no"));
                    if(isNaN(fullSupplyLineItem.nextMonthPatient)) fullSupplyLineItem.nextMonthPatient=0;
                    break;
                default:
                    break;

            }
            //Need to put this somewhere
            if(fullSupplyLineItem.stockInHand){
             // fullSupplyLineItem.quantityReceived = 0;
              fullSupplyLineItem.totalRequirement = fullSupplyLineItem.nextMonthPatient * fullSupplyLineItem.dosesPerMonth;
              fullSupplyLineItem.totalQuantityNeededByHF = (fullSupplyLineItem.totalRequirement * 2);
              fullSupplyLineItem.quantityToIssue = fullSupplyLineItem.totalQuantityNeededByHF - fullSupplyLineItem.stockInHand;
              if(fullSupplyLineItem.quantityToIssue < 0) fullSupplyLineItem.quantityToIssue = 0;
              fullSupplyLineItem.total = Math.floor(fullSupplyLineItem.quantityToIssue / utils.parseIntWithBaseTen(fullSupplyLineItem.packSize));
           }
        });
    }

    function getMappedVisibleColumns( rnrColumns, fixedColumns, skipped, reportOnlyPeriod, patientLineItemsCount) {
        skipped = skipped || [];
        var filteredColumns = _.reject(rnrColumns, function(column) {
            return (skipped.indexOf(column.name) !== -1) || (column.visible !== true);
        });

        var fullSupplyVisibleColumns = _.groupBy(filteredColumns, function(column) {
            if ((fixedColumns.indexOf(column.name) > -1))
                return 'fixed';

            return 'scrollable';
        });
        var validateUndefinedValue = (patientLineItemsCount === undefined)? 0:patientLineItemsCount;

        if (reportOnlyPeriod || parseInt(validateUndefinedValue,0) === 0) {
            fullSupplyVisibleColumns.scrollable = _.filter(fullSupplyVisibleColumns.scrollable, function(column) {
                return _.contains(['skipped', 'product', 'productCode', 'dispensingUnit',
                    'stockInHand', 'stockOutDays'
                ], column.name);
            });
        }

        return {
            fullSupply: fullSupplyVisibleColumns,
            nonFullSupply: {
                fixed: _.filter(fullSupplyVisibleColumns.fixed, function(column) {
                    return _.contains(['skipped', 'product', 'productCode'], column.name);
                }),
                scrollable: _.filter(fullSupplyVisibleColumns.scrollable, function(column) {
                    return _.contains(RegularRnrLineItem.visibleForNonFullSupplyColumns, column.name);
                })
            }
        };
    }

    return {
        refreshGrid: refreshGrid,
        populateScope: populateScope,
        setErrorPages: setErrorPages,
        resetErrorPages: resetErrorPages,
        getMappedVisibleColumns: getMappedVisibleColumns
    };
});