'use strict';

var AngularSpringApp = {};

var App = angular.module('AngularSpringApp', [
    'AngularSpringApp.filters', 'ngRoute','ngResource'
]);

App.config(['$routeProvider', function ($routeProvider) {
    $routeProvider.when('/compute', {
        templateUrl: 'compute/layout.html',
        controller: ComputeController
    });

    $routeProvider.when('/evaluate', {
        templateUrl: 'evaluate/layout.html',
        controller: SearchController
    });

    $routeProvider.otherwise({redirectTo: '/evaluate'});
}]);
