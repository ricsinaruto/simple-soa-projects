'use strict';
Object.defineProperty(exports, "__esModule", { value: true });
const express = require("express");
const movies_1 = require("../schemas/movies");
const router = express.Router();
router.post('/', (req, res) => {
    var movie = new movies_1.Movie(req.body);
    movies_1.Movie.find((err, movies) => {
        if (err) {
            console.log("Error");
        }
        else {
            let id = 1;
            for (let m of movies) {
                if (m.id >= id) {
                    id = m.id + 1;
                }
            }
            movie.id = id;
            movie.save((err) => {
                if (err) {
                    res.send(err);
                }
                else {
                    let movie_id = new movies_1.MovieId();
                    movie_id.id = id;
                    res.send(movie_id.toJSON());
                }
            });
        }
    });
});
exports.default = router;
//# sourceMappingURL=movies_post_service.js.map