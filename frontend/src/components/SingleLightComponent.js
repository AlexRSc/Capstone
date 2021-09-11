import {FormControlLabel, Snackbar, Switch} from "@material-ui/core";
import {useState} from "react";
import {turnLightOff, turnLightOn} from "../services/lights-api-service";
import styled from "styled-components";
import LightsSlider from "./LightsSlider";
import MuiAlert from "@material-ui/lab/Alert";

function Alert(props) {
    return <MuiAlert elevation={6} variant="filled" {...props} />;
}

export default function SingleLightComponent({light, token}) {
    const [checked, setChecked] = useState(false)
    const [error, setError] = useState()
    const lightsData = {uid: light.uid,
                deviceName: light.deviceName,
                itemName: light.itemName}
    const [open, setOpen]=useState(false);

    const handleError = (error) => {
        setError(error)
        setOpen(true)
    }

    const handleOpen = () => {
        setOpen(true)
    }
    const toggleChecked = () => {
        if (checked === false) {
            turnLightOn(token, lightsData).catch( error =>
                setError(error)
            )
        } else {
            turnLightOff(token, lightsData).catch( error =>
                setError(error)
            )
        }
        setChecked((prev) => !prev)
        setOpen(true)
    }

    const handleClose = (event, reason) => {
        if (reason=== 'clickaway') {
            return;
        }
        setOpen(false);
    }

    return(
        <Wrapper>
            <FormControlLabel control={<Switch color="primary"/>} label={light.deviceName}
                              labelPlacement="top" checked={checked} onChange={toggleChecked}
                              value={light}
            />
            <LightsSlider light={light} token={token} handleError={handleError} handleOpen={handleOpen}/>
            {!error &&<Snackbar open={open} autoHideDuration={1000} onclose={handleClose}>
                <Alert onClose={handleClose} severity="success">
                    {lightsData.deviceName} turned on/off successfully!
                </Alert>
            </Snackbar>}
            {error &&<Snackbar open={open} autoHideDuration={1000} onclose={handleClose}>
                <Alert onClose={handleClose} severity="error">
                    Something went wrong with {lightsData.deviceName} !
                </Alert>
            </Snackbar>}
        </Wrapper>
    )
}
const Wrapper = styled.div`
  justify-content: space-between;
  margin-right: 10px;
  margin-bottom: 20px;
  margin-top: 20px;
  display: inline-flex;
  flex-direction: row;
  align-items: center;`
