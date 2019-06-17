'use strict';
Object.defineProperty(exports, "__esModule", { value: true });
const express = require("express");
const movies_1 = require("../schemas/movies");
const router = express.Router();
router.get('/', (req, res) => {
    movies_1.MovieList.find({}, function (err, list) {
        if (list) {
            res.json({ data: list.message });
        }
    });
});
exports.default = router;
//# sourceMappingURL=service.js.map