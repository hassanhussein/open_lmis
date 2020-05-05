function InvoiceNotificationFunc($scope,$state, notifications) {

console.log(notifications);

$scope.notificationList = notifications;

$scope.title = '';

$scope.viewDetails = function (id) {
 console.log(id);
 $state.go('invoice-details', { 'id':parseInt(id, 10)});

};

}

InvoiceNotificationFunc.resolve = {

    notifications: function ($q, $timeout, GetNotificationList) {
        var deferred = $q.defer();
        $timeout(function () {
            GetNotificationList.get({program:1}, function (data) {
                deferred.resolve(data.notifications);
            }, {});
        }, 100);
        return deferred.promise;
    }
};