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
import styled from "styled-components"
import {CloudUpload, WbIncandescent} from "@material-ui/icons";
import PowerSettingsNewIcon from "@material-ui/icons/PowerSettingsNew";
import HomeIcon from '@material-ui/icons/Home'
import AccountBoxIcon from '@material-ui/icons/AccountBox'
import VpnKeyIcon from '@material-ui/icons/VpnKey'
import HomeWorkIcon from '@material-ui/icons/HomeWork'
import LocalCafeIcon from "@material-ui/icons/LocalCafe";

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
            <Wrapper
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
                            <ListItemIcon><HomeWorkIcon/></ListItemIcon>
                            <ListItemText>Welcome</ListItemText>
                        </ListItem>
                    </NavLink>
                    <NavLink to="/login">
                        <ListItem>
                            <ListItemIcon><VpnKeyIcon/></ListItemIcon>
                            <ListItemText>Login</ListItemText>
                        </ListItem>
                    </NavLink>
                    <NavLink to="/registration">
                        <ListItem>
                            <ListItemIcon><AccountBoxIcon/></ListItemIcon>
                            <ListItemText>Registration</ListItemText>
                        </ListItem>
                    </NavLink>
                    <Divider/>
                    <NavLink to="/home">
                        <ListItem>
                            <ListItemIcon><HomeIcon/></ListItemIcon>
                            <ListItemText>Home</ListItemText>
                        </ListItem>
                    </NavLink>
                    <NavLink to="/connectmyhub">
                        <ListItem>
                            <ListItemIcon><CloudUpload/></ListItemIcon>
                            <ListItemText>SetUp Hub</ListItemText>
                        </ListItem>
                    </NavLink>
                    <NavLink to="/lights">
                        <ListItem>
                            <ListItemIcon><WbIncandescent/></ListItemIcon>
                            <ListItemText>Lights</ListItemText>
                        </ListItem>
                    </NavLink>
                    <NavLink to="/onOff">
                        <ListItem>
                            <ListItemIcon><PowerSettingsNewIcon/></ListItemIcon>
                            <ListItemText>OnOff-Devices</ListItemText>
                        </ListItem>
                    </NavLink>
                    <NavLink to ="/coffee">
                        <ListItem>
                            <ListItemIcon><LocalCafeIcon/></ListItemIcon>
                            <ListItemText>Coffee</ListItemText>
                        </ListItem>
                    </NavLink>
                </List>
            </Wrapper>
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

const Wrapper = styled.div`


  a {
    text-decoration: none;
    color: black;
  }
`