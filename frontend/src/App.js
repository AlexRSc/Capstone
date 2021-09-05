import Header from "./components/Header";
import PageLayout from "./components/PageLayout";
import Footer from "./components/Footer";
import {Route, BrowserRouter as Router, Switch} from "react-router-dom";
import WelcomePage from "./pages/WelcomePage";


export default function App() {
  return (
      <Router>
          <Switch>
          <Route exact path="/"><WelcomePage/></Route>
          <Route path="/login"><WelcomePage/></Route>
          <Route path="/registration"><WelcomePage/></Route>
          <Route path="/Info"><WelcomePage/></Route>
          <Route path="/about-us"><WelcomePage/></Route>
          </Switch>
      </Router>

  )
}

