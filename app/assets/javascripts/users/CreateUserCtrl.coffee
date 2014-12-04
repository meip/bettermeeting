
class CreateUserCtrl

    constructor: (@$log, @$location,  @UserService) ->
        @$log.debug "CreateUserCtrl.constructor()"
        @user = {}

    createUser: () ->
        @$log.debug "createUser()"
        @user.active = true
        @UserService.createUser(@user)
        .then(
            (data) =>
                @$log.debug "Promise @UserService.createUser returned #{data} User"
                @user = data
                @$location.path("/")
            ,
            (error) =>
                @$log.error "Unable to create User: #{error}"
            )

controllersModule.controller('CreateUserCtrl', CreateUserCtrl)