const express = require('express');
const router = express.Router();
const Joi = require('joi');
const validateRequest = require('./../middleware/validate-request');
// const authorize = require('./../middleware/authorize')// old loggin deprecated
const userService = require('./user.service');// old loggin deprecated
const { response } = require('express');
const bodyParser = require("body-parser");
const session = require('express-session');
const Keycloak = require('keycloak-connect');
const config = require('../config.js');
const fileUpload = require('express-fileupload')
const cors = require('cors');



// Parse URL-encoded bodies (as sent by HTML forms)
router.use(bodyParser.urlencoded({
    extended: true,
    limit: '25mb'
}));
// app.use(express.urlencoded());
// Parse JSON bodies (as sent by API clients)
router.use(bodyParser.json({limit: '25mb'}));
// app.use(express.json());
router.use(fileUpload())
router.use(cors());
// public no protect with keycloak review just in case
router.get("/files/downloadPicturesShapPlots/:name", downloadPicturesShapPlots);
router.get("/files/downloadPicturesModelsCNN/:name", downloadPicturesModelsCNN);
router.get("/files/downloadPicturesModelsTest/:name", downloadPicturesModelsTest);
router.get("/files/getListFiles", getListFiles);
router.get("/files/download/:name" ,download);
router.get('/files/envBackEnd', envBackEnd )

//test in docker 
// router.get('/uploadClasses', uploadClasses);
// router.post('/createdirectorymodel', createdirectorymodel);
// router.get("/files/uploadJSONClasses", uploadJSONClasses);
// router.post("/uploadFilesTest", uploadFilesTest);
// router.post('/deleteModels', deleteModels);
// router.post("/filesupload",  filesupload);
// router.get("/files/compressFilesGAN",compressFilesGAN);




console.log('Keycloak Server Back  End: ' ,config.KEYCLOAK_SERVER);
const kcConfig = {
  "realm": "analyticsproduct",
  "auth-server-url": config.KEYCLOAK_SERVER,
  "ssl-required": "external",
  "resource": "analytics",
  "public-client": true,
  "confidential-port": 0,
  "enable-cors": true,
  "cors-exposed-headers": "X-Custom1",
//   "realmPublicKey": config.REALM_PUBLIC_KEY 

}


const memoryStore = new session.MemoryStore();
const keycloak = new Keycloak({ store: memoryStore }, kcConfig);
// api routes
// router.set( 'trust proxy', true );
router.use( keycloak.middleware() );
router.use(cors());


//
// // routes
// router.post('/authenticate', authenticateSchema, authenticate);
// router.post('/register', registerSchema, register);
router.post('/newworkspace',keycloak.protect(), createWorkSpaceModels);
router.post('/deleteworkspace',keycloak.protect(), deleteWorkSpaceModels);
router.post('/createdirectorymodel',  keycloak.protect(), createdirectorymodel);

router.post('/files/upload', keycloak.protect(), upload);

// router.get('/downloadJSONFiles', keycloak.protect(), downloadJSONFiles);
router.get('/downloadJSONFiles', keycloak.protect(), downloadJSONFiles);
router.get('/datasetCVSTOJSON', keycloak.protect(), datasetCVSTOJSON);
router.get('/selectUniqueValues', keycloak.protect(), selectUniqueValues);
router.post('/selectUniqueValuesPost', keycloak.protect(), selectUniqueValuesPost);
router.get('/uploadClasses', keycloak.protect(), uploadClasses);
router.post('/files/deleteModels', keycloak.protect(), deleteModels);
router.get('/readSettingsJSONFiles', keycloak.protect(), readSettingsJSONFiles);
router.get('/writeSettingsJSONFiles', keycloak.protect(), writeSettingsJSONFiles);
router.get('/readFilesJSONDirectory', keycloak.protect(), readFilesJSONDirectory);
//router.get('/readFilesJSONDirectory', keycloak.protect(), readFilesJSONDirectory); 
router.get('/readLoadingJSON', keycloak.protect(), readLoadingJSON);
router.get('/readLoadingJSONValuesColumsToSelect', keycloak.protect(), readLoadingJSONValuesColumsToSelect);
router.get('/mysqltestdb', keycloak.protect(), testingmysqldb);
router.get('/',  keycloak.protect(), getAll);
router.get('/current',  keycloak.protect(), getCurrent);
router.get('/:id',  keycloak.protect(), getById);
router.put('/:id',  keycloak.protect(), updateSchema, update);
router.delete('/:id',  keycloak.protect(), _delete);
router.post("/filesupload",  keycloak.protect(), filesupload);
router.post("/files/uploadFilesTest",  keycloak.protect(), uploadFilesTest);
router.get("/files/uploadJSONClasses",  keycloak.protect(), uploadJSONClasses);
router.get("/files/getLeyendJSONColumnsObject" , keycloak.protect(), getLeyendJSONColumnsObject);
router.get("/files/compressDirectoryModelforSendToModelManager",  keycloak.protect(), compressDirectoryModelforSendToModelManager);
router.post('/updateFiletoUploads',keycloak.protect() , updateFiletoUploads);


router.get("/files/downloadClass/:name", keycloak.protect(),  downloadClass);
router.get("/files/compressItemClases", keycloak.protect(), compressItemClases);
router.get("/files/getFilesGeneratedPicturesOfScriptsAlgorithm", keycloak.protect(), getFilesGeneratedPicturesOfScriptsAlgorithm);
router.get('/files/execDockerCompose', keycloak.protect(), execDockerCompose);
router.get("/files/loginAutorization", keycloak.protect(),loginAutorization);
router.get("/files/logoutAutorization",keycloak.protect(), logoutAutorization);

router.post("/files/uploadFilesGAN",keycloak.protect(), uploadFilesGAN);
router.get("/files/compressFilesGAN", keycloak.protect(),compressFilesGAN);
// router.get("/uploadFilesNormalTest",keycloak.protect(), uploadFilesNormalTest);
// router.get("/uploadFilesNormalValid",keycloak.protect(), uploadFilesNormalValid);
// router.get("/uploadFilesBadTrain",keycloak.protect(), uploadFilesBadTrain);
// router.get("/uploadFilesBadTest",keycloak.protect(), uploadFilesBadTest);
// router.get("/uploadFilesBadValid",keycloak.protect(), uploadFilesBadValid);





module.exports = router;



// function authenticateSchema(req, res, next) {
//     const schema = Joi.object({
//         username: Joi.string().required(),
//         password: Joi.string().required()
//     });
//     validateRequest(req, next, schema);
// }

// function authenticate(req, res, next) {
//     userService.authenticate(req.body)
//         .then(user => res.json(user))
//         .catch(next);
// }

// function registerSchema(req, res, next) {
//     const schema = Joi.object({
//         firstName: Joi.string().required(),
//         lastName: Joi.string().required(),
//         username: Joi.string().required(),
//         password: Joi.string().min(6).required()
//     });
//     validateRequest(req, next, schema);
// }

// function register(req, res, next) {

//     userService.create(req.body)
//         .then(() => res.json({ message: 'Registration successful' }))
//         .catch(next);
   
// }

function createWorkSpaceModels(req, res, next) {
 
     userService.createWorkSpaceModels(req.body)
        .then(() => res.json({ message: 'Create workspace successful' }))
        .catch(next);
   
}

function deleteWorkSpaceModels(req, res, next) {
 
     userService.deleteWorkSpaceModels(req.body)
        .then(() => res.json({ message: 'Delete workspace successful' }))
        .catch(next);
   
}

async function deleteModels(req, res, next) {
 
    try {
        userService.deleteModels(req.body) 
        .then(() => res.json({ message: 'Delete model successful' }))
        .catch(next);

    } catch (err) {
        next;
    }
}

function createdirectorymodel(req, res, next) {
 
     userService.createdirectorymodel(req.body)
        .then(() => res.json({ message: 'Create directory model successful' }))
        .catch(next);
   
}

function testingmysqldb(req, res, next) {
 
        userService.testingmysqldb(req, res, next)
        .then(() => res.json({ message: 'OK database' }))
        .catch(next);
   
}

async function downloadJSONFiles(req, res, next) {
 
    try {
        userService.downloadJSONFiles(req.url, res, next)

    } catch (err) {
        next;
    }
}

async function datasetCVSTOJSON(req, res, next) {
 
    try {
        userService.datasetCVSTOJSON(req.url, res, next)

    } catch (err) {
        next;
    }
}

async function selectUniqueValues(req, res, next) {
 
    try {
        userService.selectUniqueValues(req.url, res, next)

    } catch (err) {
        next;
    }
}

async function selectUniqueValuesPost(req, res, next) {
 
    try {
        userService.selectUniqueValuesPost(req.url, res, next)

    } catch (err) {
        next;
    }
}

async function uploadClasses(req, res, next) {
 
    try {
        userService.uploadClasses(req.url, res, next)
    } catch (err) {
        next;
    }
}

async function readSettingsJSONFiles(req, res, next) {
 
    try {
        userService.readSettingsJSONFiles(req.url, res, next)

    } catch (err) {
        next;
    }
}

async function readLoadingJSON(req, res, next) {
 
    try {
        userService.readLoadingJSON(req.url, res, next)

    } catch (err) {
        next;
    }
}

async function readLoadingJSONValuesColumsToSelect(req, res, next) {
 
    try {
        userService.readLoadingJSONValuesColumsToSelect(req.url, res, next)

    } catch (err) {
        next;
    }
}

async function writeSettingsJSONFiles(req, res, next) {
 
    try {
        userService.writeSettingsJSONFiles(req.url, res, next)

    } catch (err) {
        next;
    }
}

async function readFilesJSONDirectory(req, res, next) {

    try {

        userService.readFilesJSONDirectory(req.url, res, next)

    } catch (err) {
        next;
    }



}

function getAll(req, res, next) {
    userService.getAll()
        .then(users => res.json(users))
        .catch(next);
}

function getCurrent(req, res, next) {
    res.json(req.user);
}

function getById(req, res, next) {
    userService.getById(req.params.id)
        .then(user => res.json(user))
        .catch(next);
}

function updateSchema(req, res, next) {
    const schema = Joi.object({
        firstName: Joi.string().empty(''),
        lastName: Joi.string().empty(''),
        username: Joi.string().empty(''),
        password: Joi.string().min(6).empty('')
    });
    validateRequest(req, next, schema);
}

function update(req, res, next) {
    userService.update(req.params.id, req.body)
        .then(user => res.json(user))
        .catch(next);
}

function files(req, res, next) {
    console.log( 'Files ---  ####################')

    userService.files(req, req, next);
        // .then(user => res.json(user))
        // .catch(next);
}

function _delete(req, res, next) {
    userService.delete(req.params.id)
        .then(() => res.json({ message: 'User deleted successfully' }))
        .catch(next);
}

function upload (req, res, next) {
    userService.upload(req, res, next)
   
    res.send(req.files);
  // console.log(req.files);

    currentFiles = req.files;
    console.log(req.files);
    
    
}

async function updateFiletoUploads(req, res, next) {
 
    try {
        userService.updateFiletoUploads(req, res, next)

    } catch (err) {
        next;
    }
}

async function filesupload(req, res, next) {
 
    try {
      

        userService.filesupload(req, res, next)

    } catch (err) {
        next;
    }
}

async function getListFiles (req, res, next) {
 
    try {
        userService.getListFiles(req, res, next)

    } catch (err) {
        next;
    }
}

async function download(req, res, next) {
 
    try {

        userService.download(req, res, next)

    } catch (err) {
        next;
    }
}

async function downloadClass(req, res, next) {
 
    try {

        userService.downloadClass(req, res, next)

    } catch (err) {
        next;
    }
}

async function downloadPicturesModelsCNN(req, res, next) {
 
    try {

        userService.downloadPicturesModelsCNN(req, res, next)

    } catch (err) {
        next;
    }
}

async function downloadPicturesModelsTest(req, res, next) {
 
    try {

        userService.downloadPicturesModelsTest(req, res, next)

    } catch (err) {
        next;
    }
}

async function uploadFilesTest(req, res, next) {
 
    try {

        userService.uploadFilesTest(req, res, next)

    } catch (err) {
        next;
    }
}


async function uploadJSONClasses(req, res, next) {
 
    try {

        userService.uploadJSONClasses(req.url, res, next)

    } catch (err) {
        next;
    }
}


async function getLeyendJSONColumnsObject(req, res, next) {
 
    try {

        userService.getLeyendJSONColumnsObject(req.url, res, next)

    } catch (err) {
        next;
    }
}

async function compressDirectoryModelforSendToModelManager(req, res, next) {
 
    try {

        userService.compressDirectoryModelforSendToModelManager(req.url, res, next)

    } catch (err) {
        next;
    }
}

async function downloadPicturesShapPlots(req, res, next) {
 
    try {

        userService.downloadPicturesShapPlots(req, res, next)

    } catch (err) {
        next;
    }
}

async function compressItemClases(req, res, next) {
 
    try {

        userService.compressItemClases(req.url, res, next)

    } catch (err) {
        next;
    }
}

async function getFilesGeneratedPicturesOfScriptsAlgorithm(req, res, next) {
 
    try {

        userService.getFilesGeneratedPicturesOfScriptsAlgorithm(req.url, res, next)

    } catch (err) {
        next;
    }
}

async function execDockerCompose(req, res, next) {
 
    try {

        userService.execDockerCompose(req.url, res, next)

    } catch (err) {
        next;
    }
}


async function loginAutorization(req, res, next) {
 
    try {

        userService.loginAutorization(req.url, res, next)

    } catch (err) {
        next;
    }
}

async function logoutAutorization(req, res, next) {
 
    try {

        userService.logoutAutorization(req.url, res, next)

    } catch (err) {
        next;
    }
}

async function uploadFilesGAN(req, res, next) {
 
    try {
      

        userService.uploadFilesGAN(req, res, next)

    } catch (err) {
        next;
    }
}

async function compressFilesGAN(req, res, next) {
 
    try {
        userService.compressFilesGAN(req.url, res, next)
    } catch (err) {
        next;
    }
}

async function envBackEnd(req, res, next) {
 
    try {
        userService.envBackEnd(req.url, res, next)
    } catch (err) {
        next;
    }
}

