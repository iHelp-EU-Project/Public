var express = require('express');
var router = express.Router();

router.get('/pepe45', function(req, res){
    res.send("Hello Pepe");
});

// router.get('/user', function(req, res){
//     res.send("Hello User");
// });

// router.get('/admin', function(req, res){
//     res.send("Hello Admin");
// });

// router.get('/all-user', function(req, res){
//     res.send("Hello All User");
// });

module.exports = router;