'use strict';
Object.defineProperty(exports, "__esModule", { value: true });
const express = require("express");
const movies_1 = require("../schemas/movies");
const router = express.Router();
router.get('/', (req, res) => {
    movies_1.Movie.find((err, movies) => {
        if (err) {
            res.send("Error");
        }
        else {
            let movies_list = new movies_1.MovieList();
            movies_list.movie = movies;
            res.send(movies_list.toJSON());
        }
    });
});
exports.default = router;
//# sourceMappingURL=movies_get_service.js.map