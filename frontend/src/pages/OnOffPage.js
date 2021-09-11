import PageLayout from "../components/PageLayout";
import Header from "../components/Header";
import Footer from "../components/Footer";
import styled from "styled-components";
import {Link} from "react-router-dom";
import Button from "@material-ui/core/Button";
import SaveIcon from "@material-ui/icons/Save";
import {useAuth} from "../auth/AuthProvider";
import {useEffect, useState} from "react";
import {getMyOnOff} from "../services/onOff-api-service";
import OnOffDevice from "../components/OnOffDevice";


export default function OnOffPage() {
    const {token} = useAuth()
    const [error, setError] = useState()
    const [onOffs, setOnOffs] = useState([])
    const [loading, setLoading] = useState()

    useEffect(() => {
        setError()
        getMyOnOff(token).then(setOnOffs)
            .catch(setError)
            .finally(() => setLoading(false))
    }, [token])



    return (
        <PageLayout>
            <Header title="On Off"/>
            <Wrapper>
                <Button variant="contained" color="primary"
                        size="large" startIcon={<SaveIcon/>}>
                    <Link class="Links" to="/addOnOff">Add On Off Device</Link>
                </Button>
                <OnOffWrapper>
                {onOffs.map(onOff => {
                    return(
                <OnOffDevice onOff={onOff} token={token}/>)
                })}
                </OnOffWrapper>
                <Link to="/home"><Button color="primary" variant="contained">Back</Button></Link>
            </Wrapper>
            <Footer/>
        </PageLayout>
    )
}

const Wrapper = styled.div`
  display: grid;
  place-items: center;
  flex-direction: column;
  grid-template-rows: 1fr 2fr 1fr;
  .Links {
    text-decoration: none;
    color: white;
  }
`
const OnOffWrapper = styled.div `
  display: grid;
  grid-template-columns: repeat(3, 1fr);
`

