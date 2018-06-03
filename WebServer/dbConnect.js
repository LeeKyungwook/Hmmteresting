var express = require('express');
var router = express.Router();
var mongoose = require('mongoose');
mongoose.connect('mongodb://127.0.0.1:27017/oikwho');
var db = mongoose.connection

db.on('error', function(){
  console.log('Connection failed!');
});


var users = mongoose.Schema({
  Uid:'number',
  name: 'string',
  id:'string',
  pw:'string',
  family:'number'
});

var schedules = mongoose.Schema({
  title:'string',
  user:'number',
  startDate:'number',
  startTime:'number',
  endDate:'number',
  endTime:'number',
  reqItems:'string',
  isBroadcast:'number'
});

var messages = mongoose.Schema({
  from : 'number',
  to : 'number',
  title : 'string'
});

var USERS = mongoose.model('users', users);
var SCHEDULES = mongoose.model('schedules', schedules);
var MESSAGES = mongoose.model('messages',messages);

var uid;
var dd;
var mm;
var yyyy;
var thisDate;// = yyyy*10000 + mm*100 + dd;
var sch_objID;
var string_ID;

uid = 2;
yyyy =2018; mm = 5; dd = 31;
thisDate = yyyy*10000 + mm*100 + dd;
sch_objID = "5afd61c4cd2a59ae077d05c5"
string_ID = "suhyun000"  //dummy


function scheduleQuery(req, callback) {
  //search 20180531's schedules
  var request = req;

  userName2UidQuery(userName, function(Uid){
    SCHEDULES.find({ $and:[{ "startDate":{$lte:request.startDate} }, {"endDate":{$gte:request.endDate}}, {user:Uid}] }, { __v:0}, function(error, schedules) {
      console.log('--- Read today\'s Schedules of User'+ uid +' ---');
      if(error){ console.log(error); }
      else{
        console.log(schedules);
        if (typeof callback === "function"){
          callback(schedules);
        };
      };
    });
  });
};


function requiredItemQuery(req, callback){
  //search today's requiredItems
  var request = req;

  userName2UidQuery(userName, function(Uid){
    SCHEDULES.find({ $and:[{ date:request.startDate }, {user:Uid}] }, {_id:0, __v:0, title:0, user:0, startDate:0, endDate:0, endTime:0, isBroadcast:0},function(error, reqitems){
      console.log('--- Read Required today\'s Item List of User ' + Uid + ' ---');
      if(error){ console.log(error); }
      else{
        console.log(reqitems);
        if (typeof callback === "function"){
          callback(reqitems);
        };
      };
    });
  });
};


function userId2UidQuery(req, callback){
  //search user whose id is string_ID
  USERS.find({ id:string_ID }, { _id:0, __v:0 },function(error,users) {
    console.log('--- User Info Test ---');
    if(error){
      console.log(error);
    }else{
      console.log(users);
      if (typeof callback === "function"){
        callback(users.Uid);
      };
    }
  });
};


function userName2UidQuery(req, callback){
  //search user whose name is userName
  USERS.find({ name:req.userName }, { _id:0, __v:0 },function(error,users) {
    console.log('--- User Info Test ---');
    if(error){
      console.log(error);
    } else{
      console.log(users);
      if (typeof callback === "function"){
        callback(users.Uid);
      };
    }
  });
};


function insertScheduleQuery(req, callback){
  var request = req;
  db.collection('schedules').insert(request);
  console.log('insert Schedule success');
  return callback('insert Schedule success');
};


function updateScheduleQuery(req, callback){
  //update schedule by its ObjectId
  var request = req;
  SCHEDULES.update(ObjectId(request._id),{$set:{title:requset.title, user: request.user, startDate: request.startDate, startTime: request.startTime, endDate: request.endDate, endTime:request.endTime , isBroadcast:request.isBroadcast}}, function(error, schedules) {
    console.log('--- Update Info Test ---');
    if(error) { console.error(); }
    else{
      console.log(schedules);
      if (typeof callback === "function"){
        callback('update success');
      };
    }
  });
};


function deleteScheduleQuery(req, callback){
  //delete schedule by its ObjectId
  var request = req;
  SCHEDULES.deleteOne(ObjectId(request._id), function(err, users) {
    console.log('--- Delete Schedule Test ---');
    if(error) { console.error(); }
    else{
      console.log('successfully deleted... good bey schedule');
      if (typeof callback === "function"){
        callback('delete success');
      };
    }
  });
};


function insertUserQuery(req, callback){ //req : name Uid pw
  var request = req;
  db.collection('users').insert(request);
  console.log('insert Schedule success');
  return callback('insert Schedule success');
};


function sendMessageQuery(req, callback) { //req : from to title
  var request = req;
  db.collection('messages').insert(request);
  console.log('insert Schedule success');
  return callback('insert Schedule success');
};

function receiveMessageQuery(req, callback) { //req : from to title
  var request = req;

  
  callback('delete success'); //to from message
};


//////////////////아직안씀
function veiwMessageQuery(req, callback) {
  // MESSAGES.find({to:Uid})
  // .sort({uploadDate:-1})
  // .limit(5)
  // .exec(function(err, messages){
  //   console.log(messges);
  //   return messges;
  // });
};

function howManyMassageQuery(req, callback) {

};

module.exports = {
  scheduleQuery: scheduleQuery,
  requiredItemQuery: requiredItemQuery,
  userId2UidQuery: userId2UidQuery,
  userName2UidQuery: userName2UidQuery,
  insertScheduleQuery: insertScheduleQuery,
  updateScheduleQuery: updateScheduleQuery,
  deleteScheduleQuery: deleteScheduleQuery,
  insertUserQuery: insertUserQuery,
  sendMessageQuery: sendMessageQuery,
  receiveMessageQuery: receiveMessageQuery,
  veiwMessageQuery: veiwMessageQuery,
  howManyMassageQuery: howManyMassageQuery
};
