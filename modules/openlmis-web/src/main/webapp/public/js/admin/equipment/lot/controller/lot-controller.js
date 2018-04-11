function LotController($scope, VaccineProducts, SaveLotByProduct, manufacturerInfo, GetLotById, $routeParams, GetDeleteLot, $location, messageService) {
    $scope.productList = VaccineProducts;

    var product;
    $scope.changeProduct = function () {
        product = $scope.lot.product;

    };

    if (manufacturerInfo !== null)
        $scope.manufacturers = manufacturerInfo;


    $scope.lot = {};
    $scope.message = {};

    if ($routeParams.id) {
        GetLotById.get({id: $routeParams.id}, function (data) {
            $scope.lot = data.lotsById;
            console.log(data.lotsById);
            $scope.showError = true;
        }, {});
    }


    $scope.cancelAddEdit = function () {
        $scope.$parent.message = {};
        $scope.$parent.id = null;
        $location.path('#/list');
    };

    $scope.saveLot = function () {
        var successHandler = function (response) {
            $scope.lot = response.lots;
            $scope.showError = false;
            $scope.error = "";
            $scope.$parent.message = response.success;
            $scope.$parent.id = $scope.lot.id;

            $location.path('');
        };

        var errorHandler = function (response) {
            $scope.showError = true;
            $scope.error = messageService.get(response.data.error);
        };

        if (!$scope.lotForm.$invalid) {
            SaveLotByProduct.save($scope.lot, successHandler, errorHandler);
        }

        return true;
    };

    $scope.delete = function (lot) {

        function errorCallBack(errorResponse) {
            $scope.showError = true;
            $scope.errorMessage = messageService.get(errorResponse.data.error);
        }

        function successCallBack(successResponse) {
            $scope.$parent.id = lot.id;
            $scope.$parent.message = messageService.get(successResponse.success);
            $location.path('#/list');
        }

        var callBack = function (result) {
            GetDeleteLot.delete({'id': parseInt(lot.id, 10)}, successCallBack, errorCallBack);
        };
        var options = {
            id: "confirmDialog",
            header: "label.confirm.delete.lot",
            body: "msg.question.delete.lot.confirmation"
        };

        OpenLmisDialog.newDialog(options, callBack, $dialog);

    };

}

LotController.resolve = {

    manufacturerInfo: function ($q, $timeout, $route, ManufacturerList) {
        var deferred = $q.defer();

        $timeout(function () {
            var values = [];
            ManufacturerList.get(function (data) {
                values = data.manufacturers;
                deferred.resolve(values);
            });
        }, 100);
        return deferred.promise;
    },
    VaccineProducts: function ($q, $timeout, $route, ReportProductsByProgramWithoutDescriptions) {
        var deferred = $q.defer();

        $timeout(function () {
            var values = [];
            ReportProductsByProgramWithoutDescriptions.get({programId: 82}, function (data) {
                if (data.productList.length > 0) {
                    values = _.sortBy(data.productList, function (o) {
                        return parseInt(o.categoryId, 10);
                    });
                }
                deferred.resolve(values);
            });
        }, 100);
        return deferred.promise;
    }

};
