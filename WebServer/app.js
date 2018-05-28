var express = require('express')
var app = express();
var port = 7000;
var bodyParser = require('body-parser');
var formidable = require('formidable');
var async = require('async');
var PythonShell = require('python-shell');
var fs = require('fs-extra');

//var rootDir = __dirname.replace('','');
// var userID ='jh';
// var userInfo = {
//   "name" : "jh"
// };
var filePath;

app.use(bodyParser.json());
app.use(bodyParser.urlencoded({extended : true}));

var dbConnectRouter = require('./dbConnect');
var weatherRouter = require('./weather');
var fileRouter = require('./file');

// app.use('/dbConnect',dbConnectRouter);

app.get('/', (req, res, next) => {
  res.send('hello world!');
});

app.listen(port, () => {
  console.log(`Server is running at ${port}`);
});

var schedule1 = {
  startDate: '2018-05-10-19:30',
  endDate: '2018-05-10',
  title: '예나랑 저녁약속',
  where: '강남역'
}

var schedule2 = {
  startDate: '2018-05-10-19:30',
  endDate: '2018-05-10',
  title: '수현쓰랑 저녁약속',
  where: '강남역'
}

var schedule = [schedule1, schedule2];

app.post('/',function(req, res){
  console.log(req.body);
  res.send(req.body);
});

app.post('/test', function(req,res){

});

var request, response;
app.post('/init', function(req,res) { //날씨, 스케쥴 초기에 보여주기 +초기에 받은 메세지 개수도 보여줘야,,,
  request = req;
  async.waterfall(
    [
      function(callback) {
        fileRouter.fileDownloadRaz(request, function(newFileName){
          filePath = newFileName;
          callback(null, filePath);
          // callback(null, 'yena');
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

      function(arg1, callback) { //arg1 = 'not found' or imagePath
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
            //throw err;
            //console.log(err);
            return res.send(err);
          }
          console.log("<== align_img result : "+result)
          callback(null, result);
        });
      }
    },

    function(arg1, callback){ // arg1 = userName
      var str = arg1;
      var weather;
      // var schedule;
      var messageNum;
      if(str.indexOf('None Detected')==0){
        return res.send('who are you?');
      }

      weatherRouter.getWeather(function(weather_){
        weather = weather_;
      });

      //*DB
      messageNum = 3
      //*

      //dbConnectRouter.scheduleQuery(arg1,function(schedule_){
      //schedule = schedule_;
      callback(null, weather, schedule, messageNum);
      // });
    },

    function(arg1, arg2, arg3, callback) { // arg1 = userName, arg2 = shedule, arg3 = messageNum
      res.json({weather: arg1, schedule : arg2, messageNum : arg3});
      callback(null, 'done')
    },
  ],

  function (err, result) {
    console.log( result );
  });
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

app.post('/joinInfo', function(req,res) { //회원가입
  var userName = req.body.name;
  userID = req.body.userID;
  ///디비 쿼리,,,,,,,, 확인,,,,

  // res.send('alread joinInfo');


  return;
}

app.post('/joinPicture', function(req,res) { //회원가입
  /*
  1. 사진 5장 받기
  2. 얼굴,,,,,,,,,처리,,,,, -> if else 사진에 얼굴이 제대로 없으면 처리! -> 어떻게..?
  3. 안드로이드로 결과 전송
  4. 끝!
  */
});
