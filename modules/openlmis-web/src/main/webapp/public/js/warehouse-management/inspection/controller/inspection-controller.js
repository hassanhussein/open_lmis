function InspectionController($scope){

$scope.inspection=$scope.$parent.inspection;




     $scope.showInspectLotModal = function() {
     console.log('helo')
          $scope.inspectLotModal = true;


      };


      $scope.closeInspectLotModal = function() {
          $scope.inspectLotModal = false;
      };
}