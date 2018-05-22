var express = require('express')
var app = express();
var port = 7000;
var bodyParser = require('body-parser');
var formidable = require('formidable');
var async = require('async');
var PythonShell = require('python-shell');

//var rootDir = __dirname.replace('','');
//var fs = require('fs-extra');
var ID =0;
var filePath;

app.use(bodyParser.json());
app.use(bodyParser.urlencoded({extended : true}));

var dbConnectRouter = require('./dbConnect');
var weatherRouter = require('./weather');
var fileRouter = require('./file');

app.use('/dbConnect',dbConnectRouter);

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

app.post('/test', function(req,res) {

});

var request, response;
app.post('/init', function(req,res) { //날씨, 스케쥴 초기에 보여주기 +초기에 받은 메세지 개수도 보여줘야,,,
  request = req;
  async.waterfall([
    function(callback) {
      fileRouter.fileDownloadz(request, function(newFileName){
        filePath = newFileName;
        callback(null, filePath); //file.js -> path 수정
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

      PythonShell.run('test.py',options, function(err, result){
        if(err) throw err;
        console.log('path :'+result);
      });
/*--------------------------recognize result-------------------------------*/
      var face = 1;
      if (face != 1){
        return res.send('cannot find face');
      }else{
        console.log('face racognize success');
      }
      callback(null, 'three');
    },
    function(arg1, callback) {
      // dbRouter.query();
      var messageNum = 3;//쿼리문 결과,,,
      var weather = weatherRouter.getWeather();
      res.json({weather: weather, schedule : schedule, messageNum : messageNum});
      callback(null, 'done');
    }
  ],
  function (err, result) {
    console.log( result )
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


app.post('/join', function(req,res) { //회원가입
  /*
  1. 사진 5장 받기
  2. 얼굴,,,,,,,,,처리,,,,, -> if else 사진에 얼굴이 제대로 없으면 처리! -> 어떻게..?
  3. 안드로이드로 결과 전송
  4. 끝!
  */
});
