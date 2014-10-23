
class MeetingStorageCtrl

  constructor: (@$log, @$location, @$element, @MeetingStorageService, @localStorageService) ->
    @$log.debug "constructing MeetingStorageCtrl"
    @meeting = {}
    @meeting.name = ""

    if localStorageService.isSupported
      @$log.debug "Storage Supported"
      @$log.debug "Type: " + localStorageService.getStorageType()
    else
      @$log.debug "Storage not Supported"




  createMeeting: () ->
    @$log.debug "createMeeting()"


controllersModule.controller('MeetingStorageCtrl', MeetingStorageCtrl)