
class OfflineCreationMeetingCreateCtrl extends OfflineCreationMeeting

  constructor: (@$log, @$location, @$routeParams, @OfflineCreationMeetingService, @localStorageService) ->
    @$log.debug "OfflineCreationMeetingCreateCtrl.constructor()"
    idParam = @$routeParams.id

    if idParam == undefined
      @initializeNewMeeting()
      return false

    if idParam.length == 24
      @$location.path("/offlinecreation/edit").replace();
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
      goal: "",
      organizer: "r1bader@hsr.ch",
      created: actualTime,
      remote: false,
      attendees: [""],
      meetingPoints: [
        subject: "",
        lastEditor: "",
        owner: "",
        dueDate: "16.10.2014 16:30",
        pointType: "info"
      ]
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
      attendees: @meeting.attendees,
      meetingPoints: @meeting.meetingPoints
    }

    @OfflineCreationMeetingService.postMeeting(toPublish)
    .then(
      (data) =>
        @$log.debug "Promise returned #{data} Meeting"
        @removeLocalMeeting(@meeting._id.oid)
        @$location.path("/")
    ,
    (error) =>
      @$log.error "Unable to create Meeting: #{error}"
    )

  removeLocalMeeting: (meetingId) ->
    @$log.debug "OfflineCreationMeetingCreateCtrl.removeLocalMeeting("  + meetingId + ")"
    localMeetings = @localStorageService.get("localMeetings")
    index = localMeetings.indexOf(meetingId);
    if index > -1
      localMeetings.splice(index, 1)
    @localStorageService.set("localMeetings", localMeetings)
    @localStorageService.remove(meetingId);



controllersModule.controller('OfflineCreationMeetingCreateCtrl', OfflineCreationMeetingCreateCtrl)
