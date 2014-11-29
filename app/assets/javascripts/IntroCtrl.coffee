
class IntroCtrl
  constructor: (@$log, @$scope) ->
    @$log.debug "IntroCtrl.constructor()"
    intros =
      meeting: [
        {
          element: ".add-meeting"
          intro: """This is the main Menu. <br>Hover to show the
                        tooltips and Submenu buttons."""
          position: "top"
        }
        {
          element: "#todo-container"
          intro: "See your open Todos"
          position: "left"
        }
        {
          element: "#leaderboard-container"
          intro: "See your performance stats"
          position: "top"
        }
      ]

    steps = intros["meeting"]
    @$log.debug steps

    @$scope.IntroOptions =
      steps:
        steps
      showStepNumbers: true
      showProgress: true
      exitOnOverlayClick: true
      exitOnEsc: true
      nextLabel: "<button class=\"btn btn-primary btn-xs\"><span class=\"glyphicon glyphicon-chevron-right\" aria-hidden=\"true\"></span>Next</button>"
      prevLabel: "<button class=\"btn btn-primary btn-xs\"><span class=\"glyphicon glyphicon-chevron-left\" aria-hidden=\"true\"></span>Previous</button>"
      skipLabel: "Exit"
      doneLabel: "Thanks"




controllersModule.controller('IntroCtrl', IntroCtrl)
