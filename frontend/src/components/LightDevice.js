import styled from "styled-components";
import SingleLightComponent from "./SingleLightComponent";

export default function LightsDevice({lights, token}) {

    return (
        <Wrapper>
            {lights&&lights.map(light => {
                return (
                        <SingleLightComponent key={light.uid} light={light} token={token} />
                )
            })}
        </Wrapper>
    )
}

const Wrapper = styled.div`
  display: grid;
  grid-template-columns: repeat(2, 1fr);
`

