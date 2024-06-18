require('rootpath')();
var express = require('express');
var os = require("os");
var app = express();
var bodyParser = require('body-parser');
const cors = require('cors');
const config = require('./config.js');
// const fileUpload = require('express-fileupload')

// const userService = require('./users/user.service');

const errorHandler = require('./middleware/error-handler');

var multer = require('multer');
var net = require('net');
function getNetworkIP(callback) {
  var socket = net.createConnection(80, 'www.google.com');
  socket.on('connect', function() {
    callback(undefined, socket.address().address);
    socket.end();
  });
  socket.on('error', function(e) {
    callback(e, 'error');
  });
}

var ip = getNetworkIP(function (error, ip) {
    if (error) {
        console.log('error:', error);
    }
});




// app.use(fileUpload())
app.use((req, res, next) => {
    res.header('Access-Control-Allow-Origin', '*');
    res.header('Access-Control-Allow-Headers', 'Authorization, X-API-KEY, Origin, X-Requested-With, Content-Type, Accept, Access-Control-Allow-Request-Method');
    res.header('Access-Control-Allow-Methods', 'GET, POST, OPTIONS, PUT, DELETE');
    res.header('Allow', 'GET, POST, OPTIONS, PUT, DELETE');
    next();
});



app.use(bodyParser.urlencoded({ extended: true, limit: '25mb' }));
app.use(bodyParser.json({limit: '25mb'}));


app.use(cors());

app.set('trust proxy', true);

app.use('/users'  ,require('./users/users.controller'));

// global error handler
app.use(errorHandler);



// from multer upload files old (deprecated)
// var storage = multer.diskStorage({
//   destination: function (req, file, cb) {
   
//     cb(null, 'uploads')
//   },
//   filename: function (req, file, cb) {
//     // cb(null, file.fieldname + '-' + Date.now());
//     cb(null,file.originalname);
//     // cb(null, file.originalname);
//   }
// });

// var upload = multer({ storage: storage });
// app.post( '/api/upload', upload.any(), function( req, res, next ) {
//   // Metadata about the uploaded file can now be found in req.file
//   res.send(req.files);

//   currentFiles = req.files;
//   console.log(req.files);



// });



// const HOST = ip // production
// const HOST = '0.0.0.0' //develop

app.listen(config.NODE_PORT, config.NODE_HOST, function () {
  console.log('API NODE http://'+ config.NODE_HOST  + ':' + config.NODE_PORT);
    var fs = require('fs');
    if(os.platform() === 'win32'){

        var uploadfiles = 'uploads';
        if (!fs.existsSync(uploadfiles)) {
            return fs.mkdirSync(uploadfiles);
        }

       
    
    }else if(os.platform() === 'linux'){

        var uploadfiles = '../uploads';
        if (!fs.existsSync(uploadfiles)) {
            return fs.mkdirSync(uploadfiles);
        }
    }
});


