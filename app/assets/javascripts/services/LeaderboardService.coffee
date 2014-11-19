
class LeaderboardService

  @headers = {'Accept': 'application/json', 'Content-Type': 'application/json'}
  @defaultConfig = { headers: @headers }

  constructor: (@$log, @$http, @$q, @$location) ->
    @$log.debug "LeaderboardService.constructor()"

  checkLogin: (status) ->
    if(status == 401)
      @$log.info "Not logged in"
      @$location.path("/login");

  getLeaderboard: () ->
    @$log.debug "LeaderboardService.getLeaderboard()"
    deferred = @$q.defer()

    @$http.get("/api/leaderboard/leaderBoard")
    .success((data, status, headers) =>
      @$log.info("Successfully listed Leaderboard - status #{status}")
      deferred.resolve(data, status, headers)
    )
    .error((data, status, headers) =>
      @checkLogin(status)
      @$log.info("Failed to list Leaderboard - status #{status}")
      deferred.resolve(data);
    )
    deferred.promise

  getCountOrganizer: () ->
    @$log.debug "LeaderboardService.getCountOrganizer()"
    deferred = @$q.defer()

    @$http.get("/api/leaderboard/countOrganizer")
    .success((data, status, headers) =>
      @$log.info("Successfully listed Count Organizer - status #{status}")
      deferred.resolve(data, status, headers)
    )
    .error((data, status, headers) =>
      @checkLogin(status)
      @$log.info("Failed to list Count Organizer - status #{status}")
      deferred.resolve(data);
    )
    deferred.promise

  getCountAttendees: () ->
    @$log.debug "LeaderboardService.getCountAttendees()"
    deferred = @$q.defer()

    @$http.get("/api/leaderboard/countAttendees")
    .success((data, status, headers) =>
      @$log.info("Successfully listed Count Attendees - status #{status}")
      deferred.resolve(data, status, headers)
    )
    .error((data, status, headers) =>
      @checkLogin(status)
      @$log.info("Failed to list Count Attendees - status #{status}")
      deferred.resolve(data);
    )
    deferred.promise

  getCountActionpoints: () ->
    @$log.debug "LeaderboardService.getCountActionpoints()"
    deferred = @$q.defer()

    @$http.get("/api/leaderboard/countActionpoints")
    .success((data, status, headers) =>
      @$log.info("Successfully listed Count Action Points - status #{status}")
      deferred.resolve(data, status, headers)
    )
    .error((data, status, headers) =>
      @checkLogin(status)
      @$log.info("Failed to list Count Action Points - status #{status}")
      deferred.resolve(data);
    )
    deferred.promise

servicesModule.service('LeaderboardService', LeaderboardService)