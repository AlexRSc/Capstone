import PageLayout from "../components/PageLayout";
import Header from "../components/Header";
import Button from "@material-ui/core/Button";
import {Link} from "react-router-dom";
import Footer from "../components/Footer";
import styled from "styled-components";
import {useAuth} from "../auth/AuthProvider";
import TextField from "@material-ui/core/TextField";
import {ButtonGroup} from "@material-ui/core";



export default function ConnectMyHub() {
    const {user, token} = useAuth()
    return (
        <PageLayout>
            <Header title="Connect My Hub"/>
            <Wrapper>
                <ExplanationWrapper>
                    <h3>How does this work?</h3>
                    <p>In order to connect to the Openhab API, we need use your Username and Password once.
                        Once we connect to the API, you´ll be able to access your devices from everywhere!</p>
                </ExplanationWrapper>
                <TextWrapper>
                <TextField required id="standard required" label="Openhab Username"
                           name="userName" />
                <TextField required id="standard required" label="Openhab Password" type="password"
                           name="password" />
                </TextWrapper>
                <ButtonGroup>
                    <Link to ="/home"><Button color="primary" variant="contained">Back</Button></Link>
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
  Buttongroup {
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