function InvoiceNotificationFunc($scope,$filter,$state, notifications) {

console.log(notifications);

$scope.notificationList = notifications;

$scope.title = '';

$scope.formatDate = function (not) {
   return $filter('date')(Date.parse(not.invoicedate), 'dd-MM-yyyy');
};

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