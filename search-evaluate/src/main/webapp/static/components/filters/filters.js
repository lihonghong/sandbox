'use strict';

/* Filters */

angular.module('AngularSpringApp.filters', []).

    filter('msImageProxy', function () {
        return function (result) {
            var imageURL = result.image;

            if (!imageURL && result.doc_type == 'author') {
                return '/images/portrait_placeholder.jpg';
            }

            return imageURL;
        }
    }).

    filter('msURL', ['$window', function ($window) {
        return function (result) {
            if (result.url) {
                return result.url.url;
            }
        }
    }]);
