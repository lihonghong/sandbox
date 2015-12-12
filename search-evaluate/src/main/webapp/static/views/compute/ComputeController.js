'use strict';

/**
 * CarController
 * @constructor
 */
var ComputeController = function ($scope, $http) {

    $scope.fetchComputeScore = function () {
        $http.get('/compute/score').success(function (score) {
            $scope.query = score;
        });
    };

    $scope.reset = function () {
        $http.post('/compute/reset').success(function () {
            window.location.href = "/";
        });
    }

    $scope.fetchComputeScore();
};