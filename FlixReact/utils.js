const imageURIPrefix = 'https://image.tmdb.org/t/p/original';
export const getPosterURI = movie => `${imageURIPrefix}${movie.poster_path}`;
export const getBackdropURI = movie => `${imageURIPrefix}${movie.backdrop_path}`;
