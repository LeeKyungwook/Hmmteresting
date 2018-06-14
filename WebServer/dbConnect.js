var express = require('express');
var router = express.Router();
var mongoose = require('mongoose');
mongoose.connect('mongodb://127.0.0.1:27017/oikwho');
var db = mongoose.connection

db.on('error', function(){
  console.log('Connection failed!');
});


var users = mongoose.Schema({
  name: 'string',
  id:'string',
  pw:'string',
  family:'string'
});

var schedules = mongoose.Schema({
  title:'string',
  User:'string',
  startDate:'string',
  startTime:'string',
  endDate:'string',
  endTime:'string',
  reqItems:'string',
  isBroadcast:'string'
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
  console.log("today req : "+JSON.stringify(req));
  SCHEDULES.find({ $and:[{User : req.name}, { startDate:req.thisDate }, {endDate:req.thisDate}] }, { __v:0}).sort({startTime:1}).exec(function(error, schedules) {
    console.log('--- Read today\'s Schedules of User' +' ---');
    console.log(schedules);
    if (typeof callback === "function"){
      callback(schedules);
    };
  });
};

function scheduleQuery(req, callback) {
  //search 20180531's schedules
  console.log("schdule req : "+JSON.stringify(req));
  SCHEDULES.find({ $and:[{User : req.name}, { startDate:{$lte:req.thisDate} }, {endDate:{$gte:req.thisDate}},{isBroadcast : "0"}] }, { __v:0}).sort({startTime:1}).exec(function(error, schedules) {
    // SCHEDULES.find({ $and:[{ startDate:{$lte:req.thisDate} }, {endDate:{$gte:req.thisDate}}] }, { __v:0}, function(error, schedules) {
    console.log('--- Read Schedules of User'+' ---');
    console.log(schedules);
    if (typeof callback === "function"){
      callback(schedules);
    };
  });
};


function shareScheduleQuery(req, callback) {
  //search 20180531's schedules
  console.log("share req : "+JSON.stringify(req));
  SCHEDULES.find({ $and:[{ startDate:{$lte:req.thisDate} }, {endDate:{$gte:req.thisDate}}, {isBroadcast : "1"}] }, { __v:0}).sort({startDate:1}).exec(function(error, schedules) {
    console.log('--- Read Share Schedules of User' +' ---');
    console.log(schedules);
    if (typeof callback === "function"){
      callback(schedules);
    };
  });
};


function requiredItemQuery(req, callback){
  //search today's requiredItems
  SCHEDULES.find({ $and:[{User : req.name}, { startDate:{$lte:req.thisDate} }, {endDate:{$gte:req.thisDate}}] }, {_id:0, __v:0, title:0, User:0, startDate:0, endDate:0, startTime:0, endTime:0, isBroadcast:0},function(error, reqitems){
    console.log('--- Read Required today\'s Item List of User ' + ' ---');
    console.log(reqitems);
    if (typeof callback === "function"){
      callback(reqitems);
    };
  });
};


function isUserIdQuery(req, callback){ //변수
  var request = req;
  console.log('111req111 :'+request);
  USERS.find({ id: request }, { _id:0, __v:0 },function(error, users) {
    console.log('--- 1User Info Test ---');
    console.log(users);
    if (typeof callback === "function"){
      callback(users);
    };
  });
};


function isUserNameQuery(req, callback){ //변수
  var request = req;
  console.log('222req222 :'+request);
  USERS.find({ name: request }, { _id:0, __v:0 },function(error, users) {
    console.log('--- 2User Info Test ---');
    console.log(users);
    if (typeof callback === "function"){
      callback(users);
    };
  });
};


function familyNumber2userNameQuery(req, callback){ //변수
  console.log('333req333 :'+req);
  USERS.find({ family : req }, { _id:0, __v:0 },function(error,users) {
    console.log('--- 3User Info Test ---');
    console.log(users);
    if (typeof callback === "function"){
      console.log(users[0].name);
      callback(users[0].name);
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
  SCHEDULES.update({ _id : request._id},{$set:{title:request.title, User: request.User, startDate: request.startDate, startTime: request.startTime, endDate: request.endDate, endTime:request.endTime , reqItems : request.reqItems, isBroadcast:request.isBroadcast}}, function(error, schedules) {
    console.log('--- Update Info Test ---');
    console.log(schedules);
    if (typeof callback === "function"){
      callback('update Schedule success');
    };
  });
};


function deleteScheduleQuery(req, callback){
  //delete schedule by its ObjectId
  var request = req;
  SCHEDULES.deleteOne({_id : request._id}, function(err, users) {
    console.log('--- Delete Schedule Test ---');
    if (typeof callback === "function"){
      console.log('delete Schedule success');
      callback('delete Schedule success');
    };
  });
};


function insertUserQuery(req, callback){ //req : name Uid pw
  var request = req;
  console.log(request);
  console.log(JSON.stringify(request));
  db.collection('users').insert(request);
  console.log('insert user success');
  return callback('insert user success');
};

function checkUserQuery(req, callback){

  console.log("ididid" + req.id);
  console.log("pwpwpw" + req.pw);
  USERS.find({ $and:[{id : req.id}, { pw:req.pw }] }, { _id:0, __v:0 },function(error,userName) {
    console.log('--- checkcheck ---');
    console.log(userName);
    if (typeof callback === "function"){
      callback(userName);
    }
  });
}


function sendMessageQuery(req, callback) { //req : from to title
  var request = req;

  console.log("tototototo : "+request.to);
  console.log("fromfrom : "+request.from);
  console.log("titletitle : "+request.title);

  if(request.to == '엄마' || request.to == '아빠'||request.to == '누나'||request.to == '동생' ||request.to == '와이프'||request.to == '남편'||request.to == '딸'||request.to == '아들'){
    if(request.to == '엄마' || request.to == '와이프'){
      request.to = 'mother' ;
    }else if (request.to == '아빠' || request.to == '남편') {
      request.to = 'father';
    }else if (request.to == '누나'||request.to == '딸') {
      request.to = 'daughter';
    }else if (request.to == '동생'||request.to == '아들') {
      request.to = 'son';
    }

    familyNumber2userNameQuery(request.to, function(toUserName){
      console.log(toUserName);
      request.to = toUserName;
      console.log("????????????");
      console.log(JSON.stringify(request));
      db.collection('messages').insert(request);
      console.log('insert messages success');
      return callback('insert messages success');
    });
  }
  else{

  }
};


function deleteMessageQuery(req, callback){
  var request = req;
  console.log(request);
  MESSAGES.deleteOne({$and :[{from : request.from}, {title : request.title}]}, function(err,result) {
    console.log('--- Delete Message Test ---');
    console.log(result);
    if (typeof callback === "function"){
      callback('delete Message success');
    };
  });
};


function receiveMessageQuery(req, callback) { //req : from
  var request = req;

  MESSAGES.find({ to : request }, { _id:0, __v:0, to:0 },function(error,messageList) {
    console.log(messageList);
    callback(messageList);
  });
};


//////////////////아직안씀
function veiwMessageQuery(req, callback) { //req = userName
  MESSAGES.find({to:req})
  .sort({title:1})
  // .limit(5)
  .exec(function(err, messages){
    console.log(messges);
    return callback(messges);
  });
};


function howManyMassageQuery(req, callback) {
  MESSAGES.find({to:req}).count().exec(function(err, messagesNum){
    return callback(messagesNum);
  });
};

module.exports = {
  todayScheduleQuery: todayScheduleQuery,
  scheduleQuery: scheduleQuery,
  shareScheduleQuery: shareScheduleQuery,
  requiredItemQuery: requiredItemQuery,
  isUserIdQuery: isUserIdQuery,
  isUserNameQuery: isUserNameQuery,
  familyNumber2userNameQuery: familyNumber2userNameQuery,
  insertScheduleQuery: insertScheduleQuery,
  updateScheduleQuery: updateScheduleQuery,
  deleteScheduleQuery: deleteScheduleQuery,
  checkUserQuery: checkUserQuery,
  insertUserQuery: insertUserQuery,
  sendMessageQuery: sendMessageQuery,
  receiveMessageQuery: receiveMessageQuery,
  deleteMessageQuery: deleteMessageQuery,
  veiwMessageQuery: veiwMessageQuery,
  howManyMassageQuery: howManyMassageQuery
};

