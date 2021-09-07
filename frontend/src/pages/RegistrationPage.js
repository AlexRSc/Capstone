import PageLayout from "../components/PageLayout";
import Header from "../components/Header";
import Footer from "../components/Footer";
import TextField from "@material-ui/core/TextField";
import styled from "styled-components"
import {ButtonGroup} from "@material-ui/core";
import Button from "@material-ui/core/button";
import {Link, Redirect} from "react-router-dom";
import {useState} from "react";
import {registerUser} from "../services/user-api-service";

const initialState= {
        userName: '',
        password: '',
        email: ''
}
export default function RegistrationPage() {
        const [credentials, setCredentials] = useState(initialState)
        const [loading, setLoading] = useState(false)
        const [error, setError] = useState()
        const [registeredUser, setRegisteredUser]=useState()

        const handleCredentialsChange = event =>
            setCredentials({...credentials, [event.target.name]: event.target.value})

        const handleSubmit = event => {
                event.preventDefault()
                setError()
                registerUser(credentials).then(registeredUser => setRegisteredUser(registeredUser))
                    .catch(error => {
                        setError(error)
                        setLoading(false)
                })
        }
        const handleClear = event => {
                setCredentials({
                        userName: "",
                        email: "",
                        password: "",
                })
        }

        if(registeredUser) {
                return <Redirect to ="/login"/>
        }

return(
    <PageLayout>
        <Header title="Registration Form"/>
        <Wrapper>
        <TextField required id="standard required" label="Username"
        name="userName" value={credentials.userName} onChange={handleCredentialsChange}/>
        <TextField required id="standard required" label="Email-Address"
        name="email" value={credentials.email} onChange={handleCredentialsChange}/>
        <TextField required id="standard required" label="Password" type="password"
        name="password" value={credentials.password} onChange={handleCredentialsChange}/>
        <ButtonGroup>
                <Link to ="/"><Button color="primary" variant="contained">Back</Button></Link>
                <Button color="secondary" onClick={handleClear} >Clear</Button>
                <Button color="primary" variant="contained" onClick={handleSubmit}>Submit</Button>
        </ButtonGroup>
        </Wrapper>
        <Footer/>
    </PageLayout>
)
}

const Wrapper = styled.div`
        display: grid;

`