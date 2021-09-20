import axios from "axios";

const headers = token => ({
    headers: {
        Authorization: `Bearer ${token}`,
    },
})

export const addLights = (token, credentials) =>
    axios.post('api/ProfBeluga/lights/add', credentials, headers(token))
        .then(response => response.data)

export const getMyLights = (token) =>
    axios.get('api/ProfBeluga/lights/mylightdevices', headers(token))
        .then(response => response.data)

export const turnLightOn = (token, light) =>
    axios.post('api/ProfBeluga/lights/turnon',light, headers(token))
        .then(response => response.data)

export const turnLightOff = (token, light) =>
    axios.post('api/ProfBeluga/lights/turnoff',light, headers(token))
        .then(response => response.data)

export const setBrightness = (token, brightness) =>
    axios.put('api/ProfBeluga/lights/brightness',brightness , headers(token))
        .then(response => response.data)

export const deleteLightsDevice = (token, uid) =>
    axios.delete(`api/ProfBeluga/lights/delete/${uid}`,  headers(token))
        .then(response => response.data)