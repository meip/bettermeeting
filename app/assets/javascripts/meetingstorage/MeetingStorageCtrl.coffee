
class MeetingStorageCtrl


  constructor: (@$log, @$location, @$routeParams, @MeetingStorageService) ->
    @$log.debug "MeetingStorageCtrl.constructor()"
    @_id = @$routeParams.id
    @meeting = @MeetingStorageService.meetingFactory(@_id)

  flush: () ->
    @MeetingStorageService.flush()

  updateDatabase: () ->
    @MeetingStorageService.set(@__id, @meeting)

  publishMeeting: () ->
    @$log.debug "publishMeeting()"
    @MeetingStorageService.publishMeeting(@meeting)
    .then(
      (data) =>
        @$log.debug "Promise returned #{data} Meeting"
        @meeting = data
        @$location.path("/meetings")
    ,
    (error) =>
      @$log.error "Unable to create Meeting: #{error}"
    )


controllersModule.controller('MeetingStorageCtrl', MeetingStorageCtrl)