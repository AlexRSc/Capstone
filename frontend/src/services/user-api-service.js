import axios from "axios";

export const getToken = credentials =>
    axios.post('api/ProfBeluga/user/login', credentials)
        .then(response => response.data)

const headers = token => ({
    headers: {
        Authorization: `Bearer ${token}`,
    },
})