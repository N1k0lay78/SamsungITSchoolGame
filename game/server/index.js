var app = require('express')();
var server = require('http').Server(app);
var io = require('socket.io')(server);

class Player {
    constructor(id, x, y, frame) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.action = frame;
    }
}

class Enemy {
    constructor(x, y, frame) {
        this.x = x;
        this.y = y;
        this.frame = frame;
    }
}

class Barrel {
    constructor(x, y, frame) {
        this.x = x;
        this.y = y;
    }
}

class Effect {
    constructor(name) {
        this.name = name;
    }
}

var enemy;
var players = [];
var barrels = [];
var effect = new Effect("None");

server.listen(8000, function(){
	console.log("Server is now running...");
});

io.on('connection', function(socket) {
	console.log("Player Connected!");
	socket.emit('socketID', {
	    id: socket.id,
	    host: (players.length==0)?true:false
	});
	socket.on('message', function(msg) {
	    console.log("new message: " + msg.event);
	    if (msg.event == 'spawn') {
	        console.log("player spawned");
	        players.push(new Player(socket.id, msg.x, msg.y, msg.action));
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