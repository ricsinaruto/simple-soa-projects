'use strict';
Object.defineProperty(exports, "__esModule", { value: true });
const express = require("express");
const movies_1 = require("../schemas/movies");
const router = express.Router();
router.delete('/', (req, res) => {
    movies_1.Movie.deleteOne({ id: req.baseUrl.split("/")[2] }, (err) => {
        if (err) {
            console.log(err);
        }
        else {
            res.send();
        }
    });
});
exports.default = router;
//# sourceMappingURL=movies_delete.js.map