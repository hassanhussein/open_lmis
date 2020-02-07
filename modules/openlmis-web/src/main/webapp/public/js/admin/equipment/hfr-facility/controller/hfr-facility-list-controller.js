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


       $scope.showConfirmActivateFaicility = function () {
            var dialogOpts = {
                id: "enableConfirmModal",
                header: "Creating new Facility",
                body: "'{0}' / '{1}' will be enabled in the system.".format($scope.originalFacilityName, $scope.originalFacilityCode)
            };
            OpenLmisDialog.newDialog(dialogOpts, $scope.enableFacilityCallBack, $dialog);
        };

        $scope.enableFacilityCallBack = function (result) {
            if (!result) return;
            Facility.restore({id: $scope.facility.id}, successFunc, errorFunc);
        };



    };
}