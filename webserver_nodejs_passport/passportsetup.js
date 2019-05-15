var passport = require('passport');
var LocalStrategy = require('passport-local').Strategy;

module.exports = function() {

passport.use(new LocalStrategy(
  function(username, password, done) {
  
  if(username == 'carmine' && password == 'carmine')
    return done(null, username);
  
  else return done(null,false);
  }
));


passport.serializeUser(function(user, done) {
  done(null, user);
});

passport.deserializeUser(function(user, done) {
    done(null, user);
});


};