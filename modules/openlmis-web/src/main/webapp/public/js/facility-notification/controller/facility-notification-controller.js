function FacilityNotificationController($scope,$routeParams,  $location, $dialog, $route, messageService ,  FacilityNotification) {
    $scope.messagetypes = [{

        "typeid": 1,

        "name": "Notification",
        "bgColor":"lightblue"

    }, {

        "typeid": 2,

        "name": "Warning",
        "bgColor":"yellow"

    }, {

        "typeid": 3,

        "name": "Error",
        "bgColor":"red"

    }];

$scope.messageturgencies = [{

    "urgencyid": 1,

    "name": "High Urgency",
    "bgColor":"red"

}, {

    "urgencyid": 2,

    "name": "Mild Urgency",
    "bgColor":"yellow"

}, {

    "urgencyid": 3,

    "name": "Low Urgency",
    "bgColor":"lightblue"

}];



    $scope.viewMode = $route.current.$$route.mode;

    if ($scope.viewMode === 'LIST') {
        FacilityNotification.get(function(data){
            $scope.facilityNotificationTypeList = data.notifications;
        });
    }

    else if($scope.viewMode === 'EDIT' && $routeParams.id){
        $scope.$parent.message = '';
        var facilityNotificationId = $routeParams.id;
        FacilityNotification.get({'nid':facilityNotificationId},
            function(response){
                $scope.facilityNotification = response.notification;
            });
    }
    else //NEW
    {
        $scope.$parent.message = '';
    }


    $scope.saveFacilityNotification = function(){
        FacilityNotification.save($scope.facilityNotification, function(response){
                $scope.$parent.message = response.success;
                $location.path('');
            },
            function(errorResponse){
                $scope.error =  messageService.get(errorResponse.data.error);
            });
    };

    $scope.showRemoveFacilityNotificationConfirmDialog = function () {


        var options = {
            id: "removeFacilityNotificationConfirmDialog",
            header: "Confirmation",
            body: "Are you sure you want to remove the Facility Notification: " + $scope.facilityNotification.name
        };
        OpenLmisDialog.newDialog(options, $scope.removeManualTestTypeConfirm, $dialog, messageService);
    };

    $scope.removeFacilityNotificationConfirm = function (result) {
        if (result) {

            FacilityNotification.delete({'tid': $scope.facilityNotification.id}, function (data) {
                $scope.$parent.message = data.success;
                $location.path('');
                $scope.facilityNotification = undefined;
            }, function (error) {
                $scope.error = error.data.error;
            });

        }

    };

}