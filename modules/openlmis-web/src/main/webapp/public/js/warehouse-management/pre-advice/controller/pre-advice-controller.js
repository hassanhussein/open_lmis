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




 function PreAdviceController($scope,$filter,$location,asn,Preadvice,configurations,homeFacility,asnLookups,ProductLots,FacilityTypeAndProgramProducts,VaccineProgramProducts,manufacturers,Lot){
$scope.homeFacilityId=homeFacility.id;
$scope.userPrograms=configurations.programs;
$scope.manufacturers = manufacturers;
$scope.ports=asnLookups.ports;
$scope.documentTypes=asnLookups['document-types'];
$scope.suppliers=asnLookups.suppliers;
$scope.productError=false;
//console.log(configurations.productsConfiguration[0].product.id)
$scope.configurations=configurations;

$scope.asn=asn;

$scope.loadProducts=function(facilityId,programId){
              FacilityTypeAndProgramProducts.get({facilityId:facilityId, programId:programId},function(data){
                      var allProducts=data.facilityProduct;
                      $scope.allProducts=_.sortBy(allProducts,function(product){
                          return product.programProduct.product.id;
                      });


                       $scope.productsToDisplay=$scope.allProducts;
                       console.log($scope.allProducts)

              });


          };

 $scope.loadProducts($scope.homeFacilityId,82);
 $scope.getProductFromId=function(productId){
  var editProduct={}
        angular.forEach($scope.configurations.productsConfiguration,function(product,value){
//        console.log(product.product.id)
        if(productId==product.product.id){
        console.log('am here')
         editProduct=product
        }
        })
        return editProduct.product

 }

if($scope.asn){
$scope.editMode=true;
$scope.asnReceiptDate=asn.asndate;
$scope.asnCode=asn.asnnumber;
$scope.blAwbNumber=asn.blawbnumber;
$scope.clearingAgent=asn.clearingagent;
$scope.expectedArrivalDate=asn.expectedarrivaldate;
$scope.flightVesselNumber=asn.flightvesselnumber;
$scope.notes=asn.note;
$scope.poDate=asn.podate;
$scope.poNumber=asn.ponumber;
$scope.portOfArrivalId=asn.portofarrival;
$scope.supplierId=asn.supplierid;
$scope.productsToAdd=[]
angular.forEach($scope.asn.asnLineItems,function(product,value){
   editProduct=$scope.getProductFromId(product.productid);
        var productLots=[]
    angular.forEach(product.asnLots,function(lot,value){

    productLots.push({
    quantity:lot.quantity,
    displayCodeOnly:true,
    info:{
    expirationDate:lot.expirydate,
    lotCode:lot.lotnumber,
    manufactureDate:lot.manufacturingdate
    }

    })

    })

       $scope.productsToAdd.push({
                                     id:0,
                                     programProduct:editProduct,
                                     maxMonthsOfStock:0,
                                     minMonthsOfStock:0,
                                     eop:null,
                                     lots:productLots,
                                     unitPrice:0

                                     })


})

}else{

 $scope.productsToAdd=[{
 id:0,
 programProduct:{},
 maxMonthsOfStock:0,
 minMonthsOfStock:0,
 eop:null,
 lots:[{quantity:0,displayCodeOnly:false}],
 unitPrice:0

 }];

}











    $scope.addLot=function(productIndex){
       $lotIndex=$scope.productsToAdd[productIndex].lots.length-1;
       $scope.productsToAdd[productIndex].lots[$lotIndex].displayCodeOnly=true;
       $scope.productsToAdd[productIndex].lots.push({quantity:0,displayCodeOnly:false});
 }

$scope.validateProduct=function(){
 if(angular.equals($scope.productsToAdd[0].programProduct,{})||!$scope.productsToAdd[0].unitPrice || !$scope.productsToAdd[0].lots[0].quantity){
    $scope.productError=true
    return;
 }

 $scope.productError=false
}

 $scope.addProduct=function(){
 $scope.productsToAdd.push({
                            id:0,
                            programProduct:{},
                            maxMonthsOfStock:0,
                            minMonthsOfStock:0,
                            eop:null,
                            lots:[{quantity:0,displayCodeOnly:false}],
                            unitPrice:0

                            });
 }


 $scope.removeProduct=function(productIndex){
 $scope.productsToAdd.splice(productIndex,1);
 if($scope.productsToAdd.length==1 && productIndex==0){
    $scope.productsToAdd=[{
     id:0,
     programProduct:{},
     maxMonthsOfStock:0,
     minMonthsOfStock:0,
     eop:null,
     lots:[{quantity:0,displayCodeOnly:false}],
     unitPrice:0

     }]


 }

 }


 $scope.removeLot=function(productIndex,lotIndex){
    $scope.productsToAdd[productIndex].lots.splice(lotIndex,1);
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

 $scope.seeLots=function(){



 }


 $scope.cancel=function(){
  $scope.error = "";
  $scope.showError = false;
  $location.path('');

 }


   var success = function (data) {
     $scope.error = "";
     console.log(data)
     $scope.$parent.message = data.success;
     $scope.$parent.asnId = true

     $scope.showError = false;
     $location.path('');
   };


    var error = function (data) {
       $scope.$parent.message = "";
       $scope.error = data.data.error;
       $scope.showError = true;
     };


 $scope.changeLotDisplay=function(lotId){
  console.log($scope.productsToAdd)
  }
  $scope.showNewLotModal=function(product){
         $scope.newLotModal=true;
         $scope.newLot={};
         $scope.newLot.product=product;

      };

      $scope.closeNewLotModal=function(){
          $scope.newLot={};
          $scope.newLotModal=false;
      };


      $scope.grandTotal=function(){
                sum=0;
                $scope.productsToAdd.forEach(function(product){
                  sum+=$scope.totalCostPerProduct(product);


                })
                return sum;
      }




          function updateLotsToDisplay(lotsToAdd)
              {
                       var toExclude = _.pluck(_.pluck(lotsToAdd, 'lot'), 'lotCode');
                       $scope.lotsToDisplay = $.grep($scope.allLots, function (lotObject) {
                             return $.inArray(lotObject.lotCode, toExclude) == -1;
                       });
              }


              $scope.createLot=function(){
                     var newLot={};
                     newLot.product=$scope.newLot.product;
                     newLot.lotCode=$scope.newLot.lotCode;
                     newLot.manufacturerName=$scope.newLot.manufacturerName;
                     newLot.expirationDate=$filter('date')($scope.newLot.expirationDate,"yyyy-MM-dd");
                      Lot.create(newLot,function(data){
                             $scope.newLotModal=false;
//                             $scope.lotToAdd.lotId=data.lot.id;
//                             console.log(JSON.stringify($scope.selectedLot));
                             console.log(data.lot.product)
                             $scope.loadProductLots(data.lot.product);
                      });
                   };



          $scope.loadProductLots=function(product)

             {

                 $scope.productError=false;

                  $scope.lotsToDisplay={};

                  if(product !==null)
                  {


                         ProductLots.get({productId:product.id},function(data){
                              $scope.allLots=data.lots;
                              console.log(data.lots)
                              $scope.lotsToDisplay=$scope.allLots;

                         });


                  }
             };


             $scope.saveAsn=function(status){

                $scope.validateProduct();
                console.log($scope.asnForm)
             if ( $scope.asnForm.$error.required) {
                   $scope.showError = true;
                   $scope.error = 'form.error';
                   $scope.message = "";
                   return;
                 }

           var lotflag=true
            var asnLineItems=[]
             angular.forEach($scope.productsToAdd,function(product,key){
             var asnLots=[]


                angular.forEach(product.lots,function(lot,key){
                    if(lot.info){
                    asnLots.push({
                    expirydate:lot.info.expirationDate,
                    lotnumber:lot.info.lotCode,
                    manufacturingdate:lot.info.manufactureDate,
                    quantity:lot.quantity,
                    serialnumber:'string'
                    })



}
                })


                 asnLineItems.push({
                                    asnLots:asnLots,
                                    lotflag:true,
                                    productid:product.programProduct.product.id
                                    })


             })
            var asn={
             asnLineItems:asnLineItems,
             asndate:$scope.asnReceiptDate,
             asnnumber:$scope.asnCode,
             blawbnumber:$scope.blAwbNumber,
             clearingagent:$scope.clearingAgent,
             expectedarrivaldate:$scope.expectedArrivalDate,
             flightvesselnumber:$scope.flightVesselNumber,
             note:$scope.notes,
             podate:$scope.poDate,
             ponumber:$scope.poNumber,
             portofarrival:$scope.portOfArrivalId,
             purchaseDocuments:[
                                 {
                                    "documentType": {
                                    "id":1,
                                    "description": "Testing",
                                    "name": "Testing"
                                    },
                                    "filelocation": "string"
                                    }
                                    ],
             status:status,
             supplierid:$scope.supplierId
             }


            console.log(asn)

             Preadvice.save({}, asn, success, error);

             }




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

             asn: function ($q, $route, $timeout, Preadvice) {
                if ($route.current.params.id === undefined) return undefined;

                var deferred = $q.defer();
                var asnId = $route.current.params.id;

                $timeout(function () {
                  Preadvice.get({id: asnId}, function (data) {

                    deferred.resolve(data.asn);
                  }, {});
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
                     manufacturers : function($q, $timeout, $route, ManufacturerList){
                                         var deferred = $q.defer();

                                         $timeout(function () {
                                           ManufacturerList.get(function (data) {
                                             deferred.resolve(data.manufacturers);
                                           });
                                         }, 100);
                                         return deferred.promise;
                                       },
                      asnLookups : function($q, $timeout, $route, AsnLookups){
                                                               var deferred = $q.defer();

                                                               $timeout(function () {
                                                                 AsnLookups.get(function (data) {
                                                                   deferred.resolve(data);
                                                                 });
                                                               }, 100);
                                                               return deferred.promise;
                                                             }
 }