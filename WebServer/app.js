var express = require('express')
var app = express();
var port = 7000;
var bodyParser = require('body-parser');
var formidable = require('formidable');
var rootDir = __dirname.replace('','');
var fs = require('fs-extra');

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
        }
      });
    }
  }); 
});

app.listen(port, () => {
    console.log(`Server is running at ${port}`);
});
