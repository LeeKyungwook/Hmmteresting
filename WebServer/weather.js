var express = require('express');
var router = express.Router();
var bodyParser = require('body-parser');

router.use(bodyParser.json());
router.use(bodyParser.urlencoded({extended : true}));

router.get('/',function(req, res){
  console.log("#############");
});

function getWeather(callback) {
  var Forecast = require('forecast');
  var weatherData;
  var forecast = new Forecast({
          service: 'forecast.io',
          key: '3ed04ee2692b0344d29afd96a34b18fe',
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
	  weatherData = weather.currently;
	  callback(weatherData); 
 //      console.dir();
  //      console.dir(weather.currently.summary);
  //      console.dir(weather.currently.temperature);
  });


  //if (typeof callback === "function"){
  //  callback(weatherData);
 // }
  return weatherData;
};

module.exports = {
  getWeather: getWeather
};
