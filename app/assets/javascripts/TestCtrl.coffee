
class TestCtrl
  constructor: (@$log, @$scope) ->
    @$log.debug "TestCtrl.constructor()"

    @$scope.alert = {
      "title": "Holy guacamole!",
      "content": "Best check yo self, you're not looking too good.",
      "type": "info"
    }

    @$scope.modal = {
      "title": "Title",
      "content": "Hello Modal<br />This is a multiline message!"
    }

    @$scope.popover = {
      "title": "Title",
      "content": "Hello Popover<br />This is a multiline message!"
    }

    @$scope.selectedDateAsNumber = Date.now()

controllersModule.controller('TestCtrl', TestCtrl)
