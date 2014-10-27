
class MeetingListCtrl

  constructor: (@$log, @MeetingService, @localStorageService) ->
    @$log.debug "MeetingListCtrl.constructor()"
    @meetings = {}
    @meetings.localMeetings = []
    @meetings.remoteMeetings = []

    @meetings.localMeetings = @getLocalMeetings()
    @getRemoteMeetings()

  getRemoteMeetings: () ->
    @MeetingService.getRemoteMeetings()
    .then(
      (data) =>
        @$log.debug "Promise returned #{data.length} Meetings"
        if data == ""
          data = []
        @meetings.remoteMeetings = data
    ,
    (error) =>
      @$log.error "Unable to get Meetings: #{error}"
    )

  getLocalMeetings: () ->
    localMeetingIndexes = @localStorageService.get("localMeetings")
    localMeetings = []
    if localMeetingIndexes != null
      for index in localMeetingIndexes
        localMeetings.push(@localStorageService.get(index))
    return localMeetings

  removeMeeting: (meetingId) ->
    @$log.debug meetingId
    if meetingId.length == 24
      @MeetingService.removeMeeting(meetingId)
      .then(
        (data) =>
          @$log.debug "Promise returned #{data.length} Meetings"
          @getRemoteMeetings()
      ,
      (error) =>
        @$log.error "Unable to get Meetings: #{error}"
      )
    @removeMeetingLocal(meetingId)
    @meetings.localMeetings = @getLocalMeetings()


  removeMeetingLocal: (meetingId) ->
    localMeetings = @localStorageService.get("localMeetings")
    index = localMeetings.indexOf(meetingId);
    if index > -1
      localMeetings.splice(index, 1)
    @localStorageService.set("localMeetings", localMeetings)
    @localStorageService.remove(meetingId)



controllersModule.controller('MeetingListCtrl', MeetingListCtrl)