import {useAuth} from "../auth/AuthProvider";
import {useState} from "react";
import {Redirect, useHistory} from "react-router-dom";
import PageLayout from "../components/PageLayout";
import Header from "../components/Header";
import {ButtonGroup, CircularProgress, Snackbar} from "@material-ui/core";
import TextField from "@material-ui/core/TextField";
import Button from "@material-ui/core/Button";
import Footer from "../components/Footer";
import styled from "styled-components";
import MuiAlert from "@material-ui/lab/Alert";
import {createAlarmDevice} from "../services/alarm-api-service";

const initialState = {
    deviceName: '',
    uid: '',
    mediaItem: '',
    volumeItem: '',
}


function Alert(props) {
    return <MuiAlert elevation={6} variant="filled" {...props} />;
}

export default function AddAlarmPage() {
    const {token} = useAuth()
    const [error, setError] = useState()
    const [loading, setLoading] = useState(false)
    const [credentials, setCredentials] = useState(initialState)
    const [redirect, setRedirect] = useState()
    const [open, setOpen] = useState(false)
    const history = useHistory()

    const handleCredentialsChange = event =>
        setCredentials({...credentials, [event.target.name]: event.target.value})

    const handleClose = (event, reason) => {
        if (reason === 'clickaway') {
            return;
        }
        setOpen(false);
    }

    const handleSubmit = event => {
        event.preventDefault()
        setError()
        setOpen(false)
        setLoading(true)
        createAlarmDevice(token, credentials).then(setRedirect).catch(setError).finally(() => setOpen(true))
            .finally(() => setLoading(false))
    }

    const handleClear = () => {
        setCredentials({
            deviceName: '',
            uid: '',
            mediaItem: '',
            volumeItem: '',
        })
    }

    const handleBack = () => {
        history.push("/alarm")
    }

    if (redirect) {
        return <Redirect to="/alarm"/>
    }
    return (
        <PageLayout>
            <Header title="Add Alarm"/>
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
                        <TextField required id="standard required" label="MediaItemName"
                                   name="mediaItem" value={credentials.mediaItem} onChange={handleCredentialsChange}/>
                        <TextField required id="standard required" label="VolumeItemName"
                                   name="volumeItem" value={credentials.volumeItem} onChange={handleCredentialsChange}/>

                    </TextWrapper>
                    <ButtonGroup>
                        <Button color="primary" variant="contained" onClick={() => handleBack()}>Back</Button>
                        <Button color="secondary" onClick={handleClear}>Clear</Button>
                        <Button color="primary" variant="contained" onClick={handleSubmit}>Submit</Button>
                    </ButtonGroup>
                </Wrapper>)}
            {!error && <Snackbar open={open} autoHideDuration={1000} onClose={handleClose}>
                <Alert onClose={handleClose} severity="success">
                    {credentials.deviceName} added successfully!
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
  grid-template-rows: 1fr 1fr 1fr;

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