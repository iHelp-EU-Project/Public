const util = require("util");
const multer = require("multer");
const maxSize = 2 * 1024 * 1024;
var os = require("os");


let storage = multer.diskStorage({
  destination: (req, file, cb) => {
    const temp = getJsonFromUrl(req.url);
    const username = temp.username;
    const modelsname = temp.modelsname;
    const directory = temp.directory;
    console.log(username, modelsname, directory, 'upload');
    //cb(null, "./user_upload/");
    if(directory !== 'test'){
        if(os.platform() === 'win32'){
           cb(null, "./scriptsML/imagesup/" + username + "/" + modelsname + "/images/");
        } 
        else if(os.platform() === 'linux'){
           cb(null, "./imagesup/" + username + "/" + modelsname + "/images/");
        }
      }else{
      if(os.platform() === 'win32'){
          cb(null, "./scriptsML/imagesup/" + username + "/" + modelsname + "/test/");
        }
        else if(os.platform() === 'linux'){
           cb(null, "./imagesup/" + username + "/" + modelsname + "/test/");
        }
      }
  },
  filename: (req, file, cb) => {
    const temp = getJsonFromUrl(req.url);
    const directory = temp.directory;
    if(directory !== './user_upload'){
      console.log(file.originalname.replace(/[^a-z0-9]/gi, '_').toLowerCase());
      cb(null, file.originalname.replace(/[^a-z0-9]/gi, '_').toLowerCase() + '.png');
    }else{
      console.log(file.originalname);
      cb(null, file.originalname);
    }
  },
});

let uploadFile = multer({
  storage: storage,
  limits: { fileSize: maxSize },
}).single("file");

let uploadFileMiddleware = util.promisify(uploadFile);
module.exports = uploadFileMiddleware;

// get url params and return sort object.
function getJsonFromUrl(url) {
    if (!url) url = location.href;
    var question = url.indexOf("?");
    var hash = url.indexOf("#");
    if (hash == -1 && question == -1) return {};
    if (hash == -1) hash = url.length;
    var query = question == -1 || hash == question + 1 ? url.substring(hash) :
        url.substring(question + 1, hash);
    var result = {};
    query.split("&").forEach(function (part) {
        if (!part) return;
        part = part.split("+").join(" "); // replace every + with space, regexp-free version
        var eq = part.indexOf("=");
        var key = eq > -1 ? part.substr(0, eq) : part;
        var val = eq > -1 ? decodeURIComponent(part.substr(eq + 1)) : "";
        var from = key.indexOf("[");
        if (from == -1) result[decodeURIComponent(key)] = val;
        else {
            var to = key.indexOf("]", from);
            var index = decodeURIComponent(key.substring(from + 1, to));
            key = decodeURIComponent(key.substring(0, from));
            if (!result[key]) result[key] = [];
            if (!index) result[key].push(val);
            else result[key][index] = val;
        }
    });
    return result;
}