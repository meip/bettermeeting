
dependencies = [
    'ngRoute',
    'ngResource',
    'ui.bootstrap',
    'bmApp.filters',
    'bmApp.services',
    'bmApp.controllers',
    'bmApp.directives',
    'bmApp.common',
    'bmApp.routeConfig',
    'bmApp.localStorage',
    'bmApp.animations'
]

app = angular.module('bmApp', dependencies)

angular.module('bmApp.routeConfig', ['ngRoute'])
    .config ($routeProvider) ->
        $routeProvider
            .when('/', {
                templateUrl: '/assets/partials/home.html'
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
            .when('/meeting/create', {
                templateUrl: '/assets/partials/meeting/create.html'
            })
            .when('/meeting/edit', {
              templateUrl: '/assets/partials/meeting/edit.html'
            })
            .when('/login', {
              templateUrl: '/assets/partials/login/login.html',
              public: true,
              login: true
            })
            .when('/signup', {
              templateUrl: '/assets/partials/login/signup.html',
              public: true
            })
            .otherwise({redirectTo: '/'})
@commonModule = angular.module('bmApp.common', [])
@controllersModule = angular.module('bmApp.controllers', ['ngResource'])
@servicesModule = angular.module('bmApp.services', [])
@factoriesModule = angular.module('bmApp.factories', [])
@modelsModule = angular.module('bmApp.models', [])
@directivesModule = angular.module('bmApp.directives', [])
@filtersModule = angular.module('bmApp.filters', [])
@localStorageModule = angular.module('bmApp.localStorage', ['LocalStorageModule'])
@animationsModule = angular.module('bmApp.animations', ['ngAnimate']);