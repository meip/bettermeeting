
class HomeMeetingListCtrl
  constructor: (@$log, @$scope, @MeetingService, @$location) ->
    @$log.debug "HomeMeetingListCtrl.constructor()"
    @meetings = []

    @MeetingService.getRemoteMeetings()
    .then(
      (data) =>
        @$log.debug "Promise returned #{data.length} Meetings"
        @meetings = data
        for meeting in @meetings
          meeting.goalStatus = @calculateGoalStatus(meeting)
          meeting.todoStatus = @calculateTodoStatus(meeting)

        @freeTile()
    ,
    (error) =>
      @$log.error "Unable to get Meetings: #{error}"
    )

  calculateGoalStatus: (meeting) ->
    likes = meeting.votesUp.length
    total = meeting.votesDown.length + likes
    if total == 0
      return 0
    return Math.floor((likes / total) * 100)

  calculateTodoStatus: (meeting) ->
    todoLength = meeting.actionPoints.length
    if todoLength == 0
      return 0

    todoDone = 0
    for actionPoint in meeting.actionPoints
      if actionPoint.status == "done"
        todoDone++

    return Math.floor((todoDone / todoLength) * 100)

  freeTile: () ->
    $('#note-container').freetile({
      selector: '.note',
      animate: true,
      elementDelay: 30
    })

  openNewMeeting: () ->
    @$location.path("/meeting/note");

controllersModule.controller('HomeMeetingListCtrl', HomeMeetingListCtrl)
