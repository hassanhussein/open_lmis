function InspectionController($scope, inspection){

   $scope.inspection = inspection;

  console.log(inspection);



     $scope.showInspectLotModal = function() {
          $scope.inspectLotModal = true;
      };


      $scope.closeInspectLotModal = function() {
          $scope.inspectLotModal = false;
      };
}



InspectionController.resolve = {

  inspection: function ($q, $route, $timeout, GetInspectionById) {
    if ($route.current.params.id === undefined) return undefined;

    var deferred = $q.defer();
    var inspectionId = $route.current.params.id;

    $timeout(function () {
      GetInspectionById.get({id: inspectionId}, function (data) {
        deferred.resolve(data.inspection);
      }, {});
    }, 100);
    return deferred.promise;
  }




};