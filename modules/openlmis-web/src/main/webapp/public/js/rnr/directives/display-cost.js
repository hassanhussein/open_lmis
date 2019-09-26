/*
 * This program is part of the OpenLMIS logistics management information system platform software.
 * Copyright © 2013 VillageReach
 *
 *  This program is free software: you can redistribute it and/or modify it under the terms of the GNU Affero General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Affero General Public License for more details.
 *  You should have received a copy of the GNU Affero General Public License along with this program.  If not, see http://www.gnu.org/licenses.  For additional information contact info@OpenLMIS.org.
 */

//  Description:
//  Comment box behavior on the R&R screen

app.directive('displayCost', function (FundingSource,$interval,$rootScope,$q,RequisitionFacilitySourceOfFund,$timeout) {
  return {
    scope: {
      show: '=',
      fullSupplyCost: '=',
      nonFullSupplyCost: '=',
      facilitySourceOfFund: '='
    },
    link:function (scope,rootScope) {

       scope.sourceOfFund = {};


      if(!isUndefined(scope.$parent.$parent.$parent.rnr)) {
      scope.$parent.$parent.$parent.rnr.allowSubmissionIfNoSourceOfFundDefined = false;
      }



     scope.loadOtherSource = function(){

                 var deferred = $q.defer();
                   var parameter = {

                        'program':scope.$parent.$parent.$parent.rnr.program.id

                        };

                   FundingSource.get(parameter, function (data) {
                   console.log(data);

                            scope.fundingSources = data.sources;

                            if(data.sources.length > 0){

                              deferred.resolve();

                            }
                    });


          return deferred.promise;

     };


       $rootScope.$on('loadSourceOfFunds', function (event,data) {
           var loadOther = scope.loadOtherSource();

           var openPopupMenu = scope.$parent.$parent.$parent.rnr.openPopupMenu;

           loadOther.then(function(){

           if(scope.fundingSources.length > 0){

                   scope.showFacilitySourceOfFund((event.targetScope.rnr === undefined)?scope.fundingSources:event.targetScope.rnr);

                    scope.sourceOfFund.rnrId = event.targetScope.rnr.id;
                    scope.$parent.sourceOfFunds = event.targetScope.rnr.sourceOfFunds;

                    if(!isUndefined(openPopupMenu) || openPopupMenu){

                    scope.showData = showClickedFundSourcesData();
                    }

           }});

      });


    scope.showFacilitySourceOfFund = function(source)
    {

        scope.oldAdjustmentReason = angular.copy(source.sourceOfFunds);

        scope.sourceOfFund = source;

        scope.sourceOfFund.sourceOfFunds=((source.sourceOfFunds === undefined)?[]:source.sourceOfFunds);
        //Remove reason already exist from drop down
         scope.reEvaluateTotalSourceOfFund();
         updateFundingSource(source.sourceOfFunds);


    };


   scope.sourceOfFunds = [];

   scope.addRow = function(fund) {

       var newRow = {};
       newRow.name = fund.name.name;

       newRow.id = fund.name.id;
       newRow.rnrId = fund.rnrId;
       newRow.quantity = parseInt(fund.quantity,10);
       scope.sourceOfFund.sourceOfFunds.push(newRow);
       updateFundingSource(scope.sourceOfFund.sourceOfFunds);
       scope.reEvaluateTotalSourceOfFund();
       fund.quantity = undefined;
       fund.name = undefined;

       };
      if(!isUndefined(scope.$parent.$parent.$parent.rnr)) {
      scope.$parent.$parent.$parent.rnr.continueWithSubmission = false;
      }

      scope.saveFacilityFundingSources = function (sourceOfFunds) {

                if(scope.sourceOfFundForm.$invalid)
                        {
                            scope.showFormError=true;
                            scope.messageToDisplay = 'Please fill all required Amount';
                            return;
                        }


            if (isUndefined(sourceOfFunds)) return;

            var jsonData = {"sourceOfFunds":sourceOfFunds};

            var successHandler = function (data) {

             $timeout( function(){
              scope.displayLimit = false;
             }, 3000);
             scope.displayLimit = true;
             scope.$parent.$parent.$parent.message = 'Supplemental Fund(s) Saved successfully';
             scope.rnrFund = data.sourceOfFunds;

             $timeout(function(){
               angular.element('#dividedCost').triggerHandler('click');
             },10);

             scope.$parent.$parent.$parent.rnr.continueWithSubmission = true;

            };

            var errorHandler = function (data) {
              scope.error = data.error;
            };

            RequisitionFacilitySourceOfFund.save({id:parseInt(sourceOfFunds[0].rnrId,10)}, jsonData, successHandler, errorHandler);

          };

     scope.removeSourceOfFund = function(fundingSource)

       {

           scope.sourceOfFund.sourceOfFunds =  $.grep(scope.sourceOfFund.sourceOfFunds, function (reasonObj) {
                   return (fundingSource !== reasonObj);
               });

            updateFundingSource(scope.sourceOfFund.sourceOfFunds);
            scope.reEvaluateTotalSourceOfFund();

      };


   function reEvaluateTotalSourceOfFund()
        {
            var totalAdjustments = 0;
            scope.totalSources = 0;

            $(scope.sourceOfFund.sourceOfFunds).each(function (index, sourceObject) {
             totalAdjustments = totalAdjustments + parseInt(sourceObject.quantity,10);
            });
            scope.totalSources = totalAdjustments;
            scope.$parent.$parent.$parent.rnr.totalSources = scope.totalSources;
        }

     scope.reEvaluateTotalSourceOfFund = function() {reEvaluateTotalSourceOfFund();};

    function updateFundingSource(funds)
        {
            var adjustmentReasonsForLot =  _.pluck(funds, 'name');

             scope.fundsToDisplay = $.grep(scope.fundingSources, function (adjustmentTypeObject) {
                          return $.inArray(adjustmentTypeObject.name, adjustmentReasonsForLot) == -1;
                      });

        }

       var count = 0;
       function showClickedFundSourcesData() {

        $timeout(function(){

         count++;

         if(count === 1) {

         angular.element('#dividedCost').triggerHandler('click');

         }

        },200);

        }

      },
    restrict: 'E',
    templateUrl: '/public/pages/template/display-cost.html',
    replace: true
  };
});