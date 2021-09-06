import styled from "styled-components";
import Button from "@material-ui/core/button"
import {Link} from "react-router-dom";

export default function Header({title}) {

    return(
    <Wrapper>
        <img src="/public/logo192.png"/>
        <h3>{title}</h3>
        <Link to ="/login"><Button variant="contained" color="primary" >Login</Button></Link>
    </Wrapper>
)
}

const Wrapper = styled.header`
  width:100%;
  text-align: center;
  background: #89ABE3FF;
  position:relative;
  img {
    position: absolute;
    left: 10px;
    top: 10px;
  }
  
  Button {
    position: absolute;
    right: 10px;
    top: 10px;
  }
  
  
    `

