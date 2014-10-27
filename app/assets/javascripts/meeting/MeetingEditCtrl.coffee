
class MeetingEditCtrl extends MeetingBase



  constructor: (@$log, @$location, @$routeParams, @MeetingService, @localStorageService) ->
    @$log.debug "MeetingEditCtrl.constructor()"
    idParam = @$routeParams.id

    if idParam.length != 24
      @$location.path("/meeting/create").replace();

    @MeetingService.getMeeting(idParam)
    .then(
      (data) =>
        @$log.debug "Promise returned #{data.length} Meetings"
        @meeting = data
    ,
    (error) =>
      @$log.error "Unable to get Meetings: #{error}"
    )

  publishMeeting: () ->
    @$log.debug "MeetingEditCtrl.publishMeeting()"
    toPublish = {
      _id: @meeting._id,
      date: @meeting.date,
      goal: @meeting.goal,
      organizer: @meeting.organizer,
      color: @meeting.color,
      created: @meeting.created,
      updated: @meeting.updated,
      attendees: @meeting.attendees,
      meetingPoints: @meeting.meetingPoints
    }
    @MeetingService.putMeeting(toPublish)
    .then(
      (data) =>
        @$log.debug "Promise returned #{data} Meeting"
    ,
    (error) =>
      @$log.error "Unable to update Meeting: #{error}"
    )

  removeMeeting: (meetingId, forwardAfter) ->
    @$log.debug "MeetingEditCtrl.removeMeeting("  + meetingId + ")"
    @MeetingService.removeMeeting(meetingId)
    .then(
      (data) =>
        @$log.debug "Deleted #{data} Meeting"
        @$location.path("/")
    ,
    (error) =>
      @$log.error "Unable to delete Meeting: #{error}"
    )

controllersModule.controller('MeetingEditCtrl', MeetingEditCtrl)
