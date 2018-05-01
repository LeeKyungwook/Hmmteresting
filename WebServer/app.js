var express = require('express')
var app = express();
var port = 7000;
var bodyParser = require('body-parser');
var formidable = require('formidable');
var rootDir = __dirname.replace('','');
var fs = require('fs-extra');


var mongoose = require('mongoose');
mongoose.connect('mongodb://127.0.0.1:27017/oikwho');
var db = mongoose.connection;
db.on('error', function(){
  console.log('Connection failed!');
});

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

var USERS = mongoose.model('users', users);
var SCHEDULES = mongoose.model('schedules', schedules);
var REQITEMS = mongoose.model('reqitems', reqItems);

var uid; //uid will be initiated after receiving image from rasperryPi
var today = new Date();
var mm = today.getMonth()+1;
var yyyy = today.getFullYear();
var thisDate = (yyyy-2000)*10000 + mm*100;


app.use(bodyParser.json());
app.use(bodyParser.urlencoded({extended : true}));

var postdata = '';
app.get('/', (req, res, next) => {
    res.send('hello world!');
});


app.post('/', function(req,res) {
  

var form = new formidable.IncomingForm();
  form.parse(req, function(err, fields, files) {
    if (err) {
      console.error(err);
    }
  });
  form.on('end', function(fields, files) {
    for (var i=0; i < this.openedFiles.length; i++) {
      var tempPath = this.openedFiles[i].path;
      var fileName = this.openedFiles[i].name;
      var fileExt = fileName.split(".")[fileName.split(".").length-1].toLowerCase();
      var index = fileName.indexOf('/');
      var newLoc = rootDir + '/';

      var newFileName = 'result.' + fileExt;
      console.log(tempPath, newLoc + newFileName);
      fs.copy(tempPath, newLoc + newFileName, function(err) {
     
   if (err) {
          console.error(err);
        } else {
          console.log(newLoc + newFileName + ' has been saved!');
          res.send('Save Completed');
            
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
        }
      });
    }
  }); 
});

app.listen(port, () => {
    console.log(`Server is running at ${port}`);
});
