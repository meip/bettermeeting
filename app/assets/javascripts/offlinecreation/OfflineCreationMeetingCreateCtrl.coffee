
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
    colors = ["#1ABC9C", "#2ECC71", "#3498DB", "#9B59B6", "#F39C12", "#E74C3C"]
    meeting = {
      _id: {
        oid: actualTime
      }
      date: "16.10.2014 16:30",
      goal: "",
      organizer: "r1bader@hsr.ch",
      color: colors[Math.floor(Math.random()*colors.length)],
      created: actualTime,
      updated: actualTime,
      attendees: [""],
      meetingPoints: [
        subject: "",
        lastEditor: "",
        owner: "r1bader@hsr.ch",
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
      color: @meeting.color,
      created: @meeting.created,
      updated: @meeting.updated,
      attendees: @meeting.attendees,
      meetingPoints: @meeting.meetingPoints
    }

    @OfflineCreationMeetingService.postMeeting(toPublish)
    .then(
      (data) =>
        @$log.debug "Promise returned #{data} Meeting"
        @removeMeeting(@meeting._id.oid, false)
        @$location.path("/")
    ,
    (error) =>
      @$log.error "Unable to create Meeting: #{error}"
    )

  removeMeeting: (meetingId, forwardAfter) ->
    @$log.debug "OfflineCreationMeetingCreateCtrl.removeMeeting("  + meetingId + ")"
    localMeetings = @localStorageService.get("localMeetings")
    index = localMeetings.indexOf(meetingId);
    if index > -1
      localMeetings.splice(index, 1)
    @localStorageService.set("localMeetings", localMeetings)
    @localStorageService.remove(meetingId);
    if forwardAfter
      @$location.path("/")






controllersModule.controller('OfflineCreationMeetingCreateCtrl', OfflineCreationMeetingCreateCtrl)
