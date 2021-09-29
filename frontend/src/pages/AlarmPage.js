import PageLayout from "../components/PageLayout";
import {Link, useHistory} from "react-router-dom";
import {
    Button,
    Paper,
    Snackbar, Table, TableBody, TableCell, TableContainer, TableHead, TableRow
} from "@material-ui/core";
import Header from "../components/Header";
import Footer from "../components/Footer";
import styled from "styled-components"
import {makeStyles} from "@material-ui/core/styles";
import {useEffect, useState} from "react";
import TextField from "@material-ui/core/TextField";
import SaveIcon from "@material-ui/icons/Save";
import {useAuth} from "../auth/AuthProvider";
import {getMyAlarmEvents, getMyAlarms} from "../services/alarm-api-service";
import AlarmEventsComponent from "../components/AlarmEventsComponent";
import moment from "moment";
import SingleAlarmComponent from "../components/SingleAlarmComponent";
import {Alert} from "@material-ui/lab";


const useStyles = makeStyles(() => ({
    table: {
        minWidth: 250
    },
}))

export default function AlarmPage() {
    const classes = useStyles()
    const {token} = useAuth()
    const [alarms, setAlarms] = useState([])
    const [error, setError] = useState()
    const [alarmEvents, setAlarmEvents] = useState([])
    const [open, setOpen] = useState(false)
    const history = useHistory()

    const handleExpand = (value) => {
        setOpen(false)
        setError()
        getMyAlarmEvents(token, value).then(setAlarmEvents).catch(setError).finally(() => setOpen(true))
    }

    const handleCloseMessage = (event, reason) => {
        if (reason === 'clickaway') {
            return;
        }
        setOpen(false);
    }

    useEffect(() => {
        setOpen(false)
        getMyAlarms(token).then(setAlarms).catch(setError).finally(() => setOpen(true))
    }, [token])

    const handleRefreshDevice = (token) => {
        getMyAlarms(token).then(setAlarms).catch(setError)
    }

    const handleBack = () => {
        history.push("/home")
    }

    return (
        <PageLayout>
            <Header title="Alarm"/>
            <Wrapper>
                <Button variant="contained" color="primary"
                        size="large" startIcon={<SaveIcon/>}>
                    <Link className="Links" to="/addAlarm">Add Alarm Device</Link>
                </Button>
                {alarms.map((alarm) => (
                    <TableContainer component={Paper} key={alarm.uid}>
                        <Table className={classes.table} aria-label="simple table">
                            <TableHead>
                                <TableRow>
                                    <TableCell align="right">
                                        <SingleAlarmComponent token={token}
                                                              handleRefresh={handleRefreshDevice}
                                                              alarm={alarm}
                                                              handleExpand={handleExpand}/>
                                    </TableCell>
                                    <TableCell align="right">
                                        <Button color="primary" variant="contained" value={alarm.uid}
                                                onClick={() => handleExpand(alarm.uid)}
                                        >Expand</Button>
                                    </TableCell>
                                </TableRow>
                            </TableHead>
                            <TableBody>
                                {alarmEvents && alarmEvents.map((alarmEvent) => (
                                    <TableRow key={alarmEvent.id}>
                                        <TableCell align="right">
                                            <TextField id="datetime-local" label="Alarm Time!"
                                                       type="datetime-local" name="date"
                                                       value={moment(alarmEvent.date).toISOString().split('.')[0]}
                                                       InputLabelProps={{
                                                           shrink: true,
                                                       }}/>
                                        </TableCell>
                                        <TableCell align="right"><AlarmEventsComponent alarmEvent={alarmEvent}
                                                                                       alarm={alarm}
                                                                                       token={token}
                                                                                       handleExpand={handleExpand}/></TableCell>
                                    </TableRow>
                                ))}
                            </TableBody>
                        </Table>
                    </TableContainer>
                ))}
                <Button color="primary" variant="contained" onClick={() => handleBack()}>Back</Button>
            </Wrapper>
            {!error && <Snackbar open={open} autoHideDuration={1000} onClose={handleCloseMessage}>
                <Alert onClose={handleCloseMessage} severity="success">
                    List fetched successfully!
                </Alert>
            </Snackbar>}
            {error && <Snackbar open={open} autoHideDuration={1000} onClose={handleCloseMessage}>
                <Alert onClose={handleCloseMessage} severity="error">
                    Something went wrong... Call the police!
                </Alert>
            </Snackbar>}
            <Footer/>
        </PageLayout>
    )
}

const Wrapper = styled.div`
  display: flex;
  flex-direction: column;
  flex-wrap: wrap;
  gap: 10px;
  align-items: center;

  .columns {
    margin: 0 -15px;
  }

  .Links {
    text-decoration: none;
    color: white;
  }
`
