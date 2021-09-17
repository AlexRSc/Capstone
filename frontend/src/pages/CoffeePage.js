import {makeStyles} from "@material-ui/core/styles";
import {
    Paper, Snackbar,
    Table,
    TableBody,
    TableCell,
    TableContainer,
    TableHead,
    TableRow
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
import CoffeeSingleTableCell from "../components/CoffeeSingleTableCell";
import {Alert} from "@material-ui/lab";


const useStyles = makeStyles(()=> ({
    table: {
        minWidth: 250
    },
}))

export default function CoffeePage() {
    const classes = useStyles();
    const {token} = useAuth()
    const [error, setError]=useState()
    const [coffees, setCoffees] = useState([])
    const [open, setOpen] = useState(false)

    const handleClose = (event, reason) => {
        if (reason=== 'clickaway') {
            return;
        }
        setOpen(false);
    }

    useEffect(() => {
        getMyCoffees(token).then(setCoffees)
            .catch(setError).finally(()=>setOpen(true))
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
                                <CoffeeSingleTableCell coffee={coffee} open={open} token={token}/>
                            ))}
                        </TableBody>
                    </Table>
                </TableContainer>
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