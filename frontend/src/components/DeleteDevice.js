import DeleteForeverIcon from '@material-ui/icons/DeleteForever'
import {useState} from "react";
import Button from "@material-ui/core/Button";
import {Dialog, DialogActions, DialogContent, DialogContentText, DialogTitle} from "@material-ui/core";

export default function DeleteDevice({handleDelete}) {
    const [open, setOpen] = useState(false)

    const handleClickOpen = () => {
        setOpen(true)
    }

    const handleClose= () => {
        setOpen(false)
    }

    return(
        <div>
        <Button color="secondary" size="small" startIcon={<DeleteForeverIcon />}
        onClick={handleClickOpen}>

        </Button>
        <Dialog
            open={open}
            onClose={handleClose}
            aria-labelledby="alert-dialog-title"
            aria-describedby="alert-dialog-description"
        >
            <DialogTitle id="alert-dialog-title">{"Do you really want to delete this device?"}</DialogTitle>
            <DialogContent>
                <DialogContentText id="alert-dialog-description">
                    This is irreversible, your device will be deleted from this App. ARE YOU REALLY REALLY REEEEEAAAAAAAALLLLLLLLLYYYYYYY SURE?!
                </DialogContentText>
            </DialogContent>
            <DialogActions>
                <Button onClick={handleClose} color="primary">
                    Disagree
                </Button>
                <Button onClick={handleDelete} color="secondary">
                    Agree
                </Button>
            </DialogActions>
        </Dialog>
        </div>

    )
}