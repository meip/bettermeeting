
class OfflineCreationMeetingService

  @headers = {'Accept': 'application/json', 'Content-Type': 'application/json'}
  @defaultConfig = { headers: @headers }

  constructor: (@$log, @$http, @$q) ->
    @$log.debug "OfflineCreationMeetingService.constructor()"

  getMeeting: (meetingId) ->
    @$log.debug "OfflineCreationMeetingService.getRemoteMeeting(meetingId)"
    deferred = @$q.defer()

    @$http.get("/api/meetings/" + meetingId)
    .success((data, status, headers) =>
      @$log.info("Successfully listed Meeting - status #{status}")
      deferred.resolve(data)
    )
    .error((data, status, headers) =>
      @$log.info("Failed to list Meeting - status #{status}")
      deferred.reject(data);
    )
    deferred.promise

  postMeeting: (meeting) ->
    @$log.debug "OfflineCreationMeetingService.postMeeting #{angular.toJson(meeting, true)}"
    deferred = @$q.defer()

    @$http.post('/api/meetings', meeting)
    .success((data, status, headers) =>
      @$log.info("Successfully created Meeting - status #{status}")
      deferred.resolve(data)
    )
    .error((data, status, headers) =>
      @$log.error("Failed to create Meeting - status #{status}")
      deferred.reject(data);
    )
    deferred.promise

  putMeeting: (meeting) ->
    @$log.debug "OfflineCreationMeetingService.putMeeting #{angular.toJson(meeting, true)}"
    deferred = @$q.defer()

    @$http.put('/api/meetings/' + meeting._id.oid, meeting)
    .success((data, status, headers) =>
      @$log.info("Successfully updated Meeting - status #{status}")
      deferred.resolve(data)
    )
    .error((data, status, headers) =>
      @$log.error("Failed to update Meeting - status #{status}")
      deferred.reject(data);
    )
    deferred.promise

  removeMeeting: (meetingId) ->
    @$log.debug "OfflineCreationMeetingService.removeMeeting #{meetingId}"
    deferred = @$q.defer()

    @$http.delete('/api/meetings/' + meetingId)
    .success((data, status, headers) =>
      @$log.info("Successfully deleted Meeting - status #{status}")
      deferred.resolve(data)
    )
    .error((data, status, headers) =>
      @$log.error("Failed to delete Meeting - status #{status}")
      deferred.reject(data);
    )
    deferred.promise

  getRemoteMeetings: () ->
    @$log.debug "OfflineCreationMeetingService.getRemoteMeetings()"
    deferred = @$q.defer()

    @$http.get("/api/meetings")
    .success((data, status, headers) =>
      @$log.info("Successfully listed Meetings - status #{status}")
      deferred.resolve(data)
    )
    .error((data, status, headers) =>
      @$log.info("Failed to list Meetings - status #{status}")
      deferred.resolve(data);
    )
    deferred.promise

servicesModule.service('OfflineCreationMeetingService', OfflineCreationMeetingService)