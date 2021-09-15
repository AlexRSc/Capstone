import {makeStyles} from "@material-ui/core/styles";
import {
    Paper,
    Popover,
    Table,
    TableBody,
    TableCell,
    TableContainer,
    TableHead,
    TableRow,
    Typography
} from "@material-ui/core";
import PageLayout from "../components/PageLayout";
import Footer from "../components/Footer";
import Header from "../components/Header";
import styled from "styled-components";
import Button from "@material-ui/core/Button";
import SaveIcon from "@material-ui/icons/Save";
import {Link} from "react-router-dom";
import {useAuth} from "../auth/AuthProvider";
import {useEffect, useState} from "react";
import {getMyCoffees} from "../services/coffee-api-service";


const useStyles = makeStyles((theme) => ({
    table: {
        minWidth: 250
    },
    typography: {
        padding: theme.spacing(2),
    },
}))

export default function CoffeePage() {
    const classes = useStyles();
    const {token} = useAuth()
    const [coffees, setCoffees] = useState([])
    const [open, setOpen] = useState(false)
    const [anchorEl, setAnchorEl] = useState(null);

    const handleClick = (event) => {
        setAnchorEl(event.currentTarget);
    }

    const handleClose = () => {
        setAnchorEl(null);
    }


    const openIt = Boolean(anchorEl)
    const id = open ? 'simple-popover' : undefined;

    useEffect(() => {
        getMyCoffees(token).then(setCoffees)
            .catch()
        setOpen(true)
    }, [token])

    return (
        <PageLayout>
            <Header title="Coffee"/>
            <Wrapper>
                <Button variant="contained" color="primary"
                        size="large" startIcon={<SaveIcon/>}>
                    <Link className="Links" to="/addcoffee">Add Coffee Device</Link>
                </Button>
                <TableContainer component={Paper}>
                    <Table className={classes.table} aria-label="simple table">
                        <TableHead>
                            <TableRow>
                                <TableCell>Coffee Device</TableCell>
                                <TableCell align="right">State</TableCell>
                                <TableCell align="right">Options</TableCell>
                            </TableRow>
                        </TableHead>
                        <TableBody>
                            {coffees.map((coffee) => (
                                <TableRow key={coffee.deviceName}>
                                    <TableCell align="right">{coffee.deviceName}</TableCell>
                                    <TableCell align="right">
                                        <Button aria-describedby={id} variant="contained" size="small"
                                                onClick={handleClick}>
                                            Open State</Button>
                                        <Popover open={openIt} id={id} anchorEl={anchorEl} onClose={handleClose}
                                                 anchorOrigin={{
                                                     vertical: 'bottom',
                                                     horizontal: 'center',
                                                 }}
                                                 transformOrigin={{
                                                     vertical: 'top',
                                                     horizontal: 'center',
                                                 }}>
                                            <Typography
                                                className={classes.typography}>{coffee.date.toLocaleString()} Active:
                                                {coffee.onOff && <div>yes</div>}{!coffee.onOff &&
                                                <div>no</div>}</Typography>
                                        </Popover>

                                    </TableCell>
                                    <TableCell align="right"><Link to="/coffeeOptions">
                                        <Button variant="contained" color="primary"
                                                size="small">Options</Button></Link>
                                    </TableCell>
                                </TableRow>
                            ))}
                        </TableBody>
                    </Table>
                </TableContainer>
                <Link to="/home"><Button color="primary" variant="contained">Back</Button></Link>
            </Wrapper>
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