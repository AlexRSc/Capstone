import axios from "axios";

export const getToken = credentials =>
    axios.post('api/ProfBeluga/user/login', credentials)
        .then(response => response.data)
        .then(dto => dto.token)

const headers = token => ({
    headers: {
        Authorization: `Bearer ${token}`,
    },
})

export const registerUser = newUser =>
    axios.post('api/ProfBeluga/user/registration', newUser)
        .then(response => response.data)