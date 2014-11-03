
class CreateMeetingNewCtrl
  constructor: (@$log, @$scope ,@$resource) ->
    @$log.debug "CreateMeetingNewCtrl.constructor()"

    @meeting = {
      id: 1,
      goal: "Erstellen eines Prototyps",
      organizer: "r1bader@hsr.ch",
      date: "03.11.2014 09:26",
      attendees: [
        "p1meier@hsr.ch",
        "lblaeser@hsr.ch"
      ]
      decisions: [
        "According to our discussion we decide to go forward with our next steps for our Prototype #1"
      ]
      todos: [
        {
          id: 1
          note: "Create a new Mockup for Ideas",
          owner: "r1bader@hsr.ch",
          date: @getFormattedDate(0)
        },
        {
          id: 2,
          note: "Write Concept down",
          owner: "r1bader@hsr.ch",
          date: @getFormattedDate(0)
        }
      ],
      reachedTodos: 1,

      color: "color-1"
    }

  getFormattedDate: (additionalHours) ->
    calculatedTime = new Date(Date.now() + (additionalHours * 1000 * 60 * 60))
    return calculatedTime.toString("dd.MM.yyyy HH:MM")




  publishMeeting: () ->
    @$log.debug "CreateMeetingNewCtrl.publishMeeting()"

  showAttendees: () ->
    @$log.debug "CreateMeetingNewCtrl.showAttendees()"

  addTodo: () ->
    @$log.debug "CreateMeetingNewCtrl.addTodo()"
    @meeting.todos.push({
        note: "",
        owner: "",
        date: @getFormattedDate(0)
    })


  removeTodo: (todoIndex) ->
    @$log.debug "CreateMeetingNewCtrl.removeTodo(" + todoIndex + ")"
    if @meeting.todos.length > 1
      @meeting.todos.splice(todoIndex, 1)
    else
      @meeting.todos[todoIndex] = {
        note: "",
        owner: "",
        date: @getFormattedDate(0)
      }

  removeMeeting: () ->
    @$log.debug "CreateMeetingNewCtrl.removeMeeting()"

  addDecision: () ->
    @$log.debug "CreateMeetingNewCtrl.addDecision()"
    @meeting.decisions.push("")

  removeDecision: (decisionIndex) ->
    @$log.debug "CreateMeetingNewCtrl.removeDecision(" + decisionIndex + ")"
    if @meeting.decisions.length > 1
      @meeting.decisions.splice(decisionIndex, 1)
    else
      @meeting.decisions[decisionIndex] = ""

  addAttendee: () ->
    @$log.debug "CreateMeetingNewCtrl.addAttendee()"
    @meeting.attendees.push("")

  removeAttendee: (attendeeIndex) ->
    @$log.debug "CreateMeetingNewCtrl.removeAttendee(" + attendeeIndex + ")"
    if @meeting.attendees.length > 1
      @meeting.attendees.splice(attendeeIndex, 1)
    else
      @meeting.attendees[attendeeIndex] = ""


controllersModule.controller('CreateMeetingNewCtrl', CreateMeetingNewCtrl)
