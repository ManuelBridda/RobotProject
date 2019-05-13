//template from appRouting.js

var express = require("express");
var session = require("express-session");
//var flash = require('express-flash-notification');
var bodyParser = require('body-parser');
var http = require("http");
var path = require("path");
var passport = require('passport');
var tcpserver = require("./TCPServerFrontend");
var passportSetup = require("./passportsetup");

var app = express();

tcpserver.initServer(); ////NB



var publicPath = path.resolve(__dirname, "public");
var rootPath = path.resolve(__dirname, ".");


app.use(express.static(publicPath));
app.use(bodyParser.urlencoded({extended: true}));
app.use(session({ secret: "cats" }));
app.use(passport.initialize());
app.use(passport.session());
//app.use(flash(app));

passportSetup();

//logger
app.use (function(req,res,next){
	console.log("[Web Server][LOG] requested: "+req.url);
	next();
});

app.post('/login',
  passport.authenticate('local', { //session : false,
  								   successRedirect: '/console',
                                   failureRedirect: '/login',
                                   //failureFlash: true,
                                   })
);


///////Public routes///////
 app.get("/", function(req, res) {
	 //console.log("[Web Server] get / req =" + req.url);
	 if(req.isAuthenticated)
	 	res.redirect("/console");
	 else
	 	res.redirect("/login");
	 //res.end("[ok]");
 });

 app.get("/login", function(req, res) {
	 res.sendFile(path.join(rootPath, "/login.html"));
	// res.end();
 });


///////Authenticated routes///////
app.use(function(req,res,next) {
	if (req.isAuthenticated()){
		next(); //the remaining routes are just for authenticated purposes.
	}
	else{
		//res.send("Unauthorized. Redirecting to login.");
		res.redirect("/login");
	}
});

  app.get("/console", function(req, res) {
  	
 // 	if(req.isAuthenticated()) //"manual if" for each request that requires auth.
		res.sendFile(path.join(rootPath, "/console.html"));
  //	else
//		res.end();
 });

 app.get("/start", function(req, res) {
	 console.log("[Web Server] START PRESSED" );

	 tcpserver.sendMsg(JSON.stringify({"type":"start"})+'\n');
	 res.redirect("/console");
 //next();
 });

 app.get("/stop", function(req, res) {
	 console.log("[Web Server] STOP PRESSED" );
	 tcpserver.sendMsg(JSON.stringify({"type":"stop"})+'\n');
	 res.redirect("/console");
 //next();
 });




 //main
 http.createServer(app).listen(3000, function(){
 console.log('[Web Server] bound to port 3000');
 });
