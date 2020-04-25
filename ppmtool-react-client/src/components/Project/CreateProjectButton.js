import React from "react";
import { Link } from "react-router-dom";

const CreateProjectButton = () => {
  return (
    //using fragment instead of div tag. Pretty much invisible to browser
    <React.Fragment>
      <Link to="/addProject" className="btn btn-lg btn-info">
        Create a Project
      </Link>
    </React.Fragment>
  );
};

export default CreateProjectButton;
