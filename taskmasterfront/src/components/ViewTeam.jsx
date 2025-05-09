import React from "react";
import { Dialog, DialogActions, DialogContent, DialogTitle, Button, Paper, Typography, Box, Divider } from "@mui/material";
import gravatar from "gravatar";

export default function ViewTeam({ team, openView, closeView }) {

  if (!team) return null;

  return (
    <Dialog open={openView} onClose={closeView}>
      <DialogTitle
        sx={{
          padding: 2,
          display: "flex",
          justifyContent: "space-between",
          alignItems: "center",
        }}
      >
        <Box>
          <Typography variant="h5">Team Details</Typography>
        </Box>
      </DialogTitle>
      <DialogContent>
        <Paper
          elevation={3}
          sx={{
            padding: 2,
            width: 500,
            height: "auto",
            display: "flex",
            flexDirection: "column",
            justifyContent: "flex-start",
            wordWrap: "break-word",
            overflow: "hidden",
          }}
        >
          <Box mb={2}>
            <Typography variant="h6">{team.teamName}</Typography>
          </Box>
          <Divider sx={{ marginBottom: 2 }} />

          <Box mb={2}>
            <Typography variant="h6">Description:</Typography>
            <Typography
              sx={{
                marginTop: 1,
                fontSize: "20px",
                borderRadius: 2,
                padding: 1,
                wordWrap: "break-word",
              }}
            >
              {team.description}
            </Typography>
            <Divider sx={{ marginBottom: 2 }} />
          </Box>
          <Box mb={2}>
            <Typography variant="h6">Members:</Typography>
            <Box sx={{ marginTop: 1 }}>
              {team.appUsers && team.appUsers.length > 0 ? (
                team.appUsers.map((user, index) => {
                  const formattedFirstName =
                    user.firstName.charAt(0).toUpperCase() +
                    user.firstName.slice(1);
                  const formattedLastName =
                    user.lastName.charAt(0).toUpperCase() +
                    user.lastName.slice(1);
                  const initials = `${user.firstName
                    .charAt(0)
                    .toUpperCase()}${user.lastName.charAt(0).toUpperCase()}`;
                  const gravatarUrl = gravatar.url(user.email, {
                    s: "40",
                    d: "initials",
                    initials: initials,
                  });
                  return (
                    <Box key={index}
                      sx={{
                        display: "flex",
                        alignItems: "center",
                        marginBottom: "8px",
                      }}>
                      <img
                        src={gravatarUrl}
                        alt="User Gravatar"
                        style={{
                          borderRadius: "50%",
                          height: "40px",
                          marginRight: "10px"
                        }}
                        onError={(e) => {
                          e.target.style.display = "none";
                        }} // Hide image if it fails to load
                      />
                      <Typography
                        key={index}
                        sx={{
                          fontSize: "16px",
                          padding: "4px 0",
                          wordWrap: "break-word",
                        }}
                      >
                        {formattedFirstName + " " + formattedLastName}
                      </Typography>
                    </Box>
                  );
                })
              ) : (
                <Typography
                  sx={{
                    fontSize: "16px",
                    fontStyle: "italic",
                    color: "gray",
                  }}
                >
                  No members found.
                </Typography>
              )}
            </Box>
          </Box>
        </Paper>
      </DialogContent>
      <DialogActions sx={{ justifyContent: "flex-start" }}>
        <Button
          sx={{ backgroundColor: "#9E9E9E" }}
          variant="contained"
          onClick={closeView}
        >
          Close
        </Button>
      </DialogActions>
    </Dialog>
  );
}
