import styled from "styled-components";
import Button from "@material-ui/core/button"

export default function Header({title}) {

    return(
    <Wrapper>
        <img src="/public/logo192.png"/>
        <h3>{title}</h3>
        <Button variant="contained" color="primary">Login</Button>
    </Wrapper>
)
}

const Wrapper = styled.header`
  width:100%;
  text-align: center;
  background: green;
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

