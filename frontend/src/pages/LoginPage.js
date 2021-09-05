import Header from "../components/Header";
import PageLayout from "../components/PageLayout";
import Footer from "../components/Footer";
import TextField from "@material-ui/core/TextField";
import {ButtonGroup} from "@material-ui/core";
import {Link} from "react-router-dom";
import Button from "@material-ui/core/Button";
import styled from "styled-components"


export default function LoginPage () {
    return (
        <PageLayout>
            <Header title="Login Form"/>
            <Wrapper>
                <TextField required id="standard required" label="Username"/>
                <TextField required id="standard required" label="Password" type="password"/>
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