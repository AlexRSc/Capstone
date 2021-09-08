import axios from "axios";

const headers = token => ({
    headers: {
        Authorization: `Bearer ${token}`,
    },
})

export const connectHub = (token, credentials) =>
    axios.post('api/ProfBeluga/hub/newHub', credentials, headers(token))
        .then(response => response.data)