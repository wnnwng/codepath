import React, { PropTypes } from 'react';
import {
  View,
  Text,
  StyleSheet,
  TouchableOpacity,
  Image,
} from 'react-native';
import { getPosterURI } from './utils';
import MovieShape from './shapes/MovieShape';

const styles = StyleSheet.create({
  rowContainer: {
    padding: 10,
    paddingLeft: 0,
    flexDirection: 'row',
  },
  image: {
    height: 100,
    width: 100,
  },
  textContainer: {
    flex: 1,
    justifyContent: 'space-between',
  },
  text: {
    color: 'rgb(97, 97, 96)',
    fontSize: 12,
  },
  title: {
    fontSize: 16,
    fontWeight: 'bold',
  },
});

const propTypes = {
  movie: MovieShape.isRequired,
  navigator: PropTypes.object,
};

class MovieCellView extends React.Component {
  render() {
    const { movie, navigator } = this.props;

    const viewDetails = () => {
      if (navigator) {
        navigator.push({ index: 1, movie });
      }
    };

    return (
      <TouchableOpacity onPress={viewDetails}>
        <View style={styles.rowContainer}>
          <Image
            resizeMode="contain"
            style={styles.image}
            source={{ uri: getPosterURI(movie) }}
          />
          <View style={styles.textContainer}>
            <Text
              style={[styles.text, styles.title]}
              numberOfLines={1}
            >
              {movie.title}
            </Text>
            <Text
              style={styles.text}
              numberOfLines={3}
            >
              {movie.overview}
            </Text>
          </View>
        </View>
      </TouchableOpacity>
    );
  }
}

MovieCellView.propTypes = propTypes;
export default MovieCellView;
