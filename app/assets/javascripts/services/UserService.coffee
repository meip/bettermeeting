
class UserService

  @headers = {'Accept': 'application/json', 'Content-Type': 'application/json'}
  @defaultConfig = { headers: @headers }

  constructor: (@$log, @$http, @$q) ->
    @$log.debug "UserService.constructor()"

  loginUser: (username, password) ->
    @$log.debug "UserService.loginUser(username, password)"
    deferred = @$q.defer()

    @$http.get("/api/user/login?username=" + username + "&password=" + password)
    .success((data, status, headers) =>
      @$log.info("Successfully logged in - status #{status}")
      deferred.resolve(data)
    )
    .error((data, status, headers) =>
      @$log.info("Failed to login - status #{status}")
      deferred.reject(data);
    )
    deferred.promise

servicesModule.service('UserService', UserService)