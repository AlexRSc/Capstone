import {FormControlLabel, Snackbar, Switch} from "@material-ui/core";
import {useState} from "react";
import styled from "styled-components";
import {deleteOnOffDevice, turnOnOffOff, turnOnOffOn} from "../services/onOff-api-service";
import MuiAlert from "@material-ui/lab/Alert";
import DeleteDevice from "./DeleteDevice";

function Alert(props) {
    return <MuiAlert elevation={6} variant="filled" {...props} />;
}

export default function OnOffDevice({onOff, token}) {
    const [checked, setChecked] = useState(onOff.onOff)
    const [error, setError] = useState()
    const [open, setOpen] = useState(false)
    const onOffData = {uid: onOff.uid,
        deviceName: onOff.deviceName,
        itemName: onOff.itemName}

    const toggleChecked = () => {
        setOpen(false)
        if (checked === false) {
            turnOnOffOn(token, onOffData).catch( error =>
                setError(error)
            ).finally(() => setOpen(true))
        } else {
            turnOnOffOff(token, onOffData).catch( error =>
                setError(error)
            ).finally( () => setOpen(true))
        }
        setChecked((prev) => !prev)
    }

    const handleClose = (event, reason) => {
        if (reason=== 'clickaway') {
            return;
        }
        setOpen(false);
    }
    const handleDelete= () => {
        setOpen(false)
        deleteOnOffDevice(token, onOff.uid).catch(error => setError(error))
            .finally(() => setOpen(true))
    }


    return(
        <Wrapper>
            <FormControlLabel control={<Switch color="primary"/>} label={onOff.deviceName}
                              labelPlacement="top" checked={checked} onChange={toggleChecked}
                              value={onOff}
            />
            <DeleteDevice handleDelete={handleDelete} />
            {!error &&<Snackbar open={open} autoHideDuration={1000} onClose={handleClose}>
                <Alert onClose={handleClose} severity="success">
                    {onOff.deviceName} succesfully switched states!
                </Alert>
            </Snackbar>}
            {error &&<Snackbar open={open} autoHideDuration={1000} onClose={handleClose}>
                <Alert onClose={handleClose} severity="error">
                    Something went wrong with {onOff.deviceName} !
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
