
class OfflineMeetingCreateCtrl

  @meeting = {}

  constructor: (@$log, @$location, @$routeParams, @OfflineMeetingService, @localStorageService) ->
    @$log.debug "OfflineMeetingCreateCtrl.constructor()"
    idParam = @$routeParams.id

    @$log.info 1
    if idParam == undefined
      @$log.info 2
      @meeting = @initializeNewMeeting()
      return false

    @$log.info 3

    if idParam.length == 24
      @OfflineMeetingService.getMeeting(idParam)
      .then(
        (data) =>
          @$log.debug "Promise returned #{data.length} Meetings"
          @$log.info 7
          remoteMeeting = data
          localMeeting = @localStorageService.get(remoteMeeting._id.oid)
          if localMeeting != null
            @$log.info 10
            @meeting = @mergeMeetings(remoteMeeting, localMeeting)
            @updateDatabase()
            @$log.info 11
          else
            @$log.info 8
            @saveMeetingLocal(remoteMeeting)
            @meeting = remoteMeeting
            @$log.info 9
      ,
      (error) =>
        @$log.error "Unable to get Meetings: #{error}"
        @$log.info 4 + "Kein Internet"

      )
    else
      @$log.info 4
      @meeting = @localStorageService.get(idParam)


  mergeMeetings: (remoteMeeting, localMeeting) ->
    @$log.debug "OfflineMeetingCreateCtrl.mergeMeetings()"
    if remoteMeeting.updated >= localMeeting.updated
      @$log.info "Remote neuer"
      return remoteMeeting
    else
      @$log.info "Lokal neuer"
    return localMeeting

  initializeNewMeeting: () ->
    @$log.debug "OfflineMeetingCreateCtrl.initializeNewMeeting()"
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
      attendees: [""]
    }
    @saveMeetingLocal(meeting)
    @$location.path("/offline/create").search(id: meeting._id.oid).replace();
    return false

  saveMeetingLocal: (meeting) ->
    @$log.debug "OfflineMeetingCreateCtrl.saveMeetingLocal()"
    @localStorageService.set(meeting._id.oid, meeting)
    localMeetings = @localStorageService.get("localMeetings")
    if localMeetings
      localMeetings.push(meeting._id.oid)
    else
      localMeetings = [meeting._id.oid]
    @localStorageService.set("localMeetings", localMeetings)

  flush: () ->
    @$log.debug "OfflineMeetingCreateCtrl.flush()"
    @localStorageService.clearAll()

  publishMeeting: () ->
    @$log.debug "OfflineMeetingCreateCtrl.publishMeeting()"
    toPublish = {
      date: @meeting.date,
      goal: @meeting.goal,
      organizer: @meeting.organizer,
      created: @meeting.created,
      updated: @meeting.updated,
      attendees: @meeting.attendees
    }
    if(@meeting._id.oid.length == 24)
      toPublish._id = {}
      toPublish._id.oid = @meeting._id.oid
      @$log.info "ID wurde hinzugefügt"
      @OfflineMeetingService.putMeeting(toPublish)
      .then(
        (data) =>
          @$log.debug "Promise returned #{data} Meeting"
          @$location.path("/offline/list")
      ,
      (error) =>
        @$log.error "Unable to update Meeting: #{error}"
      )


    else
      @OfflineMeetingService.postMeeting(toPublish)
      .then(
        (data) =>
          @$log.debug "Promise returned #{data} Meeting"
          localMeetings = @localStorageService.get("localMeetings")
          index = localMeetings.indexOf(@meeting._id.oid);
          if index > -1
            localMeetings.splice(index, 1)
          @localStorageService.set("localMeetings", localMeetings)
          @localStorageService.remove(@meeting._id.oid);
          @$location.path("/offline/list")
      ,
      (error) =>
        @$log.error "Unable to create Meeting: #{error}"
      )

  updateDatabase: () ->
    @$log.debug "OfflineMeetingCreateCtrl.updateDatabase()"
    @meeting.updated = Date.now()
    @localStorageService.set(@meeting._id.oid, @meeting)

  addAttendee: () ->
    @$log.debug "OfflineMeetingCreateCtrl.updateDatabase()"
    @meeting.attendees.push("")
    @updateDatabase()

  removeAttendee: (index) ->
    @$log.debug "OfflineMeetingCreateCtrl.updateDatabase()"
    @meeting.attendees.splice(index, 1)
    @updateDatabase()

controllersModule.controller('OfflineMeetingCreateCtrl', OfflineMeetingCreateCtrl)