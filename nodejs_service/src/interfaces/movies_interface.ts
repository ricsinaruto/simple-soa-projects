'use_strict';

export interface IMovie {
    title: string;
    year: number;
    director: string;
    actor: string[];
}

export interface IMovieList {
    movie: IMovie[];
}

export interface IMovieId {
    id: number;
}

export interface IMovieIdList {
    id: number[];
}