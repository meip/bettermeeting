class LogoutCtrl

  constructor: (@$log, @UserControlService, @$location, @$alert) ->
    @$log.debug "LogoutCtrl.constructor()"
    @logout()
    body = document.getElementsByTagName('body')[0];
    body.style.background = "#FFFFFF";

  logout: () ->
    @$log.debug "LogoutCtrl.logout()"

    @UserControlService.logoutUser()
    .then(
      (data) =>
        @$log.debug "Promise returned #{data} Meetings"
        @$location.path("/login");
        myAlert = @$alert({title: 'Successfully Logged out!', content: "You have been logged out. Please wait to login again", placement: 'top', type: 'success', show: true, duration: 5})
    ,
    (error) =>
      @$log.error "Unable to Log Out: #{error}"
    )

controllersModule.controller('LogoutCtrl', LogoutCtrl)
