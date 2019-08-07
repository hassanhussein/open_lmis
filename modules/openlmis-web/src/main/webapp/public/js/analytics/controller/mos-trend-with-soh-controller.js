function mosTrendWithSOHController ($scope,$rootScope){
$rootScope.mos_soh_title = 'MOS Trend Of Nevirapine with SOH ,April- June, 2019';

Highcharts.chart('mos-soh-chart', {
    chart: {
        zoomType: 'xy'
    },
    credits:{
    enabled:false
    },
    title: {
        text: ''
    },
    subtitle: {
        text: ''
    },
    xAxis: [{
        categories: ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun',
            'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec'],
        crosshair: true
    }],
    yAxis: [{ // Primary yAxis
        labels: {
            format: '{value} ',
            style: {
                color: Highcharts.getOptions().colors[1]
            }
        },
        title: {
            text: 'AMC',
            style: {
                color: Highcharts.getOptions().colors[1]
            }
        }
    }, { // Secondary yAxis
        title: {
            text: 'SOH',
            style: {
                color: Highcharts.getOptions().colors[0]
            }
        },
        labels: {
            format: '{value} ',
            style: {
                color: Highcharts.getOptions().colors[0]
            }
        },
        opposite: true
    }],
    tooltip: {
        shared: true
    },
    legend: {
        layout: 'horizontal',
        align: 'left',
        x: 120,
        verticalAlign: 'bottom',
        y: 100,
        floating: true,
        backgroundColor:
            Highcharts.defaultOptions.legend.backgroundColor || // theme
            'rgba(255,255,255,0.25)'
    },
    series: [{
        name: 'SOH',
        type: 'column',
        yAxis: 1,
        data: [49.9, 71.5, 106.4, 129.2, 144.0, 176.0, 135.6, 148.5, 216.4, 194.1, 95.6, 54.4],
        tooltip: {
            valueSuffix: ' '
        }

    }, {
        name: 'AMC',
        type: 'spline',
        data: [7.0, 6.9, 9.5, 14.5, 18.2, 21.5, 25.2, 26.5, 23.3, 18.3, 13.9, 9.6],
        tooltip: {
            valueSuffix: ' '
        }
    }]
});



}