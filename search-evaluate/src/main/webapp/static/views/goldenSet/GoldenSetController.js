'use strict';

angular.module('misearch.evaluate.golden', [
    'ngRoute'
])

    .config(['$routeProvider', function ($routeProvider) {
        $routeProvider.when('/', {
            templateUrl: 'views/goldenSet/layout.html',
            controller: 'GoldenSetController'
        });
    }])

    .controller('GoldenSetController', function ($scope, $http) {

        $scope.fetchQuery = function () {
            $http.get('evaluate/query/news').success(function (results) {
                $scope.results = results;
            });
        };

        $scope.fetchQuery();
    });