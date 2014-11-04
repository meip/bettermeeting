class SignupCtrl

  constructor: (@$log, @UserControlService, @$location) ->
    @$log.debug "SignupCtrl.constructor()"
    @user = {
      email: "rob@bader.ch",
      password: "passwd",
      firstName: "Robin",
      lastName: "Bader",
      active: true
    }


  signup: () ->
    @$log.debug "SignupCtrl.signup()"

    @UserControlService.signupUser(@user)
    .then(
      (data) =>
        @$log.debug "Promise returned #{data} Signup"
        @login()
    ,
    (error) =>
      @$log.error "Unable to Signup: #{error}"
    )
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

controllersModule.controller('SignupCtrl', SignupCtrl)
