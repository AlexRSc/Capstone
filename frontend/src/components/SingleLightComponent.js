import {FormControlLabel, Switch} from "@material-ui/core";
import {useState} from "react";
import {turnLightOff, turnLightOn} from "../services/lights-api-service";
import styled from "styled-components";
import LightsSlider from "./LightsSlider";


export default function SingleLightComponent({light, token}) {
    const [checked, setChecked] = useState(false)
    const [error, setError] = useState()
    const lightsData = {uid: light.uid,
                deviceName: light.deviceName,
                itemName: light.itemName}
    const toggleChecked = () => {
        if (checked === false) {
            turnLightOn(token, lightsData).catch( error =>
                setError(error)
            )
        } else {
            turnLightOff(token, lightsData).catch( error =>
                setError(error)
            )
        }
        setChecked((prev) => !prev)
    }



    return(
        <Wrapper>
            <FormControlLabel control={<Switch color="primary"/>} label={light.deviceName}
                              labelPlacement="top" checked={checked} onChange={toggleChecked}
                              value={light}
            />
            <LightsSlider light={light} token={token}/>
        </Wrapper>
    )
}
const Wrapper = styled.div`
  justify-content: space-between;
  margin-right: 10px;
  margin-bottom: 20px;
  margin-top: 20px;
  display: inline-flex;
  flex-direction: row;
  align-items: center;`
