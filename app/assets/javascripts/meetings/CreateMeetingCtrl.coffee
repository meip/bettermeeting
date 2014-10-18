
class CreateMeetingCtrl

  constructor: (@$log, @$location,  @MeetingService) ->
    @$log.debug "constructing CreateMeetingController"
    @meeting = {}

  createMeeting: () ->
    @$log.debug "createMeeting()"
    @meeting.active = true
    @MeetingService.createMeeting(@meeting)
    .then(
      (data) =>
        @$log.debug "Promise returned #{data} Meeting"
        @meeting = data
        @$location.path("/show-meetings")
    ,
    (error) =>
      @$log.error "Unable to create Meeting: #{error}"
    )

controllersModule.controller('CreateMeetingCtrl', CreateMeetingCtrl)