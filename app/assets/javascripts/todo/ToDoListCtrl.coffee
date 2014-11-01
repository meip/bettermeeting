
class ToDoListCtrl



  constructor: (@$log, @$scope) ->
    @$log.debug "ToDoListCtrl.constructor()"
    @todos = [
      {
        id: 1,
        subject: "Architekturdoku erstellen",
        duedate: 1414867577725,
        reminderDate: 1414867577725,
        reminderType: 1414867577725,
        created: 1414867577725,
        updated: 1414867577725,
        editor: "r1bader@hsr.ch",
        owner: "r1bader@hsr.ch",
        color: "color-1"
      },
      {
        id: 2,
        subject: "Erstellen der neuen Mockups",
        duedate: 1414867577725,
        reminderDate: 1414867577725,
        reminderType: 1414867577725,
        created: 1414867577725,
        updated: 1414867577725,
        editor: "r1bader@hsr.ch",
        owner: "r1bader@hsr.ch",
        color: "color-2"
      },
      {
        id: 3,
        subject: "Dokumentieren der Änderungen",
        duedate: 1414867577725,
        reminderDate: 1414867577725,
        reminderType: 1414867577725,
        created: 1414867577725,
        updated: 1414867577725,
        editor: "r1bader@hsr.ch",
        owner: "r1bader@hsr.ch",
        color: "color-3"
      },
      {
        id: 4,
        subject: "Verarbeiten der Ergebnisse",
        duedate: 1414867577725,
        reminderDate: 1414867577725,
        reminderType: 1414867577725,
        created: 1414867577725,
        updated: 1414867577725,
        editor: "r1bader@hsr.ch",
        owner: "r1bader@hsr.ch",
        color: "color-4"
      },
    ]

controllersModule.controller('ToDoListCtrl', ToDoListCtrl)
