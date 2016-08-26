import React, { PropTypes } from 'react';
import {
  ListView,
  Text,
  View,
  ScrollView,
  TextInput,
  Navigator,
  RefreshControl,
  StyleSheet,
} from 'react-native';
import { fetchTopRatedMovies, fetchNowPlayingMovies } from './api';
import MovieCellView from './MovieCellView.react';
import MovieDetail from './MovieDetail.react';
import MovieCategories from './constants/MovieCategories';

const propTypes = {
  category: PropTypes.string.isRequired,
};

const styles = StyleSheet.create({
  searchBar: {
    backgroundColor: 'rgb(126, 126, 126)',
    height: 50,
  },
  searchInput: {
    height: 40,
    margin: 5,
    borderRadius: 5,
    backgroundColor: 'white',
  },
});

class MoviesView extends React.Component {
  constructor(props) {
    super(props);

    this.state = {
      dataSource: new ListView.DataSource({
        rowHasChanged: (r1, r2) => r1 !== r2,
      }),
      searchText: '',
      loading: true,
      error: '',
      refreshing: false,
      movies: [],
    };

    this.renderRow = this.renderRow.bind(this);
    this.onRefresh = this.onRefresh.bind(this);
    this.navigator = null;
  }

  componentDidMount() {
    this.fetchMovies();
  }

  onRefresh() {
    this.setState({ refreshing: true });
    this.fetchMovies();
  }

  setFilteredData(searchText) {
    const { dataSource, movies } = this.state;
    if (searchText === null || searchText.length === 0) {
      this.setState({ searchText });
    } else {
      const matches = movies.filter(movie => movie.title.toLowerCase().search(searchText.toLowerCase()) >= 0);
      this.setState({
        searchText,
        dataSource: dataSource.cloneWithRows(matches),
      });
    }
  }

  fetchMovies(category = this.props.category) {
    let promise;
    if (category === MovieCategories.TOP_RATED) {
      promise = fetchTopRatedMovies();
    } else {
      promise = fetchNowPlayingMovies();
    }

    promise.then(movies => {
      this.setState({
        movies,
        dataSource: this.state.dataSource.cloneWithRows(movies),
        loading: false,
        error: '',
        refreshing: false,
      });
    })
    .catch(() => {
      this.setState({
        loading: false,
        error: 'Sorry, something went wrong. Please try again later.',
        refreshing: false,
      });
    });
  }

  updateType(newType) {
    this.setState({ loading: true });
    this.fetchMovies(newType);
  }

  renderRow(movie) {
    return (
      <MovieCellView
        movie={movie}
        navigator={this.navigator}
      />
    );
  }

  render() {
    if (this.state.loading) {
      return <Text>Loading...</Text>;
    }

    if (this.state.error) {
      return <Text>{this.state.error}</Text>;
    }

    return (
      <Navigator
        initialRoute={{ index: 0 }}
        renderScene={(route, navigator) => {
          this.navigator = navigator;

          if (route.index === 0) {
            return (
              <ScrollView
                refreshControl={
                  <RefreshControl
                    refreshing={this.state.refreshing}
                    onRefresh={this.onRefresh}
                  />
                }
              >
                <View style={styles.searchBar}>
                  <TextInput
                    style={styles.searchInput}
                    value={this.state.searchText}
                    onChangeText={searchText => { this.setFilteredData(searchText); }}
                    autoCapitalize="none"
                    placeholder="Search"
                  />
                </View>
                <ListView
                  style={{ marginTop: 20 }}
                  dataSource={this.state.dataSource}
                  renderRow={this.renderRow}
                />
              </ScrollView>
            );
          }

          return (
            <MovieDetail movie={route.movie} navigator={this.navigator} />
          );
        }}
      />
    );
  }
}

MoviesView.propTypes = propTypes;
export default MoviesView;
