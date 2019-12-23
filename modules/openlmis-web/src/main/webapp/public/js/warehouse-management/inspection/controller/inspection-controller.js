function InspectionController($scope,$window, inspection){

   $scope.inspection = inspection;

  console.log(inspection);



     $scope.showInspectLotModal = function() {
          $scope.inspectLotModal = true;
      };


      $scope.closeInspectLotModal = function() {
          $scope.inspectLotModal = false;
      };


      $scope.downloadFile = function (file,asnCode){

      var url ='/rest-api/warehouse/downloadFile?filename='+file;
      $window.open(url, '_blank');

      };

     $scope.cancel = function() {
              $scope.message = "";
              $scope.error = "";
              $scope.showError = false;
           //   $scope.$parent.asnId = false;
            //  $scope.$parent.asnIdUpdate = false;
              $location.path('');

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