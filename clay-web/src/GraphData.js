import React, { Component } from 'react';
import TableGraph from './TableGraph';
import './styles.css';
import {XYPlot, XAxis, YAxis, HorizontalGridLines, LineSeries} from 'react-vis';
import {DropdownButton, Dropdown} from 'react-bootstrap';


class GraphData extends Component {

  state = {
      myInstruments: [],
      activeInstrument: '',
      plotData: []
  }

  handleSelect(e) {
    console.log(e.nativeEvent.target.text);
    this.setState({activeInstrument: e.nativeEvent.target.text});
    this.drowChart(e.nativeEvent.target.text);

  }

  drowChart(e){
    fetch("http://localhost:8081/instruments/history/read-by-name/" + e)
    .then(res => res.json())
    .then(
      (res) => {
        console.log(res);
        this.setState({plotData: res});
      },
      (error) => {
          console.log(error);
          this.setState({
              selected: false,
          });
      }
    );
  }

  fetchData() {
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
              selected: false,
          });
      }
    );
  }


  componentDidMount() {
      this.fetchData();
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
              <DropdownButton id="dropdown-basic-button" title={this.state.activeInstrument}>
                {this.state.myInstruments.map(instrument => (
                <Dropdown.Item key={instrument.id} onClick= {this.handleSelect.bind(this)}>{instrument.name}</Dropdown.Item>))}
              </DropdownButton>
              <XYPlot
                width={1000}
                height={300}>
                <HorizontalGridLines />
                <LineSeries
                  data={this.state.plotData}/>
                <XAxis />
                <YAxis />
              </XYPlot>              
              <TableGraph />
          </div>
      );
  }
}

export default GraphData;