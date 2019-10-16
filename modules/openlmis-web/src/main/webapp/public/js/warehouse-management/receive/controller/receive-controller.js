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
function ReceiveController(DeleteDocument,DocumentList,StockEvent,$window,$scope,$filter,Locations, AsnLookups, Receive,$location,UserFacilityList,VaccineProgramProducts,AllVaccineInventoryConfigurations, receive, ProductLots, FacilityTypeAndProgramProducts, Lot,
                           $rootScope,UploadFile,$http,docService, $timeout){




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

                  Locations.get({"searchParam": "%", "column": "name", "page": 1}, function (data) {
                        $scope.locations = data.locations;

                      }, {});

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

    $scope.receive = receive;

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


        function isViewMode(){
                var url=$location.url();
                return url.split("/")[1]==="view";
                }




    if ($scope.receive) {
       console.log($scope.receive.asnNumber);
       if($scope.receive.asnNumber !== undefined)
       getListOfFilesByASNumber($scope.receive.asnNumber);
        $scope.editMode = true;
        $scope.facilityId = $scope.homeFacilityId;
        $scope.viewMode=isViewMode();
        $scope.asnReceiptDate = receive.asnReceiveDate;
        $scope.asnCode = receive.asnNumber;
        $scope.blAwbNumber = receive.blawBnumber;
        $scope.clearingAgent = receive.clearingAgent;
        $scope.expectedArrivalDate = receive.expectedArrivalDate;
//        $scope.expectedDeliveryDate = asn.expecteddeliverydate;
        $scope.flightVesselNumber = receive.flightVesselNumber;
        $scope.notes = receive.note;
        $scope.poDate = receive.poDate;
        $scope.poNumber = receive.poNumber;
        $scope.portOfArrivalId = receive.port.id;
        $scope.productsToAdd = [];
        $scope.supplier = receive.supplier;
//        $scope.supplierId=$scope.supplier.id;
         $scope.isVaccine=false;
//        console.log($scope.configurations.productsConfiguration)
//        $scope.allProducts=$scope.configurations.productsConfiguration;






        $scope.productsToAdd = [];
        angular.forEach($scope.receive.receiveLineItems, function(product, value) {
            editProduct = product.productList[0];
            var productLots = [];
            angular.forEach(product.receiveLots, function(lot, value) {
                $scope.isVaccine=true;
                productLots.push({
                    quantity: lot.quantity,
                    displayCodeOnly: true,
                    info: {
                        expirationDate: lot.expiryDate,
                        lotCode: lot.lotNumber,
                        manufactureDate: lot.manufacturingDate
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
                unitPrice: product.unitPrice,

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
                                    quantity:product.quantityCounted,
                                    unitPrice: product.unitPrice,

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
        $scope.$parent.receiveSaved = false;
        $scope.$parent.received = false;
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
                                $scope.lotsToDisplay = _.sortBy($scope.allLots,'lotCode');


            });


        }
    };

$scope.saveAsn = function(status) {
//    console.log($scope.docList);
        $scope.validateProduct();
//                        console.log($scope.asnForm)
    if ($scope.asnForm.$error.required) {
            $scope.showError = true;
            $scope.error = 'form.error';
            $scope.message = "";
            return;
        }


        var receiveLineItems = [];

        var events=[];

        angular.forEach($scope.productsToAdd, function(product, key) {
            var receiveLots = [];

            if($scope.isVaccine){
             angular.forEach(product.lots, function(lot, key) {
                            if (lot.info) {

                             var event={};

                             event.type="RECEIPT";
                             event.facilityId=$scope.homeFacilityId;
                             event.productCode=product.programProduct.product.code;
                             event.quantity=parseInt(lot.quantity,10);

                             event.lot={};
                             event.lot.lotCode=lot.info.lotCode;
                             event.lot.manufacturerName=lot.info.manufacturerName;
                             event.lot.expirationDate=lot.info.expirationDate;
                             event.occurred = new Date();
                             event.customProps={};

                             if(lot.vvmStatus !==undefined)
                             {
                             event.customProps.vvmStatus=lot.vvmStatus;
                             }
                             event.customProps.receivedFrom="WMS";

                             events.push(event);

                                receiveLots.push({
                                    expirydate: lot.info.expirationDate,
                                    lotNumber: lot.info.lotCode,
                                    manufacturingDate: lot.info.manufactureDate,
                                    quantity: lot.quantity,
                                    serialnumber: 'string',
                                    locationId:lot.locationId,
                                });



                            }
                        });
            }


            receiveLineItems.push({
                receiveLots: receiveLots,
                lotFlag: $scope.isVaccine,
                productId: product.programProduct.product.id,
                unitPrice:product.unitPrice,
                boxCounted:product.boxCounted,
                quantityCounted:($scope.isVaccine)?0:product.quantity
            });


        });
        var receive = {
            receiveLineItems: receiveLineItems,
            receiveDate: $scope.receiveDate,
            facilityId: $scope.homeFacilityId,

            asnNumber: $scope.asnCode,
            blawBnumber: $scope.blAwbNumber,
            clearingAgent: $scope.clearingAgent,
            expectedArrivalDate: $scope.expectedArrivalDate,
            expecteddeliverydate:$scope.expectedDeliveryDate,
            actualArrivalDate:$scope.actualArrivalDate,
            flightVesselNumber: $scope.flightVesselNumber,
            isForeignProcurement:$scope.isForeignProcurement,
            note: $scope.notes,
            description:$scope.descriptionOfProcurement,
            noteToSupplier:$scope.noteToSupplier,
            poDate: $scope.poDate,
            poNumber: $scope.poNumber,
            portOfArrival: $scope.portOfArrivalId,
            purchaseDocuments: $scope.docList,
            status: status,
            supplierId: $scope.supplierId,
            programId:82
        };

//        console.log(receive)


        Receive.save({}, receive, function (data) {



        if(data.success && status === 'Received') {
        // receive only
        StockEvent.update({facilityId:$scope.homeFacilityId},events, function (data) {
             // saving only
                   $scope.error = "";
                          $scope.$parent.message = data.success;
                          $scope.$parent.received = true;
                          $scope.showError = false;
                          $location.path('');

        });

        }else{
        // saving only
         $scope.error = "";
                $scope.$parent.message = data.success;
                $scope.$parent.receiveSaved = true;
                $scope.showError = false;
                $location.path('');

        }

        });

    };

  $scope.upload = function(document) {

        if(document.documentType !== null && document.file !== null) {

            document.fileLocation = document.file.name;
            removeItemFromList(document.documentType);
            getFile(document.file,document);
            document.documentType = null;
            document.file = null;

        }

    };


  $scope.removeFile = function(file) {
        $scope.docList = [];

        DeleteDocument.get({id:file.id, code:file.asnNumber}, function(response){

        $scope.docList = response.list;

         $scope.displayDocumentTypes.push(file.documentType);
        });

  };

  function removeItemFromList(document) {


  var i = $scope.displayDocumentTypes.length;

  while(i--) {

  var name = $scope.displayDocumentTypes[i];


  if(document.name === name.name) {

  $scope.displayDocumentTypes.splice(i,1);

  }
  }
 }


function getFile(file,documentType) {

     var asnNum;
     if($scope.asnCode === null || $scope.asnCode === undefined) {
        var today = new Date().toLocaleDateString();
        asnNum = new Date(today).getTime();
     }else
       asnNum = $scope.asnCode;

      docService.saveDoc(file, asnNum, documentType.documentType.name).then(

      function (response) {

      $scope.openMessage = true;
      $scope.message = response;

      $timeout(function(){

      $scope.openMessage = false;

      },2000);

      getListOfFilesByASNumber(asnNum);

/*
      $http.get("/rest-api/warehouse/downloadFile?filename="+$scope.asnCode+'-'+file.name).success(

      function(response) {
      console.log(response);



      });*/




      },
      function (errResponse) {


       });



}

function getListOfFilesByASNumber(asnNumber) {
     console.log(asnNumber);
     DocumentList.get({code:asnNumber}, function(data){

      $rootScope.docList = data.list;
      if(data.list.length > 0) {
      $scope.findMatches($scope.displayDocumentTypes,data.list);
      }
    });
}


$scope.findMatches = function(data, comparedTo) {


 $scope.displayDocumentTypes = _.filter(data, function(num,index){

  if(comparedTo[index] !== undefined) {

  return num.name !== comparedTo[index].documentType.name;

  } else {

  return null;
  }

  });


};

$scope.downloadFile = function (file){

var url ='/rest-api/warehouse/downloadFile?filename='+file.name;
$window.open(url, '_blank');

};


}


ReceiveController.resolve = {

    receive: function($q, $route, $timeout, Receive) {
        if ($route.current.params.id === undefined) return undefined;

        var deferred = $q.defer();
        var receiveId = $route.current.params.id;

        $timeout(function() {
            Receive.get({
                id: receiveId
            }, function(data) {

                deferred.resolve(data.receive);
                console.log(data.receive);
            }, {});
        }, 100);
        return deferred.promise;
    }

};