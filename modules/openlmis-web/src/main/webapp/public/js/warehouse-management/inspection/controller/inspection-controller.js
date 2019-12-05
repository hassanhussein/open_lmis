function InspectionController($scope,$location){

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
]


$scope.suppliers=[{id:1,name:'UNICEF'}];
$scope.ports=[{id:1,name:'Dar'}];
$scope.inspection=$scope.$parent.inspection;




     $scope.showInspectLotModal = function() {
     console.log('helo')
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


  $scope.validateProductInspection=function(){

  //validate quantity

  //validate location

  //validate vvm status

  return errors;
  }
}