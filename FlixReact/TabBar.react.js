import React from 'react';
import {
  StyleSheet,
  View,
  Text,
  TouchableOpacity,
  BackAndroid,
  ViewPagerAndroid,
} from 'react-native';
import MoviesView from './MoviesView.react';
import MovieCategories from './constants/MovieCategories';

const styles = StyleSheet.create({
  container: {
    flex: 1,
  },
  scene: {
    flex: 1,
  },
  tabBar: {
    flexDirection: 'row',
    justifyContent: 'space-around',
    alignItems: 'center',
    height: 50,
    backgroundColor: 'gray',
  },
  tabBarText: {
    color: 'white',
    fontSize: 20,
  },
  viewPager: {
    flex: 1,
  },
});

class TabBar extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      currentTab: MovieCategories.NOW_PLAYING,
    };

    this.renderTabBar = this.renderTabBar.bind(this);
    this.goBack = this.goBack.bind(this);
    this.onPageChange = this.onPageChange.bind(this);
  }

  componentDidMount() {
    BackAndroid.addEventListener('hardwareBackPress', this.goBack);
  }

  componentWillUnmount() {
    BackAndroid.removeEventListener('hardwareBackPress', this.goBack);
  }

  onPageChange(e) {
    if (e.position === 0) {
      this.setState({ currentTab: MovieCategories.NOW_PLAYING });
    } else {
      this.setState({ currentTab: MovieCategories.TOP_RATED });
    }
  }

  goBack() {
    if (this.state.currentTab === MovieCategories.NOW_PLAYING) {
      return false;
    }

    this.setState({ currentTab: MovieCategories.NOW_PLAYING });
    this.refs.viewPager.setPage(0);
    return true;
  }

  changeScene(newScene) {
    this.setState({ currentTab: newScene });
    if (newScene === MovieCategories.NOW_PLAYING) {
      this.refs.viewPager.setPage(0);
    } else {
      this.refs.viewPager.setPage(1);
    }
  }

  renderTabBar() {
    const selectedStyle = tabId => {
      if (tabId === this.state.currentTab) {
        return { color: 'rgba(255, 255, 255, 0.36)' };
      }
      return { color: 'white' };
    };

    return (
      <View style={styles.tabBar}>
        <TouchableOpacity
          onPress={() => this.changeScene(MovieCategories.NOW_PLAYING)}
        >
          <Text style={[styles.tabBarText, selectedStyle(MovieCategories.NOW_PLAYING)]}>
            Now Playing
          </Text>
        </TouchableOpacity>

        <TouchableOpacity
          onPress={() => this.changeScene(MovieCategories.TOP_RATED)}
        >
          <Text style={[styles.tabBarText, selectedStyle(MovieCategories.TOP_RATED)]}>
            Top Rated
          </Text>
        </TouchableOpacity>
      </View>
    );
  }

  render() {
    return (
      <View style={styles.container}>
        <ViewPagerAndroid
          initialPage={0}
          style={styles.viewPager}
          onPageSelected={this.onPageChange}
          ref="viewPager"
        >
          <View style={styles.scene}>
            <MoviesView category={MovieCategories.NOW_PLAYING} />
          </View>
          <View style={styles.scene}>
            <MoviesView category={MovieCategories.TOP_RATED} />
          </View>
        </ViewPagerAndroid>
        {this.renderTabBar()}
      </View>
    );
  }
}

export default TabBar;
