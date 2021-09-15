import PageLayout from "../components/PageLayout";
import Header from "../components/Header";
import Footer from "../components/Footer";
import styled from "styled-components";
import {Link} from "react-router-dom";
import Button from "@material-ui/core/Button";
import SaveIcon from "@material-ui/icons/Save";
import {useAuth} from "../auth/AuthProvider";
import {useEffect, useState} from "react";
import {getMyLights} from "../services/lights-api-service";
import LightsDevice from "../components/LightDevice";
import {Snackbar} from "@material-ui/core";
import MuiAlert from "@material-ui/lab/Alert";

function Alert(props) {
    return <MuiAlert elevation={6} variant="filled" {...props} />;
}

export default function LightsPage() {

    const {token} = useAuth()
    const [error, setError] = useState()
    const [lights, setLights] = useState([])
    const [open, setOpen] = useState(false)

    const handleClose = (event, reason) => {
        if (reason=== 'clickaway') {
            return;
        }
        setOpen(false);
    }

    useEffect(() => {
        setError()
        getMyLights(token).then(setLights)
            .catch(setError)
        setOpen(true)
    }, [token])

    return (
        <PageLayout>
            <Header title="Lights"/>
            <Wrapper>
                <Button variant="contained" color="primary"
                        size="large" startIcon={<SaveIcon/>}>
                    <Link className="Links" to="/addlights">Add Lights Device</Link>
                </Button>
                <LightsDevice lights={lights} token={token}/>
                <Link to="/home"><Button color="primary" variant="contained">Back</Button></Link>
            </Wrapper>
            {error &&<Snackbar open={open} autoHideDuration={1000} onClose={handleClose}>
                <Alert onClose={handleClose} severity="error">
                    An error fetching your lights happened!
                </Alert>
            </Snackbar>}
            <Footer/>
        </PageLayout>
    )
}

const Wrapper = styled.div`
  display: grid;
  place-items: center;
  flex-direction: column;
  grid-template-rows: 1fr 2fr 1fr;

  .Links {
    text-decoration: none;
    color: white;
  }
`

