function InspectionController($scope){

$scope.inspection=$scope.$parent.inspection;




     $scope.showInspectLotModal = function() {
          $scope.inspectLotModal = true;
      };


      $scope.closeInspectLotModal = function() {
          $scope.inspectLotModal = false;
      };
}