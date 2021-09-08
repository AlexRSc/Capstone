import {useAuth} from "../auth/AuthProvider";
import PageLayout from "../components/PageLayout";
import Header from "../components/Header";
import Footer from "../components/Footer";
import styled from "styled-components";
import {Link} from "react-router-dom";
import Button from "@material-ui/core/Button";

export default function LightsPage() {
    return (
        <PageLayout>
            <Header title="Connect My Hub"/>
            <Wrapper>
                <h1>This is a PlaceHolder</h1>
                <p>Whatever, whenever</p>
                <p>We´re meant to be together</p>
                <p>I´ll be there, and you´ll be near</p>
                <p>And that´s the deal, my dear</p>
                <Link to ="/home"><Button color="primary" variant="contained">Back</Button></Link>
            </Wrapper>
            <Footer/>
        </PageLayout>
    )
}

const Wrapper = styled.div``