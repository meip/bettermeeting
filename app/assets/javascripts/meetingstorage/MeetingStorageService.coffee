
class MeetingStorageService

  @headers = {'Accept': 'application/json', 'Content-Type': 'application/json'}
  @defaultConfig = { headers: @headers }

  constructor: (@$log, @$http, @$q, @localStorageService, @$location) ->
    @$log.debug "MeetingStorageService.constructor()"

  meetingFactory: (_id) ->
    @$log.debug "meetingFactory()"

    if @localStorageService.isSupported
      if _id == undefined
        @initializeMeeting()
      else
        localMeeting = @localStorageService.get(_id)
        return localMeeting
    else
      @$log.debug "Storage not Supported"
      return @createEmptyMeeting()

  initializeMeeting: () ->
    @$log.debug "initializeMeeting()"
    meeting = @createEmptyMeeting()

    @localStorageService.set(meeting._id, meeting)

    @saveMeetingLocal(meeting)

    @$location.search(id: meeting._id)

  saveMeetingLocal: (meeting) ->
    localMeetings = @localStorageService.get("localMeetings")
    if localMeetings
      localMeetings.push(meeting._id)
    else
      localMeetings = [meeting._id]
    @localStorageService.set("localMeetings", localMeetings)

  removeMeetingLocal: (_id) ->
    @localStorageService.remove(_id)
    localMeetings = @localStorageService.get("localMeetings")
    index = localMeetings.indexOf(_id);
    if index > -1
      localMeetings.splice(index, 1)
    @localStorageService.set("localMeetings", localMeetings)


  createEmptyMeeting: () ->
    actualTime = Date.now()
    meeting = {
      _id: actualTime,
      goal: "",
      date: "16.12.2014 16:30",
      organizer: "",
      lastEdited: actualTime,
      published: false,
      attendees: [
        ""
      ]

    }
    return meeting

  set: (_id, meeting) ->
    @$log.info "Save Meeting-_id: " + _id
    meeting.lastEdited = Date.now()
    if @localStorageService.isSupported
      @localStorageService.set(_id, meeting)

  flush: () ->
    @$log.info "Delete all Entries in LocalDB"
    @localStorageService.clearAll()

  publishMeeting: (meeting) ->
    @$log.info "publishMeeting #{angular.toJson(meeting, true)}"
    deferred = @$q.defer()

    if meeting.published
      @publishChangedMeeting(meeting, deferred)
    else
      @publishNewMeeting(meeting, deferred)

    deferred.promise

  publishNewMeeting: (meeting, deferred) ->
    @$log.debug "MeetingStorageService.publishNewMeeting()"

    meeting.published = true

    @$http.post('/api/meetings', meeting)
    .success((data, status, user) =>
      @$log.info("Successfully created Meeting - status #{status}")
      deferred.resolve(data)
      @removeMeetingLocal(meeting._id)
    )
    .error((data, status, headers) =>
      @$log.error("Failed to create meeting - status #{status}")
      deferred.reject(data);
    )

  publishChangedMeeting: (meeting, deferred) ->
    @$log.debug "MeetingStorageService.publishChangedMeeting()"

    @$http.put('/api/meetings', meeting)
    .success((data, status, user) =>
      @$log.info("Successfully changed Meeting - status #{status}")
      deferred.resolve(data)
    )
    .error((data, status, headers) =>
      @$log.error("Failed to changed meeting - status #{status}")
      deferred.reject(data);
    )

  getLocalMeetings: () ->
    @$log.debug "MeetingStorageService.getLocalMeetings()"

    localMeetingIndexes = @localStorageService.get("localMeetings")
    localMeetings = []

    for index in localMeetingIndexes
      localMeetings.push(@localStorageService.get(index))

    return localMeetings

  getRemoteMeetings: () ->
    @$log.debug "MeetingStorageService.getRemoteMeetings()"
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

  getRemoteMeeting: (_id) ->
    @$log.debug "MeetingStorageService.getRemoteMeeting(_id)"
    deferred = @$q.defer()

    @$http.get("/api/meetings/" )
    .success((data, status, headers) =>
      @$log.info("Successfully listed Meetings - status #{status}")
      deferred.resolve(data)
    )
    .error((data, status, headers) =>
      @$log.info("Failed to list Meetings - status #{status}")
      deferred.resolve(data);
    )
    deferred.promise



servicesModule.service('MeetingStorageService', MeetingStorageService)