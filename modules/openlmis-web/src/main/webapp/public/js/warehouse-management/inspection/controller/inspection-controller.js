function InspectionController($scope){

     $scope.showInspectLotModal = function() {
     console.log('helo')
          $scope.inspectLotModal = true;


      };

      $scope.closeInspectLotModal = function() {
          $scope.inspectLotModal = false;
      };
}