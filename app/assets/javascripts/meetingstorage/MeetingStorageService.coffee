
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
        return @localStorageService.get(_id)
    else
      @$log.debug "Storage not Supported"
      return @createEmptyMeeting()

  initializeMeeting: () ->
    @$log.debug "initializeMeeting()"
    meeting = @createEmptyMeeting()

    @localStorageService.set(meeting._id, meeting)

    @saveMeetingLocal(meeting)

    @$location.search(_id: meeting._id)

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
    @$log.debug "Save Meeting-ID: " + _id
    meeting.lastEdited = Date.now()
    if @localStorageService.isSupported
      @localStorageService.set(_id, meeting)

  flush: () ->
    @$log.debug "Delete all Entries in DB"
    @localStorageService.clearAll()

  publishMeeting: (meeting) ->
    @$log.debug "publishMeeting #{angular.toJson(meeting, true)}"
    deferred = @$q.defer()

    if meeting.published
      @publishChangedMeeting(meeting, deferred)
    else
      @publishNewMeeting(meeting, deferred)

    deferred.promise

  publishNewMeeting: (meeting, deferred) ->
    @$log.debug "publish New Meeting"

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
    @$log.debug "publish Changed Meeting"

    @$http.put('/api/meetings', meeting)
    .success((data, status, user) =>
      @$log.info("Successfully changed Meeting - status #{status}")
      deferred.resolve(data)
    )
    .error((data, status, headers) =>
      @$log.error("Failed to changed meeting - status #{status}")
      deferred.reject(data);
    )

servicesModule.service('MeetingStorageService', MeetingStorageService)