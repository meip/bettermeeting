
class HomeToDoListCtrl

  constructor: (@$log, @$scope, @ToDoService) ->
    @$log.debug "HomeToDoListCtrl.constructor()"

    @todos = []

    @ToDoService.getActionPoints()
    .then(
      (data) =>
        @$log.debug "Promise returned #{data.length} ActionPoints"
        @todos = data
        for todo in @todos
          todo.color = "color-" + (Math.floor(Math.random() * 4) + 1)
    ,
    (error) =>
      @$log.error "Unable to get Todos: #{error}"
    )

  doneClicked: (todoId) ->
    @$log.debug "HomeToDoListCtrl.doneClicked(" + todoId + ")"

  laterClicked: (todoId) ->
    @$log.debug "HomeToDoListCtrl.laterClicked(" + todoId + ")"

  daysLeft: (todoIndex) ->
    @$log.debug @todos[todoIndex].dueDate
    ONE_DAY = 1000 * 60 * 60 * 24
    dueDate = new Date(@todos[todoIndex].dueDate)
    today = new Date()
    difference = dueDate - today
    differenceNumber = Math.round(difference / ONE_DAY)
    if differenceNumber == 0
      return "Today"
    else if differenceNumber > 0
      return differenceNumber + " days left"
    else
      return differenceNumber + " overdue"

controllersModule.controller('HomeToDoListCtrl', HomeToDoListCtrl)
