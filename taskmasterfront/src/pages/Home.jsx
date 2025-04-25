import { Box, Typography, Button } from "@mui/material";
import Register from "../components/Register";


function Home() {
    return (
        <Box sx={{ textAlign: "center", mt: 5 }} >
            <Typography variant="h1" gutterBottom>
                Taskmaster
            </Typography>
            <Typography variant="h6">
                Welcome to Taskmaster! This is a platform for planning, organizing, managing and tracking your all kind of projects.
                Get started by logging in and creating your own team. Taskmaster lets you collaborate with your friends and colleagues
                effectively.
            </Typography>
            <Register>Register</Register>
        </Box>
    )
}

export default Home;