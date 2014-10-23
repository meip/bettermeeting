
class MeetingStorageService

  @headers = {'Accept': 'application/json', 'Content-Type': 'application/json'}
  @defaultConfig = { headers: @headers }

  constructor: (@$log, @$http, @$q) ->
    @$log.debug "constructing MeetingService"

  listMeetings: () ->
    @$log.debug "listMeetings()"
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

  createMeeting: (meeting) ->
    @$log.debug "createMeeting #{angular.toJson(meeting, true)}"
    deferred = @$q.defer()

    @$http.post('/api/meetings', meeting)
    .success((data, status, user) =>
      @$log.info("Successfully created Meeting - status #{status}")
      deferred.resolve(data)
    )
    .error((data, status, headers) =>
      @$log.error("Failed to create meeting - status #{status}")
      deferred.reject(data);
    )
    deferred.promise

servicesModule.service('MeetingStorageService', MeetingStorageService)