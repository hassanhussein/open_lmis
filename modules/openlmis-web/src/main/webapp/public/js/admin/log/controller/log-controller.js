function LogController($scope,$sce,GetAllLongs,$window){

$scope.data = 'data V';

GetAllLongs.get({}, function(data){
  $scope.logs = data.logs;

});
var data = 'some data here...',
        blob = new Blob([data], { type: 'text/plain' }),
        url = $window.URL || $window.webkitURL;
    $scope.fileUrl = url.createObjectURL(blob);

$scope.returnData = function(details) {

var blob = new Blob(["details"], { type: 'text/plain' });
var url = $window.URL || $window.webkitURL;

console.log(url);
 //$window.open(url, '_blank');

//$scope.fileUrl2 = url.createObjectURL(blob);
return $sce.trustAsResourceUrl(url.createObjectURL(blob));

};

}