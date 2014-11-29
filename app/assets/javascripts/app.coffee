
dependencies = [
    'ngRoute',
    'bmApp.filters',
    'bmApp.services',
    'bmApp.controllers',
    'bmApp.directives',
    'bmApp.common',
    'bmApp.routeConfig',
    'ngAnimate',
    'mgcrea.ngStrap',
    'ngSanitize',
    'cfp.hotkeys',
    'angular-intro'
]

app = angular.module('bmApp', dependencies)

angular.module('bmApp.routeConfig', ['ngRoute'])
    .config ($routeProvider) ->
        $routeProvider
            .when('/', {
                templateUrl: '/assets/partials/home.html'
            })
            .when('/meeting/note', {
                templateUrl: '/assets/partials/meeting/note.html'
            })
            .when('/users', {
                templateUrl: '/assets/partials/users/list.html'
            })
            .when('/users/create', {
                templateUrl: '/assets/partials/users/create.html'
            })
            .when('/login', {
              templateUrl: '/assets/partials/login/login.html'
            })
            .when('/logout', {
              templateUrl: '/assets/partials/login/logout.html'
            })
            .when('/signup', {
              templateUrl: '/assets/partials/login/signup.html'
            })
            .when('/test', {
              templateUrl: '/assets/partials/test.html'
            })
            .otherwise({redirectTo: '/'})
@commonModule = angular.module('bmApp.common', [])
@controllersModule = angular.module('bmApp.controllers', [])
@servicesModule = angular.module('bmApp.services', [])
@factoriesModule = angular.module('bmApp.factories', [])
@modelsModule = angular.module('bmApp.models', [])
@directivesModule = angular.module('bmApp.directives', [])
@filtersModule = angular.module('bmApp.filters', [])
