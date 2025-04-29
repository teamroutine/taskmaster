import React, { useState } from "react";
import {
  Dialog,
  DialogContent,
  DialogTitle,
  DialogActions,
  Button,
  TextField,
  Snackbar,
  Typography,
  Box,
} from "@mui/material";
import { generateInvite } from "../../taskmasterApi";

function CreateInvite({ teamId, open, onClose }) {
  const [inviteDuration, setInviteDuration] = useState(24); // Default duration in hours
  const [inviteLink, setInviteLink] = useState("");
  const [openSnackbar, setOpenSnackbar] = useState(false);
  const [snackbarMessage, setSnackbarMessage] = useState("");

  const handleGenerateInvite = () => {
    generateInvite(teamId, inviteDuration)
      .then((data) => {
        const inviteURL = `https://taskmaster-8ien.com/team/join/${data.inviteCode}`;
        setInviteLink(inviteURL);
        setSnackbarMessage("Invite link generated successfully!");
        setOpenSnackbar(true);
      })
      .catch((err) => {
        console.error("Error generating invite link:", err);
        setSnackbarMessage("Error generating invite link.");
        setOpenSnackbar(true);
      });
  };

  const handleCopyInviteLink = () => {
    navigator.clipboard.writeText(inviteLink).then(() => {
      setSnackbarMessage("Invite link copied to clipboard!");
      setOpenSnackbar(true);
    });
  };
  const handleClose = () => {
    setInviteLink(""); // Clear the invite link
    onClose(); // Call the parent-provided onClose handler
  };

  return (
    <>
      <Dialog open={open} onClose={handleClose} sx={{ mt: 2, minWidth: "300px" }}>
        <DialogTitle>Generate Invite Link</DialogTitle>
        <DialogContent>
          <TextField
            margin="dense"
            label="Invite Duration (hours)"
            type="number"
            value={inviteDuration}
            onChange={(e) => setInviteDuration(e.target.value)}
            fullWidth
            variant="standard"
          />
          {inviteLink && (
            <Box sx={{ mt: 2, minWidth: "300px" }}>
              <Typography variant="body1" sx={{ mb: 1 }}>
                Invite link:
              </Typography>
              <Typography
                variant="body2"
                sx={{
                  wordWrap: "break-word",
                  mb: 2,
                  p: 1,
                  bgcolor: "#f5f5f5",
                  borderRadius: 1,
                  color: "black"
                }}
              >
                {inviteLink}
              </Typography>
              <Button
                variant="outlined"
                color="primary"
                onClick={handleCopyInviteLink}
                fullWidth
              >
                Copy Link
              </Button>
            </Box>
          )}
        </DialogContent>
        <DialogActions>
          <Button onClick={onClose}>Close</Button>
          <Button
            onClick={handleGenerateInvite}
            variant="contained"
            color="primary"
          >
            Generate
          </Button>
        </DialogActions>
      </Dialog>
      <Snackbar
        open={openSnackbar}
        message={snackbarMessage}
        autoHideDuration={2000}
        onClose={() => setOpenSnackbar(false)}
      />
    </>
  );
}

export default CreateInvite;
