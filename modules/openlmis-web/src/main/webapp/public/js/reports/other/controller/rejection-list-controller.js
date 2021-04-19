
function RejectionByZoneControllerFunction($scope,$state,$stateParams,GetRejectedRnRsWithReason) {
    "use strict";
    $scope.zone = $stateParams.zone;

    GetRejectedRnRsWithReason.get($stateParams,function (data) {

        if(data.length !==null){
            $scope.rejects = data.Rejected;
            $scope.pagination = data.pagination;
        }
    });

}