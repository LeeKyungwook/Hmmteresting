var express = require('express')
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

// var dbConnectRouter = require('./dbConnect');
var weatherRouter = require('./weather');
var fileRouter = require('./file');

app.set('views', __dirname + '/views');
app.set('view engine', 'ejs');
app.engine('html', require('ejs').renderFile);
app.use(express.static('public'));

app.get('/', (req, res, next) => {
  res.send('hello world!');
});

app.listen(port, () => {
  console.log(`Server is running at ${port}`);
});

var schedule1 = {
  title: '점심약속',
  user: '123',
  startDate: '20180604',
  startTime:'1200',
  endDate: '20180604',
  endTime: '1300',
  reqItmes: '과자',
  isBroadcast: '1'
}

var schedule2 = {
  title: '캡스톤 수업',
  user: '123',
  startDate: '20180604',
  startTime:'1630',
  endDate: '20180604',
  endTime: '2100',
  reqItmes: '노트북',
  isBroadcast: '1'
}

var schedule3 = {
  title: '컴비과제',
  user: '123',
  startDate: '20180605',
  startTime:'1030',
  endDate: '20180605',
  endTime: '2100',
  reqItmes: '컴비소스',
  isBroadcast: '1'
}

var schedule = [schedule1, schedule2,schedule3];

var requiredItem = ['노트북', '과자'];


var string = "{'key':'value'}";

app.post('/',function(req, res){
  console.log(req.body);
  res.send(req.body);
});

var request, response;
app.post('/init', function(req,res) { //날씨, 스케쥴 초기에 보여주기 +초기에 받은 <메세지 개수>도 보여줘야,,,
  request = req;
  async.waterfall(
    [
      function(callback) {
        fileRouter.fileDownloadRaz(request, function(filePath){
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
        //arg1 = 'not found' or imagePath
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
            console.log("<== align_img result : "+result);//result = jh2.jpg
            callback(null, result);
          });
        }
      },

      function(arg1, callback){
        var messageNum;
        if(arg1.toString() == 'None Detected '){
          return res.send('who are you?');
        }else {
          // global.userName = (arg1.toString()).split(".")[0];
          global.userName = '이준호';
          global.userNameJpg = global.userName + '.jpg'

          var date = (new Date()).toFormat('YYYYMMDD');
          // var json = {
          //   userName : global.userName,
          //   thisDate : date
          // };
          // json = JSON.stringify(json);

          //*DB
          messageNum = 1;
          //*

          weatherRouter.getWeather(function(weather){
            dbConnectRouter.todayScheduleQueryscheduleQuery(json, function(todaySchedule){
              dbConnectRouter.scheduleQuery(json, function(schedule){
                dbConnectRouter.shareScheduleQueryscheduleQuery(json, function(shareSchedule){
                  dbConnectRouter.requiredItemQuery(json, function(requiredItem){
                    callback(null, weather, todaySchedule, schedule, shareSchedule, requiredItem, messageNum);
                  });
                });
              });
            });
          });
        }
      },

      function(arg1, arg2, arg3, arg4, arg5, arg6,callback) { //arg1 = weather, arg2 = todaySchedule, arg3 = schedule, arg4 = shareSchedule, arg5 = requiredItem, arg6 = messageNum

        var json = {
          weather : arg1
        };
        json.user = global.userName,
        json.todaySchedule = arg2,
        json.schedule = arg3,
        json.shareSchedule = arg4,
        json.reauiredItem = arg5,
        json.messageNum = arg6.toString();
        res.json(json);
        callback(null, 'done');
      }
    ],

    function (err, result) {
      console.log( result );
    }
  );
});


app.post('/jhTest', function(req,res) {
  console.log(req);
  res.send(schedule1);
});


app.post('/join', function(req,res) { //회원가입
  request = req;
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
        }else if(arg1 == 'Error3 : Too Many Faces'){
          return res.send('Too Many Faces');
        }else {
          callback(null, 'done');
        }
      }
    ],
    function (err, result) {
      console.log( result );
      res.send('Hello!');
    }
  );

  return;
});


app.post('/joinInfo', function(req,res){ //사용자의 정보가 db에 있는지 없는지 확인하고 없으면 insert
  var request = req;

  dbConnectRouter.userName2UidQuery(request, function(Uid){
    if(Uid != null){
      console.log('이미 존재하는 이름입니다.');
      return res.send('이미 존재하는 이름입니다.');
    }else {
      dbConnectRouter.userId2UidQuery(request, function(Uid){
        if(Uid != null){
          console.log('이미 존재하는 ID 입니다.');
          return res.send('이미 존재하는 ID 입니다.');
        }else {
          dbConnectRouter.insertUserQuery(request, function(result){
            if(result == 'insert user success'){
              return res.send('join us *^^*');
            }else {
              return res.send(result);
            }
          });
        }
      });
    }
  });
});


