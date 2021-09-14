import {makeStyles} from "@material-ui/core/styles";
import {Paper, Table, TableBody, TableCell, TableContainer, TableHead, TableRow} from "@material-ui/core";
import PageLayout from "../components/PageLayout";
import Footer from "../components/Footer";
import Header from "../components/Header";
import styled from "styled-components";
import Button from "@material-ui/core/Button";
import SaveIcon from "@material-ui/icons/Save";
import {Link} from "react-router-dom";


const useStyles = makeStyles({
    table: {
        minWidth: 250
    },
});

export default function CoffeePage() {
    const classes = useStyles();


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
                            <TableRow>
                                <TableCell>DeviceName</TableCell>
                                <TableCell align="right">active</TableCell>
                                <TableCell align="right">Still no!</TableCell>
                            </TableRow>
                            <TableRow>
                                <TableCell>DeviceName</TableCell>
                                <TableCell align="right">active</TableCell>
                                <TableCell align="right">Still no!</TableCell>
                            </TableRow>
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