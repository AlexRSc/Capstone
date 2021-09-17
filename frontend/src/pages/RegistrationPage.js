import PageLayout from "../components/PageLayout";
import Header from "../components/Header";
import Footer from "../components/Footer";
import TextField from "@material-ui/core/TextField";
import styled from "styled-components"
import {ButtonGroup, CircularProgress, Snackbar} from "@material-ui/core";
import Button from "@material-ui/core/Button";
import {Link, Redirect} from "react-router-dom";
import {useState} from "react";
import {registerUser} from "../services/user-api-service";
import MuiAlert from "@material-ui/lab/Alert";

function Alert(props) {
    return <MuiAlert elevation={6} variant="filled" {...props} />;
}

const initialState = {
    userName: '',
    password: '',
    email: ''
}


export default function RegistrationPage() {
    const [credentials, setCredentials] = useState(initialState)
    const [loading, setLoading] = useState(false)
    const [error, setError] = useState()
    const [registeredUser, setRegisteredUser] = useState()
    const [open, setOpen]=useState(false);

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
        registerUser(credentials).then(registeredUser => setRegisteredUser(registeredUser))
            .catch(error => {
                setError(error)
                setLoading(false)
            }).finally(()=>setOpen(true))
    }
    const handleClear = () => {
        setCredentials({
            userName: "",
            email: "",
            password: "",
        })
    }

    if (registeredUser) {
        return <Redirect to="/login"/>
    }

    return (
        <PageLayout>
            <Header title="Registration Form"/>
            {loading && <CircularProgress/>}
            {!loading && (
                <Wrapper>

                    <TextField required id="standard required" label="Username"
                               name="userName" value={credentials.userName} onChange={handleCredentialsChange}/>
                    <TextField required id="standard required" label="Email-Address"
                               name="email" value={credentials.email} onChange={handleCredentialsChange}/>
                    <TextField required id="standard required" label="Password" type="password"
                               name="password" value={credentials.password} onChange={handleCredentialsChange}/>
                    <ButtonGroup>
                        <Link to="/"><Button color="primary" variant="contained">Back</Button></Link>
                        <Button color="secondary" onClick={handleClear}>Clear</Button>
                        <Button color="primary" variant="contained" onClick={handleSubmit}>Submit</Button>
                    </ButtonGroup>
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
                </Wrapper>)}
            <Footer/>
        </PageLayout>
    )
}

const Wrapper = styled.div`
  display: grid;

`