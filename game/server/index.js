var app = require('express')();
var server = require('http').Server(app);
var io = require('socket.io')(server);
var players = [];
player.push(Player(0, 100, 100, "idle"))
server.listen(8000, function(){
	console.log("Servre is now running...");
});

io.on('connection', function(socket) {
	console.log("Player Connected!");
	socket.emit('socketID', { id: socket.id });
	socket.emit('getPlayers', players);
	socket.broadcast.emit('newPlayer', { id: socket.id });
	socket.on('message', function(msg) {
	    if (msg['event'] == 'spawn') {
	        player.push(socket.id, msg['x'], msg['y'], msg['event'])
	    }
	});
	socket.on('disconnect', function(){
		console.log("Player Disconnected");
		socket.broadcast.emit('playerDisconnected', { id: socket.id });
		for(var i = 0; i < players.length; i++){
			if(players[i].id == socket.id){
				players.splice(i, 1);
			}
		}
	});
});

function player(id, x, y, action) {
	this.id = id;
	this.x = x;
	this.y = y;
	this.action = action;
}