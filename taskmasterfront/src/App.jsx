import { BrowserRouter as Router, Routes, Route, Link } from "react-router-dom";
import { AppBar, Toolbar, Typography, Button, Container } from "@mui/material";
import { ThemeProvider } from '@mui/material/styles';
import taskmasterLogo from "./assets/taskmaster-logo2.png";
import Home from "./pages/Home";
import PanelView from "./components/PanelView";
import CreatePanel from "./components/CreatePanel";
import ListPanelView from "./components/ListPanelView";
import theme from "./theme";

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
                        <Button color="inherit" component={Link} to="/" sx={{ marginRight: 2,fontSize: '1.05rem'  }}>Home</Button>
                        <Button color="inherit" component={Link} to="/panels" sx={{ marginRight: 2, fontSize: '1.05rem' }}>All Panels</Button>
                        <Button variant="outlined" color="inherit" sx={{ marginRight: 1 }}>Sign In</Button>
                        <Button variant="outlined" color="inherit">Register</Button>
                    </Toolbar>
                </AppBar>
                <Container>
                    <Toolbar />
                    <Routes>
                        <Route path="/" element={<Home />} />
                        <Route path="/panels" element={<ListPanelView />} />
                        <Route path="/panels/:panelid" element={<PanelView />} />
                        <Route path="/panel/create" element={<CreatePanel />} />
                    </Routes>
                </Container>
            </Router>
        </ThemeProvider>
    );

}

export default App;
