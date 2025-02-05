import { BrowserRouter as Router, Routes, Route, Link } from "react-router-dom";
import { AppBar, Toolbar, Typography, Button, Container } from "@mui/material";
import { ThemeProvider } from '@mui/material/styles';
import taskmasterLogo from "./assets/taskmaster-logo2.png";
import Home from "./pages/Home";
import PanelView from "./components/Panelview";
import theme from "./theme";
import Products from "./pages/Products";

function App() {
    return (
        <ThemeProvider theme={theme}>
            <Router>
                <AppBar
                    position="fixed"
                    sx={{ width: "100%" }}
                >
                    <Toolbar>
                        <Typography variant="h6" style={{ flexGrow: 1 }}>
                            <img src={taskmasterLogo} style={{ height: "75px" }} alt="Taskmaster Logo" />
                        </Typography>
                        <Button color="inherit" component={Link} to="/">Home</Button>
                        <Button color="inherit" component={Link} to="/panels/1">Panel 1</Button>
                        <Button color="inherit" component={Link} to="/products">Products</Button>
                        <Button color="inherit">Solutions</Button>
                        <Button color="inherit">Contact</Button>
                        <Button variant="outlined" color="inherit">Sign In</Button>
                        <Button variant="outlined" color="inherit">Register</Button>
                    </Toolbar>
                </AppBar>
                <Container>
                    <Routes>
                        <Route path="/" element={<Home />} />
                        <Route path="/panels/:panelid" element={<PanelView />} />
                        <Route path="/products" element={<Products />} />
                    </Routes>
                </Container>
            </Router>
        </ThemeProvider>
    );

}

export default App;
