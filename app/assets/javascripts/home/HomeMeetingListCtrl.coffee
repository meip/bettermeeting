
class HomeMeetingListCtrl
  constructor: (@$log, @$scope, @MeetingService, @UserControlService, @$location) ->
    @$log.debug "HomeMeetingListCtrl.constructor()"
    @meetings = []
    @user = {}

    @MeetingService.getRemoteMeetings()
    .then(
      (data) =>
        @$log.debug "Promise returned #{data.length} Meetings"
        @meetings = data
        for meeting in @meetings
          meeting.goalStatus = @calculateGoalStatus(meeting)
          meeting.todoStatus = @calculateTodoStatus(meeting)
          meeting.color = @calculateMeetingColor(meeting)
        @getActualUser()
    ,
    (error) =>
      @$log.error "Unable to get Meetings: #{error}"
    )

    body = document.getElementsByTagName('body')[0];
    body.style.background = "#FFFFFF";

  getActualUser: () ->
    @UserControlService.getActualUser()
    .then(
      (data) =>
        @$log.debug "Promise returned #{data.length} ActualUser"
        @user = data
        @freeTile()
    ,
    (error) =>
      @$log.error "Unable to get actual User: #{error}"
    )

  calculateGoalStatus: (meeting) ->
    votesOnGoal = meeting.votesOnGoal
    votesOnEfficiency = meeting.votesOnEfficiency

    votesPoints = 0
    for vote in votesOnGoal
      if vote.voteValue > 0
        votesPoints += vote.voteValue
    for vote in votesOnEfficiency
      if vote.voteValue > 0
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
    else if meeting.organizer == @user.email
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
