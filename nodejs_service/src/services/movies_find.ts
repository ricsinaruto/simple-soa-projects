'use strict';

import * as express from 'express';
import {Movie, MovieIdList} from '../schemas/movies';

const router = express.Router();

// GET movies request
router.get('/find',(req,res) => {
    let year = req.query.year;
    let orderby = req.query.orderby;

    Movie.find({year: year} ,(err: any, movies: any) => {
        if (err) {
            console.log(err);
        } else {
            let movies_sorted = movies.sort((m1, m2) => {
                if (orderby=="title") {
                    if (m1.title > m2.title) {
                        return 1;
                    }
                    if (m1.title < m2.title) {
                        return -1;
                    }
                    return 0;
                } else {
                    if (m1.director > m2.director) {
                        return 1;
                    }
                    if (m1.director < m2.director) {
                        return -1;
                    }
                    return 0;
                }
            });
            let ids: number[] = [];
            for (let m of movies_sorted) {
                ids.push(m.id);
            }
            console.log(ids);
            let movie_ids = new MovieIdList();
            movie_ids.id = ids;
            res.send(movie_ids.toJSON());
        }
    })
});

export default router;