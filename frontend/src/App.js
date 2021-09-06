import {Route, BrowserRouter as Router, Switch} from "react-router-dom";
import WelcomePage from "./pages/WelcomePage";
import RegistrationPage from "./pages/RegistrationPage";
import LoginPage from "./pages/LoginPage";
import AuthProvider from "./auth/AuthProvider";
import HomePage from "./pages/HomePage";


export default function App() {
  return (
      <AuthProvider>
      <Router>
          <Switch>
          <Route exact path="/"><WelcomePage/></Route>
          <Route path="/login"><LoginPage/></Route>
          <Route path="/registration"><RegistrationPage/></Route>
          <Route path="/Home"><HomePage/></Route>
          <Route path="/Info"></Route>
          <Route path="/about-us"></Route>
          </Switch>
      </Router>
      </AuthProvider>
  )
}

