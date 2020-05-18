function InvoiceNotificationDetailFunc($filter,$scope,$stateParams,GetNotificationById){
    "use strict";
    console.log($stateParams);
        $scope.stateParams = $stateParams;

        var fullFilledItems = [], inSufficientFundingItems = [], stocks = [];
        $scope.allLineItems = [];  $scope.headers = []; var stockOutItems=[], rationingItems = [];
        var closeToExpireItems = [], phasedOutItems = [];

        GetNotificationById.get({id:parseInt($scope.stateParams.id,10)}, function(data){

             stocks = data.stockOutNotificationDTO;

             if(!isUndefined(stocks)) {

             $scope.headers = stocks;

             stockOutItems.push({ title:'Stock Out Items', 'lineItems': stocks.stockOutItems});
             fullFilledItems.push({ title:'Full Filled Items', 'lineItems': stocks.fullFilledItems});
             inSufficientFundingItems.push({ title:'InSufficient Funding Items', 'lineItems': stocks.inSufficientFundingItems});

             rationingItems.push({ title:'Rationing Items', 'lineItems': stocks.rationingItems});
             closeToExpireItems.push({ title:'Close To ExpireItems', 'lineItems': stocks.closeToExpireItems});
             phasedOutItems.push({ title:'Phased Out Items', 'lineItems': stocks.phasedOutItems});

             $scope.allLineItems = [stockOutItems,fullFilledItems,rationingItems,inSufficientFundingItems,closeToExpireItems,phasedOutItems];

             }
             console.log($scope.headers);

        });

        $scope.formatDate = function (expiry) {
           return $filter('date')(Date.parse(expiry), 'dd-MM-yyyy');
        };

}