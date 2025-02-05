import { BrowserRouter as Router, Link } from "react-router-dom";

function App() {
    return (

        <Router>
            <nav>
                <Link to="/">Home</Link>
            </nav>
            <AppRoutes />
        </Router>

    );
}

export default App;