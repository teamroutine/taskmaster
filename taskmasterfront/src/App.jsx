import { BrowserRouter as Router, Routes, Route, Link } from "react-router-dom";
import { AppBar, Toolbar, Typography, Button, Container } from "@mui/material";
import { ThemeProvider } from '@mui/material/styles';
import taskmasterLogo from "./assets/taskmaster-logo2.png";
import Home from "./pages/Home";
import PanelView from "./components/PanelView";
import CreatePanel from "./components/CreatePanel";
import ListTeams from "./components/ListTeams";
import theme from "./theme";
import Register from "./components/Register";
import Login from "./components/Login";
import AuthButton from "./components/AuthButton";
import { jwtDecode } from "jwt-decode";
import JoinTeamPage from "./pages/JoinTeamPage";


function App() {
    const token = localStorage.getItem("accessToken");
    let username = null;
    if (token) {
        try {
            const decodedToken = jwtDecode(token);
            username = decodedToken.sub;
        } catch (error) {
            console.error("Failed to decode token:", error);
        }
    }

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
                        <Button color="inherit" component={Link} to="/" sx={{ marginRight: 2, fontSize: '1.05rem' }}>Home</Button>
                        <Button color="inherit" component={Link} to="/teams" sx={{ marginRight: 2, fontSize: '1.05rem' }}>All Teams</Button>
                        {/* <Button variant="outlined" color="inherit" component={Link} to="/login" sx={{ marginRight: 1 }}>Login</Button> */}
                        <AuthButton variant='outlined' color='inherit' component={Link} to="/login" sx={{ marginRight: 1 }} />
                        {/* <Button variant="outlined" color="inherit" component={Link} to="/register" sx={{ marginRight: 1, marginLeft: 1 }}>Register</Button> */}
                        <Register variant='outlined' color='inherit' component={Link} to="/register" sx={{ marginRight: 1 }} />
                    </Toolbar>
                </AppBar>
                <Container>
                    <Toolbar />
                    <Routes>
                        <Route path="/" element={<Home />} />
                        <Route path="/panels/:panelid" element={<PanelView />} />
                        <Route path="/panel/create" element={<CreatePanel />} />
                        <Route path="/teams" element={<ListTeams username={username} />} />
                        <Route path="/register" element={<Register />} />
                        <Route path="/login" element={<Login />} />
                        <Route path="/team/join/:inviteCode" element={<JoinTeamPage />} />
                    </Routes>
                </Container>
            </Router>
        </ThemeProvider>
    );

}

export default App;
