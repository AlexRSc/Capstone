import React from 'react';
import clsx from 'clsx';
import {makeStyles} from '@material-ui/core/styles';
import SwipeableDrawer from '@material-ui/core/SwipeableDrawer';
import Button from '@material-ui/core/Button';
import List from '@material-ui/core/List';
import Divider from '@material-ui/core/Divider';
import ListItem from '@material-ui/core/ListItem';
import ListItemIcon from '@material-ui/core/ListItemIcon';
import ListItemText from '@material-ui/core/ListItemText';
import InboxIcon from '@material-ui/icons/MoveToInbox';
import {NavLink} from "react-router-dom";

const useStyles = makeStyles({
    list: {
        width: 250,
    },
    fullList: {
        width: 'auto',
    },
});

export default function SwipeableTemporaryDrawer() {
    const classes = useStyles();
    const [state, setState] = React.useState({
        left: false,
    });

    const toggleDrawer = (anchor, open) => (event) => {
        if (event && event.type === 'keydown' && (event.key === 'Tab' || event.key === 'Shift')) {
            return;
        }

        setState({...state, [anchor]: open});
    };

    const list = (anchor) => (
            <div
                className={clsx(classes.list, {
                    [classes.fullList]: anchor === 'top' || anchor === 'bottom',
                })}
                role="presentation"
                onClick={toggleDrawer(anchor, false)}
                onKeyDown={toggleDrawer(anchor, false)}
            >
                <List>
                    <NavLink to="/">
                        <ListItem>
                            <ListItemIcon><InboxIcon/></ListItemIcon>
                            <ListItemText>Welcome</ListItemText>
                        </ListItem>
                    </NavLink>
                    <NavLink to="/login">
                        <ListItem>
                            <ListItemIcon><InboxIcon/></ListItemIcon>
                            <ListItemText>Login</ListItemText>
                        </ListItem>
                    </NavLink>
                    <NavLink to="/registration">
                        <ListItem>
                            <ListItemIcon><InboxIcon/></ListItemIcon>
                            <ListItemText>Registration</ListItemText>
                        </ListItem>
                    </NavLink>
                    <Divider/>
                    <NavLink to="/home">
                        <ListItem>
                            <ListItemIcon><InboxIcon/></ListItemIcon>
                            <ListItemText>Home</ListItemText>
                        </ListItem>
                    </NavLink>
                    <NavLink to="/connectmyhub">
                        <ListItem>
                            <ListItemIcon><InboxIcon/></ListItemIcon>
                            <ListItemText>SetUp Hub</ListItemText>
                        </ListItem>
                    </NavLink>
                    <NavLink to="/lights">
                    <ListItem>
                        <ListItemIcon><InboxIcon/></ListItemIcon>
                        <ListItemText>Lights</ListItemText>
                    </ListItem>
                    </NavLink>
                    <NavLink to="/onOff">
                    <ListItem>
                        <ListItemIcon><InboxIcon/></ListItemIcon>
                        <ListItemText>OnOff-Devices</ListItemText>
                    </ListItem>
                    </NavLink>
                </List>
            </div>
        )
    ;

    return (
        <div>
            {['left'].map((anchor) => (
                <React.Fragment key={anchor}>
                    <Button onClick={toggleDrawer(anchor, true)}>Menu</Button>
                    <SwipeableDrawer
                        anchor={anchor}
                        open={state[anchor]}
                        onClose={toggleDrawer(anchor, false)}
                        onOpen={toggleDrawer(anchor, true)}
                    >
                        {list(anchor)}
                    </SwipeableDrawer>
                </React.Fragment>
            ))}
        </div>
    );
}

