function EquipmentSummaryControllerFunc($scope,ManageEquipmentInventoryFacilityProgramList,EquipmentInventories,UserFacilityList) {

    $scope.changeFacilityType = function () {
        $scope.programs = undefined;
        $scope.selectedProgram = undefined;
        $scope.equipmentTypes = undefined;
        $scope.selectedEquipmentType = undefined;

       // $scope.loadPrograms();

    };

    function getGeographicZone(item) {
        return item.facility.geographicZone.name;
    }


    EquipmentInventories.get({
        typeId: 0,
        programId: 82,
        equipmentTypeId: 44,
        page: 1
    }, function (data) {
        $scope.inventory = data.inventory;

        var groupedInventoryData = _.groupBy($scope.inventory,function(item){
            return (item.equipment.designation) ? item.equipment.designation.name: '-';
        });

        $scope.inventoryList = $.map(groupedInventoryData, function(value, index) {
            return {"designation":index,"inventory":value};
        });

        console.log($scope.inventoryList);

        $scope.pagination = data.pagination;
        $scope.totalItems = $scope.pagination.totalRecords;
        $scope.currentPage = $scope.pagination.page;
        $scope.groups = _.groupBy($scope.inventory, function (item) {
            return item.facility.geographicZone.parent.name;
        });
        for (var prop in $scope.groups) {
            $scope.groups[prop] = _.groupBy($scope.groups[prop], getGeographicZone);
        }
    });


/*

    $scope.loadPrograms = function (initialLoad) {
        // Get home facility for user
        UserFacilityList.get({}, function (data) {
            $scope.myFacility = data.facilityList[0];
            if ($scope.myFacility) {
                // Home facility found, show home facility
                $scope.facilityDisplayName = $scope.myFacility.name;

                // Home facility found and my facility type selected, get home facility programs
                if ($scope.selectedType === "0") {
                    $scope.showTree = false;
                    $scope.showFacilityLevel = false;
                    ManageEquipmentInventoryFacilityProgramList.get({facilityId: $scope.myFacility.id}, function (data) {
                        $scope.programs = data.programs;
                        if (initialLoad) {
                            $scope.selectedProgram = _.findWhere($scope.programs, {id: parseInt($routeParams.program,10)});
                            $scope.loadEquipmentTypes(initialLoad);
                        } else if ($scope.programs.length === 1) {
                            $scope.selectedProgram = $scope.programs[0];
                            $scope.loadEquipmentTypes();
                        }
                    }, {});
                }
            } else {
                // Home facility not found, show none assigned message
                $scope.facilityDisplayName = messageService.get("label.none.assigned");
            }
        }, {});

        // Supervised facility type selected, get supervised facility programs
        if ($scope.selectedType === "1") {
            //$scope.showTree = true;

            ManageEquipmentInventoryProgramList.get({}, function (data) {
                $scope.programs = data.programs;
                if (initialLoad && $routeParams.program) {
                    $scope.selectedProgram = _.findWhere($scope.programs, {id: parseInt($routeParams.program, 10)});
                    $scope.loadEquipmentTypes(initialLoad);
                } else if ($scope.programs.length === 1) {
                    $scope.selectedProgram = $scope.programs[0];
                    $scope.loadEquipmentTypes();
                }
            }, {});
        }
    };

    $scope.loadEquipmentTypes = function (initialLoad) {
        EquipmentTypesByProgram.get({programId: $scope.selectedProgram.id}, function (data) {
            $scope.equipmentTypes = data.equipment_types;
            if (initialLoad && $routeParams.equipmentType) {
                $scope.selectedEquipmentType = _.findWhere($scope.equipmentTypes, {id: parseInt($routeParams.equipmentType,10)});
                $scope.loadInventory();
            } else if ($scope.equipmentTypes.length === 1) {
                $scope.selectedEquipmentType = $scope.equipmentTypes[0];
                $scope.loadInventory();
            }
        }, {});
    };
    $scope.selectedType = "0";


    $scope.loadInventory = function () {
        if ($scope.selectedProgram && $scope.selectedEquipmentType) {
            EquipmentInventories.get({
                typeId: $scope.selectedType,
                programId: $scope.selectedProgram.id,
                equipmentTypeId: $scope.selectedEquipmentType.id,
                page: $scope.page
            }, function (data) {
                $scope.inventory = data.inventory;

                var groupedInventoryData = _.groupBy($scope.inventory,function(item){
                    return (item.equipment.designation) ? item.equipment.designation.name: '-';
                });

                $scope.inventoryList = $.map(groupedInventoryData, function(value, index) {
                    return {"designation":index,"inventory":value};
                });

                $scope.pagination = data.pagination;
                $scope.totalItems = $scope.pagination.totalRecords;
                $scope.currentPage = $scope.pagination.page;
                $scope.groups = _.groupBy($scope.inventory, function (item) {
                    return item.facility.geographicZone.parent.name;
                });
                for (var prop in $scope.groups) {
                    $scope.groups[prop] = _.groupBy($scope.groups[prop], getGeographicZone);
                }
            });
        }
    };

    $scope.loadPrograms(true);
*/



}