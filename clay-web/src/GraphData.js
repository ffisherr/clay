import React, { Component  } from 'react';
import DateFnsUtils from '@date-io/date-fns';
import {
    MuiPickersUtilsProvider,
    TimePicker,
  } from '@material-ui/pickers';

class Manager extends Component {

  state = {
      myInstruments: [],
      selectedDate: null
  }

  fethData() {
    fetch("http://localhost:8081/instruments/my/instruments/")
    .then(res => res.json())
    .then(
      (res) => {
        console.log(res);
        this.setState({myInstruments: res});
      },
      (error) => {
          console.log(error);
          this.setState({
            myInstruments: [],
          });
      }
    );
  }

  componentDidMount() {
      this.fethData();
      this.setState({selectedDate: new Date()})
  }

  handleTime(e) {
      console.log('Time: ', e.target.value);
      this.setState({selectedDate: e.target.value});
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