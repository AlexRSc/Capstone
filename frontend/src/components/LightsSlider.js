import {useState} from "react";
import {setBrightness} from "../services/lights-api-service";
import {Slider} from "@material-ui/core";

export default function LightsSlider({light, token, handleError, handleOpen, emptyOpen}) {
    const [value, setValue] = useState(parseInt(light.brightness))

    const handleBrightness = (event, newValue) => {
        event.preventDefault()
        emptyOpen()
        setValue(newValue)
        const newLightsData = {uid: light.uid,
            deviceName: light.deviceName,
            itemName: light.itemName,
            brightness: newValue.toString()}
        setBrightness(token,newLightsData ).catch(error=>
            handleError(error)).finally(() => handleOpen())
    }
    return (
    <Slider size="small" orientation="vertical" value={value}
            onChange={handleBrightness} step={5} min={0} max={100}/> )

}
