function FacilityStockStatusMapController ($scope,$rootScope,$compile,GetGeoStockStatusForMapData) {

$scope.centerL = {
            lat: -6.397912857937015,
            lng: 34.911609148190784,
            zoom: 6
          };

$rootScope.facility = {"id":14994,"code":"DM520269","name":"Chahwa","description":"Dispensary","gln":"G17434","mainPhone":"2552603636","fax":"null","geographicZone":{"id":493,"code":"dom","name":"Dodoma CC","level":{"id":4,"code":"dist","name":"District","levelNumber":4},"parent":{"code":"dodo","name":"Dodoma","level":{"code":"reg","name":"Region"}},"catchmentPopulation":10000},"facilityType":{"id":1,"code":"disp","name":"Dispensary","description":"Dispensary","levelId":5,"nominalMaxMonth":6,"nominalEop":3,"displayOrder":13,"active":true},"catchmentPopulation":10000,"latitude":-6.06583,"longitude":35.98432,"altitude":531.3,"operatedBy":{"id":1,"code":"MOHCDGEC","text":"MOHCDGEC","displayOrder":1},"coldStorageGrossCapacity":9,"coldStorageNetCapacity":9,"suppliesOthers":false,"sdp":true,"hasElectricity":true,"online":true,"hasElectronicSCC":false,"hasElectronicDAR":false,"active":true,"goLiveDate":"2013-01-01","goDownDate":"2013-01-01","satellite":false,"enabled":true,"virtualFacility":false,"supportedPrograms":[{"id":9203,"facilityId":14994,"program":{"id":1,"code":"ils","name":"ILS","description":"ILS","active":true,"budgetingApplies":true,"templateConfigured":true,"regimenTemplateConfigured":false,"isEquipmentConfigured":false,"enableSkipPeriod":false,"showNonFullSupplyTab":true,"hideSkippedProducts":true,"enableIvdForm":false,"push":false,"usePriceSchedule":false,"enableMonthlyReporting":false},"active":true,"startDate":"2013-01-01","stringStartDate":"2013-01-01","editedStartDate":"2013-01-01"}],"owners":[{"displayName":"ZPCT ZPCT","owner":{"id":1,"code":"ZPCT","text":" ZPCT","displayOrder":1},"facility":14994,"active":false,"spc_xdOie":0,"idx_xdOie":0},{"displayName":"CIDRZ CIDRZ","owner":{"id":2,"code":"CIDRZ","text":"CIDRZ","displayOrder":2},"facility":14994,"active":false,"spc_xdOie":0,"idx_xdOie":1},{"displayName":"ZDF ZDF","owner":{"id":4,"code":"ZDF","text":"ZDF","displayOrder":3},"facility":14994,"active":false,"spc_xdOie":0,"idx_xdOie":2},{"displayName":"CHAZ CHAZ","owner":{"id":3,"code":"CHAZ","text":"CHAZ","displayOrder":4},"facility":14994,"active":false,"spc_xdOie":0,"idx_xdOie":3},{"displayName":"SBH SYSTEMS FOR BETTER HEALTH","owner":{"id":5,"code":"SBH","text":"SYSTEMS FOR BETTER HEALTH","displayOrder":5},"facility":14994,"active":false,"spc_xdOie":0,"idx_xdOie":4},{"displayName":"CRS CRS ","owner":{"id":6,"code":"CRS","text":"CRS ","displayOrder":6},"facility":14994,"active":false,"spc_xdOie":0,"idx_xdOie":5},{"displayName":"UM UNIVERSITY OF MARYLAND","owner":{"id":7,"code":"UM","text":"UNIVERSITY OF MARYLAND","displayOrder":7},"facility":14994,"active":false,"spc_xdOie":0,"idx_xdOie":6}],"stringGoLiveDate":"01-01-2013","stringGoDownDate":"01-01-2013","interfaceMappings":[]};

$rootScope.loadGeoFacilityStockMap = function(params){


   GetGeoStockStatusForMapData.get(params).then (function(data){
          console.log(data);


    google.maps.event.addDomListener(window, 'load', initialize(data));



   });

};

function initialize(data) {

            $scope.map = new google.maps.Map(document.getElementById('map'), {
                zoom: 6,
                center: { lat: -6.3690, lng:  34.8888},
                preferCanvas: true
            });

           $scope.cities = [];
            data.forEach(function(dx){
              $scope.cities.push({title:dx.facility, lat:dx.latitude, lng:dx.longitude});

            });

         /*   $scope.cities = [
                { title: 'Chahwa', lat: -6.06583, lng: 35.98432,color:'blue' },
                { title: 'Melbourne', lat: -37.812228, lng: 144.968355 }
            ];*/


            $scope.infowindow = new google.maps.InfoWindow({
                content: ''
            });


            for (var i = 0; i < $scope.cities.length; i++) {

                      var cityCircle = new google.maps.Circle ({
                                                    strokeColor: '#FF0000',
                                                    strokeOpacity: 0.8,
                                                    strokeWeight: 2,
                                                    fillColor: '#FF0000',
                                                    fillOpacity: 0.35,
                                                    map: $scope.map
                                                    center: $scope.cities[i].lat,
                                                    radius: 100
                                          });

                var marker = new google.maps.Marker({
                    position: new google.maps.LatLng($scope.cities[i].lat, $scope.cities[i].lng),
                    map: $scope.map,
                    title: $scope.cities[i].title
                });

                var content = '<a ng-click="cityDetail(' + i + ')" class="btn btn-default">View details</a>';
                var compiledContent = $compile(content)($scope);

                google.maps.event.addListener(marker, 'click', (function(marker, content, scope) {
                    return function() {
                        scope.infowindow.setContent(content);
                        scope.infowindow.open(scope.map, cityCircle);
                    };
                })(marker, compiledContent[0], $scope));

            }



        }





}