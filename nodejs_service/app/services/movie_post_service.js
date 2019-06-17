'use strict';
Object.defineProperty(exports, "__esModule", { value: true });
const express = require("express");
const movies_1 = require("../schemas/movies");
const router = express.Router();
router.post('/', (req, res) => {
    let passedRequest = req.body;
    movies_1.MovieList.create(passedRequest, function (err, mes) { });
});
exports.default = router;
//# sourceMappingURL=movie_post_service.js.map