import {useState} from "react";
import {setBrightness} from "../services/lights-api-service";
import {Slider} from "@material-ui/core";

export default function LightsSlider({light, token}) {
    const [value, setValue] = useState(50)
    const [error, setError] = useState()
    const [open, setOpen]=useState(false);

    const handleBrightness = (event, newValue) => {
        event.preventDefault()
        setValue(newValue)
        setError()
        const newLightsData = {uid: light.uid,
            deviceName: light.deviceName,
            itemName: light.itemName,
            brightness: newValue.toString()}
        setBrightness(token,newLightsData ).catch(error=>
            setError(error))
    }
    return (
    <Slider size="small" orientation="vertical" value={value}
            onChange={handleBrightness} step={5} min={0} max={100}/> )

}
