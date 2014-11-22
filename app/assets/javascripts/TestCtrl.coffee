
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

    @$scope.disabled = false

    @$scope.selectedAttendees = {}
    @$scope.availableAttendees = [
      { name: 'Adam',      email: 'adam@email.com' },
      { name: 'Amalie',    email: 'amalie@email.com' },
      { name: 'Wladimir',  email: 'wladimir@email.com' },
      { name: 'Samantha',  email: 'samantha@email.com' },
      { name: 'Estefanía', email: 'estefanía@email.com' },
      { name: 'Natasha',   email: 'natasha@email.com' },
      { name: 'Nicole',    email: 'nicole@email.com' },
      { name: 'Adrian',    email: 'adrian@email.com' },
      { name: 'Robin', email: 'r1bader@hsr.ch'}
    ]

    @attendees = [
      "r1bader@hsr.ch",
      "p1meier@hsr.ch"
    ]




controllersModule.controller('TestCtrl', TestCtrl)
