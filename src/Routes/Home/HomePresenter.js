import React from "react";
import Loader from "Components/Loader";

const HomePresenter = ({
  Ccategory,
  certification,
  Jcategory,
  error,
  loading,
}) => (loading ? <Loader /> : Ccategory.map((name) => `${name.NAME}  `));

export default HomePresenter;
