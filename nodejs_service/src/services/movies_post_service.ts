'use strict';

import * as express from 'express';
import {Movie, MovieId} from '../schemas/movies';

const router = express.Router();

// GET movies request
router.post('/',(req,res) => {
    var movie = new Movie(req.body);
    Movie.find((err: any, movies: any) => {
        if (err) {
            console.log("Error");
        } else {
            let id = 1;
            for (let m of movies) {
                if (m.id >= id) {
                    id = m.id + 1;
                }
            }
            movie.id = id;
            movie.save((err: any) => {
                if (err) {
                    res.send(err);
                } else {
                    let movie_id = new MovieId();
                    movie_id.id = id;
                    res.send(movie_id.toJSON());
                }
            });
        }
    });
});

export default router;