var express = require('express');
var app = express();
var port = 7000;
var bodyParser = require('body-parser');
var formidable = require('formidable');
var async = require('async');
var PythonShell = require('python-shell');
var fs = require('fs');
require('date-utils');


var username ='none';
var usernamejpg =username+'.jpg';
global.userName = username;
global.userNameJpg = usernamejpg;

app.use(bodyParser.json({limit:'50mb'}));
app.use(bodyParser.urlencoded({extended : true, limit:'50mb'}));

var dbConnectRouter = require('./dbConnect');
var weatherRouter = require('./weather');
var fileRouter = require('./file');

app.get('/', (req, res, next) => {
  res.send('hello world!');
});

app.listen(port, () => {
  console.log(`Server is running at ${port}`);
});

var string = "{'key':'value'}";

app.post('/',function(req, res){
  console.log(req.body);
  res.send(req.body);
});

var request, response;
app.post('/init', function(req,res) { 
  request = req;
  async.waterfall(
    [
      function(callback) {
        fileRouter.fileDownloadRaz(request, function(filePath){
          callback(null, filePath);
        });
      },

      function(arg1, callback) { 
        var options = {
          mode: 'text',
          pythonPath: '',
          pythonOptions: ['-u'],
          scriptPath: '',
          args: arg1
        };
        console.log("==> face_recognize arg1 : "+ arg1);
        PythonShell.run('../face_detection/src/raz_face_resize_alignment.py',options, function(err, result){
          if(err) {
            console.log(err);
            return res.send(err);
          }
          console.log("<== face_recognize result : "+result)
          callback(null, result);
        });
      },

      function(arg1, callback) {
        var options = {
          mode: 'text',
          pythonPath: '',
          pythonOptions: ['-u'],
          scriptPath: '',
          args: arg1
        };

        console.log("==> align_img arg1 : "+ arg1);
        if(arg1 == 'Error2 : No Face Found'){
          return res.send('cannot find face');
        }else if(arg1 == 'Error3 : Too Many Faces'){
          return res.send('Too Many Faces');
        }else {
          PythonShell.run('../caffe/extract_feature/FaceFeatureExtractor.py',options, function(err, result){
            if(err) {
              console.log(err);
              return res.send(err);
            }
            console.log("<== align_img result : "+result);

            callback(null, result);
          });
        }
      },

      function(arg1, callback){
        var messageNum;
        if(arg1.toString() == 'None Detected '){
          return res.send('who are you?');
        }else {
          global.userName = (arg1.toString()).split(".")[0];
          global.userNameJpg = global.userName + '.jpg'

          var date = (new Date()).toFormat('YYYYMMDD');
          var json = Object();
          json.name = global.userName;
          json.thisDate = date;

          dbConnectRouter.howManyMassageQuery(global.userName, function(messageNum){
            weatherRouter.getWeather(function(weather){
              dbConnectRouter.todayScheduleQuery(json, function(todaySchedule){
                dbConnectRouter.scheduleQuery(json, function(schedule){
                  dbConnectRouter.shareScheduleQuery(json, function(shareSchedule){
                    dbConnectRouter.requiredItemQuery(json, function(requiredItem){
                       dbConnectRouter.receiveMessageQuery(global.userName, function(messageList){
                        callback(null, weather, todaySchedule, schedule, shareSchedule, requiredItem, messageNum, messageList);
                      });
                    });
                  });
                });
              });
            });
          });
        }
      },

      function(arg1, arg2, arg3, arg4, arg5, arg6, arg7, callback) { //arg1 = weather, arg2 = todaySchedule, arg3 = schedule, arg4 = shareSchedule, arg5 = requiredItem, arg6 = messageNum

        var json = {
          weather : arg1
        };

        var arr = [];

        var i =0;
        if(i<arg5.length){
          arr.push(arg5[i].reqItems);
          i++;
        }
        json.user = global.userName;
        json.todaySchedule = arg2;
        json.schedule = arg3;
        json.shareSchedule = arg4;
        json.requiredItem = arr;
        json.messageNum = arg6.toString();
        json.messageList = arg7;
        console.log(JSON.stringify(json));
        res.json(json);
        callback(null, 'done');
      }
    ],

    function (err, result) {
      console.log( result );
    }
  );
});


global.userInfo = null;

app.post('/joinCheckID', function(req, res){
  dbConnectRouter.isUserIdQuery(req.body.id, function(user2){
    if(user2[0] != null){
      console.log('false');
      return res.send('false');
    }
    else{
      console.log('true');
      return res.send('true');
    }
  });
});


