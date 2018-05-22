var express = require('express');
var router = express.Router();

//일정추가/삭제/수정/조회(하루) 회원가입
//메세지조회 및 저장

var users = mongoose.Schema({
  name:'string',
  Uid:'number',
  pw:'string'
});

var schedules = mongoose.Schema({
  userID:'number',
  title:'string',
  startDate:'number',
  startTime:'number',
  endDate:'number',
  endTime:'number',
  isBroadcast:'number'
});

var reqItems = mongoose.Schema({
  userID:'number',
  name:'string',
  memo:'string'
});

var messages = mongoose.Schema({
  form:'number',
  to:'number',
  order:'number'
});

var USERS = mongoose.model('users', users);
var SCHEDULES = mongoose.model('schedules', schedules);
var REQITEMS = mongoose.model('reqitems', reqItems);
var MESSAGES = mongoos.model('messages',messages);

var uid; //uid will be initiated after receiving image from rasperryPi
var today = new Date();
var mm = today.getMonth()+1;
var yyyy = today.getFullYear();
var thisDate = (yyyy-2000)*10000 + mm*100;

function query(){
  uid=2;//dummy
  SCHEDULES.find({ $or:[ { $and:[ { startDate:{$gte:thisDate+1}},{startDate:{$lte:thisDate+30}},{userID:uid} ] }, { $and:[ {endDate:{$gte:thisDate+1}}, {endDate:{$lte:thisDate+30}},{userID:uid} ] } ] }, {_id:0, __v:0}, function(error, schedules) {
    console.log('--- Read This Month Schedules of User'+ uid +' ---');
    if(error){ console.log(error); }
    else{ console.log(schedules); }
  });

  REQITEMS.find({userID:uid}, {_id:0, __v:0},function(error, reqitems){
    console.log('--- Read Required Item List of User ' + uid + ' ---');
    if(error){ console.log(error); }
    else{ console.log(reqitems); }
  });

  USERS.find({}, {_id:0, __v:0},function(error,users) {
    console.log('--- User Info Test ---');
    if(error){ console.log(error); }
    else{ console.log(users); }
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
