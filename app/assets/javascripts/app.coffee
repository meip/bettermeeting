
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
                templateUrl: '/assets/partials/meetings/view.html'
            })
            .when('/meetings/create', {
                templateUrl: '/assets/partials/meetings/create.html'
            })
            .when('/users', {
                templateUrl: '/assets/partials/users/view.html'
            })
            .when('/users/create', {
                templateUrl: '/assets/partials/users/create.html'
            })
            .when('/test/create', {
              templateUrl: '/assets/partials/meetingstorage/create.html'
            })
            .when('/test/view', {
              templateUrl: '/assets/partials/meetingstorage/view.html'
            })
            .otherwise({redirectTo: '/'})
@commonModule = angular.module('bmApp.common', [])
@controllersModule = angular.module('bmApp.controllers', [])
@servicesModule = angular.module('bmApp.services', [])
@modelsModule = angular.module('bmApp.models', [])
@directivesModule = angular.module('bmApp.directives', [])
@filtersModule = angular.module('bmApp.filters', [])
@localStorageModule = angular.module('bmApp.localStorage', ['LocalStorageModule'])