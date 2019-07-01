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

function EquipmentInventoryController($scope, EquipmentInventorySearch, navigateBackService, GetByDistrict, GeoDistrictTree,
                                      breadcrumbs, NumberOfYears, DeleteEquipmentInventory, UserFacilityList, EquipmentInventories,
                                      ManageEquipmentInventoryProgramList, ManageEquipmentInventoryFacilityProgramList,
                                      EquipmentTypesByProgram, EquipmentOperationalStatus, $routeParams, messageService,
                                      UpdateEquipmentInventoryStatus, $timeout, SaveEquipmentInventory, localStorageService, $dialog,
                                      ToggleVerifiedEquipmentInventory, MoveEquipmentInventory) {

    $scope.currentPage = 1;
    $scope.selectedSearchOption = navigateBackService.selectedSearchOption;// || $scope.searchOptions[0];

    $scope.triggerSearch = function (event) {
        if (event.keyCode === 13) {
            $scope.searchInventory(1);
            //$scope.loadFacilities(1);
        }
    };

  /*  $scope.$watch('currentPage', function () {
         if ($scope.currentPage !== 0)
            $scope.loadFacilities($scope.currentPage, $scope.searchedQuery);
    });*/

    $scope.loadFacilities = function (page, lastQuery) {
        if (!($scope.query || lastQuery)) return;
        lastQuery ? getFacilities(page, lastQuery) : getFacilities(page, $scope.query);
    };
    $scope.$on('$viewContentLoaded', function () {
        $scope.query = navigateBackService.query;
    });

    function getFacilities(page, query) {
        $scope.searchInventory();
        query = query.trim();
        $scope.searchedQuery = query;
 /*       Facility.get({"searchParam": $scope.searchedQuery, "columnName": $scope.selectedSearchOption.value, "page": page}, function (data) {
            $scope.facilityList = data.facilities;
            $scope.pagination = data.pagination;
            $scope.totalItems = $scope.pagination.totalRecords;
            $scope.currentPage = $scope.pagination.page;
            $scope.showResults = true;
        }, {});*/
    }



    $scope.breadcrumbs = breadcrumbs;


    function getDataForDisplay(data) {
       // console.log(data);
        var district;
        if (data.id === null) {
            district = data.regionId;
        } else
            district = data.id;
        $scope.showFacilityLevel = false;

        GetByDistrict.get({districtId: parseInt(district, 10)}, function (data) {
            $scope.displayLevel = data.facility.name;
            $scope.showFacilityLevel = true;

           // $state.go('toState', { 'facilityId':parseInt(data.facility.id, 10), 'facilityName':facilityName, 'etc':'bluebell' });


        });


    }

    GeoDistrictTree.get({}, function (data) {

        var data2 = data.regionFacilityTree;
        $('#tree').treeview({
            data: data2,
            levels: 2,
            color: "#398085",
            onhoverColor: 'lightblue',
            // href: "#node-1",
            //selectable: false,
            state: {
                //checked: true,
                disabled: true,
                // expanded: true,
                selected: true
            },
            //silent: true,
            onNodeSelected: function (event, data) {
                getDataForDisplay(data);
            }



        },'disableNode',  {levels: 2, silent: true } );

    });

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
            if (initialLoad && $routeParams.program) {
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
      console.log($routeParams);
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

  $scope.searchInventory = function(){
      EquipmentInventorySearch.get({searchParam:$scope.query, typeId: $scope.selectedType,
          programId: $scope.selectedProgram.id,
          equipmentTypeId: $scope.selectedEquipmentType.id,
          page: $scope.page}, function (data) {

          $scope.inventory = data.inventories;
          $scope.pagination = data.pagination;
          $scope.totalItems = $scope.pagination.totalRecords;
          $scope.currentPage = $scope.pagination.page;

          console.log($scope.pagination);


          var groupedInventoryData = _.groupBy($scope.inventory,function(item){
              return (item.equipment.designation) ? item.equipment.designation.name: '-';
          });

          $scope.inventoryList = $.map(groupedInventoryData, function(value, index) {
              return {"designation":index,"inventory":value};
          });



      });
  };


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

  $scope.changeFacilityType = function () {
    $scope.programs = undefined;
    $scope.selectedProgram = undefined;
    $scope.equipmentTypes = undefined;
    $scope.selectedEquipmentType = undefined;

    $scope.loadPrograms();

  };

  $scope.changeProgram = function () {
    $scope.equipmentTypes = undefined;
    $scope.selectedEquipmentType = undefined;

    $scope.loadEquipmentTypes();
  };

  $scope.updateStatus = function (item) {
    // Create original inventory item to use later if user cancels
    $scope.origModalItem = angular.copy(item);
    $scope.origModalItem.operationalStatusId = $scope.origModalItem.prevStatusId;

    // updateStatus is called on dropdown load, so only do something if previous value set and is different
    if (item.prevStatusId && item.prevStatusId !== item.operationalStatusId) {
      var operationalStatus = _.findWhere($scope.operationalStatusList, {id: parseInt(item.operationalStatusId, 10)});
      // If status is "bad", open modal, otherwise just save to the server
      if (operationalStatus.isBad) {
        $scope.notFunctionalModal = true;
        $scope.modalItem = item;

        $scope.checkForBadFunctionalStatus($scope.modalItem.notFunctionalStatusId);
        $scope.prevStatusId = item.prevStatusId; // Need to save previous since item.prevStatusId gets overwritten
                                                 // at the end of this function
      } else {
        item.notFunctionalStatusId = null; // Reset for the UI
        UpdateEquipmentInventoryStatus.save({}, item, function () {
          // Success
          item.showSuccess = true;
          $timeout(function () {
            item.showSuccess = false;
          }, 3000);
        });
      }
    }

    // Set a previous value to compare with
    item.prevStatusId = item.operationalStatusId;
  };

  $scope.checkForBadFunctionalStatus = function (statusId) {
    if (statusId) {

      var notFunctionalStatus = _.findWhere($scope.notFunctionalStatusList, {id: parseInt(statusId, 10)});

      $scope.modalItem.badFunctionalStatusSelected = notFunctionalStatus.isBad;
      $scope.modalItem.needSpareFunctionalStatusSelected = notFunctionalStatus.needSparePart;
    }
  };
  $scope.saveModal = function () {
    $scope.modalError = '';
    if (!$scope.notFunctionalForm.$invalid) {
      SaveEquipmentInventory.save($scope.modalItem, function () {
        // Success
        $scope.notFunctionalModal = false;
        $scope.modalItem.badFunctionalStatusSelected = false;
        $scope.modalItem.needSpareFunctionalStatusSelected = false;
        $scope.modalItem.showSuccess = true;
        $timeout(function () {
          $scope.modalItem.showSuccess = false;
        }, 3000);
      }, function (data) {
        $scope.modalError = data.error;
      });
    } else {
      $scope.modalError = messageService.get('message.equipment.inventory.data.invalid');
    }
  };

  $scope.closeModal = function () {
    $scope.notFunctionalModal = false;

    // Reset inventory with original inventory item
    // Doing this with map because underscore version doesn't support findIndex
    $scope.inventory = _.map($scope.inventory, function (obj) {
      if (obj.id === $scope.modalItem.id) {
        return $scope.origModalItem;
      } else {
        return obj;
      }
    });

    // Reset other values
    $scope.modalItem.needSpareFunctionalStatusSelected = false;
    $scope.modalItem.badFunctionalStatusSelected = false;
    $scope.modalError = '';
  };

  $scope.getAge = function (yearOfInstallation) {
    if (yearOfInstallation) {
      return (new Date().getFullYear()) - yearOfInstallation;
    } else {
      return null;
    }
  };

  $scope.getReplacementYear = function (yearOfInstallation) {

    if (yearOfInstallation) {

      return yearOfInstallation + parseInt(NumberOfYears,10);

    } else {
      return null;
    }
  };

  $scope.toggleVerifySerialNumber = function(item) {
    //return
    // show a confirmation dialog box
    var callBack=function(result){
      if(result){
        ToggleVerifiedEquipmentInventory.put({id:item.id},function(data){
          $scope.loadInventory();
        });
      }else{
        $scope.loadInventory();
      }
    };
    var options = {
      id: "confirmDialog",
      header: "label.confirm.toggle.equipment.inventory",
      body: "msg.question.toggle.equipment.inventory.confirmation"
    };
    // display that confirmation dialog box.
    OpenLmisDialog.newDialog(options, callBack, $dialog);
  };

  $scope.updateTemperatureStatusModal = function (equipment){
    if(!isUndefined(equipment)){
      $scope.temperatureStatusModal = true;
       $scope.coldChain = equipment;
    }

  };

  $scope.closeColdChainTemperatureModal= function () {
    $scope.temperatureStatusModal = false;
  };

  $scope.$watch('currentPage', function () {
    if ($scope.currentPage > 0) {
      $scope.page = $scope.currentPage;
      $scope.loadInventory();
    }
  });
  function getGeographicZone(item) {
    return item.facility.geographicZone.name;
  }

  $scope.selectedType = $routeParams.from || "0";
  $scope.page = $routeParams.page || "1";
  $scope.loadPrograms(true);

  EquipmentOperationalStatus.get(function(data){
    $scope.operationalStatusList = _.where(data.status, {category: 'CCE'});
    $scope.notFunctionalStatusList = _.where(data.status, {category: 'CCE Not Functional'});
  });

  $scope.deleteInventory=function(id){
      var callBack=function(result){
        if(result){
           DeleteEquipmentInventory.get({inventoryId:id},function(data){
                 $scope.loadInventory();
           });
         }
       };
        var options = {
                           id: "confirmDialog",
                           header: "label.confirm.delete.equipment.inventory",
                           body: "msg.question.delete.equipment.inventory.confirmation"
                       };
      OpenLmisDialog.newDialog(options, callBack, $dialog);
  };

  $scope.moveInventory = function (item) {
    $scope.moveToFacility = undefined;
    $scope.inventoryToMove = item;
    $scope.showMoveFacilityModal = true;
  };

  $scope.associate = function (facility) {
    $scope.moveToFacility = facility;
    $scope.showSlider = !$scope.showSlider;
  };

  $scope.toggleSlider = function () {
    $scope.showSlider = !$scope.showSlider;
    $scope.extraParams = {"virtualFacility": null, "enabled": true};
  };

  $scope.moveToFacilityClicked = function () {
    console.log('move to facility called.');
    MoveEquipmentInventory.put({id: $scope.inventoryToMove.id, facilityId: $scope.moveToFacility.id}, function (data) {
      $scope.loadInventory();
      $scope.showMoveFacilityModal = false;
    });
  };

  $scope.loadRights = function () {
        $scope.rights = localStorageService.get(localStorageKeys.RIGHT);
  }();

  $scope.hasPermission = function (permission) {
              if ($scope.rights !== undefined && $scope.rights !== null) {
                var rights = JSON.parse($scope.rights);
                var rightNames = _.pluck(rights, 'name');
                return rightNames.indexOf(permission) > -1;
              }
              return false;
  };

}


EquipmentInventoryController.resolve = {

  NumberOfYears: function ($q, $timeout, SettingsByKey) {
    var deferred = $q.defer();
    $timeout(function () {

      SettingsByKey.get({key: 'YEARS_TO_REPLACE_EQUIPMENT'}, function (data) {
        if (isUndefined(data.settings) || isUndefined(data.settings.value)) {
          deferred.resolve(10);
        } else {

          deferred.resolve(data.settings.value);
        }

      });

    }, 100);

    return deferred.promise;

  }



};
