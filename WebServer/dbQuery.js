var express = require('express');
var router = express.Router();

//일정추가/삭제/수정/조회(하루) 회원가입
//메세지조회 및 저장

// var mongoose = require('mongoose');
// mongoose.connect('mongodb://127.0.0.1:27017/oikwho');
// var db = mongoose.connection
//
// db.on('error', function(){
//   console.log('Connection failed!');
// });


var users = mongoose.Schema({
  Uid:'number',
  id:'string'
  pw:'string'
});

var schedules = mongoose.Schema({
  title:'string',
  user:'number',
  startDate:'number',
  startTime:'number',
  endDate:'number',
  endTime:'number',
  isBroadcast:'number'
});

var reqItems = mongoose.Schema({
  date:'number',
  user:'number',
  memo:'string'
});

var USERS = mongoose.model('users', users);
var SCHEDULES = mongoose.model('schedules', schedules);
var REQITEMS = mongoose.model('reqitems', reqItems);

var uid;
var dd;
var mm;
var yyyy;
var thisDate = yyyy*10000 + mm*100 + dd;
var sch_objID;
var string_ID;

uid = 2;
yyy y =2018; mm = 5; dd = 31;
sch_objID = "5afd61c4cd2a59ae077d05c5"
string_ID = "suhyun000"  //dummy

function query(){
  //search 20180531's schedules
  SCHEDULES.find({ $and:[{ "startDate":{$lte:thisDate} }, {"endDate":{$gte:thisDate}}, {user:uid}] }, { __v:0}, function(error, schedules) {
    console.log('--- Read today\'s Schedules of User'+ uid +' ---');
    if(error){ console.log(error); }
    else{ console.log(schedules); }
  });

  //search today's requiredItems
  REQITEMS.find({ $and:[{ date:thisDate }, {user:uid}] }, {_id:0, __v:0},function(error, reqitems){
    console.log('--- Read Required today\'s Item List of User ' + uid + ' ---');
    if(error){ console.log(error); }
    else{ console.log(reqitems); }
  });

  //sesarch user whose id is string_ID
  USERS.find({ id:string_ID }, { _id:0, __v:0 },function(error,users) {
    console.log('--- User Info Test ---');
    if(error){ console.log(error); }
    else{ console.log(users); }
  });

  //update schedule by its ObjectId
  SCHEDULES.update({ObjectId(sch_objID)},{$set:{title:"", user: , startDate: , startTime: , endDate: , endTime: , isBroadcast: }}, function(error, schdeules) {
    console.log('--- Update Info Test ---');
    if(error) { console.error(); }
    else{ console.log(schedules); }
  });

  //delete schedule by its ObjectId
  SCHEDULES.deleteOne({ObjectId(sch_objID)}, (err, users) {
    console.log('--- Delete Schedule Test ---');
    if(error) { console.error(); }
    else{ console.log('successfully deleted... good bey schedule'); }
  });

};

function scheduleQuery(req, res) {

};

function veiwMessageQuery(ID, res) {
  MESSAGES.find({to:ID})
  .sort({uploadDate:-1})
  .limit(5)
  .exec(function(err, messages){
    console.log(messges);
    return messges;
  });
};

function insertUser(req, res){ //req : name Uid pw

};

module.exports = {
  query: query
  scheduleQuery: scheduleQuery
  veiwMessageQuery: veiwMessageQuery
};
