app.directive('overlaySection',['$compile', function($compile, dashBoardService){
    return {
        restrict: 'EA',
        link: function (scope, elm, attr) {
            var el = angular.element('<div class="dashlet-overlay" id="'+attr.overlaySection+'"></div>');
            el.append('<div>Loading...</div>');
            console.log('Reched hete');
            $compile(el)(scope);
            elm.append(el);
        }
    };
}]);