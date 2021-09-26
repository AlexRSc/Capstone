import axios from "axios";

const headers = token => ({
    headers: {
        Authorization: `Bearer ${token}`,
    },
})

export const getMyAlarms = (token) =>
    axios.get('api/ProfBeluga/alarm/getMyAlarms', headers(token))
        .then(response => response.data)

export const getMyAlarmEvents = (token, uid) =>
    axios.get(`api/ProfBeluga/alarm/getMyEvents/${uid}`, headers(token))
        .then(response => response.data)

export const createAlarmDevice = (token, alarm) =>
    axios.post(`api/ProfBeluga/alarm/create`, alarm, headers(token))
        .then(response => response.data)

export const setAlarmEvent = (token, uid, alarmEvent) =>
    axios.post(`api/ProfBeluga/alarm/setEvent/${uid}`, alarmEvent, headers(token))
        .then(response => response.data)

export const activateAlarmEvent = (token, alarmEvent) =>
    axios.put(`api/ProfBeluga/alarm/activateEvent`, alarmEvent, headers(token))
        .then(response => response.data)

export const cancelAlarmEvent = (token, alarmEvent) =>
    axios.put(`api/ProfBeluga/alarm/cancelEvent`, alarmEvent, headers(token))
        .then(response => response.data)

export const setAlarmVolume = (token, uid, volume) =>
    axios.put(`api/ProfBeluga/alarm/setVolume/${uid}`,volume, headers(token))
        .then(response => response.data)

export const turnAlarmOn = (token, alarm) =>
    axios.put(`api/ProfBeluga/alarm/turnOn/`, alarm, headers(token))
        .then(response => response.data)

export const turnAlarmOff = (token, alarm) =>
    axios.put(`api/ProfBeluga/alarm/turnOff/`, alarm, headers(token))
        .then(response => response.data)

export const deleteAlarmEvent = (token, id) =>
    axios.delete(`api/ProfBeluga/alarm/deleteAlarmEvent/${id}`, headers(token))
        .then(response => response.data)

export const deleteAlarmDevice = (token, uid) =>
    axios.delete(`api/ProfBeluga/alarm/deleteAlarmDevice/${uid}`, headers(token))
        .then(response => response.data)
