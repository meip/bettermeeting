
class CreateMeetingNewCtrl
  constructor: (@$log, @$scope) ->
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
          date: 1415003186226
        },
        {
          id: 2,
          note: "Write Concept down",
          owner: "r1bader@hsr.ch",
          date: 1415003186226
        }
      ],
      reachedTodos: 1,

      color: "color-1"
    }

  publishMeeting: () ->
    @$log.debug "CreateMeetingNewCtrl.publishMeeting()"


controllersModule.controller('CreateMeetingNewCtrl', CreateMeetingNewCtrl)
