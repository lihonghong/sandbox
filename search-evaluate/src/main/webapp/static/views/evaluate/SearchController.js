'use strict';

var app = angular.module('AngularSpringApp', [
    'AngularSpringApp.filters',
    'ngRoute'
]);

app.config(['$routeProvider', function ($routeProvider) {
    $routeProvider.when('/', {
        templateUrl: 'views/evaluate/layout.html',
        controller: 'SearchController'
    });
    $routeProvider.when('/evaluate', {
        templateUrl: 'views/evaluate/layout.html',
        controller: 'SearchController'
    });
}]);

app.controller('SearchController', function ($scope, $http) {
    var marks = {};

    $scope.fetchSearchResults = function () {
        $http.get('evaluate/query').success(function (query) {
            if (query.query == null) {
                window.location.href = "/compute/score";
            }

            $http.get('evaluate/next/' + query.query).success(function (searchResults) {
                $scope.results = searchResults;
                $scope.query = query;
            });
        });
    };

    $scope.choice = function () {
        $http.post('evaluate/submit/' + JSON.stringify(marks)).success(function () {
        });
    }

    $scope.mark = function (isGood, id) {
        marks[id] = isGood;
        console.log(marks);
    };

    $scope.next = function () {
        marks['query'] = $scope.query.query;
        $scope.choice();
        $scope.fetchSearchResults();
        marks = {};
    };

    $scope.fetchSearchResults();

});





