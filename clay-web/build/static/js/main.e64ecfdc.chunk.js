(this["webpackJsonpclay-web"]=this["webpackJsonpclay-web"]||[]).push([[0],{122:function(t,e,n){"use strict";n.r(e);var c=n(1),s=n.n(c),a=n(20),i=n.n(a),o=(n(82),n(11)),r=n(12),l=n(14),h=n(13),u=(n(83),n(74)),d=n(66),j=(n(53),n(67)),b=n(2),m=function(t){Object(l.a)(n,t);var e=Object(h.a)(n);function n(){var t;Object(o.a)(this,n);for(var c=arguments.length,s=new Array(c),a=0;a<c;a++)s[a]=arguments[a];return(t=e.call.apply(e,[this].concat(s))).state={selected:{},avaliableInstruments:[]},t}return Object(r.a)(n,[{key:"fetchData",value:function(){var t=this;fetch("http://localhost:8081/instruments/clay/read-all/").then((function(t){return t.json()})).then((function(e){var n=new Map;e.map((function(t){return n.set(t.id.toString(),!1)})),t.setState({avaliableInstruments:e,selected:n})}),(function(e){console.log(e),t.setState({avaliableInstruments:[],selected:{}})}))}},{key:"addInstrument",value:function(t){var e="http://localhost:8081/instruments/add-instruments/";console.log("\u0417\u0430\u043f\u0440\u043e\u0441 \u043f\u043e ",t,e);var n={method:"POST",headers:{"Content-Type":"application/json"},body:t};console.log(JSON.stringify({selectedItems:t})),fetch(e,n)}},{key:"componentDidMount",value:function(){this.fetchData()}},{key:"handleSubmit",value:function(t){var e,n="",c=Object(d.a)(this.state.selected);try{for(c.s();!(e=c.n()).done;){var s=Object(u.a)(e.value,2),a=s[0],i=s[1];console.log("\u0421\u0442\u0430\u0442\u0443\u0441",a,i),i&&(n+=","+a)}}catch(o){c.e(o)}finally{c.f()}this.addInstrument(n),console.log(n),window.location.reload()}},{key:"handleSelect",value:function(t){var e=this.state.selected;e.set(t.target.name,t.target.checked),this.setState({selected:e})}},{key:"render",value:function(){var t=this;return Object(b.jsxs)("div",{className:"selectDiv",children:[Object(b.jsx)("h2",{children:"\u0412\u044b\u0431\u0435\u0440\u0438\u0442\u0435 \u0438\u043d\u0441\u0442\u0440\u0443\u043c\u0435\u043d\u0442\u044b, \u0434\u043b\u044f \u0440\u0430\u0431\u043e\u0442\u044b"}),this.state.avaliableInstruments.map((function(e){return Object(b.jsx)("p",{children:Object(b.jsxs)("label",{children:[Object(b.jsx)("input",{type:"checkbox",name:e.ticker,onChange:t.handleSelect.bind(t)}),e.ticker]})},e.id)})),Object(b.jsx)(j.a,{onClick:this.handleSubmit.bind(this),children:"\u0412\u044b\u0431\u0440\u0430\u0442\u044c"})]})}}]),n}(c.Component),f=n(23),v=n(129),O=n(73),p=n(69),x=n.n(p),y=function(t){Object(l.a)(n,t);var e=Object(h.a)(n);function n(){var t;Object(o.a)(this,n);for(var c=arguments.length,s=new Array(c),a=0;a<c;a++)s[a]=arguments[a];return(t=e.call.apply(e,[this].concat(s))).state={myInstruments:[],activeInstrument:"",plotData:[],timeRange:["10:00","18:00"],transactions:[],inAction:!1},t}return Object(r.a)(n,[{key:"handleSelect",value:function(t){console.log(t.nativeEvent.target.text),this.setState({activeInstrument:t.nativeEvent.target.text}),this.drowChart(t.nativeEvent.target.text),this.fetchHistory(t.nativeEvent.target.text)}},{key:"drowChart",value:function(t){var e=this;fetch("http://localhost:8081/instruments/history/read-by-name/"+t).then((function(t){return t.json()})).then((function(t){console.log(t),e.setState({plotData:t})}),(function(t){console.log(t),e.setState({selected:!1})}))}},{key:"fetchHistory",value:function(t){var e=this;fetch("http://localhost:8081/instruments/read-select/?param="+t).then((function(t){return t.json()})).then((function(t){console.log(t),e.setState({transactions:t})}),(function(t){console.log(t),e.setState({selected:!1})}))}},{key:"fetchData",value:function(){var t=this;fetch("http://localhost:8081/instruments/my/instruments/").then((function(t){return t.json()})).then((function(e){console.log(e);var n="";e.map((function(t){return n=t.name})),t.setState({myInstruments:e,activeInstrument:n})}),(function(e){console.log(e),t.setState({selected:!1})}))}},{key:"fetchDownload",value:function(){console.log("http://localhost:8081/instruments/load-csv/?param="+this.state.activeInstrument),fetch("http://localhost:8081/instruments/load-csv/?param="+this.state.activeInstrument)}},{key:"handleRange",value:function(t){this.setState({timeRange:t}),console.log(this.state.timeRange)}},{key:"componentDidMount",value:function(){this.fetchData(),this.fetchHistory()}},{key:"handleStartTrade",value:function(t){this.setState({inAction:!0});var e=this.state.timeRange;fetch("http://localhost:8081/instruments/start-trading/?startTime="+e[0]+"&endTime="+e[1],{method:"POST"})}},{key:"render",value:function(){var t=this;return Object(b.jsxs)("div",{children:[Object(b.jsx)(x.a,{value:this.state.timeRange,onChange:this.handleRange.bind(this)}),Object(b.jsx)("button",{disabled:this.state.inAction,onClick:this.handleStartTrade.bind(this),children:"\u0417\u0430\u043f\u0443\u0441\u0442\u0438\u0442\u044c \u0442\u043e\u0440\u0433\u0438"}),Object(b.jsx)(v.a,{id:"dropdown-basic-button",title:this.state.activeInstrument,children:this.state.myInstruments.map((function(e){return Object(b.jsx)(O.a.Item,{onClick:t.handleSelect.bind(t),children:e.name},e.id)}))}),Object(b.jsxs)(f.d,{width:1e3,height:300,children:[Object(b.jsx)(f.a,{}),Object(b.jsx)(f.b,{data:this.state.plotData}),Object(b.jsx)(f.c,{}),Object(b.jsx)(f.e,{})]}),Object(b.jsxs)("table",{className:"table",children:[Object(b.jsx)("thead",{children:Object(b.jsxs)("tr",{children:[Object(b.jsx)("th",{scope:"col",children:"#"}),Object(b.jsx)("th",{scope:"col",children:"\u0412\u0440\u0435\u043c\u044f"}),Object(b.jsx)("th",{scope:"col",children:"\u041d\u0430\u043f\u0440\u0430\u0432\u043b\u0435\u043d\u0438\u0435 \u043e\u043f\u0435\u0440\u0430\u0446\u0438\u0438"}),Object(b.jsx)("th",{scope:"col",children:"\u041d\u0430\u0437\u0432\u0430\u043d\u0438\u0435 \u0438\u043d\u0441\u0442\u0440\u0443\u043c\u0435\u043d\u0442\u0430"}),Object(b.jsx)("th",{scope:"col",children:"\u0426\u0435\u043d\u0430 \u0437\u0430 \u0435\u0434."}),Object(b.jsx)("th",{scope:"col",children:"\u041a\u043e\u043b\u0438\u0447\u0435\u0441\u0442\u0432\u043e \u0438\u043d\u0441\u0442\u0440. \u0432 \u043e\u043f\u0435\u0440\u0430\u0446\u0438\u0438"}),Object(b.jsx)("th",{scope:"col",children:"\u041e\u0431\u0449\u0430\u044f \u0441\u0442\u043e\u0438\u043c\u043e\u0441\u0442\u044c"}),Object(b.jsx)("th",{scope:"col",children:"\u0422\u0435\u043a\u0443\u0449\u0435\u0435 \u0437\u043d\u0430\u0447\u0435\u043d\u0438\u0435 \u0438\u043d\u0441\u0442\u0440\u0443\u043c\u0435\u043d\u0442\u043e\u0432"}),Object(b.jsx)("th",{scope:"col",children:"\u041e\u0441\u0430\u0442\u043e\u043a \u043d\u0430 \u0441\u0447\u0435\u0442\u0435"})]})}),Object(b.jsx)("tbody",{children:this.state.transactions.map((function(t){return Object(b.jsxs)("tr",{children:[Object(b.jsx)("th",{scope:"row",children:t.id}),Object(b.jsx)("td",{children:t.createdAt}),Object(b.jsx)("td",{children:t.direction}),Object(b.jsx)("td",{children:t.instrument.name}),Object(b.jsx)("td",{children:t.oneItemCost}),Object(b.jsx)("td",{children:t.purchasedNumber}),Object(b.jsx)("td",{children:t.oneItemCost*t.purchasedNumber}),Object(b.jsx)("td",{children:t.totalAmount}),Object(b.jsx)("td",{children:t.leftAmount})]},t.id)}))})]}),Object(b.jsx)("a",{href:"http://localhost:8081/instruments/load-csv/?param="+this.state.activeInstrument,download:!0,children:Object(b.jsx)(j.a,{onClick:this.fetchDownload.bind(this),variant:"primary",children:"\u0421\u043a\u0430\u0447\u0430\u0442\u044c"})})]})}}]),n}(c.Component),g=function(t){Object(l.a)(n,t);var e=Object(h.a)(n);function n(){var t;Object(o.a)(this,n);for(var c=arguments.length,s=new Array(c),a=0;a<c;a++)s[a]=arguments[a];return(t=e.call.apply(e,[this].concat(s))).state={selected:!1},t}return Object(r.a)(n,[{key:"componentDidMount",value:function(){this.isSmthSelected()}},{key:"isSmthSelected",value:function(){var t=this;fetch("http://localhost:8081//instruments/my/instruments/").then((function(t){return t.json()})).then((function(e){e.length&&t.setState({selected:!0})}),(function(e){console.log(e),t.setState({selected:!1})}))}},{key:"render",value:function(){return this.state.selected?Object(b.jsx)(y,{}):Object(b.jsx)(m,{})}}]),n}(c.Component),S=(n(121),function(t){Object(l.a)(n,t);var e=Object(h.a)(n);function n(){return Object(o.a)(this,n),e.apply(this,arguments)}return Object(r.a)(n,[{key:"render",value:function(){return Object(b.jsx)(g,{})}}]),n}(c.Component)),k=function(t){t&&t instanceof Function&&n.e(3).then(n.bind(null,130)).then((function(e){var n=e.getCLS,c=e.getFID,s=e.getFCP,a=e.getLCP,i=e.getTTFB;n(t),c(t),s(t),a(t),i(t)}))};i.a.render(Object(b.jsx)(s.a.StrictMode,{children:Object(b.jsx)(S,{})}),document.getElementById("root")),k()},53:function(t,e,n){},82:function(t,e,n){},83:function(t,e,n){}},[[122,1,2]]]);
//# sourceMappingURL=main.e64ecfdc.chunk.js.map