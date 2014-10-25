
dependencies = [
    'ngRoute',
    'ui.bootstrap',
    'bmApp.filters',
    'bmApp.services',
    'bmApp.controllers',
    'bmApp.directives',
    'bmApp.common',
    'bmApp.routeConfig',
    'bmApp.localStorage'
]

app = angular.module('bmApp', dependencies)

angular.module('bmApp.routeConfig', ['ngRoute'])
    .config ($routeProvider) ->
        $routeProvider
            .when('/', {
                templateUrl: '/assets/partials/home.html'
            })
            .when('/meetings', {
                templateUrl: '/assets/partials/meetings/list.html'
            })
            .when('/meetings/create', {
                templateUrl: '/assets/partials/meetings/create.html'
            })
            .when('/users', {
                templateUrl: '/assets/partials/users/list.html'
            })
            .when('/users/create', {
                templateUrl: '/assets/partials/users/create.html'
            })
            .when('/offline/create', {
              templateUrl: '/assets/partials/offline/create.html'
            })
            .when('/offline/list', {
              templateUrl: '/assets/partials/offline/list.html'
            })
            .when('/offlinecreation/create', {
                templateUrl: '/assets/partials/offlinecreation/create.html'
            })
            .when('/offlinecreation/list', {
                templateUrl: '/assets/partials/offlinecreation/list.html'
            })
            .otherwise({redirectTo: '/'})
@commonModule = angular.module('bmApp.common', [])
@controllersModule = angular.module('bmApp.controllers', [])
@servicesModule = angular.module('bmApp.services', [])
@modelsModule = angular.module('bmApp.models', [])
@directivesModule = angular.module('bmApp.directives', [])
@filtersModule = angular.module('bmApp.filters', [])
@localStorageModule = angular.module('bmApp.localStorage', ['LocalStorageModule'])