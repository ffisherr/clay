import React, { Component } from 'react';
import './styles.css';

class SelectInstruments extends Component {
    state = {
        selected: {},
        avaliableInstruments: [],
    }

    fetchData() {
        fetch("http://localhost:8081/instruments/clay/read-all/")
          .then(res => res.json())
          .then(
            (res) => {
                console.log(res);
              const locSel = new Map();
              res.map(instrument => (
                  locSel.set(instrument.id.toString(), false)
              ));
              this.setState({
                  avaliableInstruments: res,
                  selected: locSel
                });
            },
            (error) => {
                console.log(error);
                this.setState({
                    avaliableInstruments: [],
                    selected: {},
                });
            }
          );
    }

    addInstrument(instrumentId) {
        const query = 'http://localhost:8081/instruments/add-instrument/?name=' + instrumentId;
        const requestOptions = {
            method: 'POST',
        };
        fetch(query, requestOptions);
    }

    componentDidMount() {
        this.fetchData();
    }

    handleSubmit(e) {
        for (var [key, value] of this.state.selected) {
            if (value) {
                this.addInstrument(key);
            }
        }
        window.location.reload();
    }

    handleSelect(e) {
        const locSel = this.state.selected;
        locSel.set(e.target.name, e.target.checked);
        this.setState({
            selected: locSel
        });
    }

    render() {
        return (
            <div>
                <h2>Выберите инструменты, для работы</h2>
                {this.state.avaliableInstruments.map(instrument => (
                    <p key={instrument.id}>
                        <label>
                            <input type="checkbox" name={instrument.ticker} onChange={this.handleSelect.bind(this)} />
                            {instrument.ticker}
                        </label>
                    </p>
                ))}
                <button onClick={this.handleSubmit.bind(this)}>Выбрать</button>
            </div>
        );
    }
}

export default SelectInstruments;
