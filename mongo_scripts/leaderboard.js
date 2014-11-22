// This script is only for prototyping the map reduce function. It has no production use
var mapVotes = function() {
  printjson(this.goal);
  printjson(this.organizer);
  printjson(this.votesOnGoal);
  var summe = 0;
  this.votesOnGoal.forEach(function(vote) {
    printjson(vote); 
    if (!isNaN(vote.voteValue)) {
        summe += vote.voteValue;
    }
  });
  printjson(summe);
  //printjson(Array.reduce(voteSum.voteValue, function(pv, cv) { return pv + cv.voteValue; }, 0));
  emit(this.organizer, summe);
};
var reduceVotes = function(organizer, vote) {
    print("reduce");
    printjson(organizer);
    printjson(Array.sum(vote));
    return Array.sum(vote);
};
db.meetings.mapReduce(mapVotes, reduceVotes, {out: "leaderBoard_test"})