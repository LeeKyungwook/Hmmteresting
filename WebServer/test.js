var express = require('express');
var router = express.Router();

function testfunction(req, res){
  console.log('***test***');
  console.log(req.body);
};

console.log('tttttttttttesttttttttttttt');

module.exports = {
  testfunction: testfunction
};
