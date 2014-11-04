
class LoginCtrl

  constructor: (@$log, @UserService) ->
    @$log.debug "LoginCtrl.constructor()"
    @user = {
      email: "rob@in.ch",
      password: "passwd"
    }

  login: () ->
    @$log.debug "LoginCtrl.login()"
    @UserService.loginUser(@user.email, @user.password)
    .then(
      (data) =>
        @$log.debug "Promise returned #{data} Meetings"
    ,
    (error) =>
      @$log.error "Unable to get Meetings: #{error}"
    )

controllersModule.controller('LoginCtrl', LoginCtrl)
