'use strict';

angular.module('misearch.evaluate.search', [
    'AngularSpringApp.filters',
    'ngRoute'
])

    .config(['$routeProvider', function ($routeProvider) {
        $routeProvider.when('/', {
            templateUrl: 'views/evaluate/layout.html',
            controller: 'SearchController'
        });
    }])

    .controller('SearchController', function ($scope, $http) {
        var marks = {};

        $scope.fetchSearchResults = function () {
            $http.get('evaluate/query').success(function (query) {
                if (query.query == null) {
                    window.location.href = "#/compute";
                }

                $http.get('evaluate/next/' + query.query).success(function (searchResults) {
                    $scope.results = searchResults;
                    $scope.query = query;
                });
            });
        };

        $scope.choice = function () {
            marks['query'] = $scope.query.query;
            $http.post('evaluate/submit/' + JSON.stringify(marks)).success(function () {
            });
        }

        $scope.mark = function (isGood, id) {
            marks[id] = isGood;
            console.log(marks);
        };

        $scope.next = function () {
            $scope.choice();
            $scope.fetchSearchResults();
            marks = {};
        };

        $scope.compute = function () {
            $scope.choice();
            marks = {};
            window.location.href = "#/compute";
        };

        $scope.fetchSearchResults();

    });





