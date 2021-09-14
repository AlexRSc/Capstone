import axios from "axios";

const headers = token => ({
    headers: {
        Authorization: `Bearer ${token}`,
    },
})

export const addCoffee = (token, credentials) =>
    axios.post('api/ProfBeluga/coffee/add', credentials, headers(token))
        .then(response => response.data)

export const getMyCoffees = (token) =>
    axios.get('api/ProfBeluga/coffee/getMyCoffee', headers(token))
        .then(response => response.data)