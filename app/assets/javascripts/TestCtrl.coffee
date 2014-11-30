
class TestCtrl
  constructor: (@$location, @$scope) ->
    @field = 'Field'
    @i = 1

    @$scope.$watch(
      angular.bind(@,
        () ->
          @field # `this` IS the `this` above!!
      ),
      () -> console.log("asdfeesadf")
    )

  method: ->
    'Method'

  methodWithParam: (param) ->
    "Param: #{param}"

  accessFieldFromMethod: ->
    "Path: #{@$location.absUrl()}"

  testClick: () ->
    @field = "sadf" + @i++

controllersModule.controller('TestCtrl', TestCtrl)
