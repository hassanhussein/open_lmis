function EquipmentCategoryController($rootScope, $scope, DisciplineList, EquipmentCategories, EquipmentTypes, EquipmentFunctionalTestTypes, $routeParams, $location){
    $scope.EquipmentCategory  = getAllEquipmentCategory();
    $scope.EquipmentCategoryId  = $routeParams.id || 0;
    $scope.cmd = $routeParams.cmd || '';
    $scope.functionalTestTypes = EquipmentFunctionalTestTypes.query();

    DisciplineList.get(function(data){
    console.log(data);
         $scope.disciplines = data.disciplines;

    });

    EquipmentTypes.get(function(data){
         $scope.equipmentTypes = data.equipment_types;

    });
    $scope.selectedEquipmentTypes = [];

    $scope.getFilteredOutEquipmentTypes = function(w){
        var equipmentTypes = _.reject($scope.getSelectedEquipmentTypes(), w);
       return _.reject($scope.equipmentTypes, function(type){ return _.contains(equipmentTypes, type.id);});
    };

    $scope.getSelectedEquipmentTypes = function () {
        return _.chain($scope.EquipmentCategory).pluck('equipmentTypeIds').reduceRight(function(a, b) { return a.concat(b); }, []).value();

    };

    function getAllEquipmentCategory() {
        return EquipmentCategories.query();
    }

    if($scope.EquipmentCategoryId){
        EquipmentCategories.get({'id':$routeParams.id}, function(data){ $scope.equipmentCategory = data;});
        $rootScope.message = null;
    }


    $scope.saveEquipmentCategory = function(){

        function SaveEquipmentCategorySuccessCallback(result) {
            $scope.error = null;
            $rootScope.message = result.success;
            $location.path("list");
        }

        function SaveEquipmentCategoryErrorCallback(errorResponse) {
            $scope.error = errorResponse.data.error;
            $rootScope.message = null;
        }


        if($scope.EquipmentCategoryId)
            EquipmentCategories.update($scope.equipmentCategory, SaveEquipmentCategorySuccessCallback, SaveEquipmentCategoryErrorCallback);
        else
            EquipmentCategories.save($scope.equipmentCategory, SaveEquipmentCategorySuccessCallback, SaveEquipmentCategoryErrorCallback);

    };

    $scope.deleteEquipmentCategory = function(id){
        EquipmentCategories.EquipmentCategory.delete({'id': id}, deleteEquipmentCategorySuccessCallback, deleteEquipmentCategoryErrorCallback);
        function deleteEquipmentCategorySuccessCallback(successResponse){
            $rootScope.message = successResponse.success;
            $scope.EquipmentCategory  = getAllEquipmentCategory();
        }
        function deleteEquipmentCategoryErrorCallback(errorResponse){
            $scope.errorMessage = errorResponse.data.error;
        }

    };

    $scope.saveEquipmentTypeAssociation = function(){
        $scope.formData = [];
        $scope.formData = $scope.EquipmentCategory;
        EquipmentCategories.associateEquipmentTypes($scope.EquipmentCategory, successCallback);

        function successCallback(successResponse){
            $rootScope.message = successResponse.success;
        }

    };


    $scope.getEquipmentTypesWithNoCategories = function(){
        return _.without(_.pluck($scope.equipmentTypes.equipment_types,'id'), _.chain($scope.EquipmentCategory).pluck('equipmentTypeIds').reduceRight(function(a, b) { return a.concat(b); }, []).value());
    };

    $scope.hideTypes = function (category) {
        category.add = true;
        category.editMode = true;
        category.hideAdd = false;
    };

    $scope.EditTypes = function (category) {
        category.add = false;
        category.editMode = true;
        category.hideEdit = true;
    };
}