
class IntroCtrl
  constructor: (@$log, @$scope) ->
    @$log.debug "IntroCtrl.constructor()"
    intros =
      home: [
        {
          element: ".add-meeting"
          intro: """Click here to add a new Meeting"""
          position: "auto"
        }
        {
          element: "#todo-container"
          intro: "See your open Todos"
          position: "left"
        }
        {
          element: "#leaderboard-container"
          intro: "See your performance stats"
          position: "auto"
        }
      ]

    steps = intros["home"]
    @$log.debug steps

    @$scope.IntroOptions =
      steps:
        steps
      showStepNumbers: true
      showProgress: true
      showBullets: false
      exitOnOverlayClick: true
      exitOnEsc: true
      nextLabel: "<button class=\"btn btn-success btn-xs\"><span class=\"glyphicon glyphicon-chevron-right\" aria-hidden=\"true\"></span>Next</button>"
      prevLabel: "<button class=\"btn btn-primary btn-xs\"><span class=\"glyphicon glyphicon-chevron-left\" aria-hidden=\"true\"></span>Previous</button>"
      skipLabel: "<button class=\"btn btn-danger btn-xs\"><span class=\"glyphicon glyphicon-remove\" aria-hidden=\"true\"></span>Exit</button>"
      doneLabel: "<button class=\"btn btn-success btn-xs\"><span class=\"glyphicon glyphicon-ok\" aria-hidden=\"true\"></span>Thanks</button>"




controllersModule.controller('IntroCtrl', IntroCtrl)