app.post('/joinCheckName', function(req, res){
  dbConnectRouter.isUserNameQuery(req.body.name, function(user1){
    if(user1[0] != null){
      console.log('false');
      return res.send('false');
    }else {
      console.log('true');
      return res.send('true');
    }
  });
});


app.post('/joinInfo', function(req,res){ 
  var request = req.body;
  global.userInfo = request;
  return res.send('give me picture');
});


app.post('/joinPicture', function(req,res) { 
  async.waterfall(
    [
      function(callback) {
        fileRouter.fileDownloadAndroid(req, function(filePath){
          callback(null, filePath);
        });
      },

      function(arg1, callback) {  //arg1 = filePath
        var options = {
          mode: 'text',
          pythonPath: '',
          pythonOptions: ['-u'],
          scriptPath: '',
          args: arg1
        };
        console.log("==> android face_recognize arg1 : "+ arg1);
        PythonShell.run('../face_detection/src/android_face_resize_alignment.py',options, function(err, result){
          if(err) {
            console.log(err);
            return res.send(err);
          }
          console.log("<== android face_recognize result : "+result);
          callback(null, result);
        });
      },

      function(arg1, callback) {
        if(arg1 == 'Error2 : No Face Found'){
          return res.send('cannot find face');
        }else if(arg1 == 'Error3 : Too Many Face'){
          return res.send('Too Many Face');
        }else {
          callback(null, 'done');
        }
      }
    ],
    function (err, result) {
      console.log( result );
      dbConnectRouter.insertUserQuery(global.userInfo, function(result){
        if(result == 'insert user success'){
          global.userInfo = null;
          return res.send('join us *^^*');
        }else {
          return res.send(result);
        }
      });
    }
  );
  return;
});


app.post('/login', function(req, res) { //req id, pw;
  console.log(req.body)
  dbConnectRouter.checkUserQuery(req.body, function(result){
    if(result[0] == null) name = 'null';
    else name = result[0].name;
    res.send(name);
  });
});


app.post('/showSchedule', function(req,res) {
  console.log(req.body);
  dbConnectRouter.todayScheduleQuery(req.body, function(schedule){
    var json = {
      schedules : schedule
    };
    console.log("in /showSchedule  "+JSON.stringify(json));
    return res.json(json);
  });
});

app.post('/addSchedule', function(req,res) {
  console.log(req.body);
  dbConnectRouter.insertScheduleQuery(req.body, function(result){
    console.log(result);
    return res.send(result);
  });
});

app.post('/updateSchedule', function(req,res) {
  console.log(req.body);
  dbConnectRouter.updateScheduleQuery(req.body, function(result){
    console.log(result);
    return res.send(result);
  });
});

app.post('/deleteSchedule',function(req, res){
  console.log(req.body);
  dbConnectRouter.deleteScheduleQuery(req.body, function(result){
    console.log(result);
    return res.send(result);
  });
});


/*--------RASPBERRY--------------------------------------------------------------------------------------------*/
global.client_Param = null
global.msg_recipient = null
global.recv_msg_num = null
global.recv_msg = null
global.luvU = null

app.post('/init/sendMessage', function(req,res) {
  var title = 'abcdefg.ts';
  console.log("??? " +req.body.title);
  title = ((req.body.title).toString()).split("/")[((req.body.title).toString()).split("/").length-1];
  title = title.split(".")[0];
  title = title+".ts";

  var json = {
    from : global.userName,
    to : global.msg_recipient,
    title : title
  };
  console.log(json);
  dbConnectRouter.sendMessageQuery(json, function(result){
    global.msg_recipient = null;
    return res.send(result);
  });
});


