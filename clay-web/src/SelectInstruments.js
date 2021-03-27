import React, { Component } from 'react';
import './styles.css';
import {Button} from 'react-bootstrap';

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

    addInstrument(selectedItems) {
        const query = 'http://localhost:8081/instruments/add-instruments/';
        console.log('Запрос по ', selectedItems, query);
        const requestOptions = {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: selectedItems
        };
        console.log(JSON.stringify({ selectedItems }));
        fetch(query, requestOptions);
    }

    componentDidMount() {
        this.fetchData();
    }

    handleSubmit(e) {
        let selectedItems = '';
        for (var [key, value] of this.state.selected) {
            console.log('Статус', key, value);
            if (value) {
                selectedItems += ',' + key;
            }
        }
        this.addInstrument(selectedItems);
        console.log(selectedItems);
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
            <div className="selectDiv">
                <h2>Выберите инструменты, для работы</h2>
                {this.state.avaliableInstruments.map(instrument => (
                    <p key={instrument.id}>
                        <label>
                            <input type="checkbox" name={instrument.ticker} onChange={this.handleSelect.bind(this)} />
                            {instrument.ticker}
                        </label>
                    </p>
                ))}
                <Button onClick={this.handleSubmit.bind(this)}>Выбрать</Button>
            </div>
        );
    }
}

export default SelectInstruments;
