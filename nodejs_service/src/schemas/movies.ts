'use_strict';

import * as mongoose from 'mongoose';
import {IMovie, IMovieList, IMovieId, IMovieIdList} from '../interfaces/movies_interface';

export interface MovieEntity extends IMovie, mongoose.Document { }
let MovieSchema = new mongoose.Schema({
    id: Number,
    title: String,
    year: Number,
    director: String,
    actor: [{
        type: String
    }]
});
MovieSchema.set('toJSON', {
    transform: function(doc, ret, options) {
        var retJson = {
            title: ret.title,
            year: ret.year,
            director: ret.director,
            actor: ret.actor
        };
        return retJson;
    }
});
export var Movie = mongoose.model<MovieEntity>('Movies', MovieSchema, 'Movies');

export interface MovieListEntity extends IMovieList, mongoose.Document { }
let MovieListSchema = new mongoose.Schema({
    movie: [{
        type: MovieSchema
    }]
});
MovieListSchema.set('toJSON', {
    transform: function(doc, ret, options) {
        var retJson = {
            movie: ret.movie
        };
        return retJson;
    }
});
export var MovieList = mongoose.model<MovieListEntity>('MovieList', MovieListSchema, 'MovieList');

export interface MovieIdEntity extends IMovieId, mongoose.Document { }
let MovieIdSchema = new mongoose.Schema({
    id: Number
});
MovieIdSchema.set('toJSON', {
    transform: function(doc, ret, options) {
        var retJson = {
            id: ret.id
        };
        return retJson;
    }
});
export var MovieId = mongoose.model<MovieIdEntity>('MovieId', MovieIdSchema, 'MovieId');

export interface MovieIdListEntity extends IMovieIdList, mongoose.Document { }
let MovieIdListSchema = new mongoose.Schema({
    id: [{
        type: Number
    }]
});
MovieIdListSchema.set('toJSON', {
    transform: function(doc, ret, options) {
        var retJson = {
            id: ret.id
        };
        return retJson;
    }
});
export var MovieIdList = mongoose.model<MovieIdListEntity>('MovieIdList', MovieIdListSchema, 'MovieIdList');