class SignupCtrl

  constructor: (@$log, @UserControlService, @$location, @$alert) ->
    @$log.debug "SignupCtrl.constructor()"
    @user = {
      email: "",
      password: "",
      firstName: "",
      lastName: "",
      active: true
    }
    body = document.getElementsByTagName('body')[0];
    body.style.background = "#FFFFFF";


  signup: () ->
    @$log.debug "SignupCtrl.signup()"

    @UserControlService.signupUser(@user)
    .then(
      (data) =>
        @$log.debug "Promise returned #{data} Signup"
        myAlert = @$alert({title: 'Successfully Registered!', content: "Welcome #{@user.firstName}", placement: 'top', type: 'success', show: true, duration: 5})
        @login()

    ,
    (error) =>
      @$log.error "Unable to Signup: #{error}"
      myAlert = @$alert({title: 'Unable to Signup!', content: "Unable to Signup: #{error}", placement: 'top', type: 'info', show: true, duration: 5})
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
