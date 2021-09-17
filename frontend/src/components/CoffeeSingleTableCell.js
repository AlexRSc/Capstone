import {Popover, TableCell, TableRow, Typography} from "@material-ui/core";
import Button from "@material-ui/core/Button";
import {useState} from "react";
import {makeStyles} from "@material-ui/core/styles";
import CoffeeComponentOption from "./CoffeeComponentOption";
import TextField from "@material-ui/core/TextField";
import moment from "moment";

const useStyles = makeStyles((theme) => ({
    typography: {
        padding: theme.spacing(2),
        display: "flex",
        justifyContent: "space-evenly"
    },

}))
export default function CoffeeSingleTableCell({coffee, open, token}) {
    const classes = useStyles();
    const [anchorEl, setAnchorEl] = useState(null)
    const date = moment(coffee.date).toISOString().split('.')[0]

    const handleClick = (event) => {
        setAnchorEl(event.currentTarget);
    }

    const handleClose = () => {
        setAnchorEl(null);
    }

    const openIt = Boolean(anchorEl)
    const id = open ? 'simple-popover' : undefined;

    return (
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
                    <Typography className={classes.typography}>
                        <TextField id="datetime-local" label="Coffee Time!" type="datetime-local" name="date"
                                   value={date}
                                    InputLabelProps={{
                            shrink: true,
                        }}/>
                        {coffee.onOff && <div>Active</div>}{!coffee.onOff &&
                    <div>Inactive</div>}</Typography>
                </Popover>
            </TableCell>
            <TableCell align="right">
                <CoffeeComponentOption coffee={coffee} token={token}/>
            </TableCell>
        </TableRow>)
}