/*
 * Electronic Logistics Management Information System (eLMIS) is a supply chain management system for health commodities in a developing country setting.
 *
 * Copyright (C) 2015  John Snow, Inc (JSI). This program was produced for the U.S. Agency for International Development (USAID). It was prepared under the USAID | DELIVER PROJECT, Task Order 4.
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU Affero General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

function interpolate(value, count) {
  var val = parseFloat(value) / parseFloat(count);
  var interpolator = chroma.interpolate.bezier(['red', 'yellow', 'green']);
  return interpolator(val).hex();
}

function initiateMap(scope) {
  angular.extend(scope, {
    layers: {
      baselayers: {
        googleTerrain: {
          name: 'Terrain',
          layerType: 'TERRAIN',
          type: 'google'
        },
        googleHybrid: {
          name: 'Hybrid',
          layerType: 'HYBRID',
          type: 'google'
        },
        googleRoadmap: {
          name: 'Streets',
          layerType: 'ROADMAP',
          type: 'google'
        }
      }
    },
    legend: {
      position: 'bottomleft',
      colors: ['#FF0000', '#FFFF00', '#5eb95e', "#000000"],
      labels: ['N\'ayant soumis aucun rapport', 'Ayant soumis partiellement des rapports ', 'Ayant soumis tous les rapports', 'Ne devant pas soumettre de rapport']
    }
  });

  scope.indicator_types = [
    {
      code: 'ever_over_total',
      name: 'Nbr Ets ayant transmis au moins 1 rapport / Nbr total d\'ets'
    },
    {
      code: 'ever_over_expected',
      name: 'Nbr Ets ayant transmis au moins 1 rapport / Ets devant soumettre des rapports'
    },
    {
      code: 'period_over_expected',
      name: 'Rapports reçus / Rapports attendus'
    }
  ];


  scope.viewOptins = [
    {id: '0', name: 'Non Reporting Only'},
    {id: '1', name: 'Reporting Only'},
    {id: '2', name: 'All'}
  ];

}

function popupFormat(feature) {
  return '<table class="table table-bordered" style="width: 250px"><tr><th colspan="2"><b>' + feature.properties.name + '</b></th></tr>' +
    '<tr><td>Ets devant soumettre des rapports</td><td class="number">' + feature.expected + '</td></tr>' +
    '<tr><td>Rapports reçus</td><td class="number">' + feature.period + '</td></tr>' +
    '<tr><td>Nbr Ets ayant transmis au moins 1 rapport </td><td class="number">' + feature.ever + '</td></tr>' +
    '<tr><td class="bold">Nbr total d\'ets</b></td><td class="number bold">' + feature.total + '</td></tr>';
}

function onEachFeature(feature, layer) {
  layer.bindPopup(popupFormat(feature));
}

function zoomAndCenterMap (leafletData, $scope) {
  leafletData.getMap().then(function (map) {
    var latlngs = [];
    for (var c = 0; c < $scope.features.length; c++) {
      if ($scope.features[c].geometry === null || angular.isUndefined($scope.features[c].geometry))
        continue;
      if ($scope.features[c].geometry.coordinates === null || angular.isUndefined($scope.features[c].geometry.coordinates))
        continue;
      for (var i = 0; i < $scope.features[c].geometry.coordinates.length; i++) {
        var coord = $scope.features[c].geometry.coordinates[i];
        for (var j=0; j < coord.length; j++) {
          var points = coord[j];
          for(var p in points){
            var latlng;
            if(angular.isNumber(points[p])){
              latlng = L.GeoJSON.coordsToLatLng(points);
            }else{
              latlng = L.GeoJSON.coordsToLatLng(points[p]);
            }
            latlngs.push(latlng);
          }
        }
      }
    }

    map.fitBounds(latlngs);
  });
}
