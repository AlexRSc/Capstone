import {FormControlLabel, Switch} from "@material-ui/core";
import {useState} from "react";
import styled from "styled-components";
import {turnOnOffOff, turnOnOffOn} from "../services/onOff-api-service";


export default function OnOffDevice({onOff, token}) {
    const [checked, setChecked] = useState(false)
    const [error, setError] = useState()
    const onOffData = {uid: onOff.uid,
        deviceName: onOff.deviceName,
        itemName: onOff.itemName}

    const toggleChecked = () => {
        if (checked === false) {
            turnOnOffOn(token, onOffData).catch( error =>
                setError(error)
            )
        } else {
            turnOnOffOff(token, onOffData).catch( error =>
                setError(error)
            )
        }
        setChecked((prev) => !prev)
    }


    return(
        <Wrapper>
            <FormControlLabel control={<Switch color="primary"/>} label={onOff.deviceName}
                              labelPlacement="top" checked={checked} onChange={toggleChecked}
                              value={onOff}
            />
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
