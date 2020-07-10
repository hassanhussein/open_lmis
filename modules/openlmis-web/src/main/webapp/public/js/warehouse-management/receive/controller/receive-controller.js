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
function ReceiveController(GetAllLocationsByType,GetAllClearingAgents,DeleteDocument,DocumentList,StockEvent,$window,$scope,$filter,Locations, AsnLookups, Receive,$location,UserFacilityList,VaccineProgramProducts,AllVaccineInventoryConfigurations, receive, ProductLots, FacilityTypeAndProgramProducts, Lot,
                           $rootScope,UploadFile,$http,docService, $timeout, GetLocationSummary,GetBinLocationByCategory){


 $scope.$parent.receiveSaved = false;
                                   $scope.$parent.received = false;


$scope.clearingAgentList = [];
    function getAllLookups(){


     GetAllClearingAgents.get({}, function(data){

           $scope.clearingAgentList = data.agents;
           console.log(data);

           });

    GetBinLocationByCategory.get({category:'receiving'}, function(data){

     $scope.locationList = data.bins;

//     console.log(data);

    });



     AllVaccineInventoryConfigurations.get(function(data) {
                    $scope.configurations = data;
                    $scope.userPrograms=data.programs;

                });

               UserFacilityList.get({}, function(data) {
//                      $scope.homeFacility = data.facilityList[0];
                       $scope.homeFacilityId =data.facilityList[0].id;
                           $scope.loadProducts($scope.homeFacilityId, 82,true);

                  });


//              GetAllLocationsByType.get({type:'A'}, function(data){
//
//                   $scope.locations = data.locationList;
//
//                   console.log(data.locationList);
//
//                  });

       AsnLookups.get(function(data) {

                          $scope.displayDocumentTypes =  data.documentTypes;

                              $scope.manufacturers = data.manufactures;

                                  $scope.ports = data.ports;
                                      $scope.suppliers = data.suppliers;
                                       $scope.currencies=data.currencies;




                  });

         VaccineProgramProducts.get({programId:82},function(data) {
                           $scope.otherProducts=data;
                       });

    }

    getAllLookups();
     $scope.loadProductLots = function(product)

        {

            $scope.productError = false;

            $scope.lotsToDisplay = {};

            if (product !== null) {

//        console.log(product)
                ProductLots.get({
                    productId: product.id
                }, function(data) {
                    $scope.allLots = data.lots;
                    //                              console.log(data.lots)
                                    $scope.lotsToDisplay = _.sortBy($scope.allLots,'lotCode');


                });


            }
        };

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
//console.log(receive)
     if(receive) {
     console.log(receive);

    //if(receive.supplier === null || receive.currencyId === null) {
     if(receive.currency !== null)
     receive.currencyId = receive.currency.id;

     receive.supplierId = receive.supplier.id;

     $scope.supplierId = receive.supplier.id;

//     }
     }
    $scope.receive = receive;


     if(!isUndefined(receive)) {


        getListOfFilesByASNumber(receive.asnNumber);

       // $scope.documentDetails  = asn.purchaseDocuments;

     }

    console.log($scope.receive);

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


    $scope.addLot = function(product,lot) {
//    console.log(product)
       if(!$scope.viewMode){
        productIndex=_.indexOf($scope.productsToAdd,product);
               $lotIndex = $scope.productsToAdd[productIndex].lots.length - 1;
       //        console.log($lotIndex);
               $scope.productsToAdd[productIndex].lots[$lotIndex].displayCodeOnly = true;
               $scope.productsToAdd[productIndex].lots.push({
                   quantity: '',
                   displayCodeOnly: false
               });
       }
//        $scope.updateLotsToDisplay();
    };


$scope.print=function() {

var url = '/rest-api/warehouse/grn-report/'+parseInt($scope.receive.id, 10)+'/print';
$window.open(url, '_blank');

};



$scope.setCurrency=function(){

if(!$scope.viewMode){
$scope.currency=_.findWhere($scope.currencies,{'id':parseInt($scope.selectedCurrency,10)});

}
};

    $scope.quantityVsBox=function(){
//    get the total quantity
if($scope.isVaccine){

//console.log('and')
var total_lot_quantity = 0;
//    this is safe because we have only one product per receive
    var total_box_quantity=$scope.productsToAdd[0].boxCounted;
    angular.forEach($scope.productsToAdd,function(product,key){

        angular.forEach(product.lots,function(lot,key){
            total_lot_quantity+=parseInt(lot.quantity,10);
        });
    });

//    console.log(total_box_quantity)
//        console.log(total_lot_quantity)

//    console.log($scope.productsToAdd[0])
    return total_lot_quantity >= total_box_quantity;

}else{
 return true;
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
//       if($scope.receive.asnNumber !== undefined)
       getListOfFilesByASNumber($scope.receive.asnNumber);
        $scope.editMode = true;
        $scope.facilityId = $scope.homeFacilityId;
        $scope.viewMode=isViewMode();
        $scope.asnReceiptDate = receive.asnReceiveDate;
        $scope.asnCode = receive.asnNumber;
        $scope.invoiceNumber = receive.invoiceNumber;
        $scope.blAwbNumber = receive.blawBnumber;
        $scope.clearingAgent = receive.clearingAgent;
        $scope.expectedArrivalDate = receive.expectedArrivalDate;
//        $scope.expectedDeliveryDate = asn.expecteddeliverydate;
        $scope.flightVesselNumber = receive.flightVesselNumber;
        $scope.notes = receive.note;
        $scope.poDate = receive.poDate;
        $scope.poNumber = receive.poNumber;
        $scope.descriptionOfProcurement=receive.description;
        $scope.receiveDate = receive.receiveDate;
        $scope.portOfArrivalId = receive.port.id;
        $scope.actualArrivalDate=receive.actualArrivalDate;
        $scope.productsToAdd = [];
        $scope.supplier = receive.supplier;
         $scope.supplierId=$scope.supplier.id;
        $scope.currency=receive.currency;
        if($scope.currency !== null)
        $scope.selectedCurrency=$scope.currency.id;
         $scope.isVaccine=false;
//        console.log($scope.configurations.productsConfiguration)
//        $scope.allProducts=$scope.configurations.productsConfiguration;






        $scope.productsToAdd = [];

        angular.forEach($scope.receive.receiveLineItems, function(product, value) {
            var lineItem=product;
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
                        manufactureDate: lot.manufacturingDate,

                    },
                    locationId:lot.locationId,
                    boxCounted:lot.boxNumber

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
                unitPrice: product.unitPrice
//                boxCounted:lineItem.boxCounted,

            });

            $scope.addLot($scope.productsToAdd[0],'ad');
          $scope.loadProductLots($scope.productsToAdd[0].programProduct.product);
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
                quantity: '',
                displayCodeOnly: false
            }],
            unitPrice: ''

        }];

    }



