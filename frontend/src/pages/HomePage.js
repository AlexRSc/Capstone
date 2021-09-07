import PageLayout from "../components/PageLayout";
import Header from "../components/Header";
import Footer from "../components/Footer";
import styled from "styled-components"
import Button from "@material-ui/core/button";
import {CloudUpload, WbIncandescent, } from "@material-ui/icons";
import AlarmIcon from "@material-ui/icons/Alarm"


export default function HomePage() {
    return (
        <PageLayout>
            <Header title="Home"/>
            <Wrapper>
                <Button variant="contained" color="primary" size="large" startIcon={<CloudUpload/>}> Add your Hub! </Button>
                <IconList>
                    <Icon>
                    <div>Lights</div>
                    <WbIncandescent fontSize="large">Lights</WbIncandescent>
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
  
`
const IconList = styled.div`
    display: grid;
    grid-template-columns: repeat(3, 1fr);
`

const Icon = styled.div`
    padding: 20px 20px 20px 20px;
    display:flex;
    flex-direction: column;
    align-items: center;
`