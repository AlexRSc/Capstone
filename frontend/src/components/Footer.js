import styled from "styled-components";
import {NavLink} from "react-router-dom";

export default function Footer() {

    return(
        <Wrapper>
            <NavLink></NavLink>
        </Wrapper>
    )
}

const Wrapper = styled.footer`
  width:100%;
  text-align: center;
  background: green;
  position:relative;

  `