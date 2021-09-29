import {Checkbox, FormControlLabel, Popover, Snackbar, Switch} from "@material-ui/core";
import styled from "styled-components"
import Button from "@material-ui/core/Button";
import TextField from "@material-ui/core/TextField";
import {useState} from "react";

import {
    activateAlarmEvent,
    cancelAlarmEvent, deleteAlarmEvent,
    setAlarmEvent,
    turnAlarmOff,
    turnAlarmOn
} from "../services/alarm-api-service";
import DeleteDevice from "./DeleteDevice";
import {Alert} from "@material-ui/lab";

export default function AlarmEventsComponent({alarmEvent, token, alarm, handleExpand}) {
    const [anchorEl, setAnchorEl] = useState(null);
    const [checked, setChecked] = useState(alarmEvent.event)
    const [credentials, setCredentials] = useState(alarmEvent)
    const [onOff, setOnOff] = useState(alarm.onOff)
    const [dailyStatus, setDailyStatus] = useState(alarmEvent.daily)
    const [open, setOpen] = useState(false)
    const [error, setError] = useState()
    const [alarmCredentials, setAlarmCredentials] = useState(alarm)

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
        setOpen(false)
    }

    const handleDelete = () => {
        setError()
        deleteAlarmEvent(token, alarmEvent.id).catch(setError).finally(() => setOpen(true))
            .finally(() => handleExpand(alarm.uid))
    }

    const handleSubmit = event => {
        event.preventDefault()
        setError()
        setOpen(false)
        setAlarmEvent(token, alarm.uid, credentials).then(setCredentials).catch(setError).finally(() => setOpen(true))
            .finally(() => handleExpand(alarm.uid))
    }


    const handleCredentialsChange = event => {
        setCredentials({...credentials, [event.target.name]: event.target.value})
    }

    const toggleActive = () => {
        setError()
        setOpen(false)
        if (!checked) {
            activateAlarmEvent(token, credentials).then(setCredentials).catch(setError).finally(() => setChecked(true))
                .finally(() => setOpen(true)).finally(() => handleExpand(alarm.uid))
        } else {
            cancelAlarmEvent(token, credentials).then(setCredentials).catch(setError).finally(() => setChecked(false))
                .finally(() => setOpen(true)).finally(() => handleExpand(alarm.uid))
        }
    }

    const toggleOnOff = () => {
        setError()
        if (!onOff) {
            turnAlarmOn(token, alarmCredentials).then(setAlarmCredentials).catch(setError).finally(() => setOnOff(true))
                .finally(() => setOpen(true)).finally(() => handleExpand(alarm.uid))
        } else {
            turnAlarmOff(token, alarmCredentials).then(setAlarmCredentials).catch(setError).finally(() => setOnOff(false))
                .finally(() => setOpen(true)).finally(() => handleExpand(alarm.uid))
        }
    }

    const toggleCheck = (event) => {
        setCredentials({...credentials, [event.target.name]: !dailyStatus})
        if (event.target.value.toString() === "false") {
            setDailyStatus(true)
        } else {
            setDailyStatus(false)
        }
        console.log(dailyStatus)
    }


    const openIt = Boolean(anchorEl)
    const id = open ? 'simple-popover' : undefined;

    return (
        <Wrapper>
            <Button aria-describedby={id} variant="contained" size="small"
                    onClick={handleClick}>
                Options</Button>
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
                    <FormControlLabel control={<Switch color="primary"/>} label="Activation"
                                      labelPlacement="top" checked={checked} onChange={toggleActive}
                                      value={checked}
                    />
                    <TextField id="datetime-local" label="Alarm Time!" type="datetime-local" name="date"
                               value={credentials.date}
                               onChange={handleCredentialsChange} InputLabelProps={{
                        shrink: true,
                    }}/>
                    <FormControlLabel control={<Checkbox value={dailyStatus} onChange={toggleCheck} checked={dailyStatus}
                                                         name="daily"/>}
                                      label="Daily" labelPlacement="top"/>

                </PopupWrapper>
                <ButtonWrapper>
                    <FormControlLabel control={<Switch color="primary"/>} label="Turn Off/On"
                                      labelPlacement="top" checked={onOff} onChange={toggleOnOff}
                                      value={onOff}
                    />
                    <Button color="primary" variant="contained" size="small"
                            onClick={handleSubmit}>
                        Submit Date</Button>
                    <DeleteDevice handleDelete={handleDelete}/>
                </ButtonWrapper>
            </Popover>
            {!error && <Snackbar open={open} autoHideDuration={1000} onClose={handleCloseMessage}>
                <Alert onClose={handleClose} severity="success">
                    ID {credentials.id} modified succesfully!
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

const Wrapper = styled.div`
`
const PopupWrapper = styled.div`
  display: flex;
  flex-direction: row;
  align-items: center;
`

const ButtonWrapper = styled.div`
  display: flex;
  flex-direction: row;
  justify-content: space-evenly;
  margin-bottom: 5px;
  row-gap: 40px;
`