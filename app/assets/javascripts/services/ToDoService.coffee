
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

  putTodo: (todo) ->
    @$log.debug "ToDoService.putTodo #{angular.toJson(todo, true)}"
    deferred = @$q.defer()

    @$http.put('/api/actionpoint/' + todo._id.$oid, todo)
    .success((data, status, headers) =>
      @$log.info("Successfully updated Action Point - status #{status}")
      deferred.resolve(data)
    )
    .error((data, status, headers) =>
      @checkLogin(status)
      @$log.error("Failed to update Action Point - status #{status}")
      deferred.reject(data);
    )
    deferred.promise

servicesModule.service('ToDoService', ToDoService)