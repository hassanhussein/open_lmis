/*
 * Electronic Logistics Management Information System (eLMIS) is a supply chain management system for health commodities in a developing country setting.
 *
 * Copyright (C) 2015  John Snow, Inc (JSI). This program was produced for the U.S. Agency for International Development (USAID). It was prepared under the USAID | DELIVER PROJECT, Task Order 4.
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU Affero General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
function ConfigureOtherProductController($scope,SaveVaccineProductDoseAgeGroup, $routeParams, VaccineProductDoseAgeGroup,CoverageAgeGroups) {

    $scope.program = $routeParams.programId;

    CoverageAgeGroups.get({}, function (data) {
        $scope.coverageAgeGroups = data.ageGroups;
    });


    VaccineProductDoseAgeGroup.get({programId: $scope.program}, function (data) {
        $scope.protocol = data.protocol;
        $scope.$parent.protocol = $scope.protocol;
        console.log($scope.protocol);
        $scope.possibleDosesToDisplay = $scope.protocol.possibleDoses;

    });

    $scope.addDosageForProduct = function(product,singleDose,data){
        if((data.dose.doseId === undefined) || (data.dose.ageGroupId === undefined)){
            return;
        }



        if(data.dose.doseId !== undefined){
           var newEntry = {doseId: parseInt(data.dose.doseId,10), productId: parseInt(singleDose.productId,10), programId:parseInt($scope.program,10),displayOrder:parseInt(1,10)};
            product.doses.push(newEntry);

        }
    };

    $scope.removeDosageFromProduct = function(product){
        product.doses.pop();
    };

    $scope.$parent.saveProtocols = function(){
        SaveVaccineProductDoseAgeGroup.update($scope.protocol, function(){
            $scope.$parent.message = 'label.vaccine.settings.coverage.configuration.saved';
        });
    };

    $scope.addProduct = function(product, scope){
        scope.showAddNewModal = false;
        var dose = $scope.protocol.possibleDoses[0];
        $scope.protocol.vaccineProtocols.push(

            {productName: product.primaryName, productId: product.id,
                productDisplayOrder: $scope.protocol.vaccineProtocols.length + 1,
                doses: [{doseId:parseInt(dose.id,10), productId:parseInt(product.id,10), programId: parseInt($scope.program,10), displayOrder: 1, displayName: dose.name, trackMale: true, trackFemale: true}]});

    };
}
