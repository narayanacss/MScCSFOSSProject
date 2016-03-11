var express = require('express')
  , app = express()
  , server = require('http').createServer(app)
  , io = require('socket.io').listen(server);

server.listen(3011);

app.use(express.static(__dirname + '/public'));

app.post('/', function (req, res) {
  res.sendFile(__dirname + '/public/111@JACRoom.html');
});

var clientio  = require('socket.io-client');
var client    = clientio.connect('http://localhost:3000');

io.sockets.on('connection', function (socket) {
	
	client.on('111@JA', function (data) {
		console.log('111@JAserver data: ', data);
    		socket.emit('message', data);
  	});
	console.log('connection 111@JA');

});
