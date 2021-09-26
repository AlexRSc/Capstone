import {Route, BrowserRouter as Router, Switch} from "react-router-dom";
import WelcomePage from "./pages/WelcomePage";
import RegistrationPage from "./pages/RegistrationPage";
import LoginPage from "./pages/LoginPage";
import AuthProvider from "./auth/AuthProvider";
import HomePage from "./pages/HomePage";
import ProtectedRoute from "./auth/ProtectedRoute";
import ConnectMyHub from "./pages/ConnectMyHub";
import LightsPage from "./pages/LightsPage";
import AddLightsPage from "./pages/AddLightsPage";
import OnOffPage from "./pages/OnOffPage";
import AddOnOffPage from "./pages/AddOnOffPage";
import CoffeePage from "./pages/CoffeePage";
import AddCoffeePage from "./pages/AddCoffeePage";
import AlarmPage from "./pages/AlarmPage";
import AddAlarmPage from "./pages/AddAlarmPage";


export default function App() {
    return (
        <AuthProvider>
            <Router>
                <Switch>
                    <Route exact path="/"><WelcomePage/></Route>
                    <Route path="/login"><LoginPage/></Route>
                    <Route path="/registration"><RegistrationPage/></Route>
                    <ProtectedRoute path="/home" component={HomePage}/>
                    <ProtectedRoute path="/connectmyhub" component={ConnectMyHub}/>
                    <ProtectedRoute path="/lights" component={LightsPage}/>
                    <ProtectedRoute path="/addlights" component={AddLightsPage}/>
                    <ProtectedRoute path="/onOff" component={OnOffPage}/>
                    <ProtectedRoute path="/addOnOff" component={AddOnOffPage}/>
                    <ProtectedRoute path="/coffee" component={CoffeePage}/>
                    <ProtectedRoute path="/addcoffee" component={AddCoffeePage}/>
                    <ProtectedRoute path="/coffeeOptions" component={AddCoffeePage}/>
                    <ProtectedRoute path="/alarm" component={AlarmPage}/>
                    <ProtectedRoute path="/addAlarm" component={AddAlarmPage}/>
                    <Route path="/info"></Route>
                    <Route path="/about-us"></Route>
                </Switch>
            </Router>
        </AuthProvider>
    )
}

