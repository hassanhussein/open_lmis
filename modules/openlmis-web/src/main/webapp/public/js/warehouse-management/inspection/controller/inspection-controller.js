function InspectionController($scope,$location,Locations){

$scope.vvms=[{status:'VVM 1'},
            {status:'VVM 2'},
            {status:'VVM 3'},
];



$scope.locations=[
{
id:1,
code:'code1'
},
{
id:2,
code:'code2'
},
{
id:3,
code:'code3'
},
{
id:4,
code:'code4'
}
];


$scope.suppliers=[{id:1,name:'UNICEF'}];
$scope.ports=[{id:1,name:'Dar'}];
$scope.inspection=$scope.$parent.inspection;




     $scope.showInspectLotModal = function() {

          $scope.inspectLotModal = true;


      };


      $scope.closeInspectLotModal = function() {
          $scope.inspectLotModal = false;
      };


  $scope.cancel = function () {
    $scope.$parent.inspection = undefined;
    $scope.$parent.message = "";
    $location.path('#/search');
  };
$scope.inspectLot=function(){

//validate the lots

if(!$scope.productValid()){
return;
}

//sum the passed lots
$scope.inspection.product.passedQty=sumLots('passedQty');

//sum the failed lots
$scope.inspection.product.failedQty=sumLots('failedQty');


//close the modal
$scope.closeInspectLotModal();

};

function sumLots(lotType){
var sum = 0;
angular.forEach($scope.inspection.product.lots,function(lot){
sum+=parseInt(lot[lotType],10);
});
return sum;
}

  $scope.productValid=function(){
  //validate quantity
  var totalLotQty;
  $scope.lotsWithError='';
  $scope.lotsWithLocationError='';
  $scope.lotsWithVvmError='';
  angular.forEach($scope.inspection.product.lots,function(lot){
  totalLotQty=parseInt(lot.passedQty,10) + parseInt(lot.failedQty,10);
  if(totalLotQty!=lot.quantity){
  $scope.lotsWithError+=lot.code+', ';
  }
  //location
  if(!lot.passedLocation || !lot.failedLocation){
  $scope.lotsWithLocationError+=lot.code+', ';
  }

    //vvm
    if(!lot.vvm){
    $scope.lotsWithVvmError+=lot.code+', ';
    }
  });

  if($scope.lotsWithError||$scope.lotsWithLocationError ||$scope.lotsWithVvmError){
  $scope.totalQtyError=true;
  return false;
  }else{
  $scope.totalQtyError=false;
  return true;
  }



  //validate location



  //validate vvm status

  return errors;
  };

  $scope.save=function(){

  $scope.productError=false;

//  validate product here

if(!$scope.productValid()){
 $scope.productError=true;
return;
}
   if ($scope.inspectionForm.$error.required) {

              $scope.showError = true;
              $scope.error = 'form.error';
              $scope.message = "";
              return;
          }
  };
}