class Meetings
  constructor: (@$resource) ->
    return @$resource(
      '/api/meetings/:meetingId',
      null,
      {
        'update': { method:'PUT' }
      }
    )

factoriesModule.factory('Meetings', Meetings)