function UploadShipmentController($scope, SupportedUploads, messageService, loginConfig, $window) {

$scope.supportedUploads = {

                    "upload-in-bound": {
                    "displayName": "In-bounds",
                    "recordHandler": null,
                    "importableClass": null
                     }

                  };


  $scope.$on('$viewContentLoaded', function () {
    var options = {
      beforeSubmit: $scope.validate,
      success: processResponse,
      error: failureHandler
    };
    $('#uploadForm').ajaxForm(options);
  });

  $scope.getMessage = function (key) {
    return messageService.get(key);
  };

  $scope.clearMessages = function(){
    $scope.successMsg = $scope.errorMsg = "";
  };

  $scope.validate = function (formData) {
    $scope.$apply(function () {
      $scope.inProgress = true;
      $scope.successMsg = $scope.errorMsg = "";
      if (setErrorMessageIfEmpty(formData[0].value, 'model', 'upload.select.type')) {
        $scope.inProgress = false;
      }
      if (setErrorMessageIfEmpty(formData[1].value, 'csvFile', 'upload.select.file')) {
        $scope.inProgress = false;
      }
    });
    return $scope.inProgress;
  };

  function setErrorMessageIfEmpty(value, fieldName, messageKey) {
    if (utils.isEmpty(value)) {
      $scope.uploadForm[fieldName].errorMessage = messageService.get(messageKey);
      return true;
    } else {
      $scope.uploadForm[fieldName].errorMessage = "";
      return false;
    }
  }

  var failureHandler = function (response) {
    $scope.$apply(function () {
      if (response.status === 401) {
        loginConfig.modalShown = loginConfig.preventReload = true;
      }
      else if (response.status === 403) {
        $window.location = "/public/pages/access-denied.html";
      }
      else {
        try {
          $scope.errorMsg = JSON.parse(response.responseText).error;
        } catch (e) {
          $scope.errorMsg = messageService.get('error.upload.network.server.down');
          $scope.inProgress = false;
        }
      }
      $scope.inProgress = false;
    });
  };

  function processResponse(responseText) {
    var response = JSON.parse(responseText);
    $scope.$apply(function () {
      if (response.success) {
        successHandler(response);
      }

      if (response.error) {
        $scope.successMsg = "";
        $scope.errorMsg = response.error;
      }

      $scope.inProgress = false;
    });
  }

  var successHandler = function (data) {
    $scope.successMsg = data.success;
    $scope.errorMsg = "";
  };

}