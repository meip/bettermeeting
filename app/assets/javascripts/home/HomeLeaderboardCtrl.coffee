
class HomeLeaderboardCtrl
  constructor: (@$log, @$scope, @LeaderboardService) ->
    @$log.debug "HomeLeaderboardCtrl.constructor()"

    @ownPoints = {
      meetingLeader: 0,
      meetingAttendee: 0,
      meetingTodos: 0
    }

    @leaderboardEfficiency = []
    @leaderboardGoal = []

    @LeaderboardService.getLeaderboardEfficiency()
    .then(
      (data) =>
        @$log.debug "Promise returned #{data.length}"
        @leaderboardEfficiency = data
    ,
    (error) =>
      @$log.error "Unable to get Leaderboard: #{error}"
    )

    @LeaderboardService.getLeaderboardGoal()
    .then(
      (data) =>
        @$log.debug "Promise returned #{data.length}"
        @leaderboardGoal = data
    ,
    (error) =>
      @$log.error "Unable to get Leaderboard: #{error}"
    )

    @LeaderboardService.getCountOrganizer()
    .then(
      (data) =>
        @$log.debug "Promise returned #{data.length}"
        @ownPoints.meetingLeader = data
    ,
    (error) =>
      @$log.error "Unable to get Count Organizer: #{error}"
    )

    @LeaderboardService.getCountAttendees()
    .then(
      (data) =>
        @$log.debug "Promise returned #{data.length}"
        @ownPoints.meetingAttendee = data
    ,
    (error) =>
      @$log.error "Unable to get Count Attendees: #{error}"
    )

    @LeaderboardService.getCountActionpoints()
    .then(
      (data) =>
        @$log.debug "Promise returned #{data.length}"
        @ownPoints.meetingTodos = data
    ,
    (error) =>
      @$log.error "Unable to get Count Todos: #{error}"
    )

  getRank: (rank) ->
    if rank.selft
      return "active-user"
    else
      return ""


controllersModule.controller('HomeLeaderboardCtrl', HomeLeaderboardCtrl)
