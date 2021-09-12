import styled from "styled-components";
import {NavLink} from "react-router-dom";
import SwipeableTemporaryDrawer from "./SwipableTemporaryDrawer";

export default function Footer() {

    return(
        <Wrapper>
            <NavLink to="/">Info</NavLink>
            <NavLink to="/">About Us</NavLink>
            <SwipeableTemporaryDrawer />
        </Wrapper>
    )
}

const Wrapper = styled.footer`
  width: 100%;
  text-align: center;
  background: #89ABE3FF;
  position: relative;
  display: flex;
  padding: var(--size-m);
  justify-content: space-around;
  a {
    text-decoration: none;
    color: black;
    flex-grow: 1;
    margin: 5px;
    margin-right: 70px;
  }
  
  `