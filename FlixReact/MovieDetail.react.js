import React, { PropTypes } from 'react';
import {
  Text,
  Image,
  StyleSheet,
  BackAndroid,
  ScrollView,
} from 'react-native';
import { getBackdropURI } from './utils';
import MovieShape from './shapes/MovieShape';

const propTypes = {
  movie: MovieShape.isRequired,
  navigator: PropTypes.object.isRequired,
};

const styles = StyleSheet.create({
  container: {
    paddingBottom: 20,
  },
  text: {
    paddingLeft: 20,
    paddingRight: 20,
    paddingBottom: 15,
  },
  title: {
    fontWeight: 'bold',
    fontSize: 18,
    paddingTop: 15,
  },
  posterImage: {
    flex: 1,
    width: null,
    height: 200,
  },
});

class MovieDetail extends React.Component {
  constructor(props) {
    super(props);

    this.goBack = this.goBack.bind(this);
  }

  componentDidMount() {
    BackAndroid.addEventListener('hardwareBackPress', this.goBack);
  }

  componentWillUnmount() {
    BackAndroid.removeEventListener('hardwareBackPress', this.goBack);
  }

  goBack() {
    this.props.navigator.pop();
    return true;
  }

  render() {
    const { movie } = this.props;
    const { title, overview, release_date } = movie;
    return (
      <ScrollView style={styles.container}>
        <Image
          source={{ uri: getBackdropURI(movie) }}
          resizeMode="cover"
          style={styles.posterImage}
        />
        <Text style={[styles.title, styles.text]}>{title}</Text>
        <Text style={styles.text}>{overview}</Text>
        <Text style={styles.text}>{`Release date: ${release_date}`}</Text>
      </ScrollView>
    );
  }
}

MovieDetail.propTypes = propTypes;
export default MovieDetail;
