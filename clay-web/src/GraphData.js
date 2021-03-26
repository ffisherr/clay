import React, { Component } from 'react';

class Manager extends Component {

  state = {
      myInstruments: [],
  }

  fethData() {
    fetch("http://localhost:8081//instruments/my/instruments/")
    .then(res => res.json())
    .then(
      (res) => {
        console.log(res);
        this.setState({myInstruments: res});
      },
      (error) => {
          console.log(error);
          this.setState({
              selected: false,
          });
      }
    );
  }

  componentDidMount() {
      this.fethData();
  }

  render() {
      return (
          <div>
              <ul>
                  <b>Текущие инструменты</b>
                {this.state.myInstruments.map(instrument => (
                    <li key={instrument.id}>{instrument.name}</li>
                    ))}
              </ul>
              
          </div>
      );
  }
}

export default Manager;