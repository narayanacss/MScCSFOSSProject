var express = require('express')
  , app = express()
  , server = require('http').createServer(app)
  , io = require('socket.io').listen(server);

server.listen(3013);

app.use(express.static(__dirname + '/public'));

app.post('/', function (req, res) {
  res.sendFile(__dirname + '/public/333@TNMCRoom.html');
});

var clientio  = require('socket.io-client');
var client    = clientio.connect('http://localhost:3000');

io.sockets.on('connection', function (socket) {
	

	client.on('333@TNM', function (data) {
		console.log('333@TNM data: ', data);
    		socket.emit('message', data);
  	});
	console.log('connection 333@TNM');

});
