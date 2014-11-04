
class LoginCtrl

  constructor: (@$log, @UserService) ->
    @$log.debug "LoginCtrl.constructor()"
    @user = {
      email: "rob@in.ch",
      password: "passwd"
    }
  login: () ->
    @UserService.loginUser(@user.email, @user.password)
    .then(
      (data) =>
        @$log.debug "Promise returned #{data.length} Meetings"
    ,
    (error) =>
      @$log.error "Unable to get Meetings: #{error}"
    )

controllersModule.controller('LoginCtrl', LoginCtrl)
