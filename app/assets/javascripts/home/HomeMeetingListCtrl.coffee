
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
          meeting.color = @calculateMeetingColor(meeting)

        @freeTile()
    ,
    (error) =>
      @$log.error "Unable to get Meetings: #{error}"
    )

    body = document.getElementsByTagName('body')[0];
    body.style.background = "#FFFFFF";

  calculateGoalStatus: (meeting) ->
    votesOnGoal = meeting.votesOnGoal
    votesOnEfficiency = meeting.votesOnEfficiency

    votesPoints = 0
    for vote in votesOnGoal
      votesPoints += vote.voteValue
    for vote in votesOnEfficiency
      votesPoints += vote.voteValue

    votesTotal = votesOnGoal.length + votesOnEfficiency.length

    if votesTotal != 0
      return Math.floor((votesPoints / votesTotal) * 100)
    else
      return 0



  calculateTodoStatus: (meeting) ->
    todoLength = meeting.actionPoints.length
    if todoLength == 0
      return 0

    todoDone = 0
    for actionPoint in meeting.actionPoints
      if actionPoint.status == "done"
        todoDone++

    return Math.floor((todoDone / todoLength) * 100)

  calculateMeetingColor: (meeting) ->
    if meeting.status == "new"
      meeting.color = "color-new"
    else if meeting.organizer == "r1bader@hsr.ch"
      meeting.color = "color-organizer"
    else
      meeting.color = "color-attendee"

  freeTile: () ->
    $('#note-container').freetile({
      selector: '.note',
      animate: true,
      elementDelay: 30
    })

  openNewMeeting: () ->
    @$location.path("/meeting/note");
  openMeeting: (index) ->
    meeting = @meetings[index]

    @$location.path("/meeting/note").search(id: meeting._id.$oid)

controllersModule.controller('HomeMeetingListCtrl', HomeMeetingListCtrl)
