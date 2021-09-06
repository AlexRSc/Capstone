import styled from "styled-components";
import {NavLink} from "react-router-dom";

export default function Footer() {

    return(
        <Wrapper>
            <NavLink to="/">Info</NavLink>
            <NavLink to="/">About Us</NavLink>
            <NavLink to="/">Services</NavLink>
        </Wrapper>
    )
}

const Wrapper = styled.footer`
  width:100%;
  text-align: center;
  background: #89ABE3FF;
  position:relative;
  display: flex;
  padding: var(--size-m);
  a {
    flex-grow: 1;
  }

  `