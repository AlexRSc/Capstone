import Header from "../components/Header";
import PageLayout from "../components/PageLayout";
import Footer from "../components/Footer";
import TextField from "@material-ui/core/TextField";
import {ButtonGroup, CircularProgress} from "@material-ui/core";
import {Link, Redirect} from "react-router-dom";
import Button from "@material-ui/core/Button";
import styled from "styled-components"
import {useAuth} from "../auth/AuthProvider";
import {useState} from "react";

const initialState = {
    userName: '',
    password: '',
}
export default function LoginPage() {
    const {login, user} = useAuth()
    const [credentials, setCredentials] = useState(initialState)
    const [loading, setLoading] = useState(false)
    const [error, setError] = useState()

    const handleCredentialsChange = event =>
        setCredentials({...credentials, [event.target.name]: event.target.value})

    const handleSubmit = event => {
        event.preventDefault()
        setLoading(true)
        setError()
        login(credentials).catch(error => {
            setError(error)
            setLoading(false)
        })
    }
    const handleClear = event => {
        setCredentials({
            userName: "",
            password: "",
        })
    }

    if(user) {
        return <Redirect to="Home"/>
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
                    <Link to="/"><Button color="primary" variant="contained">Back</Button></Link>
                    <Button color="secondary" onClick={handleClear}>Clear</Button>
                    <Button color="primary" variant="contained" onClick={handleSubmit} >Submit</Button>
                </ButtonGroup>
            </Wrapper>)}
            <Footer/>
        </PageLayout>
    )
}

const Wrapper = styled.div`
  display: grid;

`