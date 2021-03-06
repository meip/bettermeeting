class UserControlService

  @headers = {'Accept': 'application/json', 'Content-Type': 'application/json'}
  @defaultConfig = { headers: @headers }

  constructor: (@$log, @$http, @$q, @$location) ->
    @$log.debug "UserControlService.constructor()"

  checkLogin: (status) ->
    if(status == 401)
      @$log.info "Not logged in"
      @$location.path("/login");

  loginUser: (username, password) ->
    @$log.debug "UserControlService.loginUser(username, password)"
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

  logoutUser: () ->
    @$log.debug "UserControlService.logoutUser()"
    deferred = @$q.defer()

    @$http.get("/api/user/logout")
    .success((data, status, headers) =>
      @$log.info("Successfully logged out - status #{status}")
      deferred.resolve(data)
    )
    .error((data, status, headers) =>
      @$log.info("Failed to logout - status #{status}")
      deferred.reject(data);
    )
    deferred.promise

  signupUser: (user) ->
    @$log.debug "UserService.signupUser()"
    deferred = @$q.defer()

    @$http.post('/api/user', user)
    .success((data, status, headers) =>
      @$log.info("Successfully created User - status #{status}")
      deferred.resolve(data)
    )
    .error((data, status, headers) =>
      @checkLogin(status)
      @$log.error("Failed to create User - status #{status}")
      deferred.reject(data);
    )
    deferred.promise

  setShowIntro: (showIntro) ->
    @$log.debug "UserControlService.setShowIntro()"
    deferred = @$q.defer()

    @$http.put('/api/user/intro', {
      "showIntro": showIntro
    })
    .success((data, status, headers) =>
      @$log.info("Successfully updated showIntro for User - status #{status}")
      deferred.resolve(data)
    )
    .error((data, status, headers) =>
      @checkLogin(status)
      @$log.error("Failed to update showIntro for User - status #{status}")
      deferred.reject(data);
    )
    deferred.promise

  getAllUsers: () ->
    @$log.debug "UserControlService.getAllUsers()"
    deferred = @$q.defer()

    @$http.get("/api/users")
    .success((data, status, headers) =>
      @$log.info("Successfully listed Users - status #{status}")
      deferred.resolve(data, status, headers)
    )
    .error((data, status, headers) =>
      @checkLogin(status)
      @$log.info("Failed to list Users - status #{status}")
      deferred.resolve(data);
    )
    deferred.promise

  getActualUser: () ->
    @$log.debug "UserControlService.getActualUser()"
    deferred = @$q.defer()

    @$http.get("/api/user/profile")
    .success((data, status, headers) =>
      @$log.info("Successfully listed User - status #{status}")
      deferred.resolve(data)
    )
    .error((data, status, headers) =>
      @checkLogin(status)
      @$log.info("Failed to list User - status #{status}")
      deferred.reject(data);
    )
    deferred.promise



servicesModule.service('UserControlService', UserControlService)