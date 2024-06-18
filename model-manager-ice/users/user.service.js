const config = require('../config.json');
const jwt = require('jsonwebtoken');
const bcrypt = require('bcryptjs');
// const db = require('./../helpers/db');
var mkdirp = require("mkdirp");
var fs = require("fs");
var os = require("os");
const uploadFile = require("../middleware/upload");
const archiver = require('archiver');
const { count } = require('console');
var rimraf = require("rimraf");
const configEnv = require('../config.js');
const multer = require('multer');
const fetch = require('node-fetch');



module.exports = {
    // authenticate,
    // getAll,
    // getById,
    create,
    update,
    delete: _delete,
    createWorkSpaceModels,
    deleteWorkSpaceModels,
    testingmysqldb,
    createdirectorymodel,
    readFilesJSONDirectory,
    downloadJSONFiles,
    readSettingsJSONFiles,
    readLoadingJSONValuesColumsToSelect,
    writeSettingsJSONFiles,
    readLoadingJSON,
    upload,
    deleteModels,
    datasetCVSTOJSON,
    selectUniqueValues,
    selectUniqueValuesPost,
    filesupload,
    uploadFilesTest,
    uploadJSONClasses,
    getLeyendJSONColumnsObject,
    compressDirectoryModelforSendToModelManager,
    getListFiles,
    download,
    uploadClasses,
    downloadClass,
    downloadPicturesModelsCNN,
    downloadPicturesModelsTest,
    downloadPicturesShapPlots,
    compressItemClases,
    getFilesGeneratedPicturesOfScriptsAlgorithm,
    execDockerCompose,
    logoutAutorization,
    loginAutorization,
    updateFiletoUploads,
    uploadFilesGAN,
    compressFilesGAN,
    envBackEnd
    // uploadFilesNormalTest,
    // uploadFilesNormalValid,
    // uploadFilesBadTrain,
    // uploadFilesBadTest,
    // uploadFilesBadValid
};

async function authenticate({ username, password }) {
    const user = await db.User.scope('withHash').findOne({ where: { username } });

    if (!user || !(await bcrypt.compare(password, user.hash)))
        throw 'Username or password is incorrect';

    // authentication successful
    const token = jwt.sign({ sub: user.id }, config.secret, { expiresIn: '7d' });
    return { ...omitHash(user.get()), token };
}

async function getAll() {
    return await db.User.findAll();
}

async function getById(id) {
    console.log("GET USER: " + id);

    return await getUser(id);
}

async function create(params) {
    // validate
    if (await db.User.findOne({ where: { username: params.username } })) {
        throw 'Username "' + params.username + '" is already taken';
    }

    // hash password
    if (params.password) {
        params.hash = await bcrypt.hash(params.password, 10);
    }

    // save user
    await db.User.create(params);



    var settinsDirectory;

    if (os.platform() === 'win32') {

        if (!fs.existsSync('./scriptsML/settings/')) {
            fs.mkdirSync('./scriptsML/settings/');
        }

        settinsDirectory = './scriptsML/settings/' + params.username;
        if (!fs.existsSync(settinsDirectory)) {
            fs.mkdirSync(settinsDirectory);
            let data = { language: 'en' };
            var settingsJson = './scriptsML/settings/' + params.username + '/';
            fs.writeFile(settingsJson + "settings.json", JSON.stringify(data), 'utf8', (err) => {
                if (err) throw err;
                console.log('The file has been saved!');
            });
        }
    }
    else if (os.platform() === 'linux') {

        if (!fs.existsSync('./settings/')) {
            fs.mkdirSync('./settings/');
        }

        settinsDirectory = './settings/' + params.username;
        if (!fs.existsSync(settinsDirectory)) {
            fs.mkdirSync(settinsDirectory);
            let data = { language: 'en' };
            var settingsJson = './settings/' + params.username + '/';
            fs.writeFile(settingsJson + "settings.json", JSON.stringify(data), 'utf8', (err) => {
                if (err) throw err;
                console.log('The file has been saved!');
            });
        }

    }













    // let objectToSave = {variable:'1',variable2:'2'} 

    //     fs.writeFile('archivo.json', JSON.stringify(objectToSave),'utf8', (err) => { 
    //     if (err) throw err; 
    //     console.log('The file has been saved!'); 
    //     }); 

}

async function update(id, params) {
    console.log("Update USER: " + id);
    const user = await getUser(id);

    // validate
    const usernameChanged = params.username && user.username !== params.username;
    if (usernameChanged && await db.User.findOne({ where: { username: params.username } })) {
        throw 'Username "' + params.username + '" is already taken';
    }

    // hash password if it was entered
    if (params.password) {
        params.hash = await bcrypt.hash(params.password, 10);
    }

    // copy params to user and save
    Object.assign(user, params);
    await user.save();

    return omitHash(user.get());
}

async function _delete(id) {
    console.log("Delete USER: " + id);

    const user = await getUser(id);
    await user.destroy();
    console.log(user);
}

// helper functions

async function getUser(id) {
    console.log("ID USER: " + id);
    const user = await db.User.findByPk(id);
    if (!user) throw 'User not found';
    return user;
}

function omitHash(user) {
    const { hash, ...userWithoutHash } = user;
    return userWithoutHash;
}

async function createWorkSpaceModels(params) {

    var fs = require('fs');

    var settinsDirectory;

    if (os.platform() === 'win32') {

        if (!fs.existsSync('./scriptsML/settings/')) {
            fs.mkdirSync('./scriptsML/settings/');
        }

        settinsDirectory = './scriptsML/settings/' + params.username;
        if (!fs.existsSync(settinsDirectory)) {
            fs.mkdirSync(settinsDirectory);
            let data = { language: 'en' };
            var settingsJson = './scriptsML/settings/' + params.username + '/';
            fs.writeFile(settingsJson + "settings.json", JSON.stringify(data), 'utf8', (err) => {
                if (err) throw err;
                console.log('The file has been saved!');
            });
        }
    }
    else if (os.platform() === 'linux') {

        if (!fs.existsSync('./settings/')) {
            fs.mkdirSync('./settings/');
        }

        settinsDirectory = './settings/' + params.username;
        if (!fs.existsSync(settinsDirectory)) {
            fs.mkdirSync(settinsDirectory);
            let data = { language: 'en' };
            var settingsJson = './settings/' + params.username + '/';
            fs.writeFile(settingsJson + "settings.json", JSON.stringify(data), 'utf8', (err) => {
                if (err) throw err;
                console.log('The file has been saved!');
            });
        }
    }



    if (os.platform() === 'win32') {

        var imagesup = './scriptsML/imagesup';
        if (!fs.existsSync(imagesup)) {
            fs.mkdirSync(imagesup);
        }


        var dir = './scriptsML/imagesup/' + params.username;
        if (!fs.existsSync(dir)) {
            return fs.mkdirSync(dir);
        }

    } else if (os.platform() === 'linux') {

        var imagesup = './imagesup';
        if (!fs.existsSync(imagesup)) {
            fs.mkdirSync(imagesup);
        }


        var dir = './imagesup/' + params.username;
        if (!fs.existsSync(dir)) {
            return fs.mkdirSync(dir);
        }
    }

}
async function deleteWorkSpaceModels(params) {


    var rimraf = require("rimraf");
    var fs = require('fs');
    if (os.platform() === 'win32') {

        var dir = './scriptsML/imagesup/' + params.username;
        var dirSettings = './scriptsML/settings/' + params.username;
        if (fs.existsSync(dir)) {
            rimraf(dirSettings, function () { console.log("Settings deleted"); });
            return rimraf(dir, function () { console.log("Workspace deleted"); });
        }

    } else if (os.platform() === 'linux') {

        var dir = './imagesup/' + params.username;
        var dirSettings = './settings/' + params.username;
        if (fs.existsSync(dir)) {
            rimraf(dirSettings, function () { console.log("Settings deleted"); });
            return rimraf(dir, function () { console.log("Workspace deleted"); });
        }
    }

}
async function deleteModels(params) {



    console.log('./scriptsML/imagesup/' + params.username + '/' + params.modelsname)
    var rimraf = require("rimraf");
    var fs = require('fs');

    if (os.platform() === 'win32') {

        var dir = './scriptsML/imagesup/' + params.username + '/' + params.modelsname;
        if (fs.existsSync(dir)) {
            return rimraf(dir, function () { console.log("Model deleted"); });
        }

    } else if (os.platform() === 'linux') {

        var dir = './imagesup/' + params.username + '/' + params.modelsname;
        if (fs.existsSync(dir)) {
            return rimraf(dir, function () { console.log("Model deleted"); });
        }

    }

}

