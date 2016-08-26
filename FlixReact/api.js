import mockData from './mockData';

const NOW_PLAYING_URL = 'https://api.themoviedb.org/3/movie/now_playing?api_key=f64df2643b6b52a62df19cd6fae8156d';
const TOP_RATED_URL = 'https://api.themoviedb.org/3/movie/top_rated?api_key=f64df2643b6b52a62df19cd6fae8156d';

const isError = false;
export const fetchMoviesMock = () => (
  new Promise((resolve, reject) => {
    setTimeout(() => {
      if (isError) {
        reject({ msg: 'network error happened' });
      } else {
        resolve(mockData.results);
      }
    });
  })
);

export const fetchNowPlayingMovies = () => (
  fetch(NOW_PLAYING_URL)
    .then(response => response.json())
    .then(response => response.results)
);
export const fetchTopRatedMovies = () => (
  fetch(TOP_RATED_URL)
    .then(response => response.json())
    .then(response => response.results)
);
