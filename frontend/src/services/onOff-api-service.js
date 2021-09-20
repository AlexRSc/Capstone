import axios from "axios";

const headers = token => ({
    headers: {
        Authorization: `Bearer ${token}`,
    },
})

export const getMyOnOff = (token) =>
    axios.get('api/ProfBeluga/onoff/myonoffdevices', headers(token))
        .then(response => response.data)

export const turnOnOffOn = (token, onOff) =>
    axios.post('api/ProfBeluga/onoff/turnon',onOff, headers(token))
        .then(response => response.data)

export const turnOnOffOff = (token, onOff) =>
    axios.post('api/ProfBeluga/onoff/turnoff',onOff, headers(token))
        .then(response => response.data)

export const addOnOff = (token, credentials) =>
    axios.post('api/ProfBeluga/onoff/add', credentials, headers(token))
        .then(response => response.data)

export const deleteOnOffDevice = (token, uid) =>
    axios.delete(`api/ProfBeluga/onoff/delete/${uid}`,  headers(token))
        .then(response => response.data)