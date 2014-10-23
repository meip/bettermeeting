
class MeetingStorageService

  @headers = {'Accept': 'application/json', 'Content-Type': 'application/json'}
  @defaultConfig = { headers: @headers }

  constructor: (@$log, @$http, @$q, @localStorageService, @$location) ->
    @$log.debug "MeetingStorageService.constructor()"

  meetingFactory: (@id) ->
    @$log.debug "meetingFactory()"

    if @localStorageService.isSupported
      if @id == undefined
        @initializeMeeting()
      else
        return @localStorageService.get(@id)
    else
      @$log.debug "Storage not Supported"
      return @createEmptyMeeting()

  initializeMeeting: () ->
    @$log.debug "initializeMeeting()"
    @id = @createEmptyMeeting()

    @localStorageService.set(@id, @meeting)

    @saveMeetingLocal()

    @$location.search(id: @id)

  saveMeetingLocal: () ->
    @localMeetings = @localStorageService.get("localMeetings")
    if @localMeetings
      @localMeetings.push(@id)
    else
      @localMeetings = [@id]
    @localStorageService.set("localMeetings", @localMeetings)

  createEmptyMeeting: () ->
    @id = Date.now()
    @meeting = {
      id: @id,
      goal: "",
      time: "",
      lastEdited: Date.now(),
      published: false
    }
    return @id

  set: (@id, @meeting) ->
    @$log.debug "Save Meeting-ID: " + @id
    @meeting.lastEdited = Date.now()
    if @localStorageService.isSupported
      @localStorageService.set(@id, @meeting)

  flush: () ->
    @$log.debug "Delete all Entries in DB"
    @localStorageService.clearAll()


servicesModule.service('MeetingStorageService', MeetingStorageService)