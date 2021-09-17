import {Checkbox, FormControlLabel, Popover, Snackbar, Switch} from "@material-ui/core";
import {useState} from "react";
import Button from "@material-ui/core/Button";
import styled from "styled-components"
import TextField from "@material-ui/core/TextField";
import {setNewCoffeeEvent, turnCoffeeOff, turnCoffeeOn} from "../services/coffee-api-service";
import {Alert} from "@material-ui/lab";


export default function CoffeeComponentOption({coffee, token}) {
    const [checked, setChecked] =useState(coffee.onOff)
    const [anchorEl, setAnchorEl] = useState(null);
    const [credentials, setCredentials] = useState(coffee)
    const [error, setError] =useState()
    const [open, setOpen] = useState(false)

    const handleClick = (event) => {
        setAnchorEl(event.currentTarget);
    }

    const handleClose = () => {
        setAnchorEl(null);
    }

    const handleCloseMessage = (event, reason) => {
        if (reason=== 'clickaway') {
            return;
        }
        setOpen(false);
    }

    const handleCredentialsChange = event => {
        setCredentials({...credentials, [event.target.name]: event.target.value})
        console.log(credentials)
    }

    const handleCheck = event => {
        setCredentials({...credentials, [event.target.name]: !credentials.dailyAction})
        console.log(credentials)
    }

    const handleSubmit = event => {
        event.preventDefault()
        setError()
        setOpen(false)
        setNewCoffeeEvent(token, credentials).catch(error=> setError(error))
        setOpen(true)
    }

    const openIt = Boolean(anchorEl)
    const id = open ? 'simple-popover' : undefined;

    const toggleChecked = () => {
        setOpen(false)
        if (checked === false) {
            turnCoffeeOn(token, coffee).catch(error=> setError(error))
        } else {
            turnCoffeeOff(token, coffee).catch(error=>setError(error))
        }
        setChecked((prev) => !prev)
        setOpen(true)
    }

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
                <FormControlLabel control={<Switch color="primary"/>} label="Off/On"
                                  labelPlacement="top" checked={checked} onChange={toggleChecked}
                                  value={coffee}
                />
                <TextField id="datetime-local" label="Coffee Time!" type="datetime-local" name="date"
                           value={credentials.date}
                           onChange={handleCredentialsChange} InputLabelProps={{
                    shrink: true,
                }}/>
                    <FormControlLabel control={<Checkbox value={credentials.dailyAction} onChange={handleCheck}
                                                         name="dailyAction"/>}
                                      label="Daily" labelPlacement="top" />

                </PopupWrapper>
                <ButtonWrapper>
                <Button color="primary" variant="contained" size="small"
                        onClick={handleSubmit}>
                    Submit Date</Button>
                </ButtonWrapper>
            </Popover>
            {!error &&<Snackbar open={open} autoHideDuration={1000} onClose={handleCloseMessage}>
                <Alert onClose={handleCloseMessage} severity="success">
                    Houston, we got a mission!
                </Alert>
            </Snackbar>}
            {error &&<Snackbar open={open} autoHideDuration={1000} onClose={handleCloseMessage}>
                <Alert onClose={handleCloseMessage} severity="error">
                    Something went wrong... It wasn't me, Im perfect!
                </Alert>
            </Snackbar>}
        </Wrapper>
    )

}

const Wrapper = styled.div`
`
const PopupWrapper= styled.div`
  display: flex;
  flex-direction: row;
  align-items: center;
`

const ButtonWrapper= styled.div`
  text-align: center;
  padding: 10px;
`