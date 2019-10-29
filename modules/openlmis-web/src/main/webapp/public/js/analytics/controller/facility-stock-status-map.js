function FacilityStockStatusMapController ($scope,$rootScope,$compile,GetGeoStockStatusForMapData,GetGeoStockStatusDetailsData) {

$scope.centerL = {
            lat: -6.397912857937015,
            lng: 34.911609148190784,
            zoom: 6
          };

$rootScope.facility = {"id":14994,"code":"DM520269","name":"Chahwa","description":"Dispensary","gln":"G17434","mainPhone":"2552603636","fax":"null","geographicZone":{"id":493,"code":"dom","name":"Dodoma CC","level":{"id":4,"code":"dist","name":"District","levelNumber":4},"parent":{"code":"dodo","name":"Dodoma","level":{"code":"reg","name":"Region"}},"catchmentPopulation":10000},"facilityType":{"id":1,"code":"disp","name":"Dispensary","description":"Dispensary","levelId":5,"nominalMaxMonth":6,"nominalEop":3,"displayOrder":13,"active":true},"catchmentPopulation":10000,"latitude":-6.06583,"longitude":35.98432,"altitude":531.3,"operatedBy":{"id":1,"code":"MOHCDGEC","text":"MOHCDGEC","displayOrder":1},"coldStorageGrossCapacity":9,"coldStorageNetCapacity":9,"suppliesOthers":false,"sdp":true,"hasElectricity":true,"online":true,"hasElectronicSCC":false,"hasElectronicDAR":false,"active":true,"goLiveDate":"2013-01-01","goDownDate":"2013-01-01","satellite":false,"enabled":true,"virtualFacility":false,"supportedPrograms":[{"id":9203,"facilityId":14994,"program":{"id":1,"code":"ils","name":"ILS","description":"ILS","active":true,"budgetingApplies":true,"templateConfigured":true,"regimenTemplateConfigured":false,"isEquipmentConfigured":false,"enableSkipPeriod":false,"showNonFullSupplyTab":true,"hideSkippedProducts":true,"enableIvdForm":false,"push":false,"usePriceSchedule":false,"enableMonthlyReporting":false},"active":true,"startDate":"2013-01-01","stringStartDate":"2013-01-01","editedStartDate":"2013-01-01"}],"owners":[{"displayName":"ZPCT ZPCT","owner":{"id":1,"code":"ZPCT","text":" ZPCT","displayOrder":1},"facility":14994,"active":false,"spc_xdOie":0,"idx_xdOie":0},{"displayName":"CIDRZ CIDRZ","owner":{"id":2,"code":"CIDRZ","text":"CIDRZ","displayOrder":2},"facility":14994,"active":false,"spc_xdOie":0,"idx_xdOie":1},{"displayName":"ZDF ZDF","owner":{"id":4,"code":"ZDF","text":"ZDF","displayOrder":3},"facility":14994,"active":false,"spc_xdOie":0,"idx_xdOie":2},{"displayName":"CHAZ CHAZ","owner":{"id":3,"code":"CHAZ","text":"CHAZ","displayOrder":4},"facility":14994,"active":false,"spc_xdOie":0,"idx_xdOie":3},{"displayName":"SBH SYSTEMS FOR BETTER HEALTH","owner":{"id":5,"code":"SBH","text":"SYSTEMS FOR BETTER HEALTH","displayOrder":5},"facility":14994,"active":false,"spc_xdOie":0,"idx_xdOie":4},{"displayName":"CRS CRS ","owner":{"id":6,"code":"CRS","text":"CRS ","displayOrder":6},"facility":14994,"active":false,"spc_xdOie":0,"idx_xdOie":5},{"displayName":"UM UNIVERSITY OF MARYLAND","owner":{"id":7,"code":"UM","text":"UNIVERSITY OF MARYLAND","displayOrder":7},"facility":14994,"active":false,"spc_xdOie":0,"idx_xdOie":6}],"stringGoLiveDate":"01-01-2013","stringGoDownDate":"01-01-2013","interfaceMappings":[]};

$rootScope.loadGeoFacilityStockMap = function(params){
   params.period = parseInt(91,10);

   GetGeoStockStatusForMapData.get(params).then (function(data){

   $scope.facilityDetails = data;

   console.log(data);

   initMap(data,params);


    //google.maps.event.addDomListener(window, 'load', initialize(data));



   });

};

var map;
var mapProp;


function initialize(data) {

 mapProp = {
    center: new google.maps.LatLng(-6.3690, 34.8888),
    zoom: 6,
    mapTypeId: google.maps.MapTypeId.TERRAIN
  };
  map = new google.maps.Map(document.getElementById("map"), mapProp);

  var infoWindow = new google.maps.InfoWindow({
    content: "<div>Hello! World</div>",
    maxWidth: 500
  });


 $.each(data, function(i, well) {
 if(!isUndefined(well.latitude)) {

    var wellCircle = new google.maps.Circle({
         strokeColor:    checkGreaterThanZero(well),
         strokeOpacity: 1,
         strokeWeight: 0,
         fillColor:    checkGreaterThanZero(well),
         fillOpacity: 1,
         map: map,
         center: new google.maps.LatLng(well.latitude, well.longitude),
         radius: 12000,
         preferCanvas: false,
         icon: {
                     path: google.maps.SymbolPath.CIRCLE,
                     scale: 10
                   },
                   draggable: true
       });

        google.maps.event.addListener(wellCircle, 'click', function(ev) {
                  infoWindow.setPosition(ev.latLng);
                  infoWindow.open(map);
      });


 }

});

}

//google.maps.event.addDomListener(window, 'load', initialize);

function checkGreaterThanZero(data) {
var color =(data.os > 0 )?'#00B2EE':(data.uk > 0)?'gray':(data.so > 0) ?'#ff0d00':(data.us > 0)?'orange':'#006600';
return color;
}

function checkGreaterThanZero2(data) {
var color =(data.os > 0 )?'os':(data.uk > 0)?'gray':(data.so > 0) ?'so':(data.us > 0)?'us':'green';
return color;
}


 function initMap(data,params) {
        var map = new google.maps.Map(document.getElementById('map'), {
          zoom: 6,
          center: {lat:  -6.397912857937015, lng: 34.911609148190784}
 });
 mapFunc(map,data,params);
}


function mapFunc(map,data,params){

var iconBase = '/public/images/';

var icons = {
           gray: {
           icon: iconBase + 'mm_20_black.png'
           },
           green: {

          icon: iconBase + 'green-dot.png'

           },
           os:{
                               icon: iconBase + 'blue-dot.png'
           },

          so:{
                     icon: iconBase + 'red-dot.png'
           },

          us: {
            icon: iconBase + 'orange-dot.png'
          }
   };

 var featuresData = [];
 $.each(data, function(i, info) {
 if(!isUndefined(info.latitude)) {
   featuresData.push({
   position :new google.maps.LatLng(info.latitude, info.longitude),
   type:checkGreaterThanZero2(info),
   data:info

   });
 }

 });

 var infoWindow = new google.maps.InfoWindow({
     maxWidth: 500

   });

for (var i = 0; i < featuresData.length; i++) {

 var marker = new google.maps.Marker({
 position: featuresData[i].position,
 icon: icons[featuresData[i].type].icon,
 map: map,
 title: featuresData[i].facility,
 data:featuresData[i]
 });

makeMarker(marker, i);

}


function makeMarker(marker, i) {

  google.maps.event.addListener(marker, 'click', (function(marker, i) {
            return function() {
                console.log(marker.data.data);
                infoWindow.setContent(popupFormat(marker.data.data));
                infoWindow.open(map, marker);
            };
        })(marker, i));

}

 function popupFormat(feature) {
      var totalCost = (feature.currentprice) * (feature.ordered - feature.required);

         return '<table class="table table-bordered" style="width: 310px;"><tr><td><b>Facility</b></td><td>' + feature.facility + '</td><td><b>Phone #</b></td><td>' + feature.mainphone + '</td></tr>' +
         '<tr><td><b>Product Code</b></td><td>' + feature.productcode + '</td><td><b>Product </b></td><td>' + feature.product + '</td></tr>' +
         '<tr><td><b>SOH</b></td><td>' + feature.soh + '</td><td><b>MOS </b></td><td>' + feature.mos + '</td></tr>' +
          '<tr><td><b>Price</b></td><td>' + feature.currentprice + '</td><td><b>Required </b></td><td>' + feature.required + '</td><td><b>Total Cost </b></td><td>' + totalCost + '</td></tr>' +

         '</table>';
         /*    '<tr><td><b>Region</b></td><td>' + feature.georegion + '</td><td><b>Expected Facilities</b></td><td>' + feature.expected + '</td></tr>' +
             '<tr><td><b>Zone</b></td><td>' + feature.geozone + '</td><td><b>Reported This Period</b></td><td>' + feature.period + '</td></tr></table>' +
             '<table class="table table-bordered" style="width: 310px;"><tr><th class="bold">Indicator</th><th class="bold">This Period</th><th class="bold">Previous Period</th></tr>' +
             '<tr bgcolor="#dd514c"><td class="bold">Stocked Out</td><td class="number">' + feature.stockedout + '</td><td class="number">' + feature.stockedoutprev + '</td></tr>' +
             '<tr bgcolor="#faa732"><td class="bold">Under Stocked</td><td class="number">&nbsp;&nbsp;' + feature.understocked + '</td><td class="number">' +feature.understockedprev +'</td></tr>' +
             '<tr bgcolor="#4bb1cf"><td>Over Stocked</td><td class="number">&nbsp;&nbsp;' + feature.overstocked + '</td><td class="number">' +feature.overstockedprev +'</td></tr>' +
             '<tr bgcolor="#5eb95e"><td>Adequately Stocked</td><td class="number">&nbsp;&nbsp;' + feature.adequatelystocked + '</td><td class="number">' +feature.adequatelystockedprev +'</td></tr>';
*/
     }



}
}