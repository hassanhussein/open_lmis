/*
 * This program is part of the OpenLMIS logistics management information system platform software.
 * Copyright © 2013 VillageReach
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU Affero General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *  
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Affero General Public License for more details.
 * You should have received a copy of the GNU Affero General Public License along with this program.  If not, see http://www.gnu.org/licenses.  For additional information contact info@OpenLMIS.org. 
 */

//  Description:
//  Comment box behavior on the R&R screen

app.directive('reasonBox',function (RequisitionRejection,AllRejections, $routeParams) {
    return {
        restrict:'E',
        scope:{
            show:'=',
            updatable:'='
        },
        link:function (scope) {

            var commentContainer = document.getElementById('comments-list');


            scope.selectedReasons = scope.nowR = scope.rejectionReasons=[];

            AllRejections.get({}, function (data) {
                scope.rejectionReasons =  data.rejections;
            });

            RequisitionRejection.get({id:$routeParams.rnr}, function (data) {
                scope.rnrComments = data.comments;
               // scope.rejectionReasons = reasonsForRejections.reasons;


            }, {});
            scope.disableRejectionBtn = true;

            scope.selectedItems = {
                selectedItem:null
            };

            scope.showSelected = function(data) {
                var selected;
                selected = [];
                scope.disableRejectionBtn = false;
                console.log(data);
                selected.push(data);
                scope.selectedItems.selectedItem = selected;
                console.log(scope.selectedItems);
            };

            scope.isChecked = function(id){
                var match = false;
                for(var i=0 ; i < scope.rejectionReasons.length; i++) {
                    if(scope.rejectionReasons[i].id === id){
                        match = true;
                    }
                }
                return match;
            };


            scope.sync = function(bool, item){
                if(bool){
                    // add item
                   // scope.rejectionReasons.push(item);
                    scope.nowR.push(item);
                } else {
                    // remove item
                    for(var i=0 ; i < scope.nowR.length; i++) {
                        if(scope.nowR[i].id === item.id){
                            // var remV = scope.rejectionReasons[i];
                               scope.nowR = scope.nowR.splice(i,0);
                            //scope.rejectionReasons.splice(i,1);
                        }
                    }
                }

            };

          /*  scope.addReasons = function(){

                console.log(scope.reason);




                // create a blank array

                var newrow = [];

                newrow =_.where(scope.rejectionReasons, {id:parseInt(scope.reason)});

                //newrow.name = scope.name;



             /!*   // if array is blank add a standard item
                if (scope.selectedReasons.length === 0) {
                    newrow = [];
                } else {
                    // else cycle thru the first row's columns
                    // and add the same number of items
                    scope.selectedReasons.forEach(function (row) {
                        console.log(row);
                        newrow.push(row);
                    });
                }*!/
                // add the new row at the end of the array
                scope.selectedReasons.push(newrow);
                revaluateFunc(scope.selectedReasons);
            };*/

            scope.selectedItems = {
                selectedItem:null
            };
            /*scope.getSelectedItem = function () {
             console.log(scope.selectedItems);
            };*/

            scope.rejectRN = function () {
                if (scope.nowR.length <= 0) {
                    return;
                }
                scope.$parent.rejectRnR();
            };

            // remove the selected row
            scope.removeReason = function(index){

                // remove the row specified in index
                scope.selectedReasons.splice( index, 1);
                // if no rows left in the array create a blank array
                if (scope.selectedReasons.length === 0){
                    scope.selectedReasons = [];
                }
            };



            var commentEscapeKeyHandler  = function(e) {
                if (e.which == 27) {
                    scope.show = false;
                    scope.comment = "";
                    scope.$apply();
                }
            };

            scope.$watch("show", function () {
                if (scope.show) {
                    angular.element(document).bind("keyup", commentEscapeKeyHandler);
                } else {
                    angular.element(document).unbind("keyup", commentEscapeKeyHandler);
                }
            });

            scope.$watch("comment", function () {
                if (scope.comment === undefined) return;
                scope.comment = scope.comment.substring(0, 250);
            });

            scope.addComments = function () {
                if (isUndefined(scope.comment)) return;
                var comment = {"commentText":scope.comment };

                var successHandler = function (data) {
                    scope.comment = "";
                    scope.rnrComments = data.comments;

                    setTimeout(function () {
                        commentContainer.scrollTop = commentContainer.scrollHeight;
                    }, 0);
                };

                var errorHandler = function (data) {
                    scope.error = data.error;
                };

                RequisitionComment.save({id:$routeParams.rnr}, comment, successHandler, errorHandler);

            };
        },
        templateUrl:'/public/pages/template/rejection-reason-box.html',
        replace:true
    };
});