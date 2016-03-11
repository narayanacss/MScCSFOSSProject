var express = require('express')
  , app = express()
  , server = require('http').createServer(app)
  , io = require('socket.io').listen(server);

server.listen(3012);

app.use(express.static(__dirname + '/public'));

app.post('/', function (req, res) {
  res.sendFile(__dirname + '/public/222@CGLCRoom.html');
});

var clientio  = require('socket.io-client');
var client    = clientio.connect('http://localhost:3000');

io.sockets.on('connection', function (socket) {
	
	client.on('222@CGL', function (data) {
		console.log('222@CGL data: ', data);
    		socket.emit('message', data);
  	});
	console.log('connection 222@CGL');

});
