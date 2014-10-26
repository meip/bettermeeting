
class AuthenticationService

  constructor: (@$log, @Base64, @$http, @$cookieStore, @rootScope) ->
    @$log.debug "LoginCtrl.constructor()"

  Login: (username, password) ->
    return @$http.post('/api/authenticate', { username: username, password: password })

  SetCredentials: (username, password) ->
    authData = username + ':' + password
    $rootScope.globals = {
      currentUser: {
        username: username,
        authdata: authData
      }
    }
    @$http.defaults.headers.common['Authorization'] = authData
    @$cookieStore.put('globals', $rootScope.globals)

  ClearCredentials: () ->
    delete @$rootScope.globals
    @$cookieStore.remove('globals')
    @$http.defaults.headers.common.Authorization = ''

factoriesModule.factory('AuthenticationService', AuthenticationService)