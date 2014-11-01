
class HomeMeetingListCtrl
  constructor: (@$log, @$scope) ->
    @$log.debug "HomeMeetingListCtrl.constructor()"
    @meetings = [
      {
        id: 1,
        date: 1414867577725,
        goal: "Klären der Verantwortlichkeiten im Projekt",
        organizer: "r1bader@hsr.ch",
        color: "color-1",
        goalReached: 100,
        todoReached: 30,
        updated: 1414867577725
      },
      {
        _id: 2,
        date: 1414867577725,
        goal: "Besprechen der neuen Einsatzmöglichkeiten",
        organizer: "r1bader@hsr.ch",
        color: "color-2",
        goalReached: 20,
        todoReached: 100,
        updated: 1414867577725
      },
      {
        _id: 3,
        date: 1414867577725,
        goal: "Erstellen einen neuen Benutzerübersicht",
        organizer: "r1bader@hsr.ch",
        color: "color-3",
        goalReached: 80,
        todoReached: 70,
        updated: 1414867577725
      },
      {
        _id: 4,
        date: 1414867577725,
        goal: "Aufzeichnen neuer Ideen",
        organizer: "r1bader@hsr.ch",
        color: "color-4",
        goalReached: 80,
        todoReached: 70,
        updated: 1414867577725
      }
    ]

controllersModule.controller('HomeMeetingListCtrl', HomeMeetingListCtrl)
