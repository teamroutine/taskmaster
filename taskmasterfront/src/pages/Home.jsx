import { Box, Typography, Button } from "@mui/material";
import Register from "../components/Register";
import Login from "../components/Login"
import taskmasterLogo from "../assets/taskmaster-logo2.png";

function Home() {

    return (

        <Box sx={{ textAlign: "center", mt: 5, position: "absolute", left: "50%", top: "45%", marginTop: 10, transform: "translate(-50%, -50%)" }}>
            <img src={taskmasterLogo} alt="Taskmaster Logo" />
            <Typography variant="h2" gutterBottom>
                Taskmaster
            </Typography>
            <Typography variant="h6">
                Welcome to Taskmaster! This is a platform for planning, organizing, managing and tracking your all kind of projects.
                Get started by logging in and creating your own team. Taskmaster lets you collaborate with your friends and colleagues
                effectively.
            </Typography>
            <Box>
                <Register>Register</Register>

            </Box>

        </Box>
    )
}

export default Home;