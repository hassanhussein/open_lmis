/*
 * This program is part of the OpenLMIS logistics management information system platform software.
 * Copyright © 2013 VillageReach
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU Affero General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *  
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Affero General Public License for more details.
 * You should have received a copy of the GNU Affero General Public License along with this program.  If not, see http://www.gnu.org/licenses.  For additional information contact info@OpenLMIS.org. 
 */
function CreatePatientLineItemController($scope) {

    $scope.showCategory = function(index) {
        var absIndex = ($scope.pageSize * ($scope.currentPage - 1)) + index;
        return !((index > 0) && ($scope.rnr.patientLineItems.length > absIndex) && ($scope.rnr.patientLineItems[absIndex].category.name == $scope.rnr.patientLineItems[absIndex - 1].category.name));
    };


    $scope.disableInputMDR = function(colId, rowId, code) {
    if(code =='MDR-REGIMEN-1' || code =='MDR-REGIMEN-2' || code =='MDR-REGIMEN-3' || code =='MDR-REGIMEN-4' )
    {
    if (rowId===1 && colId <=18)
         return false;
    else if (rowId===2 && colId <=11)
         return false;
    else if (rowId===3 && colId <=9)
        return false;
    else if (rowId>3)
         return false;
    else
        return true;
    }
    };

    $scope.disableInput = function(colId, rowId, code) {
        if (colId < 7)
            return false;
      else if ((colId >= 6 && rowId === 3))
            return true;
        else if (colId > 6 && rowId != 3 && (rowId === 1 || rowId === 2 || rowId === 4 || rowId === 5 || rowId === 7 || rowId === 9))
            return true;
    };


      $scope.showAddSkippedRegimensModal = function(){
        $scope.addSkippedProductsModal = true;
      };

       $scope.unskipRegimens = function(rnr){
        var selected = _.where(rnr.patientLineItems, {unskip: true});
             _.each(selected, function (lineItem) {
                            lineItem.skipped = false;
                            _.extend(_.findWhere($scope.rnr.patientLineItems, { code: lineItem.code }), lineItem);

               });


     $scope.saveRnrForm.$dirty = true;
     $scope.$parent.saveRnr();

     if(rnr.fullSupplyLineItems.length === 0){
       rnr.fullSupplyLineItems = $scope.$parent.page[$scope.$parent.visibleTab];
     }

     angular.forEach(rnr.skippedLineItems, function(li){
       if(!li.unskip){
         li.unskip = false;
       }
     });

     rnr.skippedLineItems = _.where(rnr.skippedLineItems, {unskip: false});
     window.window.adjustHeight();
     // all is said and done, close the dialog box
     $scope.addSkippedProductsModal = false;
        };

      $scope.setSkipAll = function(value){
        _.each($scope.page.patient, function (patient) {
          $scope.saveRnrForm.$dirty = true;
          patient.skipped = value;
        });
      };

}