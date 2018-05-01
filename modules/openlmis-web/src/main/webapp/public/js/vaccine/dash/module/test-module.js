/**
 * Created by hassan on 10/30/17.
 */

/*
 * Electronic Logistics Management Information System (eLMIS) is a supply chain management system for health commodities in a developing country setting.
 *
 * Copyright (C) 2015 Clinton Health Access Initiative (CHAI)/MoHCDGEC Tanzania.
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU Affero General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.

 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.   See the GNU Affero General Public License for more details.
 */

var app  = angular.module('test', ['openlmis','ui.router', 'ngGrid', 'ui.bootstrap.dialog', 'ui.bootstrap.accordion',
    'ui.bootstrap.modal','ui.bootstrap.pagination', 'ui.bootstrap.dropdownToggle','ui.bootstrap',
    'angularUtils.directives.uiBreadcrumbs','ng-breadcrumbs','ncy-angular-breadcrumb','angularCombine',
    'ngTable','ui.bootstrap.pagination', 'tree.dropdown','angularScreenfull','rzModule','ui.materialize','leaflet-directive'
]);
///Start

app.config(function($stateProvider, $urlRouterProvider, $breadcrumbProvider){

    var states = [
        {
            name: 'home',
            url: '/home',
            templateUrl: 'partials/dashboard.html',
            controller:StockAvailabilityControllerFunc1,
            resolve:StockAvailabilityControllerFunc1.resolve,
            ncyBreadcrumb: {
                label: 'Proof Of Concept'
            }
        },
        {
            name: 'product',
            url: '/product:referer',
            templateUrl: 'partials/dashboard.html',
            ncyBreadcrumb: {
                label: 'Details',
                parent: 'home'

            }
        },

        {
            name: 'notification',
            url: '/notification',
            templateUrl: 'partials/notification-list.html',
            controller: 'DashboardHelperModalInstanceCtrl',
            ncyBreadcrumb: {
                label: 'All Notifications'
            }
        },
        {
            name:'toMoreStockAvailabilityView',
            url: '/indicator/:indicator/total/:total/period/:period',
            templateUrl:'partials/chart2.html',
            controller:'StockAvailableChartFunct',
            ncyBreadcrumb: {
                label: 'Coverage Chart Data  {{indicator}}',
                parent: 'home'
            }
        }


        /*,
         {
         name: 'element',
         url: '/element:idElement/:facilityId',
         templateUrl: 'partials/stock-ledger.html',
         controller: 'MyFacilityStockLedgerFunction',
         ncyBreadcrumb: {
         label: 'Stock Ledger',
         parent: 'home'
         }
         },
         {
         name: 'detail',
         url: '/detail:referer',
         templateUrl: 'partials/detail.html',
         ncyBreadcrumb: {
         label: 'Details'
         }
         },
         {
         name:'toState',
         url: '/supervisedFacility:facilityId/:facilityName',
         templateUrl:'partials/stock-on-hand2.html',
         controller:'ManageStockOnHandControllerFunc',
         ncyBreadcrumb: {
         label: 'Stock on Hand for {{facilityName}}',
         parent: 'supervisedFacility'
         }
         },

         {
         name: 'ledger',
         url: '/ledger:productId/:facilityId/:facilityName/:product',
         templateUrl: 'partials/stock-ledger.html',
         controller: 'StockLedgerFunction2',
         ncyBreadcrumb: {
         label: 'Stock Ledger of {{productName}} in {{facilityName}} Store',
         parent: 'toState'
         }
         },
         {
         name:'supervisedFacility',
         url: '/supervisedFacility?:etc',
         templateUrl:'partials/stock-on-hand3.html',
         controller:'SupervisedFacilityControllerFunc',
         resolve:SupervisedFacilityControllerFunc.resolve,
         ncyBreadcrumb: {
         label: 'My Supervised Stores',
         parent: 'home'
         }
         }*/

    ];

    states.forEach($stateProvider.state);


    $urlRouterProvider.otherwise('/home');
}).config(function(angularCombineConfigProvider) {
    angularCombineConfigProvider.addConf(/filter-/, '/public/pages/reports/shared/filters.html');
})
    .filter('routeActive', function($state, $breadcrumb) {
        return function(route, steps) {
            for(var i = 0, j = steps.length; i < j; i++) {
                if(steps[i].name === route.name) {
                    return steps[i];
                }
            }

            return false;
        };
    }).filter('positive', function() {
        return function(input) {
            if (!input) {
                return 0;
            }

            return Math.abs(input);
        };
    })
    .controller('ElementCtrl', function($scope, $stateParams){
        $scope.idElement = $stateParams.idElement;
    }).directive('excelExport',
    function () {
        return {
            restrict: 'A',
            scope: {
                fileName: "@",
                data: "&exportData"
            },
            replace: true,
            template: '<button  class="btn btn-small" ng-click="download()"><span style="float: left !important;">Export(xls) <i class="fa fa-download"></i></span></i></button>',
            link: function (scope, element) {

                scope.download = function() {

                    function datenum(v, date1904) {
                        if(date1904) v+=1462;
                        var epoch = Date.parse(v);
                        return (epoch - new Date(Date.UTC(1899, 11, 30))) / (24 * 60 * 60 * 1000);
                    }

                    function getSheet(data, opts) {
                        var ws = {};
                        var range = {s: {c:10000000, r:10000000}, e: {c:0, r:0 }};
                        for(var R = 0; R != data.length; ++R) {
                            for(var C = 0; C != data[R].length; ++C) {
                                if(range.s.r > R) range.s.r = R;
                                if(range.s.c > C) range.s.c = C;
                                if(range.e.r < R) range.e.r = R;
                                if(range.e.c < C) range.e.c = C;
                                var cell = {v: data[R][C] };
                                if(cell.v === null) continue;
                                var cell_ref = XLSX.utils.encode_cell({c:C,r:R});

                                if(typeof cell.v === 'number') cell.t = 'n';
                                else if(typeof cell.v === 'boolean') cell.t = 'b';
                                else if(cell.v instanceof Date) {
                                    cell.t = 'n'; cell.z = XLSX.SSF._table[14];
                                    cell.v = datenum(cell.v);
                                }
                                else cell.t = 's';

                                ws[cell_ref] = cell;
                            }
                        }
                        if(range.s.c < 10000000) ws['!ref'] = XLSX.utils.encode_range(range);
                        return ws;
                    }

                    function Workbook() {
                        if(!(this instanceof Workbook)) return new Workbook();
                        this.SheetNames = [];
                        this.Sheets = {};
                    }

                    var wb = new Workbook(), ws = getSheet(scope.data());
                    /* add worksheet to workbook */
                    wb.SheetNames.push(scope.fileName);
                    wb.Sheets[scope.fileName] = ws;
                    var wbout = XLSX.write(wb, {bookType:'xlsx', bookSST:true, type: 'binary'});

                    function s2ab(s) {
                        var buf = new ArrayBuffer(s.length);
                        var view = new Uint8Array(buf);
                        for (var i=0; i!=s.length; ++i) view[i] = s.charCodeAt(i) & 0xFF;
                        return buf;
                    }

                    saveAs(new Blob([s2ab(wbout)],{type:"application/octet-stream"}), scope.fileName+'.xlsx');

                };

            }
        };
    }
);



