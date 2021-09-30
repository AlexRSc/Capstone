import Header from "../components/Header";
import PageLayout from "../components/PageLayout";
import Footer from "../components/Footer";
import TextField from "@material-ui/core/TextField";
import {ButtonGroup, CircularProgress, Snackbar} from "@material-ui/core";
import {Redirect, useHistory} from "react-router-dom";
import Button from "@material-ui/core/Button";
import styled from "styled-components"
import {useAuth} from "../auth/AuthProvider";
import {useState} from "react";
import MuiAlert from "@material-ui/lab/Alert"

function Alert(props) {
    return <MuiAlert elevation={6} variant="filled" {...props} />;
}

const initialState = {
    userName: '',
    password: '',
}
export default function LoginPage() {
    const {login, user} = useAuth()
    const [credentials, setCredentials] = useState(initialState)
    const [loading, setLoading] = useState(false)
    const [error, setError] = useState()
    const [open, setOpen]=useState(false);
    const history = useHistory()


    const handleClose = (event, reason) => {
        if (reason=== 'clickaway') {
            return;
        }
        setOpen(false);
    }

    const handleCredentialsChange = event =>
        setCredentials({...credentials, [event.target.name]: event.target.value})

    const handleSubmit = event => {
        event.preventDefault()
        setOpen(false)
        setLoading(true)
        setError()
        login(credentials).catch(error => {
            setError(error)
            setLoading(false)
            setOpen(true)
        })
    }
    const handleClear = () => {
        setCredentials({
            userName: "",
            password: "",
        })
    }

    const handleBack = () => {
        history.push("/")
    }

    if(user) {
        return <Redirect to="/home"/>
    }
    return (
        <PageLayout>
            <Header title="Login Form"/>
            {loading && <CircularProgress/>}
            {!loading &&(
            <Wrapper>
                <TextField required id="standard required" label="Username"
                           name="userName" value={credentials.userName} onChange={handleCredentialsChange}/>
                <TextField required id="standard required" label="Password" type="password"
                            name="password" value={credentials.password} onChange={handleCredentialsChange}/>
                <ButtonGroup>
                    <Button color="primary" variant="contained" onClick={() => handleBack()}>Back</Button>
                    <Button color="secondary" onClick={handleClear}>Clear</Button>
                    <Button color="primary" variant="contained" onClick={handleSubmit} >Submit</Button>
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

`