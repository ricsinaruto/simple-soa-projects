'use strict';

import * as express from 'express';
import movies_get_service from '../services/movies_get_service';
import movies_post_service from '../services/movies_post_service';
import movies_getid from '../services/movies_getid';
import movies_put from '../services/movies_put';
import movies_delete from '../services/movies_delete';
const router = express.Router();

router.use('/movies', movies_get_service);
router.use('/movies', movies_post_service);
router.use('/movies/:id', movies_getid);
router.use('/movies/:id', movies_put);
router.use('/movies/:id', movies_delete);

/* GET home page. */
router.get('/',(req,res,next) => {
  res.render('index', {title: 'Express'});
});

export default router;