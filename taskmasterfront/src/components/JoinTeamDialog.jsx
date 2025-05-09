import React, { useState } from 'react';
import { Dialog, DialogTitle, DialogContent, DialogActions, TextField, Button, Snackbar, Alert } from '@mui/material';
import { joinTeamWithInvite } from '../../taskmasterApi';

function JoinTeamDialog({ open, onClose, onSuccess }) {
    const [inviteCode, setInviteCode] = useState("");
    const [snackbarOpen, setSnackbarOpen] = useState(false);
    const [snackbarMessage, setSnackbarMessage] = useState("");
    const [snackbarSeverity, setSnackbarSeverity] = useState("info");

    const handleJoin = async () => {
        try {
            const code = inviteCode

            await joinTeamWithInvite(code);

            setSnackbarSeverity("success");
            setSnackbarMessage("Successfully joined the team!");
            setInviteCode("");
            onSuccess();
            onClose();
        } catch (error) {
            console.error("Join error:", error);

            const message = error.message.includes("404")
                ? "Invalid or expired invite code."
                : "Failed to join the team.";

            setSnackbarSeverity("error");
            setSnackbarMessage(message);
        } finally {
            setSnackbarOpen(true);
        }
    };

    const handleSnackbarClose = () => {
        setSnackbarOpen(false);
    };

    return (
        <>
            <Dialog open={open} onClose={onClose}>
                <DialogTitle>Join a Team</DialogTitle>
                <DialogContent>
                    <TextField
                        label="Invite code"
                        fullWidth
                        value={inviteCode}
                        onChange={(e) => setInviteCode(e.target.value)}
                        sx={{ mt: 1, mb: 2 }}
                    />
                </DialogContent>
                <DialogActions>
                    <Button onClick={onClose}>Cancel</Button>
                    <Button onClick={handleJoin} variant="contained">Join</Button>
                </DialogActions>

            </Dialog>

            <Snackbar
                open={snackbarOpen}
                autoHideDuration={4000}
                onClose={handleSnackbarClose}
                anchorOrigin={{ vertical: 'bottom', horizontal: 'center' }}
            >
                <Alert
                    onClose={handleSnackbarClose}
                    severity={snackbarSeverity}
                    sx={{ width: "100%" }}
                >
                    {snackbarMessage}
                </Alert>
            </Snackbar>
        </>
    )
}

export default JoinTeamDialog