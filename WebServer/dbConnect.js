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
  startDate:'string',
  startTime:'string',
  endDate:'string',
  endTime:'string',
  reqItems:'string',
  isBroadcast:'number'
});

var messages = mongoose.Schema({
  from : 'string',
  to : 'string',
  title : 'string'
});

var USERS = mongoose.model('users', users);
var SCHEDULES = mongoose.model('schedules', schedules);
var MESSAGES = mongoose.model('messages',messages);

function todayScheduleQuery(req, callback) {
  //search 20180531's schedules
  var request = req;
  userName2UidQuery(request.userName, function(Uid){
    SCHEDULES.find({ $and:[{ startDate:request.thisDate }, {endDate:request.thisDate}, {user:Uid}] }, { __v:0}, function(error, schedules) {
      console.log('--- Read today\'s Schedules of User'+ uid +' ---');
      if(error){
        console.log(error);
        callback('show schedule error');
      }
      else{
        console.log(schedules);
        if (typeof callback === "function"){
          callback(schedules);
        };
      };
    });
  });
};

function scheduleQuery(req, callback) {
  //search 20180531's schedules
  var request = req;
  userName2UidQuery(request.userName, function(Uid){
    SCHEDULES.find({ $and:[{ startDate:{$lte:request.thisDate} }, {endDate:{$gte:request.thisDate}}, {user:Uid}] }, { __v:0}, function(error, schedules) {
      console.log('--- Read Schedules of User'+ uid +' ---');
      if(error){
        console.log(error);
        callback('show schedule error');
      }
      else{
        console.log(schedules);
        if (typeof callback === "function"){
          callback(schedules);
        };
      };
    });
  });
};


function shareScheduleQuery(req, callback) {
  //search 20180531's schedules
  var request = req;
  SCHEDULES.find({ $and:[{ startDate:{$lte:request.thisDate} }, {endDate:{$gte:request.thisDate}}, {isBroadcast : 1}] }, { __v:0}, function(error, schedules) {
    console.log('--- Read Share Schedules of User'+ uid +' ---');
    if(error){
      console.log(error);
      callback('show schedule error');
    }
    else{
      console.log(schedules);
      if (typeof callback === "function"){
        callback(schedules);
      };
    };
  });
};


function requiredItemQuery(req, callback){
  //search today's requiredItems
  var request = req;
  userName2UidQuery(request.userName, function(Uid){
    SCHEDULES.find({ $and:[{ startDate:{$lte:request.thisDate} }, {endDate:{$gte:request.thisDate}}, {user:Uid}] }, {_id:0, __v:0, title:0, user:0, startDate:0, endDate:0, endTime:0, isBroadcast:0},function(error, reqitems){
      console.log('--- Read Required today\'s Item List of User ' + Uid + ' ---');
      if(error){
        console.log(error);
        callback('show requiredItem error');
      }
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
  USERS.find({ id:req.id }, { _id:0, __v:0 },function(error,users) {
    console.log('--- User Info Test ---');
    if(error){
      console.log(error);
      callback('userID -> Uid error');
    }else{
      console.log(users);
      if (typeof callback === "function"){
        callback(users.Uid);
      };
    }
  });
};


function userName2UidQuery(req, callback){
  USERS.find({ name:req.userName }, { _id:0, __v:0 },function(error,users) {
    console.log('--- User Info Test ---');
    if(error){
      console.log(error);
      callback('userName -> Uid error');
    } else{
      console.log(users);
      if (typeof callback === "function"){
        callback(users.Uid);
      };
    }
  });
};


function familyNumber2userNameQuery(req, callback){
  USERS.find({ family : req.to }, { _id:0, __v:0 },function(error,users) {
    console.log('--- User Info Test ---');
    if(error){
      console.log(error);
      callback('familyNum -> userName error');
    } else{
      console.log(users);
      if (typeof callback === "function"){
        callback(users.name);
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
    if(error) {
      console.error();
      callback('update Schedule error');
    }
    else{
      console.log(schedules);
      if (typeof callback === "function"){
        callback('update Schedule success');
      };
    }
  });
};


function deleteScheduleQuery(req, callback){
  //delete schedule by its ObjectId
  var request = req;
  SCHEDULES.deleteOne(ObjectId(request._id), function(err, users) {
    console.log('--- Delete Schedule Test ---');
    if(error) {
      console.error();
      callback('delete Schedule error')
    }
    else{
      if (typeof callback === "function"){
        callback('delete Schedule success');
      };
    }
  });
};


function insertUserQuery(req, callback){ //req : name Uid pw
  var request = req;
  db.collection('users').insert(request);
  console.log('insert user success');
  return callback('insert user success');
};


function sendMessageQuery(req, callback) { //req : from to title
  var request = req;
  familyNumber2userNameQuery(request, function(toName){
    request.to = toName;
    db.collection('messages').insert(request);
    console.log('insert Schedule success');
    return callback('insert Schedule success');
  });
};


function deleteMessageQuery(req, callback){
  var request = req;
  MESSAGES.deleteOne({$and :[{to : request.to},{title : request.title}]}, function(err,result) {
    console.log('--- Delete Message Test ---');
    if(error) {
      console.error();
      callback('delete Message error')
    }
    else{
      if (typeof callback === "function"){
        callback('delete Message success');
      };
    }
  });
};


function receiveMessageQuery(req, callback) { //req : from
  var request = req;

  userName2UidQuery(requset,function(uid){
    MESSAGES.find({ to : uid }, { _id:0, __v:0, to:0 },function(error,message) {
      if(error){
        console.log(error);
        callback('receiveMessge error');
      } else{
        console.log(message);
        deleteMessageQuery(message, function(deleteMessage){
          if (typeof callback === "function"){
            callback(message);
          };
        });
      }
    });
  })
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
  todayScheduleQuery: todayScheduleQuery,
  scheduleQuery: scheduleQuery,
  shareScheduleQuery: shareScheduleQuery,
  requiredItemQuery: requiredItemQuery,
  userId2UidQuery: userId2UidQuery,
  userName2UidQuery: userName2UidQuery,
  familyNumber2userNameQuery: familyNumber2userNameQuery,
  insertScheduleQuery: insertScheduleQuery,
  updateScheduleQuery: updateScheduleQuery,
  deleteScheduleQuery: deleteScheduleQuery,
  insertUserQuery: insertUserQuery,
  sendMessageQuery: sendMessageQuery,
  receiveMessageQuery: receiveMessageQuery,
  veiwMessageQuery: veiwMessageQuery,
  howManyMassageQuery: howManyMassageQuery
};
