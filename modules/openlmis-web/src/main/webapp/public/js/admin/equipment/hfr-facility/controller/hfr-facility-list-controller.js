function HFRControllerFunc($scope,$timeout, GetHFRFacilities) {
    GetHFRFacilities.get({}, function (data) {
        console.log(data);
        $scope.facilities = data.facilities;
    });

    function showMessage() {
    $timeout(function(){

    $scope.displayMessage = false;

    },6000);

    }

    $scope.activateFacilities = function(facility) {

      if(!facility.activatedbymsd && isUndefined(facility.msdcode)) {

      $scope.facilityCode = facility.facidnumber;
      $scope.displayMessage = true;

      showMessage();

       return;
      }

    console.log(facility.msdcode);

    }
}