class LogoutCtrl

  constructor: (@$log, @UserControlService, @$location) ->
    @$log.debug "LogoutCtrl.constructor()"
    @logout()

  logout: () ->
    @$log.debug "LogoutCtrl.logout()"

    @UserControlService.logoutUser()
    .then(
      (data) =>
        @$log.debug "Promise returned #{data} Meetings"
        @$location.path("/login");
    ,
    (error) =>
      @$log.error "Unable to get Meetings: #{error}"
    )

controllersModule.controller('LogoutCtrl', LogoutCtrl)