app.post('/joinPicture', function(req,res) { //회원가입
  request = req;
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
        }else if(arg1 == 'Error3 : Too Many Faces'){
          return res.send('Too Many Faces');
        }else {
          callback(null, 'done');
        }
      }
    ],
    function (err, result) {
      console.log( result );
      res.send('Nice Meet You *^^*');
    }
  );
  return;
});


app.post('/showSchedule', function(req,res) {
  console.log(req.body);
  dbConnectRouter.scheduleQuery(req.body, function(schedule){
    console.log(schedule);
    res.send(schedule);
  });
});

app.post('/addSchedule', function(req,res) {
  console.log(req.body);
  dbConnectRouter.insertScheduleQuery(req.body, function(result){
    console.log(result);
    res.send(result);
  });
});

app.post('/deleteSchedule',function(req, res){
  console.log(req.body);
  dbConnectRouter.insertScheduleQuery(req.body, function(result){
    console.log(result);
    res.send(result);
  });
});


app.post('/veiwMessage', function(req,res) { //메세지 출력
  /*
  1. 디비에 메세지 이름 리스트 쿼리보내기
  2. 결과받기 (sender / messageTitle)
  3. json 형태로 라즈베리에 전송
  */

  res.json(messges);
});






app.post('/init/sendVideoMessage', function(req,res) { //메세지 출력
  var title = 'abcdefg.ts';

  // global.userName = '이준호';
  // global.msg_recipient = '아빠';

  var json = {
    from : global.userName,
    to : global.msg_recipient,
    title : ((req.body.title).toString()).split("/")[((req.body.title).toString()).split("/").length-1]
    // title : title
  };
  // json = JSON.stringify(json);
  console.log(json);
  // res.json(json);
  dbConnectRouter.sendMessageQuery(json, function(result){
    global.msg_recipient = null;
    return res.send(result);
  });
});


app.post('/init/sendVoiceMessage', function(req,res) { //메세지 출력
  var title = 'abceefg.ts';
  // var title = req.body.title;

  var json = {
    from : global.userName,
    to : global.msg_recipient,
    title : title
  };

  dbConnectRouter.sendMessageQuery(json, function(result){
    global.msg_recipient = null;
    return res.json(result);
  });
});


global.client_Param = null
global.msg_recipient = null

app.post('/stt',function(req, res){

  var input = req.body.command
  if(input.indexOf('에게 보내 줘') != -1){
    var strArray = input.split('에게');
    msg_recipient = strArray[0];
    client_Param = '1';
    console.log(msg_recipient);
    res.send('1')
  }else if (input.indexOf('보여 줘') != -1){
    client_Param = '2';
    res.send('2');
  }else if ((input.indexOf('다녀오겠습니다') != -1)){
    res.send('3');
  } else {
    client_Param = '4';
    res.send('4');
  }
});


app.post('/raz_client', function(req, res) {

  var response_client = new Object();

  if (client_Param == "1") {         /////////////////////// 누구누구에게 보내 줘
    response_client.client_Param = client_Param;
    var JsonInfo = JSON.stringify(response_client);
    client_Param = null;
    return res.send(JsonInfo);
  } else if (client_Param == "2"){   /////////////////////// 메세지 보여 줘
    response_client.client_Param = client_Param;
    response_client.userName = global.userName;
    dbConnectRouter.receiveMessageQuery(global.userName,function(message){
      if(messageTitle == 'receiveMessage error'){
        client_Param = null;
        return res.send(message);
      }else{
        response_client.videoName = message.title;
        var JsonInfo = JSON.stringify(response_client);
        console.log(JsonInfo);
        msg_recipient = null;
        client_Param = null;
        return res.send(JsonInfo);
      }
    });
  } else if (client_Parm == "3") { //////////////////////// 다녀오겠습니다
    response_client.client_Param = client_Param;
    var JsonInfo = JSON.stringfy(response_client);
    client_Param = null;
    return res.send(JsonInfo);
  } else {                           //////////////////////// Nothing
    response_client.client_Param = client_Param;
    var JsonInfo = JSON.stringify(response_client);
    client_Param = null;
    console.log("nothing..... ");
    return res.send(JsonInfo);
  }
});
