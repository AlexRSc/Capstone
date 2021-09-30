import styled from "styled-components";
import Button from "@material-ui/core/Button"
import {useHistory} from "react-router-dom";
import {useAuth} from "../auth/AuthProvider";

export default function Header({title}) {
    const {token, logout} = useAuth()
    const history = useHistory()

    const renderLogin = () => {
        history.push("/login")
    }

    return(
    <Wrapper>
        <img src="https://image.flaticon.com/icons/png/512/1806/1806193.png" alt="a beluga whale logo"/>
        <h3>{title}</h3>
        {!token && <Button variant="contained" color="primary" onClick={() => renderLogin()}>Login</Button>}
        {token && <Button variant="contained" color="secondary" onClick={logout}>Logout</Button> }
    </Wrapper>
)
}



const Wrapper = styled.header`
  width: 100%;
  text-align: center;
  background: #89ABE3FF;
  position:relative;
  img {
    position: absolute;
    height: 40px;
    width: 40px;
    left: 10px;
    top: 10px;
  }
  Button {
    position: absolute;
    right: 10px;
    top: 10px;
  }
`

