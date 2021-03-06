import React, { Component } from "react";
import PropTypes from "prop-types";
import { connect } from "react-redux"; //to connect to state;
import { createProject } from "../../actions/projectAction";
import classnames from "classnames";

class AddProject extends Component {
  constructor() {
    super(); //need to call this since extending Component

    //Set state while creating new form object, values need to be empty
    this.state = {
      projectName: "",
      projectIdentifier: "",
      description: "",
      start_Date: "",
      end_Date: "",

      //errors empty on load
      errors: {},
    };

    this.onChange = this.onChange.bind(this); //bind on change to make state work and to be able to type values
    this.onSubmit = this.onSubmit.bind(this);
  }

  //lifecycle hook
  //to receive props from state and add to component we use lifecycle hook
  componentWillReceiveProps(nextProps) {
    if (nextProps.errors) {
      this.setState({ errors: nextProps.errors });
    }
  }

  //event parameter
  onChange(e) {
    this.setState({ [e.target.name]: e.target.value });
  }

  onSubmit(e) {
    //prevents form to reload on submit
    e.preventDefault();

    const newProject = {
      projectName: this.state.projectName,
      projectIdentifier: this.state.projectIdentifier,
      description: this.state.description,
      start_Date: this.state.start_Date,
      end_Date: this.state.end_Date,
    };
    this.props.createProject(newProject, this.props.history);
  }

  render() {
    const { errors } = this.state;
    return (
      <div className="project">
        <div className="container">
          <div className="row">
            <div className="col-md-8 m-auto">
              <h5 className="display-4 text-center">
                Create / Edit Project form
              </h5>
              <hr />
              <form onSubmit={this.onSubmit}>
                <div className="form-group">
                  <input
                    type="text"
                    className={classnames("form-control form-control-lg ", {
                      "is-invalid": errors.projectName,
                    })}
                    placeholder="Project Name"
                    name="projectName"
                    value={this.state.projectName}
                    onChange={this.onChange}
                  />
                  {errors.projectName && (
                    <div className="invalid-feedback">{errors.projectName}</div>
                  )}
                </div>
                <div className="form-group">
                  <input
                    type="text"
                    className={classnames("form-control form-control-lg", {
                      "is-invalid": errors.projectIdentifier,
                    })}
                    placeholder="Unique Project ID"
                    name="projectIdentifier"
                    value={this.state.projectIdentifier}
                    onChange={this.onChange}
                  />
                  {errors.projectIdentifier && (
                    <div className="invalid-feedbac">
                      {errors.projectIdentifier}
                    </div>
                  )}
                </div>
                <div className="form-group">
                  <textarea
                    className={classnames("form-control form-control-lg", {
                      "is-invalid": errors.description,
                    })}
                    placeholder="Project Description"
                    name="description"
                    value={this.state.description}
                    onChange={this.onChange}
                  ></textarea>
                  {errors.description && (
                    <div className="invalid-feedback">{errors.description}</div>
                  )}
                </div>
                <h6>Start Date</h6>
                <div className="form-group">
                  <input
                    type="date"
                    className="form-control form-control-lg"
                    name="start_Date"
                    value={this.state.start_Date}
                    onChange={this.onChange}
                  />
                </div>
                <h6>Estimated End Date</h6>
                <div className="form-group">
                  <input
                    type="date"
                    className="form-control form-control-lg"
                    name="end_Date"
                    value={this.state.end_Date}
                    onChange={this.onChange}
                  />
                </div>

                <input
                  type="submit"
                  className="btn btn-primary btn-block mt-4"
                />
              </form>
            </div>
          </div>
        </div>
      </div>
    );
  }
}

//to map the errors coming from backend to the state
//variable = parameter = assignment
const mapStateToProps = (state) => ({
  errors: state.errors,
});

AddProject.propTypes = {
  //telling react that 'createProject' is a required prop type for this component to work properly,
  createProject: PropTypes.func.isRequired,
  errors: PropTypes.object.isRequired,
};

//conect component to state
export default connect(mapStateToProps, { createProject })(AddProject);
