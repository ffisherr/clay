import React, { Component } from 'react';
import TableGraph from './TableGraph';
import './styles.css';
import {XYPlot, XAxis, YAxis, HorizontalGridLines, LineSeries} from 'react-vis';
import {DropdownButton, Dropdown} from 'react-bootstrap';
import TimeRangePicker from '@wojtekmaj/react-timerange-picker';


class GraphData extends Component {

  state = {
      myInstruments: [],
      activeInstrument: '',
      plotData: [],
      timeRange: ["10:00", "18:00"]
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

  handleRange(e) {
    this.setState({timeRange: e});
    console.log(this.state.timeRange);
  }


  componentDidMount() {
      this.fetchData();
  }

  handleStartTrade(e) {
    const timeRange = this.state.timeRange;
    const requestOptions = {
        method: 'POST',
    };
    fetch('http://localhost:8081/instruments/start-trading/?startTime=' + timeRange[0] + '&endTime=' + timeRange[1], requestOptions)
  }

  render() {
      return (
          <div>
              <TimeRangePicker value={this.state.timeRange} onChange={this.handleRange.bind(this)} />
              <button onClick={this.handleStartTrade.bind(this)}>Запустить торги</button>
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