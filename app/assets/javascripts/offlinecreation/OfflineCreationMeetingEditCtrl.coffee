
class OfflineCreationMeetingEditCtrl extends OfflineCreationMeeting



  constructor: (@$log, @$location, @$routeParams, @OfflineCreationMeetingService, @localStorageService) ->
    @$log.debug "OfflineCreationMeetingEditCtrl.constructor()"
    idParam = @$routeParams.id

    if idParam.length != 24
      @$location.path("/offlinecreation/create").replace();

    @OfflineCreationMeetingService.getMeeting(idParam)
    .then(
      (data) =>
        @$log.debug "Promise returned #{data.length} Meetings"
        @meeting = data
    ,
    (error) =>
      @$log.error "Unable to get Meetings: #{error}"
    )

  saveMeeting: () ->
    @$log.debug "OfflineCreationMeetingEditCtrl.publishMeeting()"
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
    @OfflineCreationMeetingService.putMeeting(toPublish)
    .then(
      (data) =>
        @$log.debug "Promise returned #{data} Meeting"
    ,
    (error) =>
      @$log.error "Unable to update Meeting: #{error}"
    )

  removeMeeting: (meetingId, forwardAfter) ->
    @$log.debug "OfflineCreationMeetingEditCtrl.removeMeeting("  + meetingId + ")"
    @OfflineCreationMeetingService.removeMeeting(meetingId)
    .then(
      (data) =>
        @$log.debug "Deleted #{data} Meeting"
        @$location.path("/")
    ,
    (error) =>
      @$log.error "Unable to delete Meeting: #{error}"
    )

controllersModule.controller('OfflineCreationMeetingEditCtrl', OfflineCreationMeetingEditCtrl)
