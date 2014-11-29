
class IntroCtrl
  constructor: (@$log, @$scope, @UserControlService) ->
    @$log.debug "IntroCtrl.constructor()"
    @foo = @UserControlService
    intros =
      home: [
          element: ".add-meeting"
          intro: """Click here to add a new Meeting"""
          position: "auto"
        ,
          element: "#todo-container"
          intro: "See your open Todos"
          position: "left"
        ,
          element: "#leaderboard-container"
          intro: "See your performance stats"
          position: "auto"
      ]

    steps = intros["home"]

    @$scope.IntroOptions =
      steps:
        steps
      showStepNumbers: true
      showProgress: true
      showBullets: false
      exitOnOverlayClick: false
      exitOnEsc: true
      nextLabel: "<button class=\"btn btn-success btn-xs\"><span class=\"glyphicon glyphicon-chevron-right\" aria-hidden=\"true\"></span>Next</button>"
      prevLabel: "<button class=\"btn btn-primary btn-xs\"><span class=\"glyphicon glyphicon-chevron-left\" aria-hidden=\"true\"></span>Previous</button>"
      skipLabel: "<button class=\"btn btn-danger btn-xs\"><span class=\"glyphicon glyphicon-remove\" aria-hidden=\"true\"></span>Exit</button>"
      doneLabel: "<button class=\"btn btn-success btn-xs\"><span class=\"glyphicon glyphicon-ok\" aria-hidden=\"true\"></span>Thanks</button>"

    @$scope.showIntro = true
    @showIntroFunc()

  introHide: =>
    console.log("Exit Event called")
    @UserControlService.setShowIntro(false)

  showIntroFunc: ->
    @UserControlService.getActualUser().then((user) =>
      console.log("foooo:" + user.showIntro)
      @$scope.showIntro = user.showIntro
    , (error) =>
      @$log.error "Unable to get actual User: #{error}"
      true
    )


controllersModule.controller('IntroCtrl', IntroCtrl)
