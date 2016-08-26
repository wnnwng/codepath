import { PropTypes } from 'react';

const MovieShape = PropTypes.shape({
  title: PropTypes.string.isRequired,
  overview: PropTypes.string.isRequired,
  poster_path: PropTypes.string,
  backdrop_path: PropTypes.string,
  release_date: PropTypes.string,
});

export default MovieShape;
