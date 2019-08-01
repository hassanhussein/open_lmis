function MaterializedviewController($scope, MaterializedViews, RefreshMaterializedViews, $location, messageService) {
    MaterializedViews.get({}, function (data) {
        $scope.views = data.materializedViewList;
    });
    var errorFunc = function (data) {
        $scope.showError = "true";
        $scope.message = "";
        $scope.error = data.data.error;
    };
    var successFunc = function (data) {
        $scope.showError = "true";
        $scope.error = "";
        $scope.message = data.success;
    };
    $scope.refreshView=function (view) {
        RefreshMaterializedViews.update({viewName: view.matviewname},view, successFunc, errorFunc);
    };


}