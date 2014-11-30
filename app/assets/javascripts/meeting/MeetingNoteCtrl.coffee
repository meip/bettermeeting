
class MeetingNoteCtrl
  constructor: (@$log, @$location, @$routeParams, @MeetingService, @UserControlService, @$scope, @$alert, @hotkeys) ->
    @$log.debug "MeetingNoteCtrl.constructor()"
    idParam = @$routeParams.id

    @saveButtonText = "Save"
    @activePanel = 1
    @$scope.text = "hallo"

    @user = {}

    if idParam == undefined
      @initializeNewMeeting()
      @saveButtonText = "Publish"
      @activePanel = 0
      @getActualUser()
    else
      @MeetingService.getMeeting(idParam)
      .then(
        (data) =>
          @$log.debug "Promise returned #{data.length} Meetings"
          @meeting = data
          @getActualUser()
      ,
      (error) =>
        @$log.error "Unable to get Meetings: #{error}"
      )

    body = document.getElementsByTagName('body')[0];
    body.style.background = "#323A41";

    @availableUser = []
    @UserControlService.getAllUsers().then(
      (data) =>
        for user in data
          @availableUser.push(user.email)
    ,
      (error) =>
        @$log.error "Unable to get all Users: #{error}"
    )

    @hotkeys.del('t') #TODO: not a nice way
    @hotkeys.add({
      combo: 't',
      description: 'Creates a new todo',
      callback: (event, hotkey) =>
        event.preventDefault()
        @addTodo()
    })
    @hotkeys.del('d') #TODO: not a nice way
    @hotkeys.add({
      combo: 'd',
      description: 'Creates a new decision',
      callback: (event, hotkey) =>
        event.preventDefault()
        @addDecision()
    })

  getActualUser: () ->
    @$log.debug "MeetingNoteCtrl.getActualUser()"
    @UserControlService.getActualUser()
    .then(
      (data) =>
        @$log.debug "Promise returned #{data.length} ActualUser"
        @user = data
        if @meeting.organizer == ""
          @meeting.organizer = @user.email

        if @meeting.organizer == @user.email
          @meeting.color = "color-organizer"
        else
          @meeting.color = "color-attendee"
    ,
    (error) =>
      @$log.error "Unable to get actual User: #{error}"
    )

  setActivePanel: (panel) ->
    @activePanel = panel

  initializeNewMeeting: () ->
    @meeting = {
      goal: "",
      organizer: "",
      date: @getFormattedDate(0),
      attendees: [],
      decisions: [],
      actionPoints: [],
      votesUp: [],
      votesDown: [],
      created: @getFormattedDate(0),
      updated: @getFormattedDate(0),
      status: "new",
      color: "color-new"
    }

  publishMeeting: () ->
    @$log.debug "MeetingNoteCtrl.publishMeeting()"
    idParam = @$routeParams.id

    apToDelete = []
    index = 0
    for ap in @meeting.actionPoints
      if ap.subject == ""
        apToDelete.push(index)
      index++
    @$log.debug(apToDelete)

    for item in apToDelete by -1
      @meeting.actionPoints.splice(item, 1)

    dcToDelete = []
    index = 0
    for dc in @meeting.decisions
      if dc.subject == ""
        dcToDelete.push(index)
      index++
    @$log.debug(dcToDelete)

    for item in dcToDelete by -1
      @meeting.decisions.splice(item, 1)




    if idParam == undefined
      @MeetingService.postMeeting(@meeting)
      .then(
        (data, status, headers) =>
          @$log.debug "Promise returned #{data} Meeting"
          @$location.path("/meeting/note").search(id: data.message).replace();
          myAlert = @$alert({title: 'Meeting created!', content: 'The meeting has been successfully created', placement: 'top', type: 'success', show: true, duration: 5})
      ,
      (error) =>
        @$log.error "Unable to create Meeting: #{error}"
        myAlert = @$alert({title: 'Unable to create Meeting', content: "Error: #{error}", placement: 'top', type: 'info', show: true, duration: 5})
      )
    else
      @MeetingService.putMeeting(@meeting)
      .then(
        (data) =>
          @$log.debug "Promise returned #{data} Meeting"
          myAlert = @$alert({title: 'Meeting saved!', content: 'The meeting has been successfully saved', placement: 'top', type: 'success', show: true, duration: 5})
      ,
      (error) =>
        @$log.error "Unable to update Meeting: #{error}"
        myAlert = @$alert({title: 'Unable to update Meeting', content: "Error: #{error}", placement: 'top', type: 'info', show: true, duration: 5})
      )


  removeMeeting: () ->
    @$log.debug "MeetingNoteCtrl.removeMeeting()"
    @MeetingService.removeMeeting(@meeting._id.$oid)
    .then(
      (data) =>
        @$log.debug "Deleted #{data} Meeting"
        @$location.path("/")
        myAlert = @$alert({title: 'Meeting deleted!', content: 'The meeting has been successfully deleted', placement: 'top', type: 'success', show: true, duration: 5})

    ,
    (error) =>
      @$log.error "Unable to delete Meeting: #{error}"
      myAlert = @$alert({title: 'Unable to delete Meeting', content: "Error: #{error}", placement: 'top', type: 'info', show: true, duration: 5})
    )

  finishMeeting: () ->
    @$log.debug "MeetingNoteCtrl.finishMeeting()"
    @MeetingService.finishMeeting(@meeting._id.$oid)
    .then(
      (data) =>
        @$log.debug "Finished #{data} Meeting"
        @$location.path("/")
        myAlert = @$alert({title: 'Meeting finished!', content: "The meeting has been successfully finished. <br />Your Attendees have been notified to Vote", placement: 'top', type: 'success', show: true, duration: 5})
    ,
    (error) =>
      @$log.error "Unable to finish Meeting: #{error}"
      myAlert = @$alert({title: 'Unable to finish Meeting', content: "Error: #{error}", placement: 'top', type: 'info', show: true, duration: 5})
    )

  getFormattedDate: (additionalHours) ->
    return Date.now()

  addTodo: () ->
    @$log.debug "MeetingNoteCtrl.addTodo()"
    @meeting.actionPoints.push({
        subject: "",
        owner: @user.email,
        dueDate: @getFormattedDate(0)
    })
    @activePanel = 1

  removeTodo: (todoIndex) ->
    @$log.debug "MeetingNoteCtrl.removeTodo(" + todoIndex + ")"
    if @meeting.actionPoints.length > 1
      @meeting.actionPoints.splice(todoIndex, 1)
    else
      @meeting.actionPoints[todoIndex] = {
        subject: "",
        owner: @user.email,
        status: "open",
        duedate: @getFormattedDate(0)
      }

  addDecision: () ->
    @$log.debug "MeetingNoteCtrl.addDecision()"
    @meeting.decisions.push({
      subject: "",
      editor: @user.email
    })
    @activePanel = 1

  removeDecision: (decisionIndex) ->
    @$log.debug "MeetingNoteCtrl.removeDecision(" + decisionIndex + ")"
    if @meeting.decisions.length > 1
      @meeting.decisions.splice(decisionIndex, 1)
    else
      @meeting.decisions[decisionIndex] = ""

  addAttendee: () ->
    @$log.debug "MeetingNoteCtrl.addAttendee()"
    @meeting.attendees.push("")

  removeAttendee: (attendeeIndex) ->
    @$log.debug "MeetingNoteCtrl.removeAttendee(" + attendeeIndex + ")"
    if @meeting.attendees.length > 1
      @meeting.attendees.splice(attendeeIndex, 1)
    else
      @meeting.attendees[attendeeIndex] = ""

  isPublished: () ->
    if @saveButtonText == "Publish"
      return false
    else
      return true

controllersModule.controller('MeetingNoteCtrl', MeetingNoteCtrl)
