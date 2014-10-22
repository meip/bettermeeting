
class CreateMeetingCtrl

  constructor: (@$log, @$location, @$element, @MeetingService) ->
    @$log.debug "constructing CreateMeetingController"
    @meeting = {}
    @meeting.date = ""
    @meeting.organizer = ""
    @meeting.goal = ""
    @meeting.attendees = []
    @meeting.attendees.push("")
    @meeting.meetingPoints = []
    @meeting.meetingPoints.push({
      pointType: "information",
      note: "",
      owner: "",
      date: ""
    })

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

  addMeetingPoint: () ->
    @$log.debug "addMeetingPoint()"
    @meeting.meetingPoints.push({
      pointType: "information",
      note: "",
      owner: "",
      date: ""
    })

  removeMeetingPoint: (@meetingPointIndex) ->
    @$log.debug "removeMeetingPoint()"
    @meeting.meetingPoints.splice(@meetingPointIndex, 1)


controllersModule.controller('CreateMeetingCtrl', CreateMeetingCtrl)