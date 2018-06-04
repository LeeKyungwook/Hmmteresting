var express = require('express');
var router = express.Router();
var bodyParser = require('body-parser');

router.use(bodyParser.json());
router.use(bodyParser.urlencoded({extended : true}));

router.get('/',function(req, res){
  console.log("#############");
});

function getWeather(callback) {

  var weatherData = {
    summary: 'Clear',
    icon: 'clear-night',
    temperature: 10.43,
    humidity: 0.58,
    windSpeed: 2.84,
  };

  var Forecast = require('forecast');

  var forecast = new Forecast({
    service: 'forecast.io',
    key: '3ed04ee2692b0344d29afd96a34b18f',
    units: 'celcius',
    cache: true,
    ttl:{
      minutes: 27,
      seconds: 45
    }
  });

  forecast.get([37.5,127], function(err, weather){
    console.dir(weather.currently);
    if(err) return console.dir(err);
    weatherData = weather.currently
    if (typeof callback === "function"){
      return callback(weatherData);
    }else return weatherData;
  });
};

module.exports = {
  getWeather: getWeather
};
