import Header from "../components/Header";
import PageLayout from "../components/PageLayout";
import Footer from "../components/Footer";
import styled from "styled-components"
import Button from "@material-ui/core/button";
import {Link} from "react-router-dom";
import RegistrationPage from "./RegistrationPage";


export default function WelcomePage() {
    return (
        <PageLayout>
            <Header title="Welcome"/>
            <Wrapper>
                <h1>Welcome to our Smart App</h1>
                <p>If you are new here, please consider registering! </p>
                <Button variant="contained" color="primary" component={Link} to="/registration">Register here</Button>
                </Wrapper>
            <Footer/>
        </PageLayout>
    )
}

const Wrapper = styled.div `
  margin-left: 10px;
  margin-right: 10px;
  display: grid;
  place-items: center;
  text-align: center;
  

`