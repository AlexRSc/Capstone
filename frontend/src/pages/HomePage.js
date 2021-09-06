import PageLayout from "../components/PageLayout";
import Header from "../components/Header";
import Footer from "../components/Footer";
import styled from "styled-components"


export default function HomePage() {
    return (
        <PageLayout>
            <Header title="Home"/>
            <Wrapper>
            <h1>Home</h1>
            </Wrapper>
            <Footer/>
        </PageLayout>
    )
}

const Wrapper = styled.div`
`