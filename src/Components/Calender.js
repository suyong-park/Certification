import React, { Component } from "react";
//import "./materialize-css/dist/sass/materialize.scss";
import "./materialize-css/dist/css/admin-materialize.min.css";

class Calender extends Component {
  componentDidMount() {
    const script = document.createElement("script");

    script.src = "./Components/materialize-css/dist/js/Chart.js";
    script.async = true;

    document.body.appendChild(script);
  }
  render() {
    return (
      <div>
        <div class="row">
          <div class="col s12">
            <h2>Day View</h2>
            <div class="card">
              <div id="calendar-day"></div>
            </div>
          </div>
        </div>
      </div>
    );
  }
}

export default Calender;
