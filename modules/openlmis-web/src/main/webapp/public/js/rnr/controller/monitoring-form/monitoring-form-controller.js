function CreateMonitoringFormController($location,$scope,programs,CreateRequisitionProgramList,UserFacilityList,navigateBackService,GetDistrictsBy,UserSupervisedFacilitiesForProgramForMonitoringForm) {

 $scope.programList = programs;


 $scope.OnFilterChanged = function() {

   loadDistricts();

 };

 function loadDistricts() {

  GetDistrictsBy.get({programId:parseInt($scope.filter.program,10)}, function (data) {
    $scope.districtList = [data.districts];
    console.log(data.districts);
  })
 }

 $scope.openMonitoringForm = function() {

 var params = {};

 params.zoneId = parseInt($scope.zoneId,10);

 params.programId = parseInt($scope.filter.program,10);

 params.reportedDate = null;

var createRnrPath = '/create-form/' + params.zoneId +'/' + params.programId;
   $location.url(createRnrPath);
 }

var resetRnrData = function () {
    $scope.periodGridData = [];
    $scope.selectedProgram = null;
    $scope.selectedFacilityId = null;
    $scope.myFacility = null;
    $scope.programs = null;
    $scope.facilities = null;
    $scope.error = null;
  };

 $scope.$on('$viewContentLoaded', function () {
    $scope.selectedType = navigateBackService.selectedType || "0";
    $scope.selectedProgram = navigateBackService.selectedProgram;
    $scope.selectedFacilityId = navigateBackService.selectedFacilityId;
    isNavigatedBack = navigateBackService.isNavigatedBack;
    $scope.$watch('programs', function () {
      isNavigatedBack = navigateBackService.isNavigatedBack;
      if (!isNavigatedBack) $scope.selectedProgram = undefined;
      if ($scope.programs && !isUndefined($scope.selectedProgram)) {
        $scope.selectedProgram = _.where($scope.programs, {id: $scope.selectedProgram.id})[0];
        $scope.loadPeriods();
      }
    });
    $scope.loadFacilityData($scope.selectedType);
    if (isNavigatedBack) {
      $scope.loadFacilitiesForProgram();
    }
    $scope.$watch('facilities', function () {
      if ($scope.facilities && isNavigatedBack) {
        $scope.selectedFacilityId = navigateBackService.selectedFacilityId;
        isNavigatedBack = false;
      }
    });
  });

$scope.loadFacilityData = function (selectedType) {
    isNavigatedBack = isNavigatedBack ? selectedType !== "0" : resetRnrData();

    if (selectedType === "0") { //My facility
      UserFacilityList.get({}, function (data) {
        $scope.facilities = data.facilityList;
        $scope.myFacility = data.facilityList[0];
        if ($scope.myFacility) {
          $scope.facilityDisplayName = $scope.myFacility.code + '-' + $scope.myFacility.name;
          $scope.selectedFacilityId = $scope.myFacility.id;

          CreateRequisitionProgramList.get({facilityId: $scope.selectedFacilityId}, function (data) {
            $scope.programs = data.programList;
          }, {});
        } else {
          $scope.facilityDisplayName = messageService.get("label.none.assigned");
          $scope.programs = null;
          $scope.selectedProgram = null;
        }
      }, {});
    } else if (selectedType === "1") { // Supervised facility
      resetRnrData();
      CreateRequisitionProgramList.get({}, function (data) {
        $scope.programs = data.programList;
      }, {});
    }
  };

  $scope.loadFacilitiesForProgram = function () {
    if ($scope.selectedProgram.id) {
      UserSupervisedFacilitiesForProgramForMonitoringForm.get({programId: $scope.selectedProgram.id}, function (data) {
        $scope.facilities = data.facilities;
        $scope.selectedFacilityId = null;
        $scope.error = null;
      }, {});
    } else {
      $scope.facilities = null;
      $scope.selectedFacilityId = null;
    }
  };






}


CreateMonitoringFormController.resolve = {

   programs: function ($q, $timeout, Programs) {
        var deferred = $q.defer();
        $timeout(function () {
            Programs.get({}, function (data) {

                deferred.resolve(data.programs);
            }, {});
        }, 100);
        return deferred.promise;
    }


}