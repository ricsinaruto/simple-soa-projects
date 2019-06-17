'use strict';
Object.defineProperty(exports, "__esModule", { value: true });
const express = require("express");
const movies_1 = require("../schemas/movies");
const router = express.Router();
router.put('/', (req, res) => {
    var movie = new movies_1.Movie(req.body);
    movie.id = req.baseUrl.split("/")[2];
    movies_1.Movie.findOne({ id: movie.id }, (err, mov) => {
        if (mov) {
            movies_1.Movie.deleteOne({ id: movie.id }, (err) => { if (err)
                console.log(err); });
        }
    });
    movie.save((err) => {
        if (err) {
            res.send(err);
        }
        else {
            res.send();
        }
    });
});
exports.default = router;
//# sourceMappingURL=movies_put.js.map