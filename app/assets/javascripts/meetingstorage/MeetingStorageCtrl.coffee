
class MeetingStorageCtrl


  @id

  constructor: (@$log, @$routeParams, @MeetingStorageService) ->
    @$log.debug "MeetingStorageCtrl.constructor()"
    @id = @$routeParams.id
    @meeting = @MeetingStorageService.meetingFactory(@id)

  flush: () ->
    @MeetingStorageService.flush()

  updateDatabase: () ->
    @MeetingStorageService.set(@id, @meeting)

  publishMeeting: () ->
    @$log.debug "publishMeeting()"


controllersModule.controller('MeetingStorageCtrl', MeetingStorageCtrl)