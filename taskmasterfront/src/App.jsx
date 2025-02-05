import { BrowserRouter as Router, Link } from "react-router-dom";
import AppRoutes from "./routes"

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