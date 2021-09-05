import Header from "../components/Header";
import PageLayout from "../components/PageLayout";
import Footer from "../components/Footer";


export default function WelcomePage() {
    return (
        <PageLayout>
            <Header title="Welcome"/>
            <h1>Hey mate, welcome to the jungle!</h1>
            <Footer/>
        </PageLayout>
    )
}