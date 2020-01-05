function InspectionController($scope,$window, inspection, UpdateInspection,$location,vvmList){

   $scope.inspection = inspection;
   $scope.vvmStatusList = vvmList;

   $scope.locations = [{"id":1,"name":'AA11'},{"id":2,"name":'BB11'},{"id":3,"name":'AA17'}];
   $scope.failReasons = [{"id":1,"name":'Temperature'},{"id":2,"name":'Rain'},{"id":3,"name":'Opened Vial'}];




    $scope.inspectLot = function(lineItem) {
        console.log($scope.inspection);

    }

     $scope.showInspectLotModal = function(lineItem) {

     $scope.lineItem = lineItem;

     console.log(lineItem);
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

  var success = function (data) {
    $scope.error = "";
    $scope.$parent.message = data.success;
   // $scope.$parent.geoZoneId = data.geoZone.id;
    $scope.showError = false;
    $location.path('');
  };

  var error = function (data) {
    $scope.$parent.message = "";
    $scope.error = data.data.error;
    $scope.showError = true;
  };


    $scope.save = function () {


       if ($scope.inspectionForm.$error.pattern || $scope.inspectionForm.$error.required) {
         $scope.showError = true;
         $scope.error = 'form.error';
         $scope.message = "";
         return;
       }


       if ($scope.inspection.id) {
         $scope.inspection.status  = 'DRAFT';
         console.log($scope.inspection);
         UpdateInspection.update({id: $scope.inspection.id}, $scope.inspection, success, error);
       }

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
  },

   vvmList: function ($q, $route, $timeout, GetVVMStatusList) {

    var deferred = $q.defer();

    $timeout(function () {
      GetVVMStatusList.get({}, function (data) {
        deferred.resolve(data.vvms);
      }, {});
    }, 100);
    return deferred.promise;
  }




};