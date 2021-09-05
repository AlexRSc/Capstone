import styled from "styled-components";
import {NavLink} from "react-router-dom";

export default function Footer() {

    return(
        <Wrapper>
            <div>Info</div>
            <div>About Us</div>
            <div>Services</div>
        </Wrapper>
    )
}

const Wrapper = styled.footer`
  width:100%;
  text-align: center;
  background: green;
  position:relative;
  display: flex;
  padding: var(--size-m);
  div {
    flex-grow: 1;
  }

  `