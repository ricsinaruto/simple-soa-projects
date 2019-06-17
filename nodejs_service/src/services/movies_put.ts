'use strict';

import * as express from 'express';
import {Movie} from '../schemas/movies';

const router = express.Router();

// GET movies request
router.put('/',(req,res) => {
    var movie = new Movie(req.body);
    movie.id = req.baseUrl.split("/")[2];

    Movie.findOne({id: movie.id}, (err: any, mov: any) => {
        if (mov) {
            Movie.deleteOne({id: movie.id}, (err: any) => {if (err) console.log(err);});
        }
    });
    movie.save((err: any) => {
        if (err) {
            res.send(err);
        } else {
            res.send();
        }
    });
});

export default router;