/*
 * This program is part of the OpenLMIS logistics management information system platform software.
 * Copyright © 2013 VillageReach
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU Affero General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Affero General Public License for more details.
 * You should have received a copy of the GNU Affero General Public License along with this program.  If not, see http://www.gnu.org/licenses.  For additional information contact info@OpenLMIS.org.
 */

function ProgramProductController($scope, programs,ProgramProductCategoryList,ProgramProductCategories, productCategories, ProductsFilter, ProgramProducts, $dialog, messageService) {

  $scope.programs = programs;
  $scope.productCategories = productCategories;

  $scope.showResults = false;
  $scope.currentPage = 1;

  $scope.loadProducts = function (page, lastQuery) {
    if (!$scope.program || !$scope.productCategory) return;
    if (lastQuery !== undefined) {
      getProducts(page, lastQuery);
    } else {
      getProducts(page, $scope.query);
    }
  };

  function getProducts(page, query) {
    $scope.searchedQuery = query || "";
      ProgramProductCategoryList.get({page: page, searchParam: $scope.searchedQuery,
          categoryId: $scope.productCategory.id,
          programId: $scope.program.id
      }, function (data) {
      $scope.programProductsList = data.programProducts;
      // $scope.pagination = data.pagination;
      // $scope.totalItems = $scope.pagination.totalRecords;
      // $scope.currentPage = $scope.pagination.page;
      $scope.showResults = true;
    }, {});
  }

  $scope.$watch('currentPage', function () {
    if ($scope.currentPage !== 0)
      $scope.loadProducts($scope.currentPage, $scope.searchedQuery);
  });

  $scope.triggerSearch = function (event) {
    if (event.keyCode === 13) {
      $scope.loadProducts(1);
    }
  };

  $scope.showCategory = function (list, index) {
    return !((index > 0 ) && (list[index].productCategory.name === list[index - 1].productCategory.name));
  };

  $scope.clearSearch = function () {
    $scope.query = "";
    $scope.loadProducts(1);
  };

  $scope.edit = function (programProducts) {
    programProducts.underEdit = true;
    programProducts.dosesPerMonth = programProducts.dosesPerMonth;
    programProducts.currentPrice = programProducts.currentPrice;

  };

  var deleteProgramProduct = function (result) {
    if (result) {
      ProgramProducts.remove({id: $scope.programProductsToBeDeleted.id}, $scope.programProductsToBeDeleted, function (data) {
        $scope.message = messageService.get(data.success, $scope.programProductsToBeDeleted.programProduct.product.primaryName);
        $scope.loadProducts($scope.currentPage);
      }, {});
    }
  };

  $scope.confirmDelete = function (programProducts) {
    $scope.programProductsToBeDeleted = programProducts;
    var options = {
      id: "confirmDialog",
      header: "label.confirm.action",
      body: messageService.get('msg.delete.facility.approved.product.confirmation',
        $scope.programProductsToBeDeleted.programProduct.product.primaryName, $scope.productCategory.name, $scope.program.name)
    };
    OpenLmisDialog.newDialog(options, deleteProgramProduct, $dialog);
  };

  $scope.cancel = function (programProducts) {
    programProducts.maxMonthsOfStock = programProducts.previousMaxMonthsOfStock;
    programProducts.minMonthsOfStock = programProducts.previousMinMonthsOfStock;
    programProducts.eop = programProducts.previousEop;
    programProducts.underEdit = false;
    programProducts.error = "";
  };

  $scope.focusSuccessMessageDiv = function () {
    var searchProgramProductLabel = angular.element('#searchProgramProductLabel').get(0);
    if (!isUndefined(searchProgramProductLabel)) {
      searchProgramProductLabel.scrollIntoView();
    }
  };

  function updateListToDisplay(updatedProgramProduct) {
    for (var i = 0; i < $scope.programProductsList.length; i++) {
      if ($scope.programProductsList[i].id == updatedProgramProduct.id) {
        $scope.programProductsList[i] = updatedProgramProduct;
      }
    }
  }

  $scope.update = function (programProducts) {
    if (isUndefined(programProducts.dosesPerMonth)) {
      programProducts.error = 'error.correct.highlighted';
      return;
    }
    programProducts.productCategory = $scope.productCategory;
    programProducts.program = $scope.program;

      ProgramProductCategories.update({id: programProducts.id}, programProducts, function (data) {
      $scope.updatedProgramProduct = data.programProducts;
      $scope.message = data.success;
      programProducts.underEdit = false;
      programProducts.error = "";
      updateListToDisplay($scope.updatedProgramProduct);
    }, function (data) {
      programProducts.error = data.data.error;
    });
    $scope.focusSuccessMessageDiv();
  };

  $scope.openProgramProductsModal = function () {
      ProductsFilter.get({programId: $scope.program.id, categoryId: $scope.productCategory.id}, function (data) {
      $scope.productList = data.products;
      $scope.programProductsListModal = true;
    }, {});
  };
}

ProgramProductController.resolve = {
  programs: function ($q, $timeout, Programs) {
    var deferred = $q.defer();

    $timeout(function () {
      Programs.get({type: "pull"}, function (data) {
        deferred.resolve(data.programs);
      }, {});
    }, 100);
    return deferred.promise;
  },
  productCategories: function ($q, $route, $timeout, ProductCategories) {
    var deferred = $q.defer();

    $timeout(function () {
        ProductCategories.get({}, function (data) {
        deferred.resolve(data.productCategoryList);
      }, {});
    }, 100);
    return deferred.promise;
  }
};