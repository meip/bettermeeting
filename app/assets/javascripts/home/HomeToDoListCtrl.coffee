
class HomeToDoListCtrl

  constructor: (@$log, @$scope, @ToDoService, @$alert) ->
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

    @$scope.popover = {
      "title": "Title",
      "content": "Hello Popover<br />This is a multiline message!"
    }
    @$scope.selectedDateAsNumber = Date.now()

  doneClicked: (todoId) ->
    @$log.debug "HomeToDoListCtrl.doneClicked(" + todoId + ")"
    todo = @todos[todoId]
    todo.status = "done"

    @ToDoService.putTodo(todo)
    .then(
      (data) =>
        @$log.debug "Action Point marked as Done"
        myAlert = @$alert({title: 'Action Point Closed!', content: 'The Action Point has been successfully marked as done', placement: 'top', type: 'success', show: true, duration: 5})
    ,
    (error) =>
      @$log.error "Unable to update Action-Point: #{error}"
      myAlert = @$alert({title: 'Unable to update Action Point', content: "Error: #{error}", placement: 'top', type: 'info', show: true, duration: 3})
    )
    @todos.splice(todoId, 1)

  timeChanged: (todoId) ->
    @$log.debug "HomeToDoListCtrl.timeChanged(" + todoId + ")"
    todo = @todos[todoId]
    @ToDoService.putTodo(todo)
    .then(
      (data) =>
        @$log.debug "Time changes Saved"
        myAlert = @$alert({title: 'Time Changed!', content: 'The Time for the Action Point has been successfully saved', placement: 'top', type: 'success', show: true, duration: 5})
    ,
    (error) =>
      @$log.error "Unable to update Action-Point: #{error}"
      myAlert = @$alert({title: 'Unable to update Action Point', content: "Error: #{error}", placement: 'top', type: 'info', show: true, duration: 3})
    )


  daysLeft: (todoIndex) ->
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
      return differenceNumber + " days overdue"

controllersModule.controller('HomeToDoListCtrl', HomeToDoListCtrl)
