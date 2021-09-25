import {AddCircle} from "@material-ui/icons";
import Button from "@material-ui/core/Button";
import {useState} from "react";
import styled from 'styled-components'
import {Checkbox, FormControlLabel, Popover, Snackbar} from "@material-ui/core";
import TextField from "@material-ui/core/TextField";
import moment from "moment";
import {setAlarmEvent} from "../services/alarm-api-service";
import {Alert} from "@material-ui/lab";

const init = {
    date: moment().format().toString(),
    daily: false,
}
export default function AddNewAlarmEventComponent({token, alarm, handleExpand}) {
    const [anchorEl, setAnchorEl] = useState(null);
    const [open, setOpen] = useState(false)
    const [credentials, setCredentials] = useState(init)
    const [error, setError] = useState()

    const openIt = Boolean(anchorEl)
    const id = open ? 'simple-popover' : undefined;

    const handleClick = (event) => {
        setAnchorEl(event.currentTarget);
    }

    const handleCredentialsChange = event => {
        setCredentials({...credentials, [event.target.name]: event.target.value})
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

    const handleSubmit = event => {
        event.preventDefault()
        setError()
        setOpen(false)
        setAlarmEvent(token, alarm.uid, credentials).then(setCredentials).catch(setError)
            .finally(() => handleExpand(alarm.uid)).finally(() => setOpen(true))
    }

    return (
        <Wrapper>
            <Button color="primary" size="small" startIcon={<AddCircle/>} onClick={handleClick}>
                Add Event
            </Button>
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
                    <TextField id="datetime-local" label="Alarm Time!" type="datetime-local" name="date"
                               defaultValue={moment.date}
                               onChange={handleCredentialsChange} InputLabelProps={{
                        shrink: true,
                    }}/>
                    <FormControlLabel control={<Checkbox value={credentials.daily} onChange={handleCredentialsChange}
                                                         name="daily"/>}
                                      label="Daily" labelPlacement="top"/>
                    <Button color="primary" variant="contained" size="small"
                            onClick={handleSubmit}>
                        Submit Event</Button>
                </PopupWrapper>
            </Popover>
            {!error && <Snackbar open={open} autoHideDuration={1000} onClose={handleCloseMessage}>
                <Alert onClose={handleCloseMessage} severity="success">
                    AlarmEvent added successfully!
                </Alert>
            </Snackbar>}
            {error && <Snackbar open={open} autoHideDuration={1000} onClose={handleCloseMessage}>
                <Alert onClose={handleCloseMessage} severity="error">
                    Something went wrong... Call the police!
                </Alert>
            </Snackbar>}
        </Wrapper>
    )
}

const Wrapper = styled.div``

const PopupWrapper = styled.div`
  display: flex;
  flex-direction: row;
  align-items: flex-start;
  margin: 10px;
`