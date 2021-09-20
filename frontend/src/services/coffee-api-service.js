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

export const turnCoffeeOn = (token, coffee) =>
    axios.post('api/ProfBeluga/coffee/turnOn', coffee, headers(token))
        .then(response => response.data)

export const turnCoffeeOff = (token, coffee) =>
    axios.post('api/ProfBeluga/coffee/turnOff', coffee, headers(token))
        .then(response => response.data)

export const setNewCoffeeEvent = (token, coffee) =>
    axios.put('api/ProfBeluga/coffee/newschedule', coffee, headers(token))
        .then(response => response.data)

export const activateCoffeeEvent = (token, coffee) =>
    axios.put('api/ProfBeluga/coffee/activate', coffee, headers(token))
        .then(response => response.data)

export const deactivateCoffeeEvent = (token, coffee) =>
    axios.put('api/ProfBeluga/coffee/deactivate', coffee, headers(token))
        .then(response => response.data)

export const deleteCoffeeDevice = (token, uid) =>
    axios.delete(`api/ProfBeluga/coffee/delete/${uid}`,  headers(token))
        .then(response => response.data)