$scope.changeProductType=function(isVaccine){
    if($scope.isVaccine!==isVaccine){

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
                                quantity: '',
                                displayCodeOnly: false
                            }],
                            unitPrice: ''

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
                            quantity:'',
                            unitPrice: ''

                        }];

                        $scope.loadProducts($scope.homeFacilityId, 82,false);
            }
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
                       quantity: '',
                       displayCodeOnly: false
                   }],
                   unitPrice: ''

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
                                       quantity:'',
                                       unitPrice: ''

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
                            quantity: '',
                            displayCodeOnly: false
                        }],
                        unitPrice: ''

                    }];

        }else{
         $scope.productsToAdd = [{
                                id: 0,
                                displayNameOnly:false,
                                programProduct: {},
                                maxMonthsOfStock: 0,
                                minMonthsOfStock: 0,
                                eop: null,
                                quantity:'',
                                unitPrice: ''

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
                    sum += parseInt(lot.quantity?lot.quantity:0,10);
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
        newLot.packSize=$scope.newLot.packSize;
        newLot.expirationDate = $filter('date')($scope.newLot.expirationDate, "yyyy-MM-dd");
        Lot.create(newLot, function(data) {
            $scope.newLotModal = false;
            $scope.batchCreated=true;
            $timeout(function(){
            $scope.batchCreated=false;
            },3000);
            $scope.loadProductLots(data.lot.product);
        });
    };


$scope.quantitiesValid=function(){
      var qError=false;
    angular.forEach($scope.productsToAdd[0].lots,function(lot){
            if(lot.info && lot.quantity===""){
             qError=true;
             return;
            }
    });

    if(!$scope.productsToAdd[0].unitPrice || qError){

    $scope.quantityError = true;
         $timeout(function(){

              $scope.quantityError = false;

              },10000);
    return false;
    }

    return true;



    };

$scope.saveAsn = function(status) {

//    console.log($scope.docList);
        $scope.validateProduct();

        $scope.quantitiesValid();

$scope.quantityBoxError=false;

//   if (!$scope.quantityVsBox()){
//        $scope.quantityBoxError=true;
//         return;
//     }else{
//     $scope.quantityBoxError=false;
//     }

//                        console.log($scope.asnForm)
    if ($scope.asnForm.$error.required) {
            $scope.showError = true;
            $scope.error = 'form.error';
            $scope.message = "";
//            console.log($scope.asnForm.$error)
            return;
        }



        var receiveLineItems = [];


        angular.forEach($scope.productsToAdd, function(product, key) {
            var receiveLots = [];

            if($scope.isVaccine){
             angular.forEach(product.lots, function(lot, key) {
                            if (lot.info) {



                                receiveLots.push({
                                    expirydate: lot.info.expirationDate,
                                    lotNumber: lot.info.lotCode,
                                    manufacturingDate: lot.info.manufactureDate,
                                    quantity: lot.quantity,
                                    boxNumber:lot.boxCounted,
                                    serialnumber: 'string',
                                    locationId:parseInt(lot.locationId,10),
                                });




                            }
                        });
            }

console.log(receiveLots);

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
            invoiceNumber:$scope.invoiceNumber,
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
            currencyId:$scope.selectedCurrency,
            portOfArrival: $scope.portOfArrivalId,
            purchaseDocuments: $scope.docList,
            status: status,
            supplierId: $scope.supplierId,
            programId:82
        };

//        console.log(receive)

        if($scope.receive){
         Receive.update({id:$scope.receive.id},receive,function(){
         $scope.error = "";
          console.log('update');
          if(status==='RECEIVED'){
              $scope.$parent.received = true;
          }else{
          $scope.$parent.receiveSaved = true;
          }
          $scope.showError = false;
          $location.path('');
            });
        }else{

        Receive.save({}, receive, function (data) {
            $scope.error = "";
            console.log('save');
              if(status==='DRAFT'){
                          $scope.$parent.receiveSaved = true;
                      }else{
                      $scope.$parent.receive = true;
                      }
            $scope.showError = false;
            $location.path('');

                });

        }

    };

  $scope.upload = function(document) {
        validateAnsNumber($scope.asnCode,document);

        if(document.documentType !== null && document.file !== null && !isUndefined(document.file)) {

            document.fileLocation = document.file.name;
            removeItemFromList(document.documentType);
            getFile(document.file,document);
            document.documentType = null;
            document.file = null;

        }

    };

    function validateAnsNumber(asnCode,document) {

    if(asnCode === undefined) {

    $scope.asnCode = $scope.poNumber;
           console.log($scope.asnCode);
    }

    }


      $scope.removeFile = function(file) {

         DeleteDocument.get({id:file.id, code:file.asnNumber}, function(response) {

         getListOfFilesByASNumber(file.asnNumber);

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
    var docLists = [];
     DocumentList.get({code:asnNumber}, function(data){

       docLists = data.list;
      if(data.list.length > 0) {

      $timeout(function(){
      getOnlyMatchedDocumentTypes($scope.displayDocumentTypes,  docLists);
      },1000);

      console.log(data.list);
    // $scope.findMatches($scope.displayDocumentTypes, data.list);
      }


      });
}

function getOnlyMatchedDocumentTypes(documentTypes, docs) {

 $scope.displayDocumentTypes = [];
 docs.forEach(function(data) {

     for(var i=0;i<documentTypes.length;i++){

       if(documentTypes[i].name === data.documentType.name){
        documentTypes[i].isAvailable = true;
       }
     }

});

var filteredData = [];
filteredData = _.filter(documentTypes, function(dx){
return dx.isAvailable !== true;
});
$scope.docLists = docs;
$scope.displayDocumentTypes  = filteredData;

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

var url ='/rest-api/warehouse/downloadFile?filename='+file;
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