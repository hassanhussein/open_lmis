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
app.directive('aFloat', function() {
    function link(scope, element, attrs){
        scope.$watch('afData', function(){
            init(scope.afData,scope.afOption);
        });
        scope.$watch('afOption', function(){
            init(scope.afData,scope.afOption);
        });

        scope.$watch('afRender', function(){
            if(scope.afRender){

               // alert('render chart is '+scope.afRender);
                $.plot(element,scope.afData,scope.afOption);
               // init(scope.afData,scope.afOption);
            }

        },true);


        function init(o,d){
            var totalWidth = element.width(), totalHeight = element.height();

            if (totalHeight === 0 || totalWidth === 0) {
                throw new Error('Please set height and width for the aFloat element'+'width is '+element);
            }

            if(element.is(":visible") && !isUndefined(d) && !isUndefined(o)){
                $.plot(element, o , d);
            }
        }
    }
    return {
        restrict: 'EA',
        template: '<div></div>',
        link: link,
        replace:true,
        scope: {
            afOption: '=',
            afData: '=',
            afRender:'='
        }
    };
});