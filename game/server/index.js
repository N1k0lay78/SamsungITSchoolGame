var app = require('express')();
var server = require('http').Server(app);
var io = require('socket.io')(server);
var players = [];

class Player {
    constructor(id, x, y, action) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.action = action;
    }
}

players.push(new Player(0, 100, 100, "idle"))
server.listen(8000, function(){
	console.log("Server is now running...");
});

io.on('connection', function(socket) {
	console.log("Player Connected!");
	socket.emit('socketID', { id: socket.id });
	socket.on('message', function(msg) {
	    console.log("new message: " + msg.event);
	    if (msg.event == 'spawn') {
	        console.log("player spawned");
	        players.push(new Player(socket.id, msg.data.x, msg.data.y, msg.data.action));
            socket.broadcast.emit('newPlayer', { id: socket.id });
	        socket.emit('getPlayers', players);
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