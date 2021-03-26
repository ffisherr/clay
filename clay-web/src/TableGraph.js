import React, { Component } from 'react';

class TableGraph extends Component {

    state = {
        transactions: []
    }
  
  
    fetchHistory(){
      fetch("http://localhost:8081/instruments/history/read-all/")
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
  
    componentDidMount() {
        this.fetchHistory();
    }
  
    render() {
        return (
            <div>
                
                      <table className="table">
                        <thead>
                          <tr>
                            <th scope="col">#</th>
                            <th scope="col">Дата</th>
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
                
            </div>
        );
    }
  }
  
  export default  TableGraph;