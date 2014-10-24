
class MeetingStorageViewCtrl

  constructor: (@$log, @MeetingStorageService) ->
    @$log.debug "MeetingController.constructor()"
    @meetings = {}
    @meetings.localMeetings = []
    @meetings.remoteMeetings = []

    @meetings.localMeetings = @MeetingStorageService.getLocalMeetings()

    @MeetingStorageService.getRemoteMeetings()
    .then(
      (data) =>
        @$log.debug "Promise returned #{data.length} Meetings"
        @meetings.remoteMeetings = data
    ,
    (error) =>
      @$log.error "Unable to get Meetings: #{error}"
    )

controllersModule.controller('MeetingStorageViewCtrl', MeetingStorageViewCtrl)