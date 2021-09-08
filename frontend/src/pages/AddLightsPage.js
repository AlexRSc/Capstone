import PageLayout from "../components/PageLayout";
import Header from "../components/Header";
import Button from "@material-ui/core/Button";
import {Link, Redirect} from "react-router-dom";
import Footer from "../components/Footer";
import styled from "styled-components";
import {useAuth} from "../auth/AuthProvider";
import TextField from "@material-ui/core/TextField";
import {ButtonGroup, CircularProgress} from "@material-ui/core";
import {useState} from "react";
import {connectHub} from "../services/hub-api-service";

export default function AddLightsPage() {
       return (
        <PageLayout>
            <Header title="Connect My Hub"/>
                <Wrapper>
                    <ExplanationWrapper>
                        <h3>How does this work?</h3>
                        <p>Just add your UUID or ItemName (from OpenHub), and you're ready to go!</p>
                    </ExplanationWrapper>
                    <TextWrapper>
                        <TextField required id="standard required" label="Name (for this App)"
                                   name="lightName" />
                        <TextField required id="standard required" label="UUID"
                                   name="lightUUID"  />
                        <TextField required id="standard required" label="ItemName"
                                   name="lightItem"  />
                    </TextWrapper>
                    <ButtonGroup>
                        <Link to ="/lights"><Button color="primary" variant="contained">Back</Button></Link>
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
  place-items: center;
  grid-template-rows: 2fr 1fr 1fr;
  .Buttongroup {
    align-self: center;
  }
`

const ExplanationWrapper = styled.div`
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 20px;
`
const TextWrapper = styled.div`
  display: flex;
  flex-direction: column;
  justify-content: center;
`