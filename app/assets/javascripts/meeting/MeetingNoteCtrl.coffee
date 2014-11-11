
class MeetingNoteCtrl
  constructor: (@$log, @$location, @$routeParams, @MeetingService, @UserControlService, @$scope) ->
    @$log.debug "MeetingNoteCtrl.constructor()"
    idParam = @$routeParams.id

    if idParam == undefined
      @initializeNewMeeting()
    else
      @MeetingService.getMeeting(idParam)
      .then(
        (data) =>
          @$log.debug "Promise returned #{data.length} Meetings"
          @meeting = data
          @meeting.date = moment(@meeting.date).format("DD.MM.YYYY HH:mm")
          for actionPoint in @meeting.actionPoints
            actionPoint.dueDate = moment(actionPoint.dueDate).format("DD.MM.YYYY HH:mm")
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
      color: "color-" + (Math.floor(Math.random() * 4) + 1)
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
      ,
      (error) =>
        @$log.error "Unable to create Meeting: #{error}"
      )
    else
      @MeetingService.putMeeting(@meeting)
      .then(
        (data) =>
          @$log.debug "Promise returned #{data} Meeting"
      ,
      (error) =>
        @$log.error "Unable to update Meeting: #{error}"
      )


  removeMeeting: () ->
    @$log.debug "MeetingNoteCtrl.removeMeeting()"
    @MeetingService.removeMeeting(@meeting._id.$oid)
    .then(
      (data) =>
        @$log.debug "Deleted #{data} Meeting"
        @$location.path("/")
    ,
    (error) =>
      @$log.error "Unable to delete Meeting: #{error}"
    )

  getFormattedDate: (additionalHours) ->
    return moment().add(additionalHours, "hours").format("DD.MM.YYYY HH:mm")

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
