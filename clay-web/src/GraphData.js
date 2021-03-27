import React, { Component } from 'react';
import './styles.css';
import {XYPlot, XAxis, YAxis, HorizontalGridLines, LineSeries} from 'react-vis';
import {DropdownButton, Dropdown, Button} from 'react-bootstrap';
import TimeRangePicker from '@wojtekmaj/react-timerange-picker';


class GraphData extends Component {

  state = {
      myInstruments: [],
      activeInstrument: '',
      plotData: [],
      timeRange: ["10:00", "18:00"],
      transactions: [],
      inAction: false,
  }

  handleSelect(e) {
    console.log(e.nativeEvent.target.text);
    this.setState({activeInstrument: e.nativeEvent.target.text});
    this.drowChart(e.nativeEvent.target.text);
    this.fetchHistory(e.nativeEvent.target.text);
  }

  componentWillUnmount() {
    clearInterval(this.myInterval);
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

  fetchHistory(e){
    fetch("http://localhost:8081/instruments/read-select/?param="+e)
    .then(res => res.json())
    .then(
      (res) => {
        console.log(res);
        this.setState({transactions: res});
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
        let locA = '';
        res.map(instrument => (locA = instrument.name));
        this.setState({
          myInstruments: res,
          activeInstrument: locA
        });
      },
      (error) => {
          console.log(error);
          this.setState({
              selected: false,
          });
      }
    );
  }

  fetchDownload(){
    console.log("http://localhost:8081/instruments/load-csv/?param="+this.state.activeInstrument);
    fetch("http://localhost:8081/instruments/load-csv/?param="+this.state.activeInstrument);
  }

  handleRange(e) {
    this.setState({timeRange: e});
    console.log(this.state.timeRange);
  }


  componentDidMount() {
      this.fetchData();
      this.fetchHistory();
      this.myInterval = setInterval(() => {
        const param = this.state.activeInstrument;
        if (param) {
          this.drowChart(param);
          this.fetchHistory(param);
        }
    }, 1000);
}

  handleStartTrade(e) {
    this.setState({inAction: true});
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
              <button disabled={this.state.inAction} onClick={this.handleStartTrade.bind(this)}>Запустить торги</button>
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
              <table className="table">
                        <thead>
                          <tr>
                            <th scope="col">#</th>
                            <th scope="col">Время</th>
                            <th scope="col">Направление операции</th>
                            <th scope="col">Название инструмента</th>
                            <th scope="col">Цена за ед.</th>
                            <th scope="col">Количество инстр. в операции</th>
                            <th scope="col">Общая стоимость</th>
                            <th scope="col">Текущее значение инструментов</th>
                            <th scope="col">Осаток на счете</th>
                          </tr>
                        </thead>
                        <tbody>
                        {this.state.transactions.map(transaction => (
                      <tr key={transaction.id}>
                        <th scope="row">{transaction.id}</th>
                        <td>{transaction.createdAt}</td>
                        <td>{transaction.direction}</td>
                        <td>{transaction.instrument.name}</td>
                        <td>{transaction.oneItemCost}</td>
                        <td>{transaction.purchasedNumber}</td>
                        <td>{transaction.oneItemCost * transaction.purchasedNumber}</td>
                        <td>{transaction.totalAmount}</td>
                        <td>{transaction.leftAmount}</td>
                      </tr>
                      ))}
                        </tbody>
                      </table>
              <a href={"http://localhost:8081/instruments/load-csv/?param="+this.state.activeInstrument} download>
              <Button
                    onClick= {this.fetchDownload.bind(this)}
                    variant="primary">Скачать
                </Button>  
                </a> 
          </div>
      );
  }
}

export default GraphData;