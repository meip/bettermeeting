class LoginCtrl

  constructor: (@$log, @UserControlService, @$location) ->
    @$log.debug "LoginCtrl.constructor()"
    @user = {
      email: "rob@bader.ch",
      password: "passwd"
    }


  login: () ->
    @$log.debug "LoginCtrl.login()"

    @UserControlService.loginUser(@user.email, @user.password)
    .then(
      (data) =>
        @$log.debug "Promise returned #{data} Meetings"
        @$location.path("/");
    ,
    (error) =>
      @$log.error "Unable to Login: #{error}"
    )

controllersModule.controller('LoginCtrl', LoginCtrl)
