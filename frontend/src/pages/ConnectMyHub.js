import PageLayout from "../components/PageLayout";
import Header from "../components/Header";
import Button from "@material-ui/core/Button";
import {Link} from "react-router-dom";
import Footer from "../components/Footer";
import styled from "styled-components";
import {useAuth} from "../auth/AuthProvider";



export default function ConnectMyHub() {
    const {user, token} = useAuth()
    return (
        <PageLayout>
            <Header title="Connect My Hub"/>
            <Wrapper>
                <h1>This is a PlaceHolder</h1>
                <p>Whatever, whenever</p>
                <p>We´re meant to be together</p>
                <p>I´ll be there, and you´ll be near</p>
                <p>And that´s the deal, my dear</p>
            </Wrapper>
            <Footer/>
        </PageLayout>
    )
}

const Wrapper = styled.div``