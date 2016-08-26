import {
  View,
} from 'react-native';
import React, { PropTypes } from 'react';


class ColorView extends React.Component {
  render() {
    const { color } = this.props;
    return (
      <View style={{ backgroundColor: color, flex: 1 }} />
    );
  }
}

ColorView.propTypes = {
  color: PropTypes.string.isRequired,
};

export default ColorView;
