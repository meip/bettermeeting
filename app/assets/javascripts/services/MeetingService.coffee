
class MeetingService

  @headers = {'Accept': 'application/json', 'Content-Type': 'application/json'}
  @defaultConfig = { headers: @headers }

  constructor: (@$log, @$http, @$q, @$location) ->
    @$log.debug "MeetingService.constructor()"

  checkLogin: (status) ->
    if(status == 401)
      @$log.info "Not logged in"
      @$location.path("/login");

  getMeeting: (meetingId) ->
    @$log.debug "MeetingService.getRemoteMeeting(meetingId)"
    deferred = @$q.defer()

    @$http.get("/api/meetings/" + meetingId)
    .success((data, status, headers) =>
      @$log.info("Successfully listed Meeting - status #{status}")
      deferred.resolve(data)
    )
    .error((data, status, headers) =>
      @checkLogin(status)
      @$log.info("Failed to list Meeting - status #{status}")
      deferred.reject(data);
    )
    deferred.promise

  postMeeting: (meeting) ->
    @$log.debug "MeetingService.postMeeting #{angular.toJson(meeting, true)}"
    deferred = @$q.defer()

    @$http.post('/api/meetings', meeting)
    .success((data, status, headers) =>
      @$log.info("Successfully created Meeting - status #{status}")
      deferred.resolve(data)
    )
    .error((data, status, headers) =>
      @checkLogin(status)
      @$log.error("Failed to create Meeting - status #{status}")
      deferred.reject(data);
    )
    deferred.promise

  putMeeting: (meeting) ->
    @$log.debug "MeetingService.putMeeting #{angular.toJson(meeting, true)}"
    deferred = @$q.defer()

    @$http.put('/api/meetings/' + meeting._id.oid, meeting)
    .success((data, status, headers) =>
      @$log.info("Successfully updated Meeting - status #{status}")
      deferred.resolve(data)
    )
    .error((data, status, headers) =>
      @checkLogin(status)
      @$log.error("Failed to update Meeting - status #{status}")
      deferred.reject(data);
    )
    deferred.promise

  removeMeeting: (meetingId) ->
    @$log.debug "MeetingService.removeMeeting #{meetingId}"
    deferred = @$q.defer()

    @$http.delete('/api/meetings/' + meetingId)
    .success((data, status, headers) =>
      @$log.info("Successfully deleted Meeting - status #{status}")
      deferred.resolve(data)
    )
    .error((data, status, headers) =>
      @checkLogin(status)
      @$log.error("Failed to delete Meeting - status #{status}")
      deferred.reject(data);
    )
    deferred.promise

  getRemoteMeetings: () ->
    @$log.debug "MeetingService.getRemoteMeetings()"
    deferred = @$q.defer()

    @$http.get("/api/meetings")
    .success((data, status, headers) =>
      @$log.info("Successfully listed Meetings - status #{status}")
      deferred.resolve(data, status, headers)
    )
    .error((data, status, headers) =>
      @checkLogin(status)
      @$log.info("Failed to list Meetings - status #{status}")
      deferred.resolve(data);
    )
    deferred.promise

servicesModule.service('MeetingService', MeetingService)