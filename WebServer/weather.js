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

  /*var Forecast = require('forecast');

  var forecast = new Forecast({
          service: 'forecast.io',
          key: '04f979b9c6e43c2ed0af5a10252ef78f',
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

  //      console.dir();
  //      console.dir(weather.currently.summary);
  //      console.dir(weather.currently.temperature);
  });

  weatherData = weather.currently*/
  if (typeof callback === "function"){
    callback(weatherData);
  }
  return weatherData;
};

module.exports = {
  getWeather: getWeather
};
