import {useState} from "react";
import jwt from "jsonwebtoken"

export default function AuthProvider({children}) {
    const [token, setToken] = useState()
    const claims = jwt.decode(token)


}