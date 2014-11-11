
class ToDoService

  @headers = {'Accept': 'application/json', 'Content-Type': 'application/json'}
  @defaultConfig = { headers: @headers }

  constructor: (@$log, @$http, @$q, @$location) ->
    @$log.debug "ToDoService.constructor()"

  checkLogin: (status) ->
    if(status == 401)
      @$log.info "Not logged in"
      @$location.path("/login");

  getActionPoints: () ->
    @$log.debug "MeetingService.getRemoteMeetings()"
    deferred = @$q.defer()

    @$http.get("/api/user/actionpoints")
    .success((data, status, headers) =>
      @$log.info("Successfully listed ActionPoints - status #{status}")
      deferred.resolve(data, status, headers)
    )
    .error((data, status, headers) =>
      @checkLogin(status)
      @$log.info("Failed to list ActionPoints - status #{status}")
      deferred.resolve(data);
    )
    deferred.promise



servicesModule.service('ToDoService', ToDoService)