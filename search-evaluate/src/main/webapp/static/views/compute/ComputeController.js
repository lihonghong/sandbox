'use strict';

angular.module('misearch.evaluate.score', [
    'AngularSpringApp.filters',
    'ngRoute'
])

    .config(['$routeProvider', function ($routeProvider) {
        $routeProvider.when('/compute', {
            templateUrl: 'views/compute/layout.html',
            controller: 'ComputeController'
        });
    }])

    .controller('ComputeController', function ($scope, $http) {

        $scope.fetchComputeScore = function () {
            $http.get('/compute/score').success(function (score) {
                $scope.score = score;
            });
        };

        $scope.reset = function () {
            $http.post('/compute/reset').success(function () {
                window.location.href = "#/";
            });
        }

        $scope.fetchComputeScore();
    });