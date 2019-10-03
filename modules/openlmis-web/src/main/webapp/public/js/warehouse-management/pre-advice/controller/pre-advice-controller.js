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
function PreAdviceController($window,$scope,$filter, $location,otherProducts, asn,AsnLookups, Preadvice, UserFacilityList, configurations, AllVaccineInventoryConfigurations,homeFacility, asnLookups, ProductLots, FacilityTypeAndProgramProducts, VaccineProgramProducts, manufacturers, Lot,
$rootScope,documentTypes,UploadFile,$http,docService, $timeout
) {


    function getAllLookups(){
     AllVaccineInventoryConfigurations.get(function(data) {
                    $scope.configurations = data;
                    $scope.userPrograms=data.programs;

                });

               UserFacilityList.get({}, function(data) {
//                      $scope.homeFacility = data.facilityList[0];
                       $scope.homeFacilityId =data.facilityList[0].id;
                           $scope.loadProducts($scope.homeFacilityId, 82,true);

                  });

       AsnLookups.get(function(data) {

                          $scope.displayDocumentTypes =  data.documentTypes;
                              $scope.manufacturers = data.manufacturers;
                                  $scope.ports = data.ports;
                                      $scope.suppliers = data.suppliers;




                  });

         VaccineProgramProducts.get({programId:82},function(data) {
                           $scope.otherProducts=data;
                       });

    }

    getAllLookups();
//    $scope.homeFacilityId = homeFacility.id;
//    $scope.userPrograms = configurations.programs;
//    $scope.manufacturers = manufacturers;
//    $scope.ports = asnLookups.ports;
//    $scope.documentTypes = documentTypes;
//    $scope.suppliers = asnLookups.suppliers;
    $scope.productError = false;
    //console.log(configurations.productsConfiguration[0].product.id)
//    $scope.configurations = configurations;
    $scope.switchData=2;
//    $scope.otherProducts=otherProducts;

    $scope.asn = asn;

    $scope.loadProducts = function(facilityId, programId,isVaccine) {

        if(isVaccine){
        FacilityTypeAndProgramProducts.get({
                    facilityId: facilityId,
                    programId: programId
                }, function(data) {
                    var allProducts = data.facilityProduct;
//                    console.log(allProducts)
                    $scope.allProducts = _.sortBy(allProducts, function(product) {
                        return product.programProduct.product.id;
                    });


                    $scope.productsToDisplay = $scope.allProducts;

                });
        }else{

         VaccineProgramProducts.get({programId:82},function(data){

                var otherProducts=_.filter(data.programProductList,function(product){
                    return product.productCategory.id===51;
                });
               otherProducts=_.map(otherProducts,function(product){
               return {programProduct:product};
               });
                 $scope.allProducts = otherProducts;
                 $scope.productsToDisplay = $scope.allProducts;
                });

        }


    };

    $scope.getProductFromId = function(productId) {
        var editProduct = {};


        if($scope.isVaccine){
        $scope.allProducts=[];
           angular.forEach($scope.configurations.productsConfiguration, function(product, value) {
//                            console.log(product.product)
                    if (productId == product.product.id) {
//                                console.log('am here')
                        editProduct = product;
                    }


                });
        }else{
           $scope.allProducts=[];
        angular.forEach($scope.otherProducts.programProductList,function(product,value){
//        console.log(product)
        if(productId===product.product.id){
        editProduct=product;

        }
        if(product.productCategory.id===51){

                            $scope.allProducts.push({programProduct:product});
                            }


        });


        }
        return editProduct.product;

    };


       $scope.updateProductsToDisplay = function() {


            var toExclude = _.pluck(_.pluck(_.pluck($scope.productsToAdd, 'programProduct'), 'product'), 'primaryName');

//            console.log($scope.allProducts)
            $scope.productsToDisplay = $.grep($scope.allProducts, function(productObject) {
                return $.inArray(productObject.programProduct.product.primaryName, toExclude) == -1;
            });






        };


    if ($scope.asn) {
        $scope.editMode = true;
        $scope.asnReceiptDate = asn.asndate;
        $scope.asnCode = asn.asnnumber;
        $scope.blAwbNumber = asn.blawbnumber;
        $scope.clearingAgent = asn.clearingagent;
        $scope.expectedArrivalDate = asn.expectedarrivaldate;
        $scope.expectedDeliveryDate = asn.expecteddeliverydate;
        $scope.flightVesselNumber = asn.flightvesselnumber;
        $scope.notes = asn.note;
        $scope.poDate = asn.podate;
        $scope.poNumber = asn.ponumber;
        $scope.portOfArrivalId = asn.port.id;
        $scope.productsToAdd = [];
        $scope.supplier = asn.supplier;
        $scope.supplierId=$scope.supplier.id;
         $scope.isVaccine=false;
//        console.log($scope.configurations.productsConfiguration)
//        $scope.allProducts=$scope.configurations.productsConfiguration;






        $scope.productsToAdd = [];
        angular.forEach($scope.asn.asnLineItems, function(product, value) {
            editProduct = product.productList[0];
            var productLots = [];
            angular.forEach(product.asnLots, function(lot, value) {
                $scope.isVaccine=true;
                productLots.push({
                    quantity: lot.quantity,
                    displayCodeOnly: true,
                    info: {
                        expirationDate: lot.expirydate,
                        lotCode: lot.lotnumber,
                        manufactureDate: lot.manufacturingdate
                    }

                });

            });


            if($scope.isVaccine){

            $scope.productsToAdd.push({
                id: 0,
                displayNameOnly:true,
                programProduct: {
                product:editProduct
                },
                maxMonthsOfStock: 0,
                minMonthsOfStock: 0,
                eop: null,
                lots: productLots,
                unitPrice: product.unitprice,

            });
            }else{
            $scope.switchData=1;
             $scope.productsToAdd.push({
                                    id: 0,
                                    displayNameOnly:true,
                                    programProduct: {
                                    product:editProduct
                                    },
                                    maxMonthsOfStock: 0,
                                    minMonthsOfStock: 0,
                                    eop: null,
                                    quantity:product.quantityexpected,
                                    unitPrice: product.unitprice,

                                });

            }






        });



//$scope.updateProductsToDisplay();





    } else {

       $scope.isVaccine=true;
        $scope.productsToAdd = [{
            id: 0,
            displayNameOnly:false,
            programProduct: {},
            maxMonthsOfStock: 0,
            minMonthsOfStock: 0,
            eop: null,
            lots: [{
                quantity: 0,
                displayCodeOnly: false
            }],
            unitPrice: 0

        }];

    }



$scope.changeProductType=function(isVaccine){
    $scope.isVaccine=isVaccine;
        if(isVaccine){
        $scope.productsToAdd = [{
                        id: 0,
                        displayNameOnly:false,
                        programProduct: {},
                        maxMonthsOfStock: 0,
                        minMonthsOfStock: 0,
                        eop: null,
                        lots: [{
                            quantity: 0,
                            displayCodeOnly: false
                        }],
                        unitPrice: 0

                    }];
         $scope.loadProducts($scope.homeFacilityId, 82,true);
        }else{
        $scope.productsToAdd = [{
                        id: 0,
                        displayNameOnly:false,
                        programProduct: {},
                        maxMonthsOfStock: 0,
                        minMonthsOfStock: 0,
                        eop: null,
                        quantity:0,
                        unitPrice: 0

                    }];

                    $scope.loadProducts($scope.homeFacilityId, 82,false);
        }

};



//    function programProduct(){
//        VaccineProgramProducts.get({programId:82},function(data){
//
//        var otherProducts=_.filter(data.programProductList,function(product){
//            return product.productCategory.id===51;
//        });
//        console.log(data)
//otherProducts=_.map(otherProducts,function(product){
//               return {programProduct:product}
//               })
//        console.log(otherProducts)
//        });
//
//
//    }
//
//    programProduct();

    $scope.updateLotsToDisplay = function() {

        var toExclude = [];
        angular.forEach($scope.productsToAdd, function(product, value) {

            angular.forEach(product.lots, function(lot, value) {
                if (lot.info) {
                    toExclude.push(lot.info.lotCode);
                }

            });


        });


        $scope.lotsToDisplay = $.grep($scope.allLots, function(lotObject) {
            return $.inArray(lotObject.lotCode, toExclude) == -1;
        });
    };






    $scope.addLot = function(product,lot) {
        productIndex=_.indexOf($scope.productsToAdd,product);
        $lotIndex = $scope.productsToAdd[productIndex].lots.length - 1;
//        console.log($lotIndex);
        $scope.productsToAdd[productIndex].lots[$lotIndex].displayCodeOnly = true;
        $scope.productsToAdd[productIndex].lots.push({
            quantity: 0,
            displayCodeOnly: false
        });
        $scope.updateLotsToDisplay();
    };

    $scope.validateProduct = function() {
    if($scope.isVaccine){
        if (angular.equals($scope.productsToAdd[0].programProduct, {}) || !$scope.productsToAdd[0].unitPrice || !$scope.productsToAdd[0].lots[0].quantity) {
                $scope.productError = true;
                return;
            }
    }else{
    if (angular.equals($scope.productsToAdd[0].programProduct, {}) || !$scope.productsToAdd[0].unitPrice) {
                    $scope.productError = true;
                    return;
                }
    }

        $scope.productError = false;
    };

    $scope.addProduct = function() {

//        console.log($scope.allProducts);

       if($scope.isVaccine){

        $scope.productsToAdd.push({
                   id: 0,
                   displayNameOnly:false,
                   programProduct: {},
                   maxMonthsOfStock: 0,
                   minMonthsOfStock: 0,
                   eop: null,
                   lots: [{
                       quantity: 0,
                       displayCodeOnly: false
                   }],
                   unitPrice: 0

               });

               $scope.updateProductsToDisplay();

       //        lock the previous product and its last product if not locked
                   var previousProduct=$scope.productsToAdd[$scope.productsToAdd.length-2];
                   previousProduct.displayNameOnly=true;
       //            lock previous product lots
                   var previousProductLot=previousProduct.lots[previousProduct.lots.length-1];
                   if(previousProductLot.quantity===0 && !previousProductLot.info){
       //            lot not defined so remove it from array
                     previousProduct.lots.splice(previousProduct.lots.length-1,1);
                   }else{
                   previousProductLot.displayCodeOnly=true;
                   }




       }else{

        $scope.productsToAdd.push ({
                                       id: 0,
                                       displayNameOnly:false,
                                       programProduct: {},
                                       maxMonthsOfStock: 0,
                                       minMonthsOfStock: 0,
                                       eop: null,
                                       quantity:0,
                                       unitPrice: 0

                                   });

                                   $scope.updateProductsToDisplay();
                                   var previousProductt=$scope.productsToAdd[$scope.productsToAdd.length-2];
                                   previousProductt.displayNameOnly=true;

       }



    };




    $scope.removeProduct = function(productIndex) {

        $scope.productsToAdd.splice(productIndex, 1);
        if ($scope.productsToAdd.length + 1 === 1 && productIndex === 0) {
        if($scope.isVaccine){

        $scope.productsToAdd = [{
                        id: 0,
                        displayNameOnly:false,
                        programProduct: {},
                        maxMonthsOfStock: 0,
                        minMonthsOfStock: 0,
                        eop: null,
                        lots: [{
                            quantity: 0,
                            displayCodeOnly: false
                        }],
                        unitPrice: 0

                    }];

        }else{
         $scope.productsToAdd = [{
                                id: 0,
                                displayNameOnly:false,
                                programProduct: {},
                                maxMonthsOfStock: 0,
                                minMonthsOfStock: 0,
                                eop: null,
                                quantity:0,
                                unitPrice: 0

                            }];
        }



        }

        $scope.updateProductsToDisplay();

    };


    $scope.removeLot = function(productIndex, lotIndex) {

    var productToRemoveLot=$scope.productsToAdd[productIndex];

    if(productToRemoveLot.lots.length===1 && $scope.productsToAdd.length>=1){
//        completely remove the product
$scope.removeProduct(productIndex);

    }else{
     $scope.productsToAdd[productIndex].lots.splice(lotIndex, 1);
    }

    if($scope.allLots){
    $scope.updateLotsToDisplay();
    }


    };




    $scope.totalCostPerProduct = function(product) {

//        console.log($scope.totalQuantityPerProduct(product) * product.unitPrice)
        return $scope.totalQuantityPerProduct(product) * product.unitPrice;
    };

    $scope.totalQuantityPerProduct = function(product) {
        var sum = 0;
        if($scope.isVaccine){

          product.lots.forEach(function(lot) {
//          console.log(lot.quantity)
                    sum += parseInt(lot.quantity,10);
                });
        }else{

//        sum=product.quantity;
        }

//        console.log(sum);

        return sum;
    };



    $scope.cancel = function() {
        $scope.message = "";
        $scope.error = "";
        $scope.showError = false;
        $scope.$parent.asnId = false;
        $scope.$parent.asnIdUpdate = false;
        $location.path('');

    };


    var success = function(data) {
        $scope.error = "";
        //     console.log(data)
        $scope.$parent.message = data.success;
        $scope.$parent.asnId = true;

        $scope.showError = false;
        $location.path('');
    };


    var error = function(data) {
        $scope.$parent.message = "";
        $scope.error = data.data.error;
        $scope.showError = true;
    };


    var updateSuccess = function(data) {
            $scope.error = "";
            $scope.$parent.message = data.success;
            $scope.$parent.asnIdUpdate = true;
            $scope.showError = false;
            $location.path('');
        };


        var updateError = function(data) {
            $scope.$parent.message = "";
            $scope.error = data.data.error;
            $scope.showError = true;
        };


    $scope.changeLotDisplay = function(lotId) {
        //  console.log($scope.productsToAdd)
    };
    $scope.showNewLotModal = function(product) {
        $scope.newLotModal = true;
        $scope.newLot = {};
        $scope.newLot.product = product;

    };

    $scope.closeNewLotModal = function() {
        $scope.newLot = {};
        $scope.newLotModal = false;
    };


    $scope.grandTotal = function() {
        sum = 0;
        $scope.productsToAdd.forEach(function(product) {
            sum += $scope.totalCostPerProduct(product);


        });
        return sum;
    };




    function updateLotsToDisplay(lotsToAdd) {
        var toExclude = _.pluck(_.pluck(lotsToAdd, 'lot'), 'lotCode');
        $scope.lotsToDisplay = $.grep($scope.allLots, function(lotObject) {
            return $.inArray(lotObject.lotCode, toExclude) == -1;
        });
    }


    $scope.createLot = function() {
        var newLot = {};
        newLot.product = $scope.newLot.product;
        newLot.lotCode = $scope.newLot.lotCode;
        newLot.manufacturerName = $scope.newLot.manufacturerName;
        newLot.expirationDate = $filter('date')($scope.newLot.expirationDate, "yyyy-MM-dd");
        Lot.create(newLot, function(data) {
            $scope.newLotModal = false;
            //                             console.log(data.lot.product)
            $scope.loadProductLots(data.lot.product);
        });
    };



    $scope.loadProductLots = function(product)

    {

        $scope.productError = false;

        $scope.lotsToDisplay = {};

        if (product !== null) {


            ProductLots.get({
                productId: product.id
            }, function(data) {
                $scope.allLots = data.lots;
                //                              console.log(data.lots)
                $scope.lotsToDisplay = $scope.allLots;

            });


        }
    };


    $scope.saveAsn = function(status) {
    console.log($scope.documentDetails);
        $scope.validateProduct();
      if ($scope.asnForm.$error.required) {
            $scope.showError = true;
            $scope.error = 'form.error';
            $scope.message = "";
            return;
        }


        var asnLineItems = [];
        angular.forEach($scope.productsToAdd, function(product, key) {
            var asnLots = [];



            if($scope.isVaccine){
             angular.forEach(product.lots, function(lot, key) {
                            if (lot.info) {
                                asnLots.push({
                                    expirydate: lot.info.expirationDate,
                                    lotnumber: lot.info.lotCode,
                                    manufacturingdate: lot.info.manufactureDate,
                                    quantity: lot.quantity,
                                    serialnumber: 'string'
                                });



                            }
                        });
            }


            asnLineItems.push({
                asnLots: asnLots,
                lotflag: $scope.isVaccine,
                productid: product.programProduct.product.id,
                unitprice:product.unitPrice,
                quantityexpected:($scope.isVaccine)?0:product.quantity
            });


        });
        var asn = {
            asnLineItems: asnLineItems,
            asndate: $scope.asnReceiptDate,
            asnnumber: $scope.asnCode,
            blawbnumber: $scope.blAwbNumber,
            clearingagent: $scope.clearingAgent,
            expectedarrivaldate: $scope.expectedArrivalDate,
            expecteddeliverydate:$scope.expectedDeliveryDate,
            flightvesselnumber: $scope.flightVesselNumber,
            note: $scope.notes,
            podate: $scope.poDate,
            ponumber: $scope.poNumber,
            portofarrival: $scope.portOfArrivalId,
            purchaseDocuments: $scope.documentDetails,
            status: status,
            supplierid: $scope.supplierId
        };

        if($scope.asn){


                  Preadvice.update({id:$scope.asn.id}, asn, updateSuccess, updateError);


        }else{

        Preadvice.save({}, asn, success, error);


        }
    };



          $scope.documentDetails = [ ];


              $scope.addNew = function(documentDetail) {

                  $scope.documentDetails.push({
                         'documentType':{
                                          "id": "",
                                          "description": "",
                                          "name": ""
                                          },
                         'fileLocation':""
                    });

                    prepareSaveDocument($scope.documentDetails);

                };

                $scope.remove = function(){
                    var newDataList=[];
                    $scope.selectedAll = false;
                    angular.forEach($scope.documentDetails, function(selected){
                        if(!selected.selected){
                            newDataList.push(selected);
                        }else {
                        $scope.displayDocumentTypes.push(selected.documentType);
                        }


                    });
                    $scope.documentDetails = newDataList;
                };


            $scope.checkAll = function () {
                if (!$scope.selectedAll) {
                    $scope.selectedAll = true;
                } else {
                    $scope.selectedAll = false;
                }
                angular.forEach($scope.documentDetails, function(documentDetail) {
                    documentDetail.selected = $scope.selectedAll;
                });
            };

          $scope.loadDocumentDetails = function (data){};

          $scope.uploadFile = function(element) {};

          $scope.doUpload = function (document) {

           $scope.file = document;

          };

          $scope.disableSelectedRows = false;

          function removeItemFromList(document) {


           var i = $scope.displayDocumentTypes.length;

           while(i--) {

            $scope.displayDocumentTypes[i].disableSelectedRows = false;
            var name = $scope.displayDocumentTypes[i];

            if(document.name === name.name) {

               $scope.displayDocumentTypes[i].disableSelectedRows = true;
               $scope.displayDocumentTypes.splice(i,1);

            }


           }

          }

          function prepareSaveDocument(selectedDocuments) {

            selectedDocuments.forEach(function(document) {


               if(!isUndefined(document.documentType) && !isUndefined(document.file) ) {
                  removeItemFromList(document.documentType);
                 document.fileLocation = document.file.name;
                 getFile(document.file, document.documentType);

               }

            });

           }


  function getFile(file,documentType) {

      docService.saveDoc(file, documentType).then(

      function (response) {

      $scope.openMessage = true;
      $scope.message = response;

      $timeout(function(){

      $scope.openMessage = false;

      },2000);


      $http.get("/rest-api/warehouse/downloadFile?filename="+file.name).success(

      function(response) {
      console.log(response);

      $rootScope.docList = response;

      });
      },
      function (errResponse) { });



}

$scope.downloadFile = function (file){

var url ='/rest-api/warehouse/downloadFile?filename='+file.name;
$window.open(url, '_blank');

};

}
PreAdviceController.resolve = {

   documentTypes: function ($q, $timeout, $route, DocumentTypes) {
         var deferred = $q.defer();
//         $timeout(function () {
//             DocumentTypes.get({},function (data) {
//
//                var documents = [];
//
//                  if(!isUndefined(data.documents)){
//                   documents = data.documents;
//                  }
//
//                 deferred.resolve(documents);
//             });
//         }, 100);
deferred.resolve('data');
         return deferred.promise;
     },


    configurations: function($q, $timeout, AllVaccineInventoryConfigurations) {
        var deferred = $q.defer();
//        var configurations = {};
//        $timeout(function() {
//            AllVaccineInventoryConfigurations.get(function(data) {
//                configurations = data;
//                deferred.resolve(configurations);
//            });
//        }, 100);
deferred.resolve('data');
        return deferred.promise;
    },

    asn: function($q, $route, $timeout, Preadvice) {
        if ($route.current.params.id === undefined) return undefined;

        var deferred = $q.defer();
        var asnId = $route.current.params.id;

        $timeout(function() {
            Preadvice.get({
                id: asnId
            }, function(data) {

                deferred.resolve(data.asn);
            }, {});
        }, 100);
        return deferred.promise;
    },

    homeFacility: function($q, $timeout, UserFacilityList, StockCards) {
        var deferred = $q.defer();
        var homeFacility = {};

//        $timeout(function() {
//            UserFacilityList.get({}, function(data) {
//                homeFacility = data.facilityList[0];
//                StockCards.get({
//                    facilityId: homeFacility.id
//                }, function(data) {
//                    if (data.stockCards.length > 0) {
//                        homeFacility.hasStock = true;
//                    } else {
//                        homeFacility.hasStock = false;
//                    }
//                    deferred.resolve(homeFacility);
//                });
//
//            });
//
//        }, 100);
deferred.resolve('data');
        return deferred.promise;
    },
    manufacturers: function($q, $timeout, $route, ManufacturerList) {
        var deferred = $q.defer();
//
//        $timeout(function() {
//            ManufacturerList.get(function(data) {
//                deferred.resolve(data.manufacturers);
//            });
//        }, 100);
deferred.resolve('data');
        return deferred.promise;
    },
    asnLookups: function($q, $timeout, $route, AsnLookups) {
        var deferred = $q.defer();
//
//        $timeout(function() {
//            AsnLookups.get(function(data) {
//                deferred.resolve(data);
//            });
//        }, 100);
deferred.resolve('data');
        return deferred.promise;
    },
    otherProducts: function($q, $timeout, $route,  VaccineProgramProducts) {
            var deferred = $q.defer();
//
//            $timeout(function() {
//                 VaccineProgramProducts.get({programId:82},function(data) {
//                    deferred.resolve(data);
//                });
//            }, 100);
            deferred.resolve('data');
            return deferred.promise;
        },
};
