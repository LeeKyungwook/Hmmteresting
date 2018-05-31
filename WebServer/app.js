var express = require('express')
var app = express();
var port = 7000;
var bodyParser = require('body-parser');
var formidable = require('formidable');
var async = require('async');
var PythonShell = require('python-shell');
var fs = require('fs');

//var rootDir = __dirname.replace('','');
var userID ='jh';
var filePath;

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
  startDate: '2018-05-10-19:30',
  endDate: '2018-05-10',
  title: '저녁약속',
  where: '강남역'
}

var schedule2 = {
  startDate: '2018-05-10-19:30',
  endDate: '2018-05-10',
  title: '수현쓰랑 저녁약속',
  where: '강남역'
}

var schedule = [schedule1, schedule2];

var string = "{'key':'value'}";

app.post('/',function(req, res){
  console.log(req.body);
  res.send(req.body);
});

app.get('/test', function (req, res) {
  // fs.readFile('jh.jpg', function (error, data){
  //   res.end(data)
  // })
  res.render('index', {
    title: "Hmmteresting Demo",
    startDate: schedule1.startDate,
    endDate : schedule1.endDate,
    title_name : schedule1.title,
    where : schedule1.where
  });
});

app.get('/imgs', function (req,res){
  fs.readFile('jh.jpg', function (error, data){
    res.end(data)
  })
})

var request, response;
app.post('/init', function(req,res) { //날씨, 스케쥴 초기에 보여주기 +초기에 받은 메세지 개수도 보여줘야,,,
  request = req;
  async.waterfall(
    [
      function(callback) {
        fileRouter.fileDownloadRaz(request, function(newFileName){
          filePath = newFileName;
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
      var weather;
      var messageNum;

      if(arg1.indexOf('None Detected')>=0){
        return res.send('who are you?');
      }

      messageNum = 3;

      weatherRouter.getWeather(function(weather_){
        weather = weather_;
        //	callback(null, weather, schedule, messageNum);
      });

      //*DB
      //      messageNum = 3
      //*

      dbConnectRouter.scheduleQuery(arg1,userID,function(schedule){
        dbConnectRouter.requiredItemQuery(userID,function(requiredItem){
          callback(null, weather, schedule, requiredItem, messageNum);
        });
      });
    },

    function(arg1, arg2, arg3, arg4,callback) { // arg1 = userName, arg2 = shedule, arg3 = requiredItem, arg4 = messageNum
      res.json({weather: arg1, schedule : arg2, requiredItem : arg3, messageNum : arg4});
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


var path = require('path');
function decode_base64(base64str , filename){
  var buf = Buffer.from(base64str,'base64');
  fs.writeFile(path.join(__dirname,'/',filename), buf, function(error){
    if(error){
      throw error;
    }else{
      console.log('File created from base64 string!');
      return true;
    }
  });
}

app.post('/jhTest', function(req,res) {
  console.log(req);
  res.send(schedule1);
});

app.post('/join', function(req,res) { //회원가입
  var userName = req.body.name;
  userID = req.body.userID;
  console.log(req.body);
  // decode_base64(req.body.image,'androidTest.jpg');
  // decode_base64(req.body.command.image,'androidTest.jpg');

  ///디비 쿼리,,,,,,,, 확인,,,,
  res.send('join us *^^*');

  // res.send('alread joinInfo');


  return;
});



app.post('/joinPicture', function(req,res) { //회원가입
  /*
  1. 사진 5장 받기
  2. 얼굴,,,,,,,,,처리,,,,, -> if else 사진에 얼굴이 제대로 없으면 처리! -> 어떻게..?
  3. 안드로이드로 결과 전송
  4. 끝!
  */
});
