import React from 'react';
import {render} from 'react-dom';
import axios from 'axios';

console.clear();

class TransactionForm  extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            type: ""
        };
        this.addTransaction = props.addTransaction;
    }

    componentWillReceiveProps(nextProps) {
        this.message = nextProps.message;
    }

    render() {return (
        <form onSubmit={(e) => {
            e.preventDefault();
            this.addTransaction(this.from.value, this.to.value, this.state.type, this.amount.value);
            this.to.value = '';
            this.amount.value = '';
        }}>
            <label className="col-md-6">
                <p>From</p>
                <input type="number" min="0" id="from" className="form-control" disabled={this.state.type === "accrual"}
                       required={this.state.type != "accrual"}
                       ref={node => {this.from = node;}}/>
            </label>
            <label className="col-md-6">
                <p>To</p>
                <input type="number" min="0" id="to" className="form-control" disabled={this.state.type === "writeOff"}
                       required={this.state.type != "writeOff"}
                       ref={node => {this.to = node;}}/>
            </label>
            <label className="col-md-4">
                <p>Type</p>
                <div>
                    <div className="col-md-4 col-md-offset-1">
                    <label className="radio-inline">
                        <input type="radio" name="type" required="true" value="writeOff"
                               onChange={()=>{this.setState({type: "writeOff"});}}/>
                        Writeoff
                    </label>
                    </div>
                    <div className="col-md-4 col-md-offset-1">
                    <label className="radio-inline">
                        <input type="radio" name="type" required="true" value="accrual"
                               onChange={()=>{this.setState({type: "accrual"});}}/>
                        Accrual
                    </label>
                    </div>
                    <div className="col-md-12 col-md-offset-4">
                    <label className="radio-inline">
                        <input type="radio" name="type" required="true" value="transfer"
                               onChange={()=>{this.setState({type: "transfer"});}}/>
                        Transfer
                    </label>
                    </div>
                </div>
            </label>
            <button className="btn btn-info col-md-4 mt-30">Submit</button>
            <label className="col-md-4">
                <p>Amount</p>
                <input type="number" min="0" className="form-control" required="true" ref={node => {
                    this.amount = node;
                }}/>
            </label>
            <div className="clearfix"/>
            <div className="alert alert-warning" hidden={!this.message}>
                <p className="text-center">{this.message}</p>
            </div>
        </form>
    ); }
};

const Transaction = ({transaction}) => {
    let definedType = defineTransactionType(transaction);
    return (<tr className={definedType.class}>
        <td>{definedType.fromId}</td>
        <td>{definedType.toId}</td>
        <td>${transaction.amount}</td>
    </tr>);
};

const TransactionList = ({transactions}) => {
    const transactionNode = transactions.map((transaction) => {
        return (<Transaction transaction={transaction} key={transaction.id}/>)
    });
    return (<table className="col-md-12 table mt-50">
        <thead>
        <tr>
            <td>From</td>
            <td>To</td>
            <td>Amount</td>
        </tr>
        </thead>
        <tbody>
        {transactionNode}
        </tbody>
    </table>);
};
 
window.id = 0;
 
class TransactionApp extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            data: [],
            errorMsg: ''
        };
        this.apiUrl = 'http://192.168.1.30:9000/transact'
    }
 
    componentDidMount() {
        axios.get(this.apiUrl)
            .then((res) => {
                this.setState({data: res.data});
            });
    }
 
   	  
    addTransaction(from, to, type, amount) {
        const transaction = {fromId: from, toId: to, type: type, amount: amount};
        axios.post(this.apiUrl, transaction)
            .then((res) => {
                this.state.data.push(res.data);
                this.setState({data: this.state.data, errorMsg: ""});
            })
            .catch((error) => {
                this.setState({errorMsg: error.response.data});
            });
    }

    render() {
        return (
            <div>
                <h1>Transactions</h1>
                <TransactionForm addTransaction={this.addTransaction.bind(this)}
                                 message={this.state.errorMsg}/>
                <TransactionList transactions={this.state.data}/>
            </div>
        );
    }
}

render(<TransactionApp/>, document.getElementById('container'));

function defineTransactionType(transaction){
    switch (transaction.type){
        case 'writeOff': return {class:'danger', fromId: transaction.fromId, toId: 'BANK'};
        case 'accrual': return {class:'success', fromId: 'BANK', toId: transaction.toId};
        default: return {class:'info', fromId: transaction.fromId, toId: transaction.toId};
    }
}