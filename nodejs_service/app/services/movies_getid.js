'use strict';
Object.defineProperty(exports, "__esModule", { value: true });
const express = require("express");
const movies_1 = require("../schemas/movies");
const router = express.Router();
router.get('/', (req, res) => {
    if (req.baseUrl.split("?")[0] == "/movies/find") {
        let year = req.query.year;
        let orderby = req.query.orderby;
        movies_1.Movie.find({ year: year }, (err, movies) => {
            if (err) {
                console.log(err);
            }
            else {
                let movies_sorted = movies.sort((m1, m2) => {
                    if (orderby == "Title") {
                        if (m1.title > m2.title) {
                            return 1;
                        }
                        if (m1.title < m2.title) {
                            return -1;
                        }
                        return 0;
                    }
                    else {
                        if (m1.director > m2.director) {
                            return 1;
                        }
                        if (m1.director < m2.director) {
                            return -1;
                        }
                        return 0;
                    }
                });
                let ids = [];
                for (let m of movies_sorted) {
                    ids.push(m.id);
                }
                let movie_ids = new movies_1.MovieIdList();
                movie_ids.id = ids;
                res.send(movie_ids.toJSON());
            }
        });
    }
    else {
        movies_1.Movie.findOne({ id: req.baseUrl.split("/")[2] }, (err, movie) => {
            if (err) {
                console.log(err);
            }
            if (movie) {
                res.send(movie);
            }
            else {
                res.status(404).send();
            }
        });
    }
});
exports.default = router;
//# sourceMappingURL=movies_getid.js.map