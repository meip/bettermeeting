
dependencies = [
    'ngRoute',
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
            .when('/meeting/note', {
                templateUrl: '/assets/partials/meeting/note.html'
            })
            .when('/users', {
                templateUrl: '/assets/partials/users/list.html'
            })
            .when('/users/create', {
                templateUrl: '/assets/partials/users/create.html'
            })
            .when('/meeting-offline/create', {
                templateUrl: '/assets/partials/meeting/edit.html'
            })
            .when('/meeting-offline/edit', {
              templateUrl: '/assets/partials/meeting/edit.html'
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
            .otherwise({redirectTo: '/'})
@commonModule = angular.module('bmApp.common', [])
@controllersModule = angular.module('bmApp.controllers', [])
@servicesModule = angular.module('bmApp.services', [])
@factoriesModule = angular.module('bmApp.factories', [])
@modelsModule = angular.module('bmApp.models', [])
@directivesModule = angular.module('bmApp.directives', [])
@filtersModule = angular.module('bmApp.filters', [])
@localStorageModule = angular.module('bmApp.localStorage', ['LocalStorageModule'])
@animationsModule = angular.module('bmApp.animations', ['ngAnimate']);