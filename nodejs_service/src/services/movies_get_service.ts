'use strict';

import * as express from 'express';
import {MovieList,Movie} from '../schemas/movies';

const router = express.Router();

// GET movies request
router.get('/',(req,res) => {
    Movie.find((err: any, movies: any) => {
        if (err) {
            res.send("Error");
        } else {
            let movies_list = new MovieList();
            movies_list.movie = movies
            res.send(movies_list.toJSON());
        }
    });
});

export default router;