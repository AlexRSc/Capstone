import PageLayout from "../components/PageLayout";
import Header from "../components/Header";
import Footer from "../components/Footer";
import styled from "styled-components";
import {Link} from "react-router-dom";
import Button from "@material-ui/core/Button";
import SaveIcon from "@material-ui/icons/Save";
import {FormControlLabel, Slider, Switch} from "@material-ui/core";

export default function LightsPage() {
    //just a basic Layout, it has 0 functionality besides looking ok
    return (
        <PageLayout>
            <Header title="Connect My Hub"/>
            <Wrapper>
                <Button variant="contained" color="primary"
                        size="large" startIcon={<SaveIcon/>}>
                    <Link class="Links" to="/addlights">Add Lights Device</Link>
                </Button>
                <LightsWrapper>
                    <Light>
                        <FormControlLabel control={<Switch color="primary"/>} label={"Device 1"}
                                          labelPlacement="top"/>
                        <Slider size="small" orientation="vertical"/>
                    </Light>
                    <Light>
                        <FormControlLabel control={<Switch color="primary"/>} label={"Device 2"}
                                          labelPlacement="top"/>
                        <Slider size="small" orientation="vertical"/>
                    </Light>
                    <Light>
                        <FormControlLabel control={<Switch color="primary"/>} label={"Device 3"}
                                          labelPlacement="top"/>
                        <Slider size="small" orientation="vertical"/>
                    </Light>
                    <Light>
                        <FormControlLabel control={<Switch color="primary"/>} label={"Device 4"}
                                          labelPlacement="top"/>
                        <Slider size="small" orientation="vertical"/>
                    </Light>
                    <Light>
                        <FormControlLabel control={<Switch color="primary"/>} label={"Device 3"}
                                          labelPlacement="top"/>
                        <Slider size="small" orientation="vertical"/>
                    </Light>
                    <Light>
                        <FormControlLabel control={<Switch color="primary"/>} label={"Device 4"}
                                          labelPlacement="top"/>
                        <Slider size="small" orientation="vertical"/>
                    </Light>
                </LightsWrapper>
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

const LightsWrapper = styled.div`
  display: grid;
  grid-template-columns: repeat(2, 1fr);
`

const Light = styled.div`
  justify-content: space-between;
  margin-right: 10px;
  margin-bottom: 20px;
  margin-top: 20px;
  display: inline-flex;
  flex-direction: row;
  align-items: center;
  
`