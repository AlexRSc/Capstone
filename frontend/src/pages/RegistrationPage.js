import PageLayout from "../components/PageLayout";
import Header from "../components/Header";
import Footer from "../components/Footer";
import TextField from "@material-ui/core/TextField";
import styled from "styled-components"
import {ButtonGroup} from "@material-ui/core";
import Button from "@material-ui/core/button";
import {Link} from "react-router-dom";


export default function RegistrationPage() {
return(
    <PageLayout>
        <Header title="Registration Form"/>
        <Wrapper>
        <TextField required id="standard required" label="Username"/>
        <TextField required id="standard required" label="Email-Address"/>
        <TextField required id="standard required" label="Password"
        type="password"/>
        <ButtonGroup>
                <Link to ="/"><Button color="primary" variant="contained">Back</Button></Link>
                <Button color="secondary" >Clear</Button>
                <Button color="primary" variant="contained">Submit</Button>
        </ButtonGroup>
        </Wrapper>
        <Footer/>
    </PageLayout>
)
}

const Wrapper = styled.div`
        display: grid;

`