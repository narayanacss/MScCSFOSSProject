/**
 * Module dependencies.
 */
var express  = require('express');
var connect = require('connect');
var bodyParser = require('body-parser');
var app      = express();
var server = require('http').createServer(app);
var io1 = require('socket.io').listen(3000)

// Configuration
app.use(express.static(__dirname + '/public')); // Make files in public folder available to world
app.use(connect.json());
app.use(bodyParser.urlencoded({ extended: true })); 

// Routes
server.listen(8080);
require('./routes.js')(app, io1);
console.log('httpServer runs on port 8080');