async function testingmysqldb(params) {


    var fs = require('fs');
    if (os.platform() === 'win32') {

        var imagesup = './scriptsML/imagesup';
        if (!fs.existsSync(imagesup)) {
            return fs.mkdirSync(imagesup);
        }

    } else if (os.platform() === 'linux') {

        var imagesup = './imagesup';
        if (!fs.existsSync(imagesup)) {
            return fs.mkdirSync(imagesup);
        }
    }

    return db.initialize();


}

async function createdirectorymodel(params) {



    var fs = require('fs');
    if (os.platform() === 'win32') {

        var imagesup = './scriptsML/imagesup';
        if (!fs.existsSync(imagesup)) {
            return fs.mkdirSync(imagesup);
        }

        var dir = './scriptsML/imagesup/' + params.username + '/' + params.model;
        if (!fs.existsSync(dir)) {
            return fs.mkdirSync(dir);
        } else {
            rimraf.sync(dir);
            return fs.mkdirSync(dir);

        }

    } else if (os.platform() === 'linux') {

        var imagesup = './imagesup';
        if (!fs.existsSync(imagesup)) {
            return fs.mkdirSync(imagesup);
        }

        var dir = './imagesup/' + params.username + '/' + params.model;
        if (!fs.existsSync(dir)) {
            return fs.mkdirSync(dir);
        } else {
            rimraf.sync(dir);
            return fs.mkdirSync(dir);

        }
    }
}
// transform url with params  in JSON
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

async function downloadJSONFiles(params, res, next) {

    temp = getJsonFromUrl(params)

    var pathDir;

    if (os.platform() === 'win32') {
        pathDir = './scriptsML/imagesup/' + temp.username + '/' + temp.directory + '/' + temp.modelsname;
    }
    else if (os.platform() === 'linux') {
        pathDir = './imagesup/' + temp.username + '/' + temp.directory + '/' + temp.modelsname;
    }
    console.log(pathDir, 'parDir files');


    fs.readFile(pathDir, function (err, data) {
        if (err) {
            res.statusCode = 500;
            res.end(`Error getting the file: ${err}.`);
        } else {
            // if the file is found, set Content-type and send data
            res.header('Access-Control-Allow-Origin', '*');
            res.header('Access-Control-Allow-Headers', 'Authorization, X-API-KEY, Origin, X-Requested-With, Content-Type, Accept, Access-Control-Allow-Request-Method,Content-Length');
            res.header('Access-Control-Allow-Methods', 'GET, POST, OPTIONS, PUT, DELETE');
            res.header('Allow', 'GET, POST, OPTIONS, PUT, DELETE');
            return res.end(data);
        }
    });




}

async function datasetCVSTOJSON(params, res, next) {

    temp = getJsonFromUrl(params)
    // let csvToJson = require('convert-csv-to-json');
    const csv = require('csvtojson');

    var pathDir;

    if (os.platform() === 'win32') {
        pathDir = temp.directory + '/' + temp.modelsname;
    }
    else if (os.platform() === 'linux') {
        pathDir = '../' + temp.directory + '/' + temp.modelsname;
    }
    // console.log(pathDir, 'parDir files +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++');


    fs.readFile(pathDir, function (err, data) {
        if (err) {
            res.statusCode = 500;
            res.end(`Error getting the file: ${err}.`);
        } else {
            // if the file is found, set Content-type and send data
            res.header('Access-Control-Allow-Origin', '*');
            res.header('Access-Control-Allow-Headers', 'Authorization, X-API-KEY, Origin, X-Requested-With, Content-Type, Accept, Access-Control-Allow-Request-Method,Content-Length');
            res.header('Access-Control-Allow-Methods', 'GET, POST, OPTIONS, PUT, DELETE');
            res.header('Allow', 'GET, POST, OPTIONS, PUT, DELETE');

            // let json = csvToJson.getJsonFromCsv(pathDir);
            csv()
                .fromFile(pathDir)
                .then((jsonObj) => {
                    // console.log(jsonObj);
                    return res.send(jsonObj);
                })

            // console.log(json);
            //return res.send(json);
        }
    });




}




