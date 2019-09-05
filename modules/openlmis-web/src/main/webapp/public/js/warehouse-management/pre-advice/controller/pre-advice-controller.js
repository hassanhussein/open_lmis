/*
 *
 * Electronic Logistics Management Information System (eLMIS) is a supply chain management system for health commodities in a developing country setting.
 *
 *  Copyright (C) 2015  John Snow, Inc (JSI). This program was produced for the U.S. Agency for International Development (USAID). It was prepared under the USAID | DELIVER PROJECT, Task Order 4.
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU Affero General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Affero General Public License for more details.
 *
 *    You should have received a copy of the GNU Affero General Public License along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */




 function PreAdviceController($scope){

 $scope.products=[{
        id:1,
        unitPrice:0,
        unitOfMeasure:null,
        lots:[{'id':Date.now(),'quantity':0}],
        totalCost:0
        }]

    $scope.addLot=function(productIndex){
       $scope.products[productIndex].lots.push({'id':Date.now(),'quantity':0});
 }

 $scope.addProduct=function(){
 $scope.products.push({'id':$scope.products.length+1,unitPrice:0,lots:[{'id':Date.now(),'quantity':0}]});
 }


 $scope.removeProduct=function(productIndex){
 console.log(productIndex)

 $scope.products.splice(productIndex,1);
 if($scope.products.length==1 && productIndex==0){
     $scope.products=[{
        id:1,
        unitPrice:0,
        unitOfMeasure:null,
        lots:[{'id':Date.now(),'quantity':0}],
        totalCost:0
        }]
  }
 }


 $scope.removeLot=function(productIndex,lotIndex){
    $scope.products[productIndex].lots.splice(lotIndex,1);
 }

 $scope.totalCostPerProduct=function(product){

       return $scope.totalQuantityPerProduct(product)*product.unitPrice
 }

 $scope.totalQuantityPerProduct=function(product){
            sum=0;
        product.lots.forEach(function(lot){
          sum+=lot.quantity;
        })
        return sum
 }


  $scope.showNewLotModal=function(product){
         $scope.newLotModal=true;
         $scope.newLot={};

      };

      $scope.closeNewLotModal=function(){
          $scope.newLot={};
          $scope.newLotModal=false;
      };


      $scope.grandTotal=function(){
                sum=0;
                $scope.products.forEach(function(product){
                  sum+=$scope.totalCostPerProduct(product);


                })
                return sum;
      }

 }