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




 function PreAdviceController($scope,configurations,homeFacility,ProductLots,FacilityTypeAndProgramProducts,VaccineProgramProducts){

$scope.homeFacilityId=homeFacility.id;
$scope.userPrograms=configurations.programs;


 $scope.products=[{
        id:1,
        unitPrice:0,
        unitOfMeasure:null,
        lots:[{'id':Date.now(),'quantity':0}],
        totalCost:0
        }]

  $scope.dbProducts=[
  {
  'primaryName':'TT',
  'category':101,
  'lots':['t342524','t42352','t23432','t23432','t234234','t463232','t5623462']
  },
  {
    'primaryName':'BoPV',
    'category':101,
    'lots':['b23432','b23432','b234234','b463232','b5623462']
    },
    {
        'primaryName':'ROTA',
        'category':101,
        'lots':['r23432','r23432','r234234','r463232','r5623462']
        },


        {
            'primaryName':'Sdilution',
            'category':102,
            'lots':['s23432','s23432','s234234','s463232','s5623462']
            },

         {
                     'primaryName':'ADS_0.05ml',
                     'category':102,
                     'lots':['a23432','a23432','a234234','a463232','a5623462']
                     }
  ]

    $scope.addLot=function(productIndex){
       $scope.products[productIndex].lots.push({'id':Date.now(),'quantity':0});
 }

// $scope.addLot=function(lotToAdd){
//             lotToAdd.lot=_.findWhere($scope.lotsToDisplay,{id:parseInt(lotToAdd.lotId,10)});
//             $scope.productToAdd.lots.push(lotToAdd);
//             $scope.lotToAdd={};
//             updateLotsToDisplay($scope.productToAdd.lots);
//     };

 $scope.addProduct=function(){
 $scope.products.push({'id':$scope.products.length+1,unitPrice:0,lots:[{'id':Date.now(),'quantity':0}]});
 }


 $scope.removeProduct=function(productIndex){
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


      $scope.loadProducts=function(facilityId,programId){
              FacilityTypeAndProgramProducts.get({facilityId:facilityId, programId:programId},function(data){
                      var allProducts=data.facilityProduct;
                      $scope.allProducts=_.sortBy(allProducts,function(product){
                          return product.programProduct.product.id;
                      });

                       $scope.productsToDisplay=$scope.allProducts;

              });


          };

          function updateLotsToDisplay(lotsToAdd)
              {
                       var toExclude = _.pluck(_.pluck(lotsToAdd, 'lot'), 'lotCode');
                       $scope.lotsToDisplay = $.grep($scope.allLots, function (lotObject) {
                             return $.inArray(lotObject.lotCode, toExclude) == -1;
                       });
              }


          $scope.loadProductLots=function(product)
             {
                  $scope.lotsToDisplay={};

                  if(product !==null)
                  {


                         ProductLots.get({productId:product.id},function(data){
                              $scope.allLots=data.lots;
                              $scope.lotsToDisplay=$scope.allLots;
                         });


                  }
             };

             $scope.loadProducts($scope.homeFacilityId,82);

 }


 PreAdviceController.resolve={
    configurations:function($q, $timeout, AllVaccineInventoryConfigurations) {
              var deferred = $q.defer();
              var configurations={};
              $timeout(function () {
              AllVaccineInventoryConfigurations.get(function(data)
                 {
                     configurations=data;
                     deferred.resolve(configurations);
                 });
              }, 100);
              return deferred.promise;
            },

            homeFacility: function ($q, $timeout,UserFacilityList,StockCards) {
                        var deferred = $q.defer();
                        var homeFacility={};

                        $timeout(function () {
                               UserFacilityList.get({}, function (data) {
                                       homeFacility = data.facilityList[0];
                                       StockCards.get({facilityId:homeFacility.id},function(data){
                                         if(data.stockCards.length> 0)
                                         {
                                            homeFacility.hasStock=true;
                                         }
                                         else{
                                           homeFacility.hasStock=false;
                                         }
                                         deferred.resolve(homeFacility);
                                       });

                               });

                        }, 100);
                        return deferred.promise;
                     },
 }