import Button from "@material-ui/core/Button";
import {FormControlLabel, Popover, Slider, Snackbar, Switch} from "@material-ui/core";
import styled from "styled-components"
import {useState} from "react";
import {deleteAlarmDevice, setAlarmVolume, turnAlarmOff, turnAlarmOn} from "../services/alarm-api-service";
import DeleteDevice from "./DeleteDevice";
import {Alert} from "@material-ui/lab";
import AddNewAlarmEventComponent from "./AddNewAlarmEventComponent";

export default function SingleAlarmComponent({token, alarm, handleRefresh, handleExpand}) {
    const [anchorEl, setAnchorEl] = useState(null);
    const [open, setOpen] = useState(false)
    const [value, setValue] = useState(parseInt(alarm.volume))
    const [error, setError] = useState()


    const openIt = Boolean(anchorEl)
    const id = open ? 'simple-popover' : undefined;

    const handleClick = (event) => {
        setAnchorEl(event.currentTarget);
    }

    const handleClose = () => {
        setAnchorEl(null);
    }

    const handleCloseMessage = (event, reason) => {
        if (reason === 'clickaway') {
            return;
        }
        setOpen(false);
    }

    const handleDelete = () => {
        setOpen(false)
        deleteAlarmDevice(token, alarm.uid).catch(setError).finally(() => setOpen(true)).finally(() => handleRefresh(token))
    }
    const handleVolume = (event, newValue) => {
        event.preventDefault()
        const Volume = {
            volume: newValue.toString()
        }
        setOpen(false)
        setValue(newValue)
        setAlarmVolume(token, alarm.uid, Volume).catch(error => setError(error)).finally(() => setOpen(true))
            .finally(() => handleRefresh(token))
    }
    const toggleOnOff = () => {
        setError()
        setOpen(false)
        if (alarm.onOff.toString() === "false") {
            turnAlarmOn(token, alarm).catch(setError).finally(() => setOpen(true)).finally(() => handleRefresh(token))
        } else {
            turnAlarmOff(token, alarm).catch(setError).finally(() => setOpen(true)).finally(() => handleRefresh(token))
        }
    }

    return (
        <Wrapper>
            <Button aria-describedby={id} variant="contained" size="small"
                    onClick={handleClick}>
                {alarm.deviceName}</Button>
            <Popover open={openIt} id={id} anchorEl={anchorEl} onClose={handleClose}
                     anchorOrigin={{
                         vertical: 'bottom',
                         horizontal: 'center',
                     }}
                     transformOrigin={{
                         vertical: 'top',
                         horizontal: 'center',
                     }}>
                <PopupWrapper>
                    <FormControlLabel control={<Switch color="primary"/>} label="Off/On"
                                      labelPlacement="top" checked={alarm.onOff} onChange={toggleOnOff}
                                      value={alarm.onOff}/>
                    <Slider size="small" orientation="vertical" value={value} name="volume"
                            onChange={handleVolume} step={5} min={0} max={100}/>
                    <AddNewAlarmEventComponent alarm={alarm} handleExpand={handleExpand} token={token}/>
                    <DeleteDevice handleDelete={handleDelete}/>
                </PopupWrapper>
            </Popover>
            {!error && <Snackbar open={open} autoHideDuration={1000} onClose={handleCloseMessage}>
                <Alert onClose={handleClose} severity="success">
                    Device: {alarm.deviceName} modified successfully!
                </Alert>
            </Snackbar>}
            {error && <Snackbar open={open} autoHideDuration={1000} onClose={handleCloseMessage}>
                <Alert onClose={handleClose} severity="error">
                    Something went wrong... Call the police!
                </Alert>
            </Snackbar>}
        </Wrapper>
    )
}

const Wrapper = styled.div``

const PopupWrapper = styled.div`
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  margin: 20px;
`
