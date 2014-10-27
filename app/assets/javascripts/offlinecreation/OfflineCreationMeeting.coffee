class @OfflineCreationMeeting

  @meeting = {}

  updateDatabase: () ->
    @meeting.updated = Date.now()
    @localStorageService.set(@meeting._id.oid, @meeting)

  addAttendee: () ->
    @$log.debug "OfflineCreationMeetingCreateCtrl.addAttendee()"
    @meeting.attendees.push("")
    @updateDatabase()

  removeAttendee: (index) ->
    @$log.debug "OfflineCreationMeetingCreateCtrl.removeAttendee()"
    if @meeting.attendees.length > 1
      @meeting.attendees.splice(index, 1)
    @updateDatabase()

  addMeetingPoint: () ->
    @$log.debug "OfflineCreationMeetingCreateCtrl.addMeetingPoint()"
    @meeting.meetingPoints.push({
      subject: "",
      lastEditor: "r1bader@hsr.ch",
      owner: "r1bader@hsr.ch",
      dueDate: "16.10.2014 16:30",
      pointType: "info"
    })
    @updateDatabase()

  removeMeetingPoint: (index) ->
    @$log.debug "OfflineCreationMeetingCreateCtrl.addMeetingPoint()"
    if @meeting.meetingPoints.length > 1
      @meeting.meetingPoints.splice(index, 1)
    @updateDatabase()
