var express = require('express');
var router = express.Router();

var dbQueryRouter = require('./dbQuery');

var mongoose = require('mongoose');
mongoose.connect('mongodb://127.0.0.1:27017/oikwho');
var db = mongoose.connection

db.on('error', function(){
  console.log('Connection failed!');
});

module.exports = router;
