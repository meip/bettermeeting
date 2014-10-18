
class MeetingCtrl

  constructor: (@$log, @MeetingService) ->
    @$log.debug "constructing MeetingController"
    @meetings = []
    @getAllMeetings()

  getAllMeetings: () ->
    @$log.debug "getAllMeetings()"

    @MeetingService.listMeetings()
    .then(
      (data) =>
        @$log.debug "Promise returned #{data.length} Meetings"
        @meetings = data
      ,
      (error) =>
        @$log.error "Unable to get Meetings: #{error}"
    )


    ###
    @$log.debug "constructing UserController"
    @users = []
    @getAllUsers()

  getAllUsers: () ->
    @$log.debug "getAllUsers()"

    @UserService.listUsers()
    .then(
      (data) =>
        @$log.debug "Promise returned #{data.length} Users"
        @users = data
    ,
    (error) =>
      @$log.error "Unable to get Users: #{error}"
    )
###

controllersModule.controller('MeetingCtrl', MeetingCtrl)