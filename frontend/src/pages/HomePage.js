import PageLayout from "../components/PageLayout";
import Header from "../components/Header";
import Footer from "../components/Footer";
import styled from "styled-components"
import Button from "@material-ui/core/button";
import {CloudUpload, WbIncandescent, } from "@material-ui/icons";
import AlarmIcon from "@material-ui/icons/Alarm"
import ConnectMyHub from "./ConnectMyHub";
import {Link, Redirect} from "react-router-dom";


export default function HomePage() {

    const handleSubmit = event => {
        event.preventDefault()
        return <Redirect to="/connectmyhub"/>
    }

    return (
        <PageLayout>
            <Header title="Home"/>
            <Wrapper>
                <Button variant="contained" color="primary" onClick={handleSubmit}
                size="large" startIcon={<CloudUpload/>} >
                    <Link class="Links" to="/connectmyhub" >Connect my Hub!</Link>
                </Button>
                <IconList>
                    <Icon>
                    <Link to="/lights">
                    <div>Lights</div>
                    <WbIncandescent fontSize="large">Lights</WbIncandescent>
                    </Link>
                    </Icon>
                    <Icon>
                    <Link to="/lights">
                    <div>Alarm</div>
                    <AlarmIcon fontSize="large">Lights</AlarmIcon>
                    </Link>
                    </Icon>
                    <Icon>
                    <div>Alarm</div>
                    <AlarmIcon fontSize="large">Lights</AlarmIcon>
                    </Icon>
                    <Icon>
                    <div>Alarm</div>
                    <AlarmIcon fontSize="large">Lights</AlarmIcon>
                    </Icon>
                    <Icon>
                    <div>Alarm</div>
                    <AlarmIcon fontSize="large">Lights</AlarmIcon>
                    </Icon>
                    <Icon>
                    <div>Alarm</div>
                    <AlarmIcon fontSize="large">Lights</AlarmIcon>
                    </Icon>
                    <Icon>
                    <div>Alarm</div>
                    <AlarmIcon fontSize="large">Lights</AlarmIcon>
                    </Icon>
                    <Icon>
                    <div>Alarm</div>
                    <AlarmIcon fontSize="large">Lights</AlarmIcon>
                    </Icon>
                    <Icon>
                    <div>Alarm</div>
                    <AlarmIcon fontSize="large">Lights</AlarmIcon>
                    </Icon>
                </IconList>
            </Wrapper>
            <Footer/>
        </PageLayout>
    )
}

const Wrapper = styled.div`
    align-self: center;
    display: flex;
    justify-content: center;
    flex-direction: column;
.Links {
  text-decoration: none;
  color:white;
}
  
`
const IconList = styled.div`
    display: grid;
    grid-template-columns: repeat(3, 1fr);
`

const Icon = styled.div`
    padding: 20px;
    display:flex;
    flex-direction: column;
    align-items: center;
  a {
    text-decoration: none;
    color:black;
    display: flex;
    flex-direction: column;
    align-items: center;
  }
`