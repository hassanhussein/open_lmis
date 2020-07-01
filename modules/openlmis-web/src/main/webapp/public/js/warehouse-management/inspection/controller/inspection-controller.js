function InspectionController($scope,GetAllClearingAgents,$window,VaccineDiscardingReasons ,inspection, UpdateInspection,$location,vvmList,$timeout,GetLocationSummary){


$scope.globalErrorFlag=true;

    GetLocationBy.get({}, function(data){

        $scope.locations = data.locationList;

        console.log(data.locationList);

    });

/*GetLocationSummary.get({}, function(data){

     $scope.locations = data.locationList;

     console.log(data.locationList);

    });*/



 $scope.clearingAgentList = [];

GetAllClearingAgents.get({}, function(data){

           $scope.clearingAgentList = data.agents;
           console.log(data);

           });


 VaccineDiscardingReasons.get({},function(data){

   $scope.failReasons=_.reject(data.reasons, {name: "Other (Specify)"});

 });


$scope.productSum=function(lineItem){

  var sum=0;
 angular.forEach(lineItem.lots,function(lot){
    sum+=parseInt(lot.receivedQuantity,10);

 });
 $scope.totalReceivedQty=sum;
};




$scope.lotInspected=false;
//$scope.dryIceFlag=false;
//$scope.icePackFlag=false;
//$scope.noCoolantFlag=false;
   $scope.inspection = inspection;
   console.log($scope.inspection);
   $scope.lineItem=$scope.inspection.lineItems[0];
   $scope.vvmStatusList = vvmList;
   $scope.totalPassQty=0;
   $scope.totalFailQty=0;

$scope.productSum($scope.lineItem);

//   $scope.locations = [{"id":1,"name":'AA11'},{"id":2,"name":'BB11'},{"id":4,"name":'AA17'}];
   $scope.failReasons = [{"id":1,"name":'Temperature'},{"id":2,"name":'Rain'},{"id":3,"name":'Opened Vial'}];



$scope.vvmChanged=function(lot){

 if(lot.vvmStatus>2){

   lot.failQuantity=1;
   //make fail reason vvm
   lot.failReason=4;

 }else{
 lot.failQuantity='';
 lot.failReason='';
 }

};

//"inspectionLotForm['vvmStatus[' + $index + ']'].$error.required"


     $scope.updatePassedQuantity = function (lineItem) {

     var sum = 0;
     angular.forEach(lineItem.lots, function(pass){
      sum = sum + parseInt(pass.passQuantity,10);
     });
      return sum;

     };

//     $scope.updateReceivedQuantity = function (lineItem) {
//
//     var sum = 0;
//     angular.forEach(lineItem.lots, function(pass){
//      sum = parseInt(pass.passQuantity,10) + parseInt(pass.failQuantity,10);
//     });
//      return  sum;
//     };

     $scope.updateFailedQuantity = function (lineItem) {

     var sum = 0;
     angular.forEach(lineItem.lots, function(fail){
      sum = sum + parseInt(fail.failQuantity,10);
     });
      return sum;
     };


//       $scope.updateReceivedQuantity2 = function (lineItem) {
//
//          var sum = 0;
//          angular.forEach(lineItem.lots, function(pass){
//           sum = parseInt(pass.passQuantity,10) + parseInt(pass.failQuantity,10);
//          });
//           return sum;
//        };

$scope.noCoolant=function(lineItem){
if(lineItem.noCoolantFlag){
lineItem.icePackFlag=false;
lineItem.dryIceFlag=false;
}
};

$scope.coolant=function(lineItem){
if(lineItem.icePackFlag || lineItem.dryIceFlag){
lineItem.noCoolantFlag=false;
}


};

$scope.hasExpired=function(lot){
var today = new Date();
var expiryDate = new Date(lot.expiryDate);
if(today>=expiryDate){
//make fail quantity ==receive qty
lot.failQuantity=lot.receivedQuantity;
// make reason ==expiry
lot.failReason=1;
return true;
}
return false;
};

//$scope.hasExpired("2010-10-11");

    $scope.inspectLot = function(lineItem) {


      if($scope.productValid(lineItem)){
      return;
      }
//          console.log(lineItem);
     //$scope.updateReceivedQuantity(lineItem);

     //sum the passed lots


     //sum the failed lots
//     lineItem.passQuantity=sumLots('passQuantity',lineItem);
//     lineItem.failQuantity=sumLots('failQuantity',lineItem);
//
//     lineItem.quantityCounted=sumLots('receivedQuantity',lineItem);
//
//
//     $scope.inspectLotModal = false;

/*
            $scope.inspectLotModal = false;
        console.log($scope.inspection);*/


    };

    function sumLots(lotType,lineItem){

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



     $scope.showInspectLotModal = function() {
        $scope.enabled=false;
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
              $location.path('');

          };

  var success = function (data) {
    $scope.error = "";
    $scope.$parent.message = data.success;
    $scope.$parent.messageToDisplay = "Inspection Successiful Finalized";
    $scope.$parent.messageFlag = true;

    $scope.showError = false;
    $location.path('');
  };

  var error = function (data) {
    $scope.$parent.message = "";
     console.log(data);
    $scope.error = data.data.error;
    $scope.showError = true;
  };

        $scope.print = function (inspectionId){
                       console.log(inspectionId);
                        var url = '/rest-api/warehouse/inspection/var/print/'+ parseInt(inspectionId.id,10);

                        $window.open(url, '_blank');
                   };




    $scope.save = function (status) {
//check the outer form validations


       if ($scope.inspectionForm.$error.pattern || $scope.inspectionForm.$error.required) {
         $scope.showError = true;
         $scope.error = 'form.error';
         $scope.message = "";
         return;
       }

//check the inner form validation
// if invalid add css class to blink it
    if($scope.globalErrorFlag){
    $scope.enabled=true;
    return;
    }else{
    $scope.enabled=false;
    }

       if ($scope.inspection.id) {
         $scope.inspection.status  = status;
         $scope.inspection.receiptNumber = $scope.inspection.receive.receiveLineItems[0].receiveNumber;


         UpdateInspection.update({id: $scope.inspection.id}, $scope.inspection, success, error);

       }

     };



     $scope.doneLotInspection=function(lineItem){

//if ($scope.inspectLotForm.$error.required) {
//            $scope.showError = true;
//            $scope.error = 'form.error';
//            $scope.message = "";
//            return;
//        }

//console.log(lineItem);

$scope.globalErrorFlag=false;
   angular.forEach(lineItem.lots,function(lot){
//first lets check if batch is expired
if(!$scope.hasExpired(lot)){

       //check vvm
        if(lot.vvmStatus===null || !lot.vvmStatus){
            lot.vvmError=true;
            $scope.globalErrorFlag=true;
        }else{
            lot.vvmError=false;
        }
            //only check if lot has not expired
       //check storage location
//        if((lot.passLocationId===null || !lot.passLocationId) &&(lot.failQuantity<lot.receivedQuantity || lot.failQuantity==='')){
//               lot.passLocationError=true;
//               $scope.globalErrorFlag=true;
//           }else{
//               lot.passLocationError=false;
//           }
       //check fail qty
          if(lot.failQuantity>lot.receivedQuantity){
               lot.failQuantityError=true;
               $scope.globalErrorFlag=true;
           }else{
               lot.failQuantityError=false;
           }
       //check check fail reason
            //only if we have fail quantity
             if((lot.failQuantity!=='0' && lot.failQuantity)&&!lot.failReason){
                       lot.failReasonError=true;
                       $scope.globalErrorFlag=true;
                   }else{
                       lot.failReasonError=false;
                   }
}

   //check fail location
        //only if we have fail quantity
 if((lot.failLocationId===null|| !lot.failLocationId)&&lot.failQuantity!=='0' && lot.failQuantity){
                       lot.failLocationError=true;
                       $scope.globalErrorFlag=true;
                   }else{
                       lot.failLocationError=false;
                   }




   });

    if($scope.globalErrorFlag){
    return;
    }else{
    $scope.globalErrorFlag=false;
    }

   //process passed qty for all lots
   angular.forEach(lineItem.lots,function(lot){
    if(lot.failQuantity===''){
    lot.failQuantity=0;
    }
     lot.passQuantity=lot.receivedQuantity-parseInt(lot.failQuantity,10);
   });

  $scope.totalPassQty=sumLots('passQuantity',lineItem);
  $scope.totalFailQty=sumLots('failQuantity',lineItem);
  $scope.totalReceivedQty=$scope.totalFailQty+$scope.totalPassQty;
  $scope.inspectLotModal = false;
  $scope.lotInspected=true;



     };


  inspectionLotFormValidator=function(){




if ($scope.inspectLotForm.$error.required) {
            $scope.showError = true;
            $scope.error = 'form.error';
            $scope.message = "";
            return;
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
//      console.log(data.inspection)
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