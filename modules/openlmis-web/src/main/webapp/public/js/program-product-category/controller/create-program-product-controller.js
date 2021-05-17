/*
 * This program is part of the OpenLMIS logistics management information system platform software.
 * Copyright © 2013 VillageReach
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU Affero General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Affero General Public License for more details.
 * You should have received a copy of the GNU Affero General Public License along with this program.  If not, see http://www.gnu.org/licenses.  For additional information contact info@OpenLMIS.org.
 */

function CreateProgramProductCategoryController($scope, ProgramProductCategorys, messageService) {
    $scope.addedProgramProducts = [];
    $scope.selectedProgramProductList = [];
    $scope.selectedProgram = $scope.$parent.$parent.productSelected;


    var fillProgramProduct = function (selectedProduct) {
        return {
            "dosesPerMonth": $scope.newProgramProduct.dosesPerMonth,
            "currentPrice": $scope.newProgramProduct.currentPrice,
            "fullSupply": $scope.newProgramProduct.fullSupply,
            "product": selectedProduct,
            "productCategory": $scope.$parent.$parent.productCategory,
            "program": $scope.$parent.$parent.program,
            "active": true
        };
    };

    var clearProgramProductModalData = function () {
        $scope.productCategorySelected = undefined;
        $scope.productSelected = undefined;
        $scope.products = undefined;
        $scope.newProgramProduct = undefined;
    };

    var addAndRemoveFromProgramProductList = function (listToBeFiltered, listToBeAdded, value) {
        listToBeAdded.push(_.find(listToBeFiltered, function (programProduct) {
            return programProduct.code == value;
        }));

        listToBeFiltered = _.reject(listToBeFiltered, function (programProduct) {
            return programProduct.code == value;
        });
        return listToBeFiltered;
    };

    var filterProductsToDisplay = function (selectedProduct) {
        $scope.$parent.$parent.productList = addAndRemoveFromProgramProductList($scope.productList, $scope.selectedProgramProductList, selectedProduct.code);
    };

    $scope.getHeader = function () {
        return messageService.get('header.code') + " | " +
            messageService.get('header.name') + " | " +
            messageService.get('header.strength') + " | " +
            messageService.get('header.unit.of.measure') + " | " +
            messageService.get('header.template.type');
    };

    $scope.formatResult = function (product) {
        if (!product) return false;
        var productData = product.text.split("|");
        var productType = productData[4].trim();

        if (productType.toLowerCase() === "true") {
            productType = messageService.get('label.full.supply');
        }
        if (productType.toLowerCase() === "false") {
            productType = messageService.get('label.non.full.supply');
        }
        return "<div class='row-fluid'>" +
            "<div class='span2'>" + productData[0] + "</div>" +
            "<div class='span4'>" + productData[1] + "</div>" +
            "<div class='span2'>" + productData[2] + "</div>" +
            "<div class='span2'>" + productData[3] + "</div>" +
            "<div class='span2'>" + productType + "</div>" +
            "</div>";
    };

    $scope.formatSelection = function (product) {
        if (!product) return false;
        var productData = product.text.split("|");
        return productData[0] + " - " + productData[1];
    };


    $scope.isAddDisabled = function () {
        return !($scope.newProgramProduct && $scope.newProgramProduct.dosesPerMonth &&
        $scope.newProgramProduct.currentPrice && $scope.productSelected);
    };

    function sortByCategory(facilityTypeApprovedProducts) {
        return _(facilityTypeApprovedProducts).chain().sortBy(function (facilityApprovedProduct) {
            return facilityApprovedProduct.programProduct.product.code.toLowerCase();
        }).sortBy(function (facilityApprovedProduct) {
            return facilityApprovedProduct.programProduct.productCategory.name.toLowerCase();
        }).value();
    }

    $scope.addProgramProduct = function () {
        var selectedProduct = $.parseJSON($scope.productSelected);
        var programProduct = fillProgramProduct(selectedProduct);
        $scope.addedProgramProducts.push(programProduct);
        filterProductsToDisplay(selectedProduct);
        clearProgramProductModalData();
    };

    $scope.removeProgramProduct = function (index) {
        var removedFTAProduct = $scope.addedProgramProducts[index];
        $scope.addedProgramProducts.splice(index, 1);
        $scope.selectedProgramProductList = addAndRemoveFromProgramProductList($scope.selectedProgramProductList, $scope.programProductList, removedFTAProduct.programProduct.product.code);
        clearProgramProductModalData();
    };

    $scope.addProgramProducts = function () {
        if ($scope.addedProgramProducts && $scope.addedProgramProducts.length > 0) {
            var invalid = false;

            _.each($scope.addedProgramProducts, function (programProduct) {
                if (isUndefined(programProduct.dosesPerMonth) || isUndefined(programProduct.product)) {
                    invalid = true;
                    return false;
                }
                return true;
            });
            if (invalid) {
                $scope.modalError = 'error.correct.highlighted';
                return;
            }
            $scope.modalError = undefined;

            ProgramProductCategorys.save({}, $scope.addedProgramProducts, function (data) {
                $scope.$parent.$parent.message = data.success;
                $scope.$parent.$parent.programProductsListModal = false;
                $scope.$parent.$parent.loadProducts(1);
                $scope.addedProgramProducts = [];
                clearProgramProductModalData();
            }, function (data) {
                $scope.$parent.$parent.message = undefined;
                $scope.modalError = data.data.error;
            });
            $scope.$parent.$parent.focusSuccessMessageDiv();
        }
    };

    $scope.cancelProgramProducts = function () {
        $scope.$parent.$parent.programProductsListModal = false;
        $scope.addedProgramProducts = [];
        $scope.$parent.$parent.message = undefined;
        clearProgramProductModalData();
    };
}