async function selectUniqueValues(params, res, next) {

    temp = getJsonFromUrl(params)
    // let csvToJson = require('convert-csv-to-json');
    const csv = require('csvtojson');
    const csvheaders = require('csv-parser')
    // var Filequeue = require('filequeue');
    // var fq = new Filequeue(401200); // max number of files to open at once

    var pathDir;
    var parDirCopy
    var nameColumn; //= temp.columns.split(',');

    // console.log(typeof nameColumn);
    //console.log(nameColumn + "+++++++++++++++++++++++++++++++++++++++");

    // var includeColumns = new RegExp( nameColumn );  


    //console.log(includeColumns, '    (/COLUMNS/)')


    if (os.platform() === 'win32') {
        pathDir = 'uploads/' + temp.filename;
        // parDirCopy =   'uploadfile/' + temp.filename;
    }
    else if (os.platform() === 'linux') {
        pathDir = '../uploads' + '/' + temp.filename;
        // parDirCopy = '../uploadfile' +  '/' + temp.filename;

    }
    console.log(pathDir, 'parDir files +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++');
    let headers;
    jsonObjFinal = new Array();


    fs.createReadStream(parDir)
        .pipe(csvheaders())
        .on('headers', (headers) => {
            console.log(`First header: ${headers[0]}`)
            nameColumn = headers
        })


    //console.log(nameColumnH + "+++++++++++++++++++++++++++++++++++++++ sincrono");

    fs.readFile(pathDir, function (err, data) {
        if (err) {
            res.statusCode = 500;
            res.end(`Error getting the file: ${err}.`);
        } else {
            // if the file is found, set Content-type and send data
            res.header('Access-Control-Allow-Origin', '*');
            res.header('Access-Control-Allow-Headers', 'Authorization, X-API-KEY, Origin, X-Requested-With, Content-Type, Accept, Access-Control-Allow-Request-Method,Content-Length');
            res.header('Access-Control-Allow-Methods', 'GET, POST, OPTIONS, PUT, DELETE');
            res.header('Allow', 'GET, POST, OPTIONS, PUT, DELETE');
            // let json = csvToJson.getJsonFromCsv(pathDir);
            // nameColumn.forEach(element => {


            for (let index = 0; index < nameColumn.length; index++) {
                const element = nameColumn[index];
                csv({
                    includeColumns: new RegExp(element)
                })
                    .fromFile(pathDir)
                    .on('header', (header) => {
                        headers = header
                        console.log('Headers file csv: ', pathDir + '->' + headers)
                    })
                    .then((jsonObj) => {
                        // console.log(jsonObj);
                        // Get all items the columns selected and save in th new object
                        const temp = new Array();
                        const obj = {};
                        for (let i = 0, len = jsonObj.length; i < len - 1; i++) {
                            obj[jsonObj[i][element]] = jsonObj[i];
                            //console.log(obj[jsonObj[j][element]])
                        }


                        Object.entries(obj).forEach(([key, value]) => {

                            //console.log(element + ': ',key + ': ', value)
                            //jsonObjFinal.push(value);
                            temp.push(value);

                        });

                        console.log(element + ': ', temp.length)

                        if (temp.length < 25) {
                            for (const key in temp) {
                                jsonObjFinal.push(temp[key]);
                            }
                        }
                        console.log(element + ': final: ', jsonObjFinal.length)

                        if (index == nameColumn.length - 1) {
                            //console.log('final:' , jsonObjFinal)
                            return res.send(jsonObjFinal);
                        }



                    })



            }

        }
    });





}

async function selectUniqueValuesPost(params, res, next) {

    const csv = require('csvtojson');
    var fg = require('graceful-fs') //graceful-fs,  fs-extra

    var fileName = res.req.query.filename
    var userName = res.req.query.username
    var pathDir = '';// path files downloaded of form
    var pathDirCopy = '';// path data JSON object is the file when save the rest
    var nameColumn = res.req.body




    console.log("Entry in search selects", fileName, userName, nameColumn);



    if (os.platform() === 'win32') {
        pathDir = 'uploads/' + fileName;
        parDirCopy = 'uploadfile/data.json';

    }
    else if (os.platform() === 'linux') {
        pathDir = '../uploads' + '/' + fileName;
        parDirCopy = '../uploadfile' + '/data.json';

    }
    console.log(pathDir, 'parDir files +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++');
    let headers;
    jsonObjFinal = new Array();


    fg.readFile(pathDir, function (err, data) {
        if (err) {
            res.statusCode = 500;
            res.end(`Error getting the file: ${err}.`);
        } else {
            // if the file is found, set Content-type and send data
            res.header('Access-Control-Allow-Origin', '*');
            res.header('Access-Control-Allow-Headers', 'Authorization, X-API-KEY, Origin, X-Requested-With, Content-Type, Accept, Access-Control-Allow-Request-Method,Content-Length');
            res.header('Access-Control-Allow-Methods', 'GET, POST, OPTIONS, PUT, DELETE');
            res.header('Allow', 'GET, POST, OPTIONS, PUT, DELETE');

            fs.writeFile(parDirCopy, '[', 'utf8', (err) => {
                if (err) throw err;
                console.log('The file has been created!');
            });
            var objs = [];
            csv({
                //includeColumns: new RegExp(element)
            })
                .fromFile(pathDir)
                // .on('header', (header) => {
                //     headers = header
                //     console.log('Headers file csv: ', pathDir + '->' + headers)
                // })
                .then((jsonObj) => {
                    //console.log(jsonObj);
                    // Get all items the columns selected and save in th new object
                    for (let index = 0; index < nameColumn.length; index++) {


                        const element = nameColumn[index];


                        //console.log(jsonObj);
                        const temp = new Array();
                        const obj = {};
                        // Iterate for all columns of dataset for save in object
                        for (let i = 0, len = jsonObj.length; i < len - 1; i++) {
                            obj[jsonObj[i][element]] = jsonObj[i][element];
                            // console.log('Guardado array obj:', obj[jsonObj[i][element]])
                        }

                        //Delete repit items in each column
                        Object.entries(obj).forEach(([key, value]) => {

                            // console.log(element + ': ',key + ': ', value)
                            value = value.replace(/-/g, '_');
                            temp.push(element + '-' + value);

                        });

                        console.log(element + ': ', temp.length)

                        // if we have less that 25 diferent items save for view in web UI.
                       // if (temp.length < 25) {
                            for (const key in temp) {
                                //console.log('No < 25: ', temp[key])
                                jsonObjFinal.push(temp[key]);
                                fs.appendFileSync(parDirCopy, JSON.stringify(temp[key], null, 2) + ",", function (err) {
                                    if (err)
                                        throw err;

                                });
                            }
                        // } else {
                        //     console.log('No < 25: ', temp[0])
                        //     jsonObjFinal.push(temp[0]);
                        //     fs.appendFileSync(parDirCopy, JSON.stringify(temp[0], null, 2) + ",", function (err) {
                        //         if (err)
                        //             throw err;

                        //     });


                        // }
                        // console.log(element + ': final: ', jsonObjFinal.length)


                        // console.log(index + ': index: ', nameColumn.length - 1)
                        // End the workflow
                        if (index === nameColumn.length - 1) {
                            console.log('Terminado:')//, (require('buffer').constants.MAX_LENGTH + 1) / 2 ** 30)

                            // This add is for have well formated array JSON in the file.  
                            fs.appendFile(parDirCopy, JSON.stringify(jsonObjFinal[0]) + "]", function (err) {
                                if (err) throw err;
                                console.log('last!!');
                            });
                            // 
                            return res.end()    //send(jsonObjFinal);
                        }

                    }

                })





        }
    });






}


async function readSettingsJSONFiles(params, res, next) {

    temp = getJsonFromUrl(params)

    var pathDir;
    if (os.platform() === 'win32') {

        pathDir = './scriptsML/' + temp.directory + '/' + temp.modelsname;
        //console.log(pathDir, 'parDir files');

    } else if (os.platform() === 'linux') {

        pathDir = './' + temp.directory + '/' + temp.modelsname;

    }


    fs.readFile(pathDir, function (err, data) {
        if (err) {
            res.statusCode = 500;
            res.end(`Error getting the file: ${err}.`);
        } else {
            // if the file is found, set Content-type and send data
            res.header('Access-Control-Allow-Origin', '*');
            res.header('Access-Control-Allow-Headers', 'Authorization, X-API-KEY, Origin, X-Requested-With, Content-Type, Accept, Access-Control-Allow-Request-Method,Content-Length');
            res.header('Access-Control-Allow-Methods', 'GET, POST, OPTIONS, PUT, DELETE');
            res.header('Allow', 'GET, POST, OPTIONS, PUT, DELETE');
            return res.end(data);
        }
    });




}

async function readLoadingJSON(params, res, next) {

    temp = getJsonFromUrl(params)
    var pathDir;

    if (os.platform() === 'win32') {

        pathDir = './scriptsML/' + temp.directory + '/' + temp.modelsname;
        // console.log(pathDir, 'parDir files');
    } else if (os.platform() === 'linux') {

        pathDir = './' + temp.directory + '/' + temp.modelsname;

    }




    fs.readFile(pathDir, function (err, data) {
        if (err) {
            res.statusCode = 500;
            res.end(`Error getting the file: ${err}.`);
        } else {
            // if the file is found, set Content-type and send data
            res.header('Access-Control-Allow-Origin', '*');
            res.header('Access-Control-Allow-Headers', 'Authorization, X-API-KEY, Origin, X-Requested-With, Content-Type, Accept, Access-Control-Allow-Request-Method,Content-Length');
            res.header('Access-Control-Allow-Methods', 'GET, POST, OPTIONS, PUT, DELETE');
            res.header('Allow', 'GET, POST, OPTIONS, PUT, DELETE');
            return res.end(data);
        }
    });




}

async function readLoadingJSONValuesColumsToSelect(params, res, next) {

    temp = getJsonFromUrl(params)
    var pathDir;
    jsonObj = new Array();

    if (os.platform() === 'win32') {

        pathDir = 'uploadfile/data.json';
        // console.log(pathDir, 'parDir files');
    } else if (os.platform() === 'linux') {

        pathDir = '../uploadfile' + '/data.json';

    }




    fs.readFile(pathDir, function (err, data) {
        if (err) {
            res.statusCode = 500;
            res.end(`Error getting the file: ${err}.`);
        } else {
            // if the file is found, set Content-type and send data
            res.header('Access-Control-Allow-Origin', '*');
            res.header('Access-Control-Allow-Headers', 'Authorization, X-API-KEY, Origin, X-Requested-With, Content-Type, Accept, Access-Control-Allow-Request-Method,Content-Length');
            res.header('Access-Control-Allow-Methods', 'GET, POST, OPTIONS, PUT, DELETE');
            res.header('Allow', 'GET, POST, OPTIONS, PUT, DELETE');

            fs.unlink(pathDir, (err) => {
                if (err) throw err;
                console.log(pathDir + ' was deleted');
            });


            // jsonObj = data.toString('UTF8').split('\n')


            // console.log(JSON.stringify(jsonObj))


            return res.end(data);
        }
    });




}

function toObject(arr) {
    var rv = {};
    for (var i = 0; i < arr.length; ++i)
        rv[i] = arr[i];
    return rv;
}
// Write information language in setting each user
async function writeSettingsJSONFiles(params, res, next) {

    temp = getJsonFromUrl(params)


    var language = { "language": temp.modelsname };
    var pathDir;
    if (os.platform() === 'win32') {

        pathDir = './scriptsML/' + temp.directory;

    } else if (os.platform() === 'linux') {

        pathDir = './' + temp.directory;

    }


    fs.readFile(pathDir, function (err, data) {
        if (err) {
            res.statusCode = 500;
            res.end(`Error write the file: ${err}.`);
        } else {
            // if the file is found, set Content-type and send data
            res.header('Access-Control-Allow-Origin', '*');
            res.header('Access-Control-Allow-Headers', 'Authorization, X-API-KEY, Origin, X-Requested-With, Content-Type, Accept, Access-Control-Allow-Request-Method,Content-Length');
            res.header('Access-Control-Allow-Methods', 'GET, POST, OPTIONS, PUT, DELETE');
            res.header('Allow', 'GET, POST, OPTIONS, PUT, DELETE');
            var str = JSON.stringify(language);// Because the nodejs script file only recognises strings or binary numbers, the json object is converted to a string and rewritten to the json file.
            fs.writeFile(pathDir, str, function (err) {
                if (err) {
                    console.error(err);
                }
                console.log('----------successfully added-------------');
            })



            return res.end(data);
        }
    });




}


async function readFilesJSONDirectory(params, res, next) {

    res.header('Access-Control-Allow-Origin', '*');
    res.header('Access-Control-Allow-Headers', 'Authorization, X-API-KEY, Origin, X-Requested-With, Content-Type, Accept, Access-Control-Allow-Request-Method,Content-Length');
    res.header('Access-Control-Allow-Methods', 'GET, POST, OPTIONS, PUT, DELETE');
    res.header('Allow', 'GET, POST, OPTIONS, PUT, DELETE');
    var rimraf = require("rimraf");
    console.log(params);

    var user = params.split('=')[1];


    var pathDir;

    if (os.platform() === 'win32') {
        pathDir = './scriptsML/imagesup/' + user + '/';
    }
    else if (os.platform() === 'linux') {
        pathDir = './imagesup/' + user + '/';
    }
    console.log(pathDir, 'parDir');


    var filesJSON = '';
    var counttemp = 0;
    var count = 0;
    var badModel = '';
    // var countItems = 0;
    // var javaNmame = '';



    modelnames = fs.readdirSync(pathDir);//read models in workspace

    console.log("\nCurrent directory modelnames:");
    modelnames.forEach(model => {
        console.log(model);

        filenames = fs.readdirSync(pathDir + model);// read al files in model
        count = 0;
        console.log("\nCurrent directory filenames:");
        filenames.forEach(file => {
            console.log(file);
            var extension = file.split('.')[2];
            if (extension == 'json') {// looking for which files are JSON
                counttemp++;
                filesJSON += ',' + file;
                console.log(filesJSON, '  ++++++++save');
                count--;
            }
            count++;

        });



        if (filenames.length < 5 || count === filenames.length) {
            var model = pathDir + model;
            console.log("PATH Model: +++++++++++++++++++++++++++++++++++++++++++ : ", model);
            rimraf(model, function () { console.log("Model deleted"); });
            counttemp++;
            console.log("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++Model deleted: ", model);
        }
        console.log(filenames.length, "Files in directory", " NºModelos: " + modelnames.length);
        console.log(count, "Count");
        console.log(counttemp, "Disparo RESRAPI");
        console.log(pathDir + model, "Path");


        if (counttemp === modelnames.length) {
            console.log(modelnames, "ITEMS");
            console.log(filesJSON, "filesJSON");
            counttemp = 0;
            console.log('send: ', filesJSON)



            res.end(filesJSON);



        }

    });






    return filesJSON;

}


async function upload(req, res, next) {


    const multer = require('multer');
    // var upload = multer({ dest: 'uploads/' });


    const storage = multer.diskStorage({
        destination: function (req, file, cb) {
            cb(null, 'uploads')
        },
        filename: function (req, file, cb) {
            // cb(null, file.fieldname + '-' + Date.now());
            cb(null, file.originalname);
            // cb(null, file.originalname);
        }
    });

    return multer({ storage: storage });




}


async function uploadClasses(params, res, next) {


    //temp = getJsonFromUrl(params)

    // console.log('ArchivoREQ: ' + req.query);
    // console.log('ArchivoRES: ' + res.JSON);
    let tem = getJsonFromUrl(params);
    console.log(tem.classfiles, '#######################');

    var filesClass = JSON.parse(tem.classfiles);
    var user = tem.username;
    var model = tem.modelsname;
    var directoryInitialImages = tem.directory;


    // console.log(req.params);
    // console.log(res.params);
    //var obj = {class: [] };


    var pathDir;
    var directory;
    var test;

    if (os.platform() === 'win32') {
        pathDir = './scriptsML/imagesup/' + user + '/' + model + '/classes';
        directory = './scriptsML/imagesup/' + user + '/' + model + '/images';
        test = './scriptsML/imagesup/' + user + '/' + model + '/test';

    }
    else if (os.platform() === 'linux') {
        pathDir = './imagesup/' + user + '/' + model + '/classes';
        directory = './imagesup/' + user + '/' + model + '/images';
        test = './imagesup/' + user + '/' + model + '/test';


    }
    console.log(pathDir, 'parDir');

    if (!fs.existsSync(pathDir)) {
        fs.mkdirSync(pathDir);
    }

    if (!fs.existsSync(test)) {
        fs.mkdirSync(test);
    }

    for (let i = 0; i < filesClass.length; i++) {

        console.log(filesClass[i].id);
        if (filesClass[i].id) {
            if (!fs.existsSync(pathDir + '/' + filesClass[i].id)) {
                fs.mkdirSync(pathDir + '/' + filesClass[i].id);
                console.log('Directory Created: ', filesClass[i].id);
                // obj.class.push({id: filesClass[i].id });
            }

            console.log(filesClass[i].files);  // (o el campo que necesites)
            var arrayFiles = filesClass[i].files.split(',');
            arrayFiles.forEach(element => {
                console.log('File moving: ', element);
                fs.rename(directory + '/' + element, pathDir + '/' + filesClass[i].id + '/' + element, function (err) {
                    if (!err)
                        console.log('Successfully - moved! :' + element);
                });

            });
        }

        if (i === filesClass.length - 1) break;
    }
    var nameFilezip = user + '_' + model + '.zip';

    // var output = fs.createWriteStream('./user_upload/' + nameFilezip);
    var output = fs.createWriteStream(pathDir + '/' + nameFilezip);

    var archive = archiver('zip', {
        zlib: { level: 9 } // Sets the compression level.
    });

    output.on('close', () => {
        console.log(archive.pointer() + ' total bytes');
        console.log('archiver has been finalized and the output file descriptor has closed.');
    });

    archive.on('error', function (err) {
        throw err;
    });

    archive.pipe(output);

    archive.finalize();






}

async function checkIsDirectory(path) {
    const stats = fs.lstat(path)
    return stats.isDirectory()
}

function cleanFileName(fileName) {
    // Utilizar una expresión regular para eliminar espacios en blanco y paréntesis
    return fileName.replace(/[^\w.-]/g, '_').replace(/\s+/g, '_').replace(/[_()]+/g, '_');
}


async function updateFiletoUploads(req, res) {
    let EDFile = req.files.file
    var pathDir;

    console.log(EDFile);

    if (os.platform() === 'win32') {
        pathDir = './uploads';

    }
    else if (os.platform() === 'linux') {
        pathDir = '../uploads';


    }

    // Limpiar el nombre del archivo
    const originalFileName = EDFile.name;
    const cleanedFileName = cleanFileName(originalFileName);

    EDFile.mv(`${pathDir}/${cleanedFileName}`, err => {

        if (err) return res.status(500).send({ message: err })


        return res.status(200).send({ message: 'File upload' })

    })
}


async function filesupload(req, res) {
    try {

        var tem = getJsonFromUrl(req.url);
        const username = tem.username;
        const modelsname = tem.modelsname;
        // const directory = tem.directory;
        console.log(username, modelsname, "filesUpload");

        console.log('ArchivoREQ: ' + req.url);
        console.log('ArchivoREs: ' + res.params);
        console.dir('Archivo: ' + req.files[0]);

        console.log('++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++');

        var pathDir;

        if (os.platform() === 'win32') {
            pathDir = './scriptsML/imagesup/' + username + '/' + modelsname + '/images/';

        }
        else if (os.platform() === 'linux') {
            pathDir = './imagesup/' + username + '/' + modelsname + '/images/';


        }
        console.log(pathDir, 'parDir');

        if (!fs.existsSync(pathDir)) {
            fs.mkdirSync(pathDir);
        }



        let EDFile = req.files.file


        EDFile.mv(`${pathDir}/${EDFile.name}`, err => {

            if (err) return res.status(500).send({ message: err })


            return res.status(200).send({ message: 'File upload' })

        })


    } catch (err) {

        if (err.code == "LIMIT_FILE_SIZE") {
            return res.status(500).send({
                message: "File size cannot be larger than 2MB!",
            });
        }
        res.status(500).send({
            message: `Could not upload the file: ${req}. ${err}`,
        });
    }
};

async function getListFiles(req, res) {
    var tem = getJsonFromUrl(req.url);
    const username = tem.username;
    const modelsname = tem.modelsname;
    const directory = tem.directory;
    console.log(username, modelsname, directory, "getFilesLIst");
    var directoryPath; // = "./scriptsML/imagesup/" + username + "/" + modelsname + "/images/"
    // const directoryPath = "./user_upload/";

    if (directory === 'cnn') {
        if (os.platform() === 'win32') {

            directoryPath = "./scriptsML/imagesup/" + username + "/" + modelsname + "/images/"

        }
        else if (os.platform() === 'linux') {
            directoryPath = "./imagesup/" + username + "/" + modelsname + "/images/"

        }
    } else {

        if (os.platform() === 'win32') {
            directoryPath = "./scriptsML/imagesup/" + username + "/" + modelsname + "/xray/" + directory

        }
        else if (os.platform() === 'linux') {
            directoryPath = "./imagesup/" + username + "/" + modelsname + "/xray/" + directory

        }
    }


    console.log('Last Images Path:   ' + directoryPath);

    fs.readdir(directoryPath, function (err, files) {
        if (err) {
            res.status(500).send({
                message: "Unable to scan files!",
            });
        }

        let fileInfos = [];

        files.forEach((file) => {
            fileInfos.push({
                name: file,
                url: directoryPath + file,
            });
        });

        res.status(200).send(fileInfos);
    });
};

async function download(req, res) {
    const fileName = req.params.name;
    var tem = getJsonFromUrl(req.url);
    console.log(req.url);
    const username = tem.username;
    const modelsname = tem.modelsname;
    const algorithm = tem.algorithm;
    console.log(username, modelsname, algorithm);
    var directoryPath //= "./scriptsML/imagesup/" + username + "/" + modelsname + "/images/"
    // const directoryPath = "./user_upload/";




    if (algorithm !== 'cnn') {
        if (os.platform() === 'win32') {
            //pathDir = './scriptsML/imagesup/' + username + '/' + modelsname + '/test';
            directoryPath = "./scriptsML/imagesup/" + username + "/" + modelsname + '/xray' + algorithm

        }
        else if (os.platform() === 'linux') {
            //pathDir = './imagesup/' + username + '/'  + modelsname + '/test';
            directoryPath = "./imagesup/" + username + "/" + modelsname + '/xray' + algorithm

        }

    } else {
        if (os.platform() === 'win32') {
            //pathDir = './scriptsML/imagesup/' + username + '/' + modelsname + '/test';
            directoryPath = "./scriptsML/imagesup/" + username + "/" + modelsname + "/images/"

        }
        else if (os.platform() === 'linux') {
            //pathDir = './imagesup/' + username + '/'  + modelsname + '/test';
            directoryPath = "./imagesup/" + username + "/" + modelsname + "/images/"



        }
    }

    console.log(directoryPath)

    res.download(directoryPath + fileName, fileName, (err) => {
        if (err) {
            res.status(500).send({
                message: "Could not download the file. " + err,
            });
        }
    });
};


async function downloadClass(req, res) {
    const fileName = req.params.name;
    var tem = getJsonFromUrl(req.url);
    console.log(req.url);
    const username = tem.username;
    const modelsname = tem.modelsname;
    console.log(username, modelsname, "download");
    //const directoryPath = "./scriptsML/imagesup/" + username + "/" + modelsname + "/images/"
    var directoryPath = "./user_upload/";

    if (os.platform() === 'win32') {
        directoryPath = "./user_upload/";

    }
    else if (os.platform() === 'linux') {
        directoryPath = "../user_upload/";



    }


    res.download(directoryPath + fileName, fileName, (err) => {
        if (err) {
            res.status(500).send({
                message: "Could not download the file. " + err,
            });
        }
    });
};


async function downloadPicturesModelsCNN(req, res) {
    const fileName = req.params.name;
    var tem = getJsonFromUrl(req.url);
    console.log(req.url);
    const username = tem.username;
    const modelsname = tem.modelsname;
    console.log(username, modelsname, "download");
    var directoryPath; //= "./scriptsML/imagesup/" + username + "/" + modelsname + "/"
    //const directoryPath = "./user_upload/";

    if (os.platform() === 'win32') {
        //pathDir = './scriptsML/imagesup/' + username + '/' + modelsname + '/test';
        directoryPath = "./scriptsML/imagesup/" + username + "/" + modelsname + "/"

    }
    else if (os.platform() === 'linux') {
        //pathDir = './imagesup/' + username + '/'  + modelsname + '/test';
        directoryPath = "./imagesup/" + username + "/" + modelsname + "/"



    }



    res.download(directoryPath + fileName, fileName, (err) => {
        if (err) {
            res.status(500).send({
                message: "Could not download the file. " + err,
            });
        }
    });
};


async function uploadFilesTest(req, res) {
    try {

        var tem = getJsonFromUrl(req.url);
        const username = tem.username;
        const modelsname = tem.modelsname;
        const directory = tem.directory;
        console.log(username, modelsname, directory, "filesUpload");

        // console.log('ArchivoREQ: ' + req.url);
        // console.log('ArchivoREs: ' + res.params);

        var pathDir;

        if (os.platform() === 'win32') {
            pathDir = './scriptsML/imagesup/' + username + '/' + modelsname + '/test';

        }
        else if (os.platform() === 'linux') {
            pathDir = './imagesup/' + username + '/' + modelsname + '/test';


        }
        console.log(pathDir, 'parDir');

        if (!fs.existsSync(pathDir)) {
            fs.mkdirSync(pathDir);
        }

        // await uploadFile(req, res);


        // if (req.file == undefined) {
        //   return res.status(400).send({ message: "Please upload a file!" });
        // }

        // res.status(200).send({
        //   message: "Uploaded the file successfully: " + req,
        // });

        let EDFile = req.files.file


        EDFile.mv(`${pathDir}/${EDFile.name + '.png'}`, err => {

            if (err) return res.status(500).send({ message: err })


            return res.status(200).send({ message: 'File upload' })

        })
    } catch (err) {

        if (err.code == "LIMIT_FILE_SIZE") {
            return res.status(500).send({
                message: "File size cannot be larger than 2MB!",
            });
        }
        res.status(500).send({
            message: `Could not upload the file: ${req}. ${err}`,
        });
    }
}


async function downloadPicturesModelsTest(req, res) {
    const fileName = req.params.name;
    var tem = getJsonFromUrl(req.url);
    console.log(req.url);
    const username = tem.username;
    const modelsname = tem.modelsname;
    console.log(username, modelsname, "test");
    var directoryPath //= "./scriptsML/imagesup/" + username + "/" + modelsname + "/test/"
    //const directoryPath = "./user_upload/";

    if (os.platform() === 'win32') {
        directoryPath = "./scriptsML/imagesup/" + username + "/" + modelsname + "/test/"

    }
    else if (os.platform() === 'linux') {
        directoryPath = "./imagesup/" + username + "/" + modelsname + "/test/"



    }



    res.download(directoryPath + fileName, fileName, (err) => {
        if (err) {
            res.status(500).send({
                message: "Could not download the file. " + err,
            });
        }
    });
}


async function uploadJSONClasses(params, res) {

    temp = getJsonFromUrl(params)

    var pathDir;

    if (os.platform() === 'win32') {
        pathDir = './scriptsML/imagesup/' + temp.username + '/' + temp.modelsname + '/classes/classes.json';
    }
    else if (os.platform() === 'linux') {
        pathDir = './imagesup/' + temp.username + '/' + temp.modelsname + '/classes/classes.json';
    }
    console.log(pathDir, 'parDir files');


    fs.readFile(pathDir, function (err, data) {
        if (err) {
            res.statusCode = 500;
            res.end(`Error getting the file: ${err}.`);
        } else {
            // if the file is found, set Content-type and send data
            res.header('Access-Control-Allow-Origin', '*');
            res.header('Access-Control-Allow-Headers', 'Authorization, X-API-KEY, Origin, X-Requested-With, Content-Type, Accept, Access-Control-Allow-Request-Method,Content-Length');
            res.header('Access-Control-Allow-Methods', 'GET, POST, OPTIONS, PUT, DELETE');
            res.header('Allow', 'GET, POST, OPTIONS, PUT, DELETE');
            return res.end(data);
        }
    });
}

async function getLeyendJSONColumnsObject(params, res) {

    temp = getJsonFromUrl(params)

    var pathDir;

    if (os.platform() === 'win32') {
        pathDir = './scriptsML/imagesup/' + temp.username + '/' + temp.modelsname + '/experimental/' + temp.modelsname + '_informationColumnsStringToNumber.json';

    }
    else if (os.platform() === 'linux') {
        pathDir = './imagesup/' + temp.username + '/' + temp.modelsname + '/experimental/' + temp.modelsname + '_informationColumnsStringToNumber.json';

    }
    console.log(pathDir, 'parDir files');


    fs.readFile(pathDir, function (err, data) {
        if (err) {
            res.statusCode = 500;
            res.end(`Error getting the file: ${err}.`);
        } else {
            // if the file is found, set Content-type and send data
            res.header('Access-Control-Allow-Origin', '*');
            res.header('Access-Control-Allow-Headers', 'Authorization, X-API-KEY, Origin, X-Requested-With, Content-Type, Accept, Access-Control-Allow-Request-Method,Content-Length');
            res.header('Access-Control-Allow-Methods', 'GET, POST, OPTIONS, PUT, DELETE');
            res.header('Allow', 'GET, POST, OPTIONS, PUT, DELETE');
            return res.end(data);
        }
    });
}

/**
 * @param {String} sourceDir: /some/folder/to/compress
 * @param {String} outPath: /path/to/created.zip
 * @returns {Promise}
 */
async function zipDirectory(sourceDir, outPath) {
    const archive = archiver('zip', { zlib: { level: 9 } });
    const stream = fs.createWriteStream(outPath);

    return new Promise((resolve, reject) => {
        archive
            .directory(sourceDir, false)
            .on('error', err => reject(err))
            .pipe(stream)
            ;

        stream.on('close', () => resolve());
        archive.finalize();
    });
}

const axios = require('axios');

async function getTokenKeyCloak() {
  const keycloakUrl = 'https://keycloak.ihelp-project.eu/realms/ihelp/protocol/openid-connect/token';
  
  try {
    const headers = {
      'Content-Type': 'application/x-www-form-urlencoded',
    };

    const params = new URLSearchParams();
    params.append('client_id', 'ihelp');
    params.append('username', 'pepe45');
    params.append('password', 'pepe45');
    params.append('grant_type', 'password');

    const response = await axios.post(keycloakUrl, params.toString(), { headers });
    
    return response.data.access_token;
  } catch (error) {
    console.error('Error obteniendo el token:', error);
    throw error;
  }
}




const uploadfilr = multer({ dest: os.tmpdir() }); // Directorio temporal para guardar archivos

async function compressDirectoryModelforSendToModelManager(params, res) {
    
    const temp = getJsonFromUrl(params);
    var authToken = await getTokenKeyCloak();
   

    let pathDir;

    if (os.platform() === 'win32') {
        pathDir = './scriptsML/imagesup/' + temp.username + '/' + temp.modelsname + '/';
    } else if (os.platform() === 'linux') {
        pathDir = './imagesup/' + temp.username + '/' + temp.modelsname + '/';
    }

    console.log(pathDir, 'parDir files');


    var request = require('request');
    var options = {
    'method': 'POST',
    'url': 'https://modelmanager.ihelp-project.eu/uploadFiles',
    'headers': {
        'Content-Type': 'multipart/form-data',
        'Accept': 'application/json',
        'Authorization': 'Bearer '+ authToken
    },
    formData: {
        'file': {
        'value': fs.createReadStream(pathDir + temp.modelsname + '.zip'),
        'options': {
            'filename': temp.modelsname + '.zip',
            'contentType': null
        }
        }
    }
    };
    request(options, function (error, response) {
    if (error) throw new Error(error);
    console.log(response.body);
    return res.end("Sended : " + temp.modelsname + '.zip');
    });

}





async function downloadPicturesShapPlots(req, res) {
    const fileName = req.params.name;
    var tem = getJsonFromUrl(req.url);
    console.log(req.url);
    const username = tem.username;
    const modelsname = tem.modelsname;
    console.log(username, modelsname, "download");
    var directoryPath; //= "./scriptsML/imagesup/" + username + "/" + modelsname + "/"
    //const directoryPath = "./user_upload/";

    if (os.platform() === 'win32') {
        //pathDir = './scriptsML/imagesup/' + username + '/' + modelsname + '/test';
        directoryPath = "./scriptsML/imagesup/" + username + "/" + modelsname + "/shap_plots/"

    }
    else if (os.platform() === 'linux') {
        //pathDir = './imagesup/' + username + '/'  + modelsname + '/test';
        directoryPath = "./imagesup/" + username + "/" + modelsname + "/shap_plots/"



    }



    res.download(directoryPath + fileName, fileName, (err) => {
        if (err) {
            res.status(500).send({
                message: "Could not download the file. " + err,
            });
        }
    });
};

async function compressItemClases(params, res) {

    temp = getJsonFromUrl(params)

    var pathDir;

    if (os.platform() === 'win32') {
        pathDir = './scriptsML/imagesup/' + temp.username + '/' + temp.modelsname + '/experimental/' + temp.modelsname + '_informationColumnsStringToNumber.json';
    }
    else if (os.platform() === 'linux') {
        pathDir = './imagesup/' + temp.username + '/' + temp.modelsname + '/experimental/' + temp.modelsname + '_informationColumnsStringToNumber.json';
    }
    console.log(pathDir, 'parDir files');


    fs.readFile(pathDir, function (err, data) {
        if (err) {
            res.statusCode = 500;
            res.end(`Error getting the file: ${err}.`);
        } else {
            // if the file is found, set Content-type and send data
            res.header('Access-Control-Allow-Origin', '*');
            res.header('Access-Control-Allow-Headers', 'Authorization, X-API-KEY, Origin, X-Requested-With, Content-Type, Accept, Access-Control-Allow-Request-Method,Content-Length');
            res.header('Access-Control-Allow-Methods', 'GET, POST, OPTIONS, PUT, DELETE');
            res.header('Allow', 'GET, POST, OPTIONS, PUT, DELETE');
            return res.end(data);
        }
    });
}

async function getFilesGeneratedPicturesOfScriptsAlgorithm(params, res) {

    temp = getJsonFromUrl(params)

    var pathDir;

    if (os.platform() === 'win32') {
        pathDir = './scriptsML/imagesup/' + temp.username + '/' + temp.modelsname + '/shap_plots/' + temp.modelsname + '_shapValues.json';
    }
    else if (os.platform() === 'linux') {
        pathDir = './imagesup/' + temp.username + '/' + temp.modelsname + '/shap_plots/' + temp.modelsname + '_shapValues.json';
    }
    console.log(pathDir, 'parDir files');


    fs.readFile(pathDir, function (err, data) {
        if (err) {
            res.statusCode = 500;
            res.end(`Error getting the file: ${err}.`);
        } else {
            // if the file is found, set Content-type and send data
            res.header('Access-Control-Allow-Origin', '*');
            res.header('Access-Control-Allow-Headers', 'Authorization, X-API-KEY, Origin, X-Requested-With, Content-Type, Accept, Access-Control-Allow-Request-Method,Content-Length');
            res.header('Access-Control-Allow-Methods', 'GET, POST, OPTIONS, PUT, DELETE');
            res.header('Allow', 'GET, POST, OPTIONS, PUT, DELETE');
            return res.end(data);
        }
    });
}

async function execDockerCompose(params, res) {

    temp = getJsonFromUrl(params)

    var pathDir;

    console.log('datatatatatatat')

    if (os.platform() === 'win32') {
        pathDir = './scriptsML/imagesup/' + temp.username + '/' + temp.modelsname + '/shap_plots/' + temp.modelsname + '_shapValues.json';
    }
    else if (os.platform() === 'linux') {
        pathDir = './imagesup/' + temp.username + '/' + temp.modelsname + '/shap_plots/' + temp.modelsname + '_shapValues.json';
    }
    console.log(pathDir, 'parDir files');

    const execSync = require('child_process').execSync;
    // import { execSync } from 'child_process';  // replace ^ if using ES modules

    const output = execSync('docker-compose -p  ' + temp.modelsname + ' up &', { encoding: 'utf-8' });  // the default is 'buffer'
    console.log('Output was:\n', output);
    res.header('Access-Control-Allow-Origin', '*');
    res.header('Access-Control-Allow-Headers', 'Authorization, X-API-KEY, Origin, X-Requested-With, Content-Type, Accept, Access-Control-Allow-Request-Method,Content-Length');
    res.header('Access-Control-Allow-Methods', 'GET, POST, OPTIONS, PUT, DELETE');
    res.header('Allow', 'GET, POST, OPTIONS, PUT, DELETE');
    return res.end(output);


    // fs.readFile(pathDir, function (err, data) {
    //     if (err) {
    //         res.statusCode = 500;
    //         res.end(`Error getting the file: ${err}.`);
    //     } else {
    //         // if the file is found, set Content-type and send data
    //         res.header('Access-Control-Allow-Origin', '*');
    //         res.header('Access-Control-Allow-Headers', 'Authorization, X-API-KEY, Origin, X-Requested-With, Content-Type, Accept, Access-Control-Allow-Request-Method,Content-Length');
    //         res.header('Access-Control-Allow-Methods', 'GET, POST, OPTIONS, PUT, DELETE');
    //         res.header('Allow', 'GET, POST, OPTIONS, PUT, DELETE');
    //         return res.end(data);
    //     }
    // });
}
// back end login autorization for python scripts
async function loginAutorization(params, res) {

    temp = getJsonFromUrl(params)
    var myObjec;
    var users = {}
    var pathDir;

    if (os.platform() === 'win32') {
        pathDir = './scriptsML/autorization.json';

    }
    else if (os.platform() === 'linux') {
        pathDir = './autorization.json';

    }

    if (!fs.existsSync(pathDir)) {
        fs.writeFile(pathDir, JSON.stringify(users), 'utf8', function (err) {
            if (err) throw err;
            console.log('complete');
            // var data = fs.readFileSync(pathDir);
            myObject = { key: String };
        });
    } else {
        var data = fs.readFileSync(pathDir);
        myObject = JSON.parse(data);
    }


    let newData = {};
    myObject[temp.username] = temp.modelsname;


    fs.readFile(pathDir, 'utf8', function readFileCallback(err, data) {
        if (err) {
            console.log(err);
        } else {

            json = JSON.stringify(myObject); //convert it back to json
            fs.writeFile(pathDir, json, 'utf8', function (err) {
                if (err) throw err;
            }); // write it back 
        }
    });





    return res.end()

};
// back end logout autorization for python scripts
async function logoutAutorization(params, res) {

    temp = getJsonFromUrl(params)
    var pathDir;

    if (os.platform() === 'win32') {
        pathDir = './scriptsML/autorization.json';

    }
    else if (os.platform() === 'linux') {
        pathDir = './autorization.json';

    }

    var data = fs.readFileSync(pathDir);
    var myObject = JSON.parse(data);

    myObject[temp.username] = temp.modelsname;


    fs.readFile(pathDir, 'utf8', function readFileCallback(err, data) {
        if (err) {
            console.log(err);
        } else {
            json = JSON.stringify(myObject); //convert it back to json
            fs.writeFile(pathDir, json, 'utf8', function (err) {
                if (err) throw err;
            }); // write it back 
        }
    });


    return res.end()

}

async function uploadFilesGAN(req, res) {
    try {

        var tem = getJsonFromUrl(req.url);
        const username = tem.username;
        const modelsname = tem.modelsname;
        const classname = tem.classname;

        // const directory = tem.directory;
        console.log(username, modelsname, "filesUpload");

        console.log('ArchivoREQ: ' + req.url);
        console.log('ArchivoREs: ' + res.params);
        console.dir('Archivo: ' + req.files[0]);

        console.log('++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++');

        var xRayFoldere
        var pathDir;




        if (os.platform() === 'win32') {
            // xRayFoldere = './scriptsML/imagesup/' + username + '/'  + modelsname + '/xray/';
            pathDir = './scriptsML/imagesup/' + username + '/' + modelsname + '/xray/' + classname;

        }
        else if (os.platform() === 'linux') {
            // xRayFoldere = './imagesup/' + username + '/'  + modelsname + '/xray/';
            pathDir = './imagesup/' + username + '/' + modelsname + '/xray/' + classname;

        }
        // console.log(xRayFoldere, 'xRayFoldere');
        console.log(pathDir, 'parDir');
        var fs = require('fs');



        // if (!fs.existsSync(xRayFoldere)) {
        //         fs.mkdirSync(xRayFoldere);
        //     }


        if (!fs.existsSync(pathDir)) {
            fs.mkdirSync(pathDir, { recursive: true });
        }



        let EDFile = req.files.file


        EDFile.mv(`${pathDir}/${EDFile.name}`, err => {

            if (err) return res.status(500).send({ message: err })


            return res.status(200).send({ message: 'File upload' })

        })


    } catch (err) {

        if (err.code == "LIMIT_FILE_SIZE") {
            return res.status(500).send({
                message: "File size cannot be larger than 2MB!",
            });
        }
        res.status(500).send({
            message: `Could not upload the file: ${req}. ${err}`,
        });
    }
}

async function compressFilesGAN(params, res, next) {



    let tem = getJsonFromUrl(params);

    var user = tem.username;
    var model = tem.modelsname;
    var directoryInitialImages = tem.directory;




    var pathDir;


    if (os.platform() === 'win32') {
        pathDir = './scriptsML/imagesup/' + user + '/' + model + '/';
        directoryInitialImages = './scriptsML/imagesup/' + user + '/'


    }
    else if (os.platform() === 'linux') {
        pathDir = './imagesup/' + user + '/' + model + '/';
        directoryInitialImages = './imagesup/' + user + '/'


    }
    console.log(pathDir, 'compressFilesGAN');

    if (!fs.existsSync(pathDir)) {
        fs.mkdirSync(pathDir);
    }


    var nameFilezip = user + '_' + model + '.zip';

    // var output = fs.createWriteStream('./user_upload/' + nameFilezip);
    var output = fs.createWriteStream(directoryInitialImages + '/' + nameFilezip);

    var archive = archiver('zip', {
        zlib: { level: 9 } // Sets the compression level.
    });

    output.on('close', () => {
        console.log(archive.pointer() + ' total bytes');
        console.log('archiver has been finalized and the output file descriptor has closed.');
        fs.rename(directoryInitialImages + '/' + nameFilezip, pathDir + '/' + nameFilezip, function (err) {
            if (err) throw err
            console.log('Successfully renamed - AKA moved!')
        })
    });

    archive.on('error', function (err) {
        throw err;
    });

    // good practice to catch warnings (ie stat failures and other non-blocking errors)
    archive.on('warning', function (err) {
        if (err.code === 'ENOENT') {
            // log warning
        } else {
            // throw error
            throw err;
        }
    });

    // This event is fired when the data source is drained no matter what was the data source.
    // It is not part of this library but rather from the NodeJS Stream API.
    // @see: https://nodejs.org/api/stream.html#stream_event_end
    output.on('end', function () {
        console.log('Data has been drained');
    });

    archive.pipe(output);

    // append files from a sub-directory, putting its contents at the root of archive
    archive.directory(pathDir, false);

    archive.finalize();







}


async function envBackEnd(params, res, next) {
    const config = require('../config.js');

    console.log('Send env: ', config.KEYCLOAK_SERVER)
    return res.end(config.KEYCLOAK_SERVER);


}

