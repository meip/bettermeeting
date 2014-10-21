
class CreateMeetingCtrl

  constructor: (@$log, @$location, @$scope, @MeetingService) ->
    @$log.debug "constructing CreateMeetingController"
    @meeting = {}
    @meeting.attendees = []
    @meeting.organizer = ""
    @meeting.goal = ""
    @meeting.attendees.push("")

  createMeeting: () ->
    @$log.debug "createMeeting()"
    @meeting.active = true
    @MeetingService.createMeeting(@meeting)
    .then(
      (data) =>
        @$log.debug "Promise returned #{data} Meeting"
        @meeting = data
        @$location.path("/meetings")
    ,
    (error) =>
      @$log.error "Unable to create Meeting: #{error}"
    )

  addAttendee: () ->
    @$log.debug "addAttendee()"
    @meeting.attendees.push("")

  removeAttendee: (@attendeeIndex) ->
    @$log.debug "removeAttendee()"
    @meeting.attendees.splice(attendeeIndex, 1)

controllersModule.controller('CreateMeetingCtrl', CreateMeetingCtrl)