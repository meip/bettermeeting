class LoginCtrl

  constructor: (@$log, @UserControlService, @$location, @$alert) ->
    @$log.debug "LoginCtrl.constructor()"
    @user = {
      email: "",
      password: ""
    }
    @warningMessage = {
      warning: false,
      message: ""
    }
    @setBackgroundColor()

  setBackgroundColor: () ->
    ## Is needed for dark Background in MeetingnoteCreation
    body = document.getElementsByTagName('body')[0];
    body.style.background = "#FFFFFF";

  login: () ->
    @$log.debug "LoginCtrl.login()"

    @UserControlService.loginUser(@user.email, @user.password)
    .then(
      (data) =>
        @$log.debug "Promise returned #{data} Meetings"
        @$location.path("/");
        myAlert = @$alert({title: 'Successfully Logged In!', content: "Welcome", placement: 'top', type: 'success', show: true, duration: 5})
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
