/*
 * This program is part of the OpenLMIS logistics management information system platform software.
 * Copyright © 2013 VillageReach
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU Affero General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *  
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Affero General Public License for more details.
 * You should have received a copy of the GNU Affero General Public License along with this program.  If not, see http://www.gnu.org/licenses.  For additional information contact info@OpenLMIS.org. 
 */
services.factory('requisitionService', function(messageService) {

    var NON_FULL_SUPPLY = 'nonFullSupply';
    var FULL_SUPPLY = 'fullSupply';
    var REGIMEN = 'regimen';
    var EQUIPMENT = 'equipment';
    var MANUAL_TEST = 'manualTest';
    var PATIENT = 'patient';

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

        $scope.highlightPatientRequired = function(showError, value, disabled) {
            if (showError && isUndefined(value) && !disabled) {
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

        if ($scope.rnr.patientLineItems.length > 0)
            updateCalculatedColumn($scope.page[$scope.visibleTab], $scope.rnr.patientLineItems);
    };

    function updateCalculatedColumn(fullSupplyLineItems, patientLineItems) {

        angular.forEach(fullSupplyLineItems, function(fullSupplyLineItem, key) {
            switch (fullSupplyLineItem.patientCalculationFormula) {
                case 'ADULT_NEW_INTENSIVE_PHASE':
                    fullSupplyLineItem.nextMonthPatient = parseInt(patientLineItems[0].firstMonth, messageService.get("radix.no")) + parseInt(patientLineItems[0].secondMonth,messageService.get("radix.no"));
                    break;
                case 'ADULT_NEW_CONTINUATION_PHASE':
                    fullSupplyLineItem.nextMonthPatient = parseInt(patientLineItems[0].thirdMonth,messageService.get("radix.no")) + parseInt(patientLineItems[0].fourthMonth,messageService.get("radix.no")) +
                        parseInt(patientLineItems[0].fifthMonth,messageService.get("radix.no")) + parseInt(patientLineItems[0].sixthMonth,messageService.get("radix.no"));
                    break;

                case 'CHILD_NEW_INTENSIVE_PHASE':
                    fullSupplyLineItem.nextMonthPatient = parseInt(patientLineItems[1].firstMonth,messageService.get("radix.no")) + parseInt(patientLineItems[1].secondMonth,messageService.get("radix.no"));
                    break;
                case 'CHILD_NEW_CONTINUATION_PHASE':
                    fullSupplyLineItem.nextMonthPatient = parseInt(patientLineItems[1].thirdMonth,messageService.get("radix.no")) + parseInt(patientLineItems[1].fourthMonth,messageService.get("radix.no")) +
                        parseInt(patientLineItems[1].fifthMonth,messageService.get("radix.no")) + parseInt(patientLineItems[1].sixthMonth,messageService.get("radix.no"));
                    break;
                case 'MB_ADULT':
                    fullSupplyLineItem.nextMonthPatient = parseInt(patientLineItems[5].firstMonth,messageService.get("radix.no")) + parseInt(patientLineItems[5].secondMonth,messageService.get("radix.no")) +
                        parseInt(patientLineItems[5].thirdMonth,messageService.get("radix.no")) + parseInt(patientLineItems[5].fourthMonth,messageService.get("radix.no")) + parseInt(patientLineItems[5].fifthMonth,messageService.get("radix.no")) +
                        parseInt(patientLineItems[5].sixthMonth,messageService.get("radix.no")) + parseInt(patientLineItems[5].seventhMonth,messageService.get("radix.no")) + parseInt(patientLineItems[5].eighthMonth,messageService.get("radix.no")) +
                            parseInt(patientLineItems[5].ninthMonth,messageService.get("radix.no")) + parseInt(patientLineItems[5].tenthMonth,messageService.get("radix.no")) + parseInt(patientLineItems[5].eleventhMonth,messageService.get("radix.no")) +
                            parseInt(patientLineItems[5].twelfthMonth,messageService.get("radix.no"));
                    break;
                case 'PB_ADULT':
                    fullSupplyLineItem.nextMonthPatient = parseInt(patientLineItems[6].firstMonth,10) + parseInt(patientLineItems[6].secondMonth,10) +
                        parseInt(patientLineItems[6].thirdMonth,10) + parseInt(patientLineItems[6].fourthMonth,10) + parseInt(patientLineItems[6].fifthMonth,10) +
                        parseInt(patientLineItems[6].sixthMonth,10);
                    break;
                case 'MB_PEDIATRIC':
                    fullSupplyLineItem.nextMonthPatient = parseInt(patientLineItems[7].firstMonth, messageService.get("radix.no")) + parseInt(patientLineItems[7].secondMonth, messageService.get("radix.no")) +
                        parseInt(patientLineItems[7].thirdMonth, messageService.get("radix.no")) + parseInt(patientLineItems[7].fourthMonth, messageService.get("radix.no")) + parseInt(patientLineItems[7].fifthMonth, messageService.get("radix.no")) +
                        parseInt(patientLineItems[7].sixthMonth, messageService.get("radix.no")) + parseInt(patientLineItems[7].seventhMonth, messageService.get("radix.no")) + parseInt(patientLineItems[7].eighthMonth, messageService.get("radix.no")) +
                            parseInt(patientLineItems[7].ninthMonth, messageService.get("radix.no")) + parseInt(patientLineItems[7].tenthMonth, messageService.get("radix.no")) + parseInt(patientLineItems[7].eleventhMonth, messageService.get("radix.no")) +
                            parseInt(patientLineItems[7].twelfthMonth, messageService.get("radix.no"));
                    break;
                case 'PB_PEDIATRIC':
                    fullSupplyLineItem.nextMonthPatient = parseInt(patientLineItems[8].firstMonth, messageService.get("radix.no")) + parseInt(patientLineItems[8].secondMonth, messageService.get("radix.no")) +
                        parseInt(patientLineItems[8].thirdMonth, messageService.get("radix.no")) + parseInt(patientLineItems[8].fourthMonth, messageService.get("radix.no")) + parseInt(patientLineItems[8].fifthMonth, messageService.get("radix.no")) +
                        parseInt(patientLineItems[8].sixthMonth, messageService.get("radix.no"));
                    break;

                case 'IPT_PEDIATRIC':
                    fullSupplyLineItem.nextMonthPatient = parseInt(patientLineItems[4].firstMonth, messageService.get("radix.no")) + parseInt(patientLineItems[4].secondMonth, messageService.get("radix.no")) +
                        parseInt(patientLineItems[4].thirdMonth, messageService.get("radix.no")) + parseInt(patientLineItems[4].fourthMonth, messageService.get("radix.no")) + parseInt(patientLineItems[4].fifthMonth, messageService.get("radix.no")) +
                        parseInt(patientLineItems[4].sixthMonth, messageService.get("radix.no"));
                    break;
                case 'IPT_ADULT':
                    fullSupplyLineItem.nextMonthPatient = parseInt(patientLineItems[3].firstMonth, messageService.get("radix.no")) + parseInt(patientLineItems[3].secondMonth, messageService.get("radix.no")) +
                        parseInt(patientLineItems[3].thirdMonth, messageService.get("radix.no")) + parseInt(patientLineItems[3].fourthMonth, messageService.get("radix.no")) + parseInt(patientLineItems[3].fifthMonth, messageService.get("radix.no")) +
                        parseInt(patientLineItems[3].sixthMonth, messageService.get("radix.no"));
                    break;
                default:
                    break;

            }
            //Need to put this somewhere
            fullSupplyLineItem.quantityReceived = 0;
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

        if (reportOnlyPeriod && patientLineItemsCount===0) {
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