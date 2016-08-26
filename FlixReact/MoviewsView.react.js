import React from 'react';
import {
  ListView,
  Text,
} from 'react-native';

class MoviesView extends React.Component {
  constructor(props) {
    super(props);
    const ds = new ListView.DataSource({
      rowHasChanged: (r1, r2) => r1 !== r2,
    });
    this.state = {
      dataSource: ds.cloneWithRows(['row 1', 'row 2']),
    };
  }
  render() {
    const renderRow = rowData => <Text>{rowData}</Text>;
    return (
      <ListView
        dataSource={this.state.dataSource}
        renderRow={renderRow}
      />
    );
  }
}

export default MoviesView;