app.post('/stt',function(req, res){
  var input = req.body.command
  console.log(input);
  if(input.indexOf('에게 메시지 보내 줘') != -1){
    var strArray = input.split('에게');
    global.msg_recipient = strArray[0];

    if(global.msg_recipient == '엄마' || global.msg_recipient == '아빠'||global.msg_recipient == '누나'||global.msg_recipient == '동생' ||global.msg_recipient == '와이프'||global.msg_recipient == '남편'||global.msg_recipient == '딸'||global.msg_recipient == '아들'){
      global.client_Param = '1';
      console.log(global.msg_recipient);
      return res.send('1');
    }else {
      console.log('그런 사용자가 없습니다.');
      global.client_Param = '5';
      return res.send('5');
    }
  }else if (input.indexOf('보여 줘') != -1){
    var strArray = input.split(' 번째');
    global.recv_msg_num = strArray[0];

    if(global.recv_msg_num == '첫'){
      global.recv_msg_num = 1;
    }else if(global.recv_msg_num == '두'){
      global.recv_msg_num = 2;
    }else if(global.recv_msg_num == '세'){
      global.recv_msg_num = 3;
    }else if(global.recv_msg_num == '네'){
      global.recv_msg_num = 4;
    }else if(global.recv_msg_num == '다섯'){
      global.recv_msg_num = 5;
    }else {
      console.log(".......................");
      return res.send('4');
    }

    dbConnectRouter.receiveMessageQuery(global.userName, function(messageList){
      console.log('messageList.length :'+messageList.length);
      if(messageList.length > 0 && messageList.length >= global.recv_msg_num){
        global.recv_msg = messageList[global.recv_msg_num-1];
        dbConnectRouter.deleteMessageQuery(global.recv_msg, function(result){
          global.recv_msg_num = null;
          global.client_Param = '2';
          return res.send('2');
        });
      }else{
        console.log('해당 메시지가 존재하지 않습니다.');
        global.client_Param = '4';
        return res.send('4');
      }
    });
  }else if ((input.indexOf('오겠습니다') != -1)){
    global.client_Param = '3';
    res.send('3');
  }else if ((input.indexOf('사랑해') != -1)){
    global.luvU = 1;
    global.client_Param = '4';
    res.send('4');
  } else {
    global.client_Param = '4';
    res.send('4');
  }
});


app.post('/raz_client/ILuvU', function(req, res) {
  var response = new Object();

  if(global.luvU == 1){
    console.log('ILuvU');
    response.luvU = global.luvU;
    global.luvU = null;
    console.log(JSON.stringify(response))
    res.json(response);
  }else{
    response.luvU = 0;
    global.luvU = null;
    res.json(response)
  }
})

app.post('/raz_client', function(req, res) {
  console.log("*************************************************");
  var response_client = new Object();

  if (global.client_Param == '1') {         /////////////////////// 누구누구에게 보내 줘
    response_client.client_Param = global.client_Param;
    global.client_Param = null;
    return res.json(response_client);
  } else if (global.client_Param == '2'){   /////////////////////// 메세지 보여 줘
    response_client.client_Param = global.client_Param;

    var date = (new Date()).toFormat('YYYYMMDD');
    var json1 = Object();
    json1.name = global.userName;
    json1.thisDate = date;

    dbConnectRouter.howManyMassageQuery(global.userName, function(messageNum){
      weatherRouter.getWeather(function(weather){
        dbConnectRouter.todayScheduleQuery(json1, function(todaySchedule){
          dbConnectRouter.scheduleQuery(json1, function(schedule){
            dbConnectRouter.shareScheduleQuery(json1, function(shareSchedule){
              dbConnectRouter.requiredItemQuery(json1, function(requiredItem){
                dbConnectRouter.receiveMessageQuery(global.userName, function(messageList){
                  var json = {
                    weather : weather
                  };
                  var arr = [];
                  var i =1;
                  if(i<requiredItem.length){
                    arr.push(requiredItem[i].reqItems);
                    i++;
                  }
                  json.user = global.userName;
                  json.todaySchedule = todaySchedule;
                  json.schedule = schedule;
                  json.shareSchedule = shareSchedule;
                  json.requiredItem = arr;
                  json.messageNum = messageNum.toString();
                  json.messageList = messageList;
                  json.client_Param = global.client_Param;
                  json.messageTitle = global.recv_msg.title;

                  global.recv_msg = null;
                  global.recv_msg_num = null;
                  console.log(JSON.stringify(json));
                  global.client_Param = null;
                  res.json(json);
                });
              });
            });
          });
        });
      });
    });
  } else if (global.client_Param == '3') { //////////////////////// 다녀오겠습니다
    response_client.client_Param = global.client_Param;
    global.client_Param = null;
    global.msg_recipient = null
    global.userName = null;
    return res.json(response_client);
  }else if (global.client_Param == '5') { //////////////////////// 다녀오겠습니다
    response_client.client_Param = global.client_Param;
    global.client_Param = null;
    global.msg_recipient = null;
    
    return res.json(response_client);
  } else {                           //////////////////////// Nothing
    response_client.client_Param = global.client_Param;
    global.client_Param = null;
    console.log("luvU :"+global.luvU);
    console.log("nothing..... ");
    return res.json(response_client);
  }
});
