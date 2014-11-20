
class MeetingNoteCtrl
  constructor: (@$log, @$location, @$routeParams, @MeetingService, @UserControlService, @$scope, @$alert) ->
    @$log.debug "MeetingNoteCtrl.constructor()"
    idParam = @$routeParams.id

    @saveButtonText = "Save"
    @activePanel = 1

    if idParam == undefined
      @initializeNewMeeting()
      @saveButtonText = "Publish"
      @activePanel = 0
    else
      @MeetingService.getMeeting(idParam)
      .then(
        (data) =>
          @$log.debug "Promise returned #{data.length} Meetings"
          @meeting = data
          if @meeting.organizer == "r1bader@hsr.ch"
            @meeting.color = "color-organizer"
          else
            @meeting.color = "color-attendee"
      ,
      (error) =>
        @$log.error "Unable to get Meetings: #{error}"
      )

    @UserControlService.getAllUsers()
    .then(
      (data) =>
        @$log.debug "Promise returned #{data.length} Users"
        @users = data
    ,
    (error) =>
      @$log.error "Unable to get Users: #{error}"
    )

  setActivePanel: (panel) ->
    @activePanel = panel

  initializeNewMeeting: () ->
    @meeting = {
      goal: "",
      organizer: "r1bader@hsr.ch",
      date: @getFormattedDate(0),
      attendees: [""],
      decisions: [
        {
          subject: "",
          editor: "r1bader@hsr.ch"
        }
      ],
      actionPoints: [
        {
          subject: "",
          owner: "",
          dueDate: @getFormattedDate(0),
          status: "open"
        }
      ],
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

  getFormattedDate: (additionalHours) ->
    ##return moment().add(additionalHours, "hours").format("DD.MM.YYYY HH:mm")
    return Date.now()

  showAttendees: () ->
    @$log.debug "MeetingNoteCtrl.showAttendees()"

  addTodo: () ->
    @$log.debug "MeetingNoteCtrl.addTodo()"
    @meeting.actionPoints.push({
        subject: "",
        owner: "",
        dueDate: @getFormattedDate(0)
    })

  removeTodo: (todoIndex) ->
    @$log.debug "MeetingNoteCtrl.removeTodo(" + todoIndex + ")"
    if @meeting.actionPoints.length > 1
      @meeting.actionPoints.splice(todoIndex, 1)
    else
      @meeting.actionPoints[todoIndex] = {
        subject: "",
        owner: "",
        status: "open",
        duedate: @getFormattedDate(0)
      }

  addDecision: () ->
    @$log.debug "MeetingNoteCtrl.addDecision()"
    @meeting.decisions.push("")

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


controllersModule.controller('MeetingNoteCtrl', MeetingNoteCtrl)
