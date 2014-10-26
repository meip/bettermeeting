
class LoginCtrl

  constructor: (@$log, @$scope, @$rootScope, @$location, @AuthenticationService) ->
    @$log.debug "LoginCtrl.constructor()"
    @AuthenticationSerice.ClearCredentials()

  login: () ->
    @dataLoading = true
    @AuthenticationService.Login(@username, @password)
    .success((data) =>
      @AuthenticationService.SetCredentials(@username, @password)
    )
    .error((data) =>
      @$log.error data.Message
      @dataLoading = false
    )

controllersModule.controller('LoginCtrl', LoginCtrl)
