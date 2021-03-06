import PageLayout from "../components/PageLayout";
import Header from "../components/Header";
import Button from "@material-ui/core/Button";
import {Redirect, useHistory} from "react-router-dom";
import Footer from "../components/Footer";
import styled from "styled-components";
import {useAuth} from "../auth/AuthProvider";
import TextField from "@material-ui/core/TextField";
import {ButtonGroup, CircularProgress, Snackbar} from "@material-ui/core";
import {useState} from "react";
import {addLights} from "../services/lights-api-service";
import MuiAlert from "@material-ui/lab/Alert";

const initialState = {
    deviceName: '',
    uid: '',
    itemName: ''
}

function Alert(props) {
    return <MuiAlert elevation={6} variant="filled" {...props} />;
}

export default function AddLightsPage() {
    const {token} = useAuth()
    const [error, setError] = useState()
    const [loading, setLoading] = useState(false)
    const [credentials, setCredentials] = useState(initialState)
    const [redirect, setRedirect] = useState()
    const [open, setOpen] = useState(false)
    const history = useHistory()

    const handleCredentialsChange = event =>
        setCredentials({...credentials, [event.target.name]: event.target.value})

    const handleSubmit = event => {
        event.preventDefault()
        setError()
        setLoading(true)
        addLights(token, credentials).then(light => setRedirect(light.deviceName))
            .catch(error => {
                setError(error)
                setLoading(false)
            })
        setOpen(true)
    }

    const handleClose = (event, reason) => {
        if (reason === 'clickaway') {
            return;
        }
        setOpen(false);
    }

    const handleClear = () => {
        setCredentials({
            deviceName: '',
            uid: '',
            itemName: ''
        })
    }

    const handleBack = () => {
        history.push("/lights")
    }

    if (redirect) {
        return <Redirect to="/lights"/>
    }
    return (
        <PageLayout>
            <Header title="Add Lights"/>
            {loading && <CircularProgress/>}
            {!loading && (
                <Wrapper>
                    <ExplanationWrapper>
                        <h3>How does this work?</h3>
                        <p>Just add your UUID or ItemName (from OpenHub), and you're ready to go!</p>
                    </ExplanationWrapper>
                    <TextWrapper>
                        <TextField required id="standard required" label="Name (for this App)"
                                   name="deviceName" value={credentials.deviceName} onChange={handleCredentialsChange}/>
                        <TextField required id="standard required" label="UUID"
                                   name="uid" value={credentials.uid} onChange={handleCredentialsChange}/>
                        <TextField required id="standard required" label="ItemName"
                                   name="itemName" value={credentials.itemName} onChange={handleCredentialsChange}/>
                    </TextWrapper>
                    <ButtonGroup>
                        <Button color="primary" variant="contained" onClick={() => handleBack()}>Back</Button>
                        <Button color="secondary" onClick={handleClear}>Clear</Button>
                        <Button color="primary" variant="contained" onClick={handleSubmit}>Submit</Button>
                    </ButtonGroup>
                </Wrapper>)}
            {!error && <Snackbar open={open} autoHideDuration={1000} onClose={handleClose}>
                <Alert onClose={handleClose} severity="success">
                    Lightsdevice added!
                </Alert>
            </Snackbar>}
            {error && <Snackbar open={open} autoHideDuration={1000} onClose={handleClose}>
                <Alert onClose={handleClose} severity="error">
                    Wrong credentials!
                </Alert>
            </Snackbar>}
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