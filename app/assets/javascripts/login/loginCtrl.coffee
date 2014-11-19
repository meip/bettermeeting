class LoginCtrl

  constructor: (@$log, @UserControlService, @$location) ->
    @$log.debug "LoginCtrl.constructor()"
    @user = {
      email: "",
      password: ""
    }
    @warningMessage = {
      warning: false,
      message: ""
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
      @showWarning("Unable to Login: #{error}")
    )

  showWarning: (warningMessage) ->
    @warningMessage = {
      warning: true,
      message: warningMessage
    }

  removeWarning: () ->
    @warningMessage = {
      warning: false,
      message: ""
    }




controllersModule.controller('LoginCtrl', LoginCtrl)
