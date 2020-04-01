function CreateMonitoringFormController($location,$scope,programs,GetDistrictsBy) {

 $scope.programList = programs;


 $scope.OnFilterChanged = function() {

   loadDistricts();

 };

 function loadDistricts() {

  GetDistrictsBy.get({programId:parseInt($scope.filter.program,10)}, function (data) {
    $scope.districtList = [data.districts];
    console.log(data.districts);
  })
 }

 $scope.openMonitoringForm = function() {

 var params = {};

 params.zoneId = parseInt($scope.zoneId,10);

 params.programId = parseInt($scope.filter.program,10);

 params.reportedDate = null;

var createRnrPath = '/create-form/' + params.zoneId +'/' + params.programId;
   $location.url(createRnrPath);
 }

}


CreateMonitoringFormController.resolve = {

   programs: function ($q, $timeout, Programs) {
        var deferred = $q.defer();
        $timeout(function () {
            Programs.get({}, function (data) {

                deferred.resolve(data.programs);
            }, {});
        }, 100);
        return deferred.promise;
    }


}