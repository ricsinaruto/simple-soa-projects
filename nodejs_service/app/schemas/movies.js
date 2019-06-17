'use_strict';
"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
const mongoose = require("mongoose");
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
    transform: function (doc, ret, options) {
        var retJson = {
            title: ret.title,
            year: ret.year,
            director: ret.director,
            actor: ret.actor
        };
        return retJson;
    }
});
exports.Movie = mongoose.model('Movies', MovieSchema, 'Movies');
let MovieListSchema = new mongoose.Schema({
    movie: [{
            type: MovieSchema
        }]
});
MovieListSchema.set('toJSON', {
    transform: function (doc, ret, options) {
        var retJson = {
            movie: ret.movie
        };
        return retJson;
    }
});
exports.MovieList = mongoose.model('MovieList', MovieListSchema, 'MovieList');
let MovieIdSchema = new mongoose.Schema({
    id: Number
});
MovieIdSchema.set('toJSON', {
    transform: function (doc, ret, options) {
        var retJson = {
            id: ret.id
        };
        return retJson;
    }
});
exports.MovieId = mongoose.model('MovieId', MovieIdSchema, 'MovieId');
let MovieIdListSchema = new mongoose.Schema({
    id: [{
            type: Number
        }]
});
MovieIdListSchema.set('toJSON', {
    transform: function (doc, ret, options) {
        var retJson = {
            id: ret.id
        };
        return retJson;
    }
});
exports.MovieIdList = mongoose.model('MovieIdList', MovieIdListSchema, 'MovieIdList');
//# sourceMappingURL=movies.js.map