var net = require('net');

var HOST = 'localhost';
var PORT = 7777;

var ready = false;
var server = net.createServer();
var clientSock = null;

server.on('listening', function(){
			console.log('[TCPServer] Server listening on ' + server.address().address + ':' + server.address().port)
	});

server.on('connection', function(sock){
	console.log('[TCPServer] Connected to ' + sock.remoteAddress + ':' + sock.remotePort)
	ready = true;
	clientSock = sock;
	clientSock.setNoDelay(true); //to send off writes immediately.
	
	//client socket error handling
	clientSock.on('error', function(){
		console.log('[TCPServer] Error: trying to restart server.');
		ready = false; //? ready = false
	});
	clientSock.on('end', function(){
			console.log('[TCPServer] Connection ended: resetting server.');
			//initServer();
			ready = false;
		});
	clientSock.on('close', function(){
			ready = false;
			console.log('[TCPServer] Closing connection with client.');
		});

	});

//server error handling
server.on('error', function(){
		console.log('[TCPServer] Error: trying to restart server.');
		initServer();
	});
server.on('close', function(){
		ready = false;
		console.log('[TCPServer] Closing.');
	});
//handling uncaught errors
process.on('uncaughtException', function(e){
	console.log('[TCPServer] Error: trying to restart server.');
	initServer();
});


////exported functions
function initServer(){
	ready = false;
	server.listen(PORT, HOST);	

};

function sendMsg(jsonToWrite){
	if(ready){
		clientSock.write(jsonToWrite);
	}
};

module.exports.initServer = initServer;
module.exports.sendMsg = sendMsg;
