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
import {connectHub} from "../services/hub-api-service";
import MuiAlert from "@material-ui/lab/Alert";

function Alert(props) {
    return <MuiAlert elevation={6} variant="filled" {...props} />;
}

const initialState = {
    hubEmail: '',
    hubPassword: '',
}

export default function ConnectMyHub() {
    const {token} = useAuth()
    const [error, setError] = useState()
    const [loading, setLoading] = useState(false)
    const [credentials, setCredentials] =useState(initialState)
    const [redirect, setRedirect] = useState()
    const [open, setOpen]=useState(false)
    const history = useHistory()

    const handleCredentialsChange = event =>
        setCredentials({...credentials, [event.target.name]: event.target.value})

    const handleSubmit = event => {
        event.preventDefault()
        setOpen(false)
        setError()
        setLoading(true)
        connectHub(token, credentials).then(hub => setRedirect(hub.hubEmail))
            .catch(error => {
                setError(error)
                setLoading(false)
            }).finally(()=>setOpen(true))
    }

    const handleClose = (event, reason) => {
        if (reason=== 'clickaway') {
            return;
        }
        setOpen(false);
    }

    const handleClear = () => {
        setCredentials({
            hubEmail: '',
            hubPassword: '',
        })
    }

    const handleBack = () => {
        history.push("/home")
    }

    if(redirect){
        return <Redirect to ="/home"/>
    }
    return (
        <PageLayout>
            <Header title="Connect My Hub"/>
            {loading && <CircularProgress/>}
            {!loading &&(
            <Wrapper>
                <ExplanationWrapper>
                    <h3>How does this work?</h3>
                    <p>In order to connect to the Openhab API, we need use your Username and Password once.
                        Once we connect to the API, you??ll be able to access your devices from everywhere!</p>
                </ExplanationWrapper>
                <TextWrapper>
                <TextField required id="standard required" label="Openhab Email"
                           name="hubEmail" value={credentials.hubEmail} onChange={handleCredentialsChange}/>
                <TextField required id="standard required" label="Openhab Password" type="password"
                           name="hubPassword" value={credentials.hubPassword} onChange={handleCredentialsChange} />
                </TextWrapper>
                <ButtonGroup>
                    <Button color="primary" variant="contained" onClick={() => handleBack()}>Back</Button>
                    <Button color="secondary" onClick={handleClear}>Clear</Button>
                    <Button color="primary" variant="contained" onClick={handleSubmit}>Submit</Button>
                </ButtonGroup>
            </Wrapper>)}
            {!error &&<Snackbar open={open} autoHideDuration={1000} onClose={handleClose}>
                <Alert onClose={handleClose} severity="success">
                    Login SuccessFull!
                </Alert>
            </Snackbar>}
            {error &&<Snackbar open={open} autoHideDuration={1000} onClose={handleClose}>
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