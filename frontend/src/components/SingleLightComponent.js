import {FormControlLabel, Snackbar, Switch} from "@material-ui/core";
import {useState} from "react";
import {deleteLightsDevice, turnLightOff, turnLightOn} from "../services/lights-api-service";
import styled from "styled-components";
import LightsSlider from "./LightsSlider";
import MuiAlert from "@material-ui/lab/Alert";
import DeleteDevice from "./DeleteDevice";

function Alert(props) {
    return <MuiAlert elevation={6} variant="filled" {...props} />;
}

export default function SingleLightComponent({light, token}) {
    const [checked, setChecked] = useState(light.onOff)
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

    const emptyOpen = () => {
        setOpen(false)
    }

    const toggleChecked = () => {
        setOpen(false)
        setError()
        if (checked === false) {
            turnLightOn(token, lightsData).catch( error =>
                setError(error)
            ).finally(() =>setOpen(true))
        } else {
            turnLightOff(token, lightsData).catch( error =>
                setError(error)
            ).finally(() =>setOpen(true))
        }
        setChecked((prev) => !prev)
    }

    const handleClose = (event, reason) => {
        if (reason=== 'clickaway') {
            return;
        }
        setOpen(false);
    }

    const handleDelete = () => {
        setOpen(false)
        deleteLightsDevice(token, light.uid).catch(error => setError(error))
            .finally(() => setOpen(true))
    }

    return(
        <Wrapper>
            <FormControlLabel control={<Switch color="primary"/>} label={light.deviceName}
                              labelPlacement="top" checked={checked} onChange={toggleChecked}
                              value={light}
            />
            <LightsSlider light={light} token={token} handleError={handleError} handleOpen={handleOpen} emptyOpen={emptyOpen}/>
            <DeleteDevice handleDelete={handleDelete} />
            {!error &&<Snackbar open={open} autoHideDuration={1000} onClose={handleClose}>
                <Alert onClose={handleClose} severity="success">
                    {lightsData.deviceName} worked successfully!
                </Alert>
            </Snackbar>}
            {error &&<Snackbar open={open} autoHideDuration={1000} onClose={handleClose}>
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
  flex-wrap:wrap;
  align-items: center;`
