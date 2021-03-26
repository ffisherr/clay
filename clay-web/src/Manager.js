import React, { Component } from 'react';
import SelectInstruments from './SelectInstruments.js'
import GraphData from './GraphData.js'

class Manager extends Component {

  state = {
      selected: false,
  }

  componentDidMount() {
      this.isSmthSelected();
  }

  isSmthSelected() {
        fetch("http://localhost:8081//instruments/my/instruments/")
          .then(res => res.json())
          .then(
            (res) => {
              if (res.length) {
                this.setState({selected: true});
              }
            },
            (error) => {
                console.log(error);
                this.setState({
                    selected: false,
                });
            }
          );
  }

  render() {
    if (!this.state.selected) {
        return (<SelectInstruments />);
    }
    return (<GraphData />);
  }
}

export default Manager;