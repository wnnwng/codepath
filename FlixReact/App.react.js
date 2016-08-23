import React from 'react';
import ColorView from './ColorView.react';
import {
  TouchableOpacity,
} from 'react-native';

class App extends React.Component {
  constructor(props) {
    super(props);

    this.state = {
      color: 'orange',
    };

    this.onPress = this.onPress.bind(this);
  }

  onPress() {
    console.log('onPress');
  }

  render() {
    return (
      <TouchableOpacity onPress={this.onPress} style={{ flex: 1 }}>
        <ColorView color={this.state.color} />
      </TouchableOpacity>
    );
  }
}

export default App;
