import { BrowserRouter as Router, Link } from "react-router-dom";
import { AppBar, Toolbar, Typography, Button } from "@mui/material";
import taskmasterLogo from './assets/taskmaster-logo2.png'; import AppRoutes from "./routes"

function App() {
    return (
        <>
            <AppBar position="fixed" sx={{ width: '100%', backgroundColor: '#212121' }}>
                <Toolbar >
                    <Typography variant="h6" style={{ flexGrow: 1 }}>
                        <img
                            src={taskmasterLogo}
                            style={{ height: '75px' }}
                        />
                    </Typography>
                    <Button color="inherit">Home</Button>
                    <Button color="inherit">Products</Button>
                    <Button color="inherit">Solutions</Button>
                    <Button color="inherit">Contact</Button>

                    <Button variant="outlined" color="inherit">Sign In</Button>
                    <Button variant="outlined" color="inherit">Register</Button>
                </Toolbar>
            </AppBar>
            <Router>
                <nav>
                    <Link to="/">Home</Link>
                </nav>
                <AppRoutes />
            </Router>
        </>
    );
}

export default App;