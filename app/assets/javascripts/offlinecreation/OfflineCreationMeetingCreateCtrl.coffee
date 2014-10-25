
class OfflineCreationMeetingCreateCtrl

  @meeting = {}

  constructor: (@$log, @$location, @$routeParams, @OfflineCreationMeetingService, @localStorageService) ->
    @$log.debug "OfflineCreationMeetingCreateCtrl.constructor()"
    idParam = @$routeParams.id

    if idParam == undefined
      @initializeNewMeeting()
      return false

    if idParam.length == 24
      @OfflineCreationMeetingService.getMeeting(idParam)
      .then(
        (data) =>
          @$log.debug "Promise returned #{data.length} Meetings"
          @meeting = data
          @meeting.remote = true
      ,
      (error) =>
        @$log.error "Unable to get Meetings: #{error}"
      )
    else
      @meeting = @localStorageService.get(idParam)

  initializeNewMeeting: () ->
    @$log.debug "OfflineCreationMeetingCreateCtrl.initializeNewMeeting()"
    actualTime = Date.now()
    meeting = {
      _id: {
        oid: actualTime
      }
      date: "16.10.2014 16:30",
      goal: "Test",
      organizer: "Test",
      created: actualTime,
      updated: actualTime,
      remote: false,
      attendees: [""]
    }
    @saveLocalMeeting(meeting)
    @$location.path("/offlinecreation/create").search(id: meeting._id.oid).replace();
    return false

  saveLocalMeeting: (meeting) ->
    @$log.debug "OfflineCreationMeetingCreateCtrl.saveLocalMeeting()"
    @localStorageService.set(meeting._id.oid, meeting)
    localMeetings = @localStorageService.get("localMeetings")
    if localMeetings
      localMeetings.push(meeting._id.oid)
    else
      localMeetings = [meeting._id.oid]
    @localStorageService.set("localMeetings", localMeetings)

  flush: () ->
    @$log.debug "OfflineCreationMeetingCreateCtrl.flush()"
    @localStorageService.clearAll()

  publishMeeting: () ->
    @$log.debug "OfflineCreationMeetingCreateCtrl.publishMeeting()"
    toPublish = {
      date: @meeting.date,
      goal: @meeting.goal,
      organizer: @meeting.organizer,
      created: @meeting.created,
      updated: @meeting.updated,
      attendees: @meeting.attendees
    }
    if @meeting._id.oid.length ==24
      toPublish._id = {}
      toPublish._id.oid = @meeting._id.oid
      @OfflineCreationMeetingService.putMeeting(toPublish)
      .then(
        (data) =>
          @$log.debug "Promise returned #{data} Meeting"
          @$location.path("/offlinecreation/list")
      ,
      (error) =>
        @$log.error "Unable to update Meeting: #{error}"
      )
    else
      @OfflineCreationMeetingService.postMeeting(toPublish)
      .then(
        (data) =>
          @$log.debug "Promise returned #{data} Meeting"
          @removeLocalMeeting(@meeting._id.oid)
          @$location.path("/offlinecreation/list")
      ,
      (error) =>
        @$log.error "Unable to create Meeting: #{error}"
      )

  removeLocalMeeting: (meetingId) ->
    @$log.debug "OfflineCreationMeetingCreateCtrl.removeLocalMeeting("  + meetingId +")"
    localMeetings = @localStorageService.get("localMeetings")
    index = localMeetings.indexOf(meetingId);
    if index > -1
      localMeetings.splice(index, 1)
    @localStorageService.set("localMeetings", localMeetings)
    @localStorageService.remove(meetingId);

  updateDatabase: () ->
    @$log.debug "OfflineCreationMeetingCreateCtrl.updateDatabase()"
    @meeting.updated = Date.now()
    @localStorageService.set(@meeting._id.oid, @meeting)

  addAttendee: () ->
    @$log.debug "OfflineCreationMeetingCreateCtrl.updateDatabase()"
    @meeting.attendees.push("")
    @updateDatabase()

  removeAttendee: (index) ->
    @$log.debug "OfflineCreationMeetingCreateCtrl.updateDatabase()"
    @meeting.attendees.splice(index, 1)
    @updateDatabase()

controllersModule.controller('OfflineCreationMeetingCreateCtrl', OfflineCreationMeetingCreateCtrl)