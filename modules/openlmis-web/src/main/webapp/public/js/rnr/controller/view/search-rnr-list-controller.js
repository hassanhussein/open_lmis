/*
 * This program is part of the OpenLMIS logistics management information system platform software.
 * Copyright © 2013 VillageReach
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU Affero General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *  
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Affero General Public License for more details.
 * You should have received a copy of the GNU Affero General Public License along with this program.  If not, see http://www.gnu.org/licenses.  For additional information contact info@OpenLMIS.org. 
 */

function SearchRnrListController($scope, programs, years,TreeGeographicZoneListByProgram, ReportPeriodsByScheduleAndYear,
                                 ReportProgramSchedules, SearchRequisitionsForViewing,  $location,
                                 messageService, navigateBackService, FeatureToggleService, ProgramCompleteList) {
    $scope.programs = programs;
    $scope.years = years;
    $scope.facilityLabel = (!$scope.programs.length) ? messageService.get("label.none.assigned") : messageService.get("label.select.facility");
    $scope.programLabel = messageService.get("label.none.assigned");
    $scope.selectedItems = [];


    $scope.loadRequisitions = function () {
        if ($scope.viewRequisitionForm && $scope.viewRequisitionForm.$invalid) {
            $scope.errorShown = true;
            return;
        }
        var requisitionQueryParameters = {
            programId: $scope.selectedProgramId,
            scheduleId:$scope.selectedScheduleId,
            year:$scope.selectedYear,
            periodId:$scope.selectedPeriodId,
            zoneId:$scope.selectedZoneId.id

        };

        if ($scope.selectedProgramId) requisitionQueryParameters.programId = $scope.selectedProgramId;

        SearchRequisitionsForViewing.get(requisitionQueryParameters, function (data) {

            $scope.requisitions = $scope.filteredRequisitions = data.rnr_list;

            setRequisitionsFoundMessage();
        }, function () {
        });
    };

    $scope.toggleEmergencyRequisitions = function () {
        $scope.selectedFacilityId = $scope.programId = undefined;
        if ($scope.emergencyRequisitionsOnly) {

            ProgramCompleteList.get(function (response) {
                $scope.programs = response.programs;
            });
        }
    };

    $scope.selectedFacilityId = navigateBackService.facilityId;
    $scope.startDate = navigateBackService.dateRangeStart;
    $scope.endDate = navigateBackService.dateRangeEnd;
    // $scope.programs = navigateBackService.programs;
    if (navigateBackService.programId) {
        $scope.selectedProgramId = navigateBackService.programId;
        $scope.program = _.findWhere($scope.programs, {id: utils.parseIntWithBaseTen($scope.selectedProgramId)});
        setOptions();
    }
    if ($scope.selectedFacilityId && $scope.startDate && $scope.endDate) {
        $scope.loadRequisitions();
    }

    var selectionFunc = function () {
        // $scope.mySelectedRows=$scope.rnrListGrid.selection.getSelectedRows();
        $scope.$parent.rnrStatus = $scope.selectedItems[0].status;
        $scope.openRequisition();
    };


    $scope.rnrListGrid = {
        data: 'filteredRequisitions',
        displayFooter: false,
        multiSelect: false,
        selectedItems: $scope.selectedItems,
        afterSelectionChange: selectionFunc,
        displaySelectionCheckbox: false,
        enableColumnResize: true,
        showColumnMenu: false,
        showFilter: false,
        enableSorting: true,
        sortInfo: {fields: ['submittedDate'], directions: ['asc']},
        columnDefs: [
            {field: 'program.id', displayName: messageService.get("program.header")},
            {field: 'facility.code', displayName: messageService.get("option.value.facility.code")},
            {field: 'facility.name', displayName: messageService.get("option.value.facility.name")},
            {field: 'period.startDate|date', displayName: messageService.get("label.period.start.date")},
            {field: 'period.endDate|date', displayName: messageService.get("label.period.end.date")},
            {field: 'ubmittedDate |date', displayName: messageService.get("label.date.submitted")},
            {field: 'modifiedDate|date', displayName: messageService.get("label.date.modified")},
            {field: 'status', displayName: messageService.get("label.status")},
            {
                field: 'emergency', displayName: messageService.get("requisition.type.emergency"),
                cellTemplate: '<div id="emergency{{$parent.$index}}" class="ngCellText checked"><i ng-class="{\'icon-ok\': row.entity.emergency}"></i></div>',
                width: 110
            }
        ]
    };

    $scope.openRequisition = function () {
        var startDate= new Date($scope.selectedItems[0].period.startDate);
        var endDate= new Date($scope.selectedItems[0].period.endDate);
        var data = {
            facilityId: startDate,
            dateRangeStart: endDate,
            dateRangeEnd: $scope.selectedItems[0].period.endDate,
            programs: $scope.programs
        };
        if ($scope.selectedProgramId) data.programId = $scope.selectedProgramId;
        navigateBackService.setData(data);

        redirectBasedOnFeatureToggle();
    };

    function redirectBasedOnFeatureToggle() {
        var url = "requisition/";
        var supplyType = "supplyType";
        var selectedFacilityIdUrlPart = "";

        // make reports coming from FE for a LAB program, redirect to edit page that view
        if ($scope.isFELABReportInEditMode && $scope.selectedItems[0].programCode === "LAB" &&
            $scope.selectedItems[0].sourceApplication.toUpperCase() === "ELMIS_FE") {
            url = "create-rnr/";
            selectedFacilityIdUrlPart = $scope.selectedItems[0].facilityId + "/";
            supplyType = "equipment";
        }

        var urlMapping = {"ESS_MEDS": "view-requisition-via/", "MMIA": "view-requisition-mmia/"};
        var viewToggleKey = {key: "new.rnr.view"};
        FeatureToggleService.get(viewToggleKey, function (result) {
            if (result.key) {
                url = urlMapping[$scope.selectedItems[0].programCode];
            }
            url += $scope.selectedItems[0].id + "/" + selectedFacilityIdUrlPart + $scope.selectedItems[0].programId + "?supplyType=" + supplyType + "&page=1";
            $location.url(url);
        });


    }

    $scope.parameterChanged = function () {
        $scope.loadSchedules();
        $scope.loadZones();
    };
    $scope.loadSchedules = function () {
        ReportProgramSchedules.get({
            program: $scope.selectedProgramId
        }, function (data) {

            $scope.schedules = unshift(data.schedules, 'report.filter.select.group');
        });
    };

    $scope.loadPeriods = function () {
        ReportPeriodsByScheduleAndYear.get({
            scheduleId: $scope.selectedScheduleId,
            year: $scope.selectedYear
        }, function (data) {
            $scope.periods = unshift(data.periods, 'report.filter.select.period');
        });
    };

    $scope.loadZones = function () {
        TreeGeographicZoneListByProgram.get({
            program: $scope.selectedProgramId
        }, function (data) {
            $scope.zones = [data.zone];
        });
    };

    function makeMonthlyScheduleDefault(list) {
        if (utils.isEmpty(scope.filter.schedule)) {
            monthlySchedule = _.find(list, function (program) {
                return program.code == 'Monthly';
            });
            if (!utils.isNullOrUndefined(monthlySchedule))
                scope.filter.schedule = monthlySchedule.id;
        }
    }


    function setProgramsLabel() {
        $scope.selectedProgramId = undefined;
        $scope.programLabel = (!$scope.programs.length) ? messageService.get("label.none.assigned") : messageService.get("label.all");
    }

    function setOptions() {
        $scope.options = ($scope.programs.length) ? [
            {field: "All", name: "All"}
        ] : [];
    }


    function setRequisitionsFoundMessage() {
        $scope.requisitionFoundMessage = ($scope.requisitions.length) ? "" : messageService.get("msg.no.rnr.found");
    }

    $scope.filterRequisitions = function () {
        $scope.filteredRequisitions = [];
        var query = $scope.query || "";

        $scope.filteredRequisitions = $.grep($scope.requisitions, function (rnr) {
            return contains(rnr.requisitionStatus, query);
        });

    };

    function contains(string, query) {
        return string.toLowerCase().indexOf(query.toLowerCase()) != -1;
    }

    $scope.setEndDateOffset = function () {
        if ($scope.endDate < $scope.startDate) {
            $scope.endDate = undefined;
        }
        $scope.endDateOffset = Math.ceil((new Date($scope.startDate.split('-')).getTime() + oneDay - Date.now()) / oneDay);
    };
    function unshift(array, displayKey) {
        if (angular.isArray(array) && array.length > 0) {
            array.unshift({
                name: messageService.get(displayKey)
            });
        } else if (angular.isArray(array) && array.length === 0) {
            array.push({name: messageService.get(displayKey)});
        }
        return array;
    }
}

var oneDay = 1000 * 60 * 60 * 24;

SearchRnrListController.resolve = {

    preAuthorize: function (AuthorizationService) {
        AuthorizationService.preAuthorize('VIEW_REQUISITION');
    },

    programs: function ($q, $timeout, ActivePrograms) {
        var deferred = $q.defer();
        $timeout(function () {
            ActivePrograms.get({}, function (data) {
                deferred.resolve(data.programs);
            }, {});
        }, 100);
        return deferred.promise;
    },
    years: function ($q, $timeout, OperationYears) {
        var deferred = $q.defer();
        $timeout(function () {
            OperationYears.get({}, function (data) {
                deferred.resolve(data.years);
            }, {});
        }, 100);
        return deferred.promise;
    }
};