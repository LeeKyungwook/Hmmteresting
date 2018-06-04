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

var dbConnectRouter = require('./dbConnect');
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
  startDate: '2018-06-04',
  startTime:'12:00',
  endDate: '2018-06-04',
  endTime: '13:00',
  reqItmes: '',
  isBroadcast: '1'
}

var schedule2 = {
  title: '캡스톤 수업',
  user: '123',
  startDate: '2018-06-04',
  startTime:'16:30',
  endDate: '2018-06-04',
  endTime: '21:00',
  reqItmes: '노트북',
  isBroadcast: '1'
}

var schedule = [schedule1, schedule2];

var requiredItem = ['노트북', '과자']

var string = "{'key':'value'}";

app.post('/',function(req, res){
  console.log(req.body);
  res.send(req.body);
});

app.get('/init', function (req, res) {

  if(userName == 'none'){
    res.render('index2',{});
  }else {
    res.render('index', {
      name: global.userName,
      title: "Hmmteresting Demo",
      startDate: schedule1.startDate,
      endDate : schedule1.endDate,
      title_name : schedule1.title,
      where : schedule1.where
    });
  }
});

app.get('/imgs', function (req,res){
  fs.readFile('./image/'+global.userNameJpg, function (error, data){
    console.log("userNameJpg    "+global.userNameJpg);
    res.end(data)
  })
})

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
        // arg1 = jh2.jpg
        var weather;
        var messageNum;

        console.log("arg1  :*"+arg1+"*");
        if(arg1 == 'None Detected '){
          return res.send('who are you?');
        }else {
          global.userName = (arg1.toString()).split(".")[0];
          global.userNameJpg = global.userName + '.jpg'
        

        var date = (new Date()).toFormat('YYYYMMDD');

        var json = {
          userName : global.userName,
          startDate : date,
          endDate : date
        };

         //*DB
         messageNum = "1";
         //*

        weatherRouter.getWeather(function(weather_){
          weather = weather_;
          callback(null, weather, schedule, requiredItem, messageNum);
        });

       

        //callback(null, weather, schedule, requiredItem, messageNum);

        /*dbConnectRouter.scheduleQuery(json, function(schedule){
          dbConnectRouter.requiredItemQuery(json, function(requiredItem){
            callback(null, weather, schedule, requiredItem, messageNum);
          });
        });*/
      }
      },

      function(arg1, arg2, arg3, arg4, callback) { // arg1 = weather, arg2 = shedule, arg3 : requiredItem, arg4 = messageNum
        if(global.userName == '남수현'){
          arg2 = "None";
          arg3 = "None";
          arg4 = "0";
        }
        //arg4 = arg4.toString();
        res.json({name: global.userName, weather: arg1, schedule : arg2, requiredItem : arg3, messageNum : arg4});
        callback(null, 'done')
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


app.post('/veiwMessage', function(req,res) { //메세지 출력
  /*
  1. 디비에 메세지 이름 리스트 쿼리보내기
  2. 결과받기 (sender / messageTitle)
  3. json 형태로 라즈베리에 전송
  */
  // var messges = dbRouter.veiwMessageQuery(ID,res);
  res.json(messges);
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
          /* insertUser */
          return res.send('joinInfo accept');
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


app.post('/showSchedule', function(req,res) { //req = 날짜
  // scheduleQuery()
  console.log(req.body);
  res.send(schedule);
});

app.post('/addSchedule', function(req,res) { //req = 날짜
  dbConnectRouter.insertScheduleQuery(req.body, function(result){
    res.send(result);
  });
});

