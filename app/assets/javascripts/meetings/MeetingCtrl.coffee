
class MeetingCtrl

  constructor: (@$log, @MeetingService) ->
    @$log.debug "constructing MeetingController"
    @meetings = []
    @getAllMeetings()

  getAllMeetings: () ->
    @$log.debug "getAllMeetings()"

    @MeetingService.listMeetings()
    .then(
      (data) =>
        @$log.debug "Promise returned #{data.length} Meetings"
        @meetings = data
      ,
      (error) =>
        @$log.error "Unable to get Meetings: #{error}"
    )

controllersModule.controller('MeetingCtrl', MeetingCtrl)