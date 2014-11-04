
class HomeLeaderboardCtrl
  constructor: (@$log, @$scope) ->
    @$log.debug "HomeLeaderboardCtrl.constructor()"
    @ownPoints = {
      meetingLeader: 12,
      meetingAttendee: 27,
      meetingTodos: 34
    }
    @leaderboard = [
      {
        id: 1,
        name: "Marco",
        points: 70,
        rank: 1,
        selft: false
      },
      {
        id: 2,
        name: "Philipp",
        points: 54,
        rank: 2,
        selft: false
      },
      {
        id: 3,
        name: "Robin",
        points: 37,
        rank: 3,
        selft: true
      },
      {
        id: 4,
        name: "Joel",
        points: 20,
        rank: 4,
        selft: false
      },
      {
        id: 5,
        name: "Dominik",
        points: 7,
        rank: 5,
        selft: false
      }
    ]
  getSelfClass: (rankId) ->
    actual = @leaderboard[rankId - 1]
    if actual.selft
      return "active"
    else
      return ""

controllersModule.controller('HomeLeaderboardCtrl', HomeLeaderboardCtrl)
