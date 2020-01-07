function InspectionController($scope,$window, inspection, UpdateInspection,$location,vvmList,$timeout){

   $scope.inspection = inspection;
   $scope.vvmStatusList = vvmList;
   console.log(inspection);

   $scope.locations = [{"id":1,"name":'AA11'},{"id":2,"name":'BB11'},{"id":4,"name":'AA17'}];
   $scope.failReasons = [{"id":1,"name":'Temperature'},{"id":2,"name":'Rain'},{"id":3,"name":'Opened Vial'}];


     $scope.updatePassedQuantity = function (lineItem) {

     var sum = 0;
     angular.forEach(lineItem.lots, function(pass){
      sum = sum + parseInt(pass.passQuantity,10);
     });
      return sum;

     };

     $scope.updateReceivedQuantity = function (lineItem) {

     var sum = 0;
     angular.forEach(lineItem.lots, function(pass){
      sum = parseInt(pass.passQuantity,10) + parseInt(pass.failQuantity,10);
     });
      return  sum;
     };

     $scope.updateFailedQuantity = function (lineItem) {

     var sum = 0;
     angular.forEach(lineItem.lots, function(fail){
      sum = sum + parseInt(fail.failQuantity,10);
     });
     // $scope.updateReceivedQuantity2(lineItem);
      return sum;
     };


       $scope.updateReceivedQuantity2 = function (lineItem) {

          var sum = 0;
          angular.forEach(lineItem.lots, function(pass){
           sum = parseInt(pass.passQuantity,10) + parseInt(pass.failQuantity,10);
          });
           return sum;
        };

    $scope.inspectLot = function(lineItem) {


      if($scope.productValid(lineItem)){
      return;
      }
          console.log(lineItem);
     //$scope.updateReceivedQuantity(lineItem);

     //sum the passed lots


     //sum the failed lots
     lineItem.passQuantity=sumLots('passQuantity',lineItem);
     lineItem.failQuantity=sumLots('failQuantity',lineItem);

     lineItem.quantityCounted=sumLots('receivedQuantity',lineItem);


     $scope.inspectLotModal = false;

/*
            $scope.inspectLotModal = false;
        console.log($scope.inspection);*/


    };

    function sumLots(lotType,lineItem){
    console.log(lineItem);
    var sum = 0;
    angular.forEach(lineItem.lots,function(lot){
    sum+=parseInt(lot[lotType],10);
    });
    return sum;
    }

    $scope.productValid=function(lineItem) {

     var totalLotQty=0;
     $scope.lotsWithError='';
     $scope.lotsWithLocationError='';
     $scope.lotsWithVvmError='';

      $scope.lotError = false;
     angular.forEach(lineItem.lots,function(lot) {


     totalLotQty=parseInt(lot.passQuantity,10) + parseInt(lot.failQuantity,10);

     if(totalLotQty!=lot.receivedQuantity) {
     $scope.lotsWithError+=lot.lotNumber+', ';
     $scope.lotError = true;
     }
     //location
   /*  if(!lot.passedLocation || !lot.failedLocation){
     $scope.lotsWithLocationError+=lot.lotNumber+', ';
     }
*/
     //vvm
   /*  if(!lot.vvm) {
     $scope.lotsWithVvmError+=lot.lotNumber+', ';
     }*/
     });
     return $scope.lotError;

    };



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
    $scope.$parent.messageToDisplay = "Inspection Successiful Finalized";
    $scope.$parent.messageFlag = true;

/*    $timeout(function(){

    console.log($scope);
        $scope.$parent.messageFlag = false;
    },2000);*/



   // $scope.$parent.geoZoneId = data.geoZone.id;
    $scope.showError = false;
    $location.path('');
  };

  var error = function (data) {
    $scope.$parent.message = "";
     console.log(data);
    $scope.error = data.data.error;
    $scope.showError = true;
  };



    $scope.save = function (status) {


       if ($scope.inspectionForm.$error.pattern || $scope.inspectionForm.$error.required) {
         $scope.showError = true;
         $scope.error = 'form.error';
         $scope.message = "";
         return;
       }


       if ($scope.inspection.id) {
         $scope.inspection.status  = status;
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