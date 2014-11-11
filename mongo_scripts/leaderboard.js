// This script is only for prototyping the map reduce function. It has no production use
var mapVotes = function() {
  var votesUp = this.votesUp.length;
  var votesDown = this.votesDown.length;
    printjson(this.goal);
    printjson(this.organizer);
    var votesDiff = votesUp - votesDown;
    printjson(votesDiff);
    emit(this.organizer, votesDiff);
};
var reduceVotes = function(organizer, votes) {
    print("reduce");
    printjson(organizer);
    printjson(votes);         
    return Array.sum(votes);
};
db.meetings.mapReduce(mapVotes, reduceVotes, {out: "leaderBoard"})