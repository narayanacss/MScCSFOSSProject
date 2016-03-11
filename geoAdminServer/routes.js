var geoCreateUser = require('config/geoCreateUser');
var geoEditVehicle = require('config/geoEditVehicle');
var geoTrigger = require('config/geoTrigger');
var geoWallet = require('config/geoWallet');
var requestIp = require('request-ip');

module.exports = function(app, io3, io1) {

	app.get('/', function(req, res) {

		res.end("Welcome to geoTollways!"); 
	});


        app.post('/geoCreateUser',function(req,res) {
		var uid = req.body.uid;
                
                geoCreateUser.geoCreateUser(uid,function (response) {
                        console.log(response);
                        res.json(response);
                });
        });

        app.post('/geoWallet',function(req,res) {
                var uid = req.body.uid;
		var sid = req.body.sid;
		var timestamp = req.body.timestamp;
		var agent = "okhttp/2.5.0";
                geoWallet.geoWallet(uid, sid, timestamp, agent, function (response) {
                        console.log(response);
                        res.json(response);
                });
        });

        app.post('/geoEditVehicle',function(req,res) {
		var response = {"response":"Changes have been saved."};
                var uid = req.body.uid;
		geoEditVehicle.geoEditVehicle(uid,function (resp) {
                    	res.json(response);
		});
        });

        app.post('/geoTrigger',function(req,res){
		var uid = req.body.uid;
		var sid = req.body.sid;
		var timestamp = req.body.timestamp;
		var fenceID = req.body.fenceID;
                var ip = requestIp.getClientIp(req);
		var agent = req.body.agent;
		var username = req.body.username;
        	
		geoTrigger.geoTrigger(io1, uid, sid, timestamp, fenceID, ip, agent, username, function (response) {
			console.log(response);
			res.json(response);
		});
	});
};
