import axios from "axios";

const headers = token => ({
    headers: {
        Authorization: `Bearer ${token}`,
    },
})

export const addLights = (token, credentials) =>
    axios.post('api/ProfBeluga/lights/add', credentials, headers(token))
        .then(response => response.data)