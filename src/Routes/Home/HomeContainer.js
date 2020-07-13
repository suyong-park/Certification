import React from "react";
import HomePresenter from "./HomePresenter";
import { moviesApi } from "api";

export default class extends React.Component {
  state = {
    Ccategory: null,
    certification: null,
    Jcategory: null,
    error: null,
    loading: true,
  };

  async componentDidMount() {
    try {
      const { data: Ccategory } = await moviesApi.Ccategory();
      const { data: certification } = await moviesApi.certification();
      const { data: Jcategory } = await moviesApi.Jcategory();

      this.setState({
        Ccategory,
        certification,
        Jcategory,
      });
    } catch {
      this.setState({
        error: "Can't find movies information.",
      });
    } finally {
      this.setState({
        loading: false,
      });
    }
  }

  render() {
    const { Ccategory, certification, Jcategory, error, loading } = this.state;
    console.log(Ccategory, certification, Jcategory, error, loading);
    return (
      <HomePresenter
        Ccategory={Ccategory}
        certification={certification}
        Jcategory={Jcategory}
        loading={loading}
        error={error}
      />
    );
  }
}
