class TestMeetingCtrl

  constructor: (@$log, @$scope) ->
    @$log.debug "constructing CreateUserController"
    @$scope.meeting = {}



controllersModule.controller('TestMeetingCtrl', TestMeetingCtrl)