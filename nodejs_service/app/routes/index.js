'use strict';
Object.defineProperty(exports, "__esModule", { value: true });
const express = require("express");
const movies_get_service_1 = require("../services/movies_get_service");
const movies_post_service_1 = require("../services/movies_post_service");
const movies_getid_1 = require("../services/movies_getid");
const movies_put_1 = require("../services/movies_put");
const movies_delete_1 = require("../services/movies_delete");
const router = express.Router();
router.use('/movies', movies_get_service_1.default);
router.use('/movies', movies_post_service_1.default);
router.use('/movies/:id', movies_getid_1.default);
router.use('/movies/:id', movies_put_1.default);
router.use('/movies/:id', movies_delete_1.default);
router.get('/', (req, res, next) => {
    res.render('index', { title: 'Express' });
});
exports.default = router;
//# sourceMappingURL=index.js.map