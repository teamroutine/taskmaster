import React, { useEffect, useState } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import { Box, Typography, Button, Snackbar } from '@mui/material';
import { validateInvite, joinTeamWithInvite } from '../../taskmasterApi';
import { jwtDecode } from 'jwt-decode';



function JoinTeamPage() {
    const { inviteCode } = useParams();
    const navigate = useNavigate();
    const token = localStorage.getItem("accessToken");
    const [username, setUsername] = useState(null);
    const [invite, setInvite] = useState(null);
    const [error, setError] = useState("");
    const [snackbarMessage, setSnackbarMessage] = useState("");
    const code = inviteCode?.split("/").pop();

    useEffect(() => {
        if (token) {
            try {
                const decodedToken = jwtDecode(token);
                setUsername(decodedToken?.sub);
            } catch (err) {
                console.error("Token decoding failed", err);
                setUsername(null);
            }
        }
    }, [token])

    useEffect(() => {
        validateInvite(code)
            .then((data) => {
                console.log(data)
                setInvite(data);
            })
            .catch((err) => {
                setError("Invite link is invalid or it has expired" + err);
            });
    }, [code]);

    const handleJoinTeam = () => {
        if (!token || !username) {
            setSnackbarMessage("You must be logged in to join a team.");
            return;
        }


        joinTeamWithInvite(code)
            .then(() => {
                navigate("/teams", {
                    state: { message: "You joined the team successfully!" }
                });
            })
            .catch(() => {
                setSnackbarMessage("Failed to join the team.");
            });
    };

    const handleCloseSnackbar = () => {
        setSnackbarMessage("");
    };

    if (error) return <Typography color="error">{error}</Typography>;
    if (!invite) return <Typography>Loading...</Typography>;

    return (
        <Box sx={{ p: 4 }}>
            <Typography variant="h5">Join Team</Typography>
            <Typography variant="h6" sx={{ mt: 2 }}>
                You have been invited to join team: <strong>{invite.team.teamName}</strong>
            </Typography>
            <Button
                variant="contained"
                color="primary"
                sx={{ mt: 3 }}
                onClick={handleJoinTeam}
            >
                Join team
            </Button>
            <Snackbar
                open={!!snackbarMessage}
                autoHideDuration={3000}
                message={snackbarMessage}
                onClose={handleCloseSnackbar}
            />
        </Box>
    );
}

export default JoinTeamPage;