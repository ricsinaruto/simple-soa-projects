'use strict';

import * as express from 'express';
import {Movie} from '../schemas/movies';

const router = express.Router();

// GET movies request
router.delete('/',(req,res) => {
    Movie.deleteOne({id: req.baseUrl.split("/")[2]} ,(err: any) => {
        if (err) {
            console.log(err);
        } else {
            res.send();
        }
    })
});

export default router;