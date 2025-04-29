import React, { useState, useEffect } from "react";
import { useLocation } from "react-router-dom";
import { Paper, Box, Typography, Divider, Button, MenuItem, Snackbar } from "@mui/material";
import { fetchMyTeams, handleAddPanel, handleAddTeam, handleAddBlock } from "../../taskmasterApi";
import ListPanels from "./ListPanels";
import CreatePanel from "./CreatePanel";
import CreateTeam from "./CreateTeam";
import ViewTeam from "./ViewTeam";
import DropDown from "./DropDown";
import CreateInvite from "./CreateInvite"; // Import CreateInvite component

function ListTeams({ username }) {
  const [teams, setTeams] = useState([]);
  const [openSnackbar, setOpenSnackbar] = useState(false);
  const [snackbarMessage, setSnackbarMessage] = useState("");
  const [selectedTeam, setSelectedTeam] = useState(null);
  const [openViewTeam, setOpenViewTeam] = useState(false);
  const [openInviteModal, setOpenInviteModal] = useState(false); // State for invite modal
  const [selectedTeamId, setSelectedTeamId] = useState(null); // State for selected team ID

  useEffect(() => {
    fetchMyTeams()
      .then((data) => {
        setTeams(data || []);
      })
      .catch((err) => {
        console.error("Error fetching teams: " + err.message);
      });
  }, []);

  const handleTeamClick = (team) => {
    setSelectedTeam(team);
    setOpenViewTeam(true);
  };

  const handleCloseViewTeam = () => {
    setSelectedTeam(null);
    setOpenViewTeam(false);
  };

  const handleInviteUsers = (teamId) => {
    setSelectedTeamId(teamId); // Set the selected team ID
    setOpenInviteModal(true); // Open the invite modal
  };

  const addNewPanel = (newPanel, teamId) => {
    handleAddPanel({ ...newPanel, teamId })
      .then((addedPanel) => {
        setTeams((prevTeams) => {
          const updatedTeams = prevTeams.map((team) =>
            team.teamId === teamId
              ? { ...team, panels: [...(team.panels || []), addedPanel] }
              : team
          );
          setSnackbarMessage("Panel added successfully");
          setOpenSnackbar(true);
          return updatedTeams;
        });
        handleAddBlock({
          blockName: "Done",
          description: "Block for completed tickets",
          panelId: addedPanel.panelId,
        })
          .then((addedBlock) => {
            setTeams((prevTeams) =>
              prevTeams.map((team) =>
                team.teamId === teamId
                  ? {
                    ...team,
                    panels: team.panels.map((panel) =>
                      panel.panelId === addedPanel.panelId
                        ? {
                          ...panel,
                          blocks: [...(panel.blocks || []), addedBlock],
                        }
                        : panel
                    ),
                  }
                  : team
              )
            );
          });

      })
      .catch((err) => {
        console.error("Error adding panel:", err);
        setSnackbarMessage("Error adding panel");
        setOpenSnackbar(true);
      });
  };

  const createTeam = (newTeam) => {
    handleAddTeam(newTeam)
      .then((addedTeam) => {
        setTeams((prevTeams) => [...prevTeams, { ...addedTeam, panels: [] }]); // Initialize panels as an empty array
        setSnackbarMessage("Team created successfully");
        setOpenSnackbar(true);
      })
      .catch((err) => {
        console.error("Error creating team:", err);
        setSnackbarMessage("Error creating team");
        setOpenSnackbar(true);
      });
  };

  return (
    <>
      <Box>
        <h1 sx={{ marginBottom: 2 }}>Your Teams</h1>
        <CreateTeam createTeam={createTeam} />
      </Box>
      <Box sx={{ whiteSpace: "nowrap" }}>
        <Box
          component="ul"
          sx={{
            display: "inline-block",
            padding: 0,
            margin: 0,
            listStyleType: "none",
          }}
        >
          {teams.map((team) => (
            <Box
              component="li"
              key={team.teamId}
              sx={{ display: "inline-block", marginRight: 2 }}
            >
              <Paper
                elevation={5}
                sx={{
                  width: 300,
                  height: 800,
                  padding: 2,
                  textAlign: "center",
                  display: "flex",
                  flexDirection: "column",
                  justifyContent: "space-between",
                }}
              >
                <Box
                  sx={{
                    display: "flex",
                    justifyContent: "space-between",
                    alignItems: "center",
                  }}
                >
                  <Typography
                    sx={{
                      whiteSpace: "nowrap",
                      overflow: "hidden",
                      textOverflow: "ellipsis",
                      maxWidth: "200px",
                      marginBottom: 1,
                      cursor: "pointer",
                    }}
                    variant="h6"
                    onClick={() => handleTeamClick(team)}
                  >
                    {team.teamName}
                  </Typography>
                  <Typography
                    sx={{
                      textAlign: "left",
                      opacity: 0.7,
                      fontSize: "0.9rem",
                      marginLeft: 1,
                      marginBottom: 1,
                    }}
                  >
                    {team.createdBy === username && " (Owner)"}
                  </Typography>
                  <DropDown>
                    <MenuItem>
                      <Button
                        variant="contained"
                        color="primary"
                        onClick={() => handleInviteUsers(team.teamId)} // Open invite modal
                      >
                        Invite Users
                      </Button>
                    </MenuItem>
                  </DropDown>
                </Box>
                <Divider />
                <Box sx={{ p: 1, flexGrow: 1, overflowY: "auto" }}>
                  <ListPanels panels={team.panels} setTeams={setTeams} />
                </Box>
                <Box>
                  <Divider />
                  <CreatePanel
                    createPanel={(newPanel) =>
                      addNewPanel(newPanel, team.teamId)
                    }
                  />
                </Box>
              </Paper>
            </Box>
          ))}
        </Box>
      </Box>
      <ViewTeam
        team={selectedTeam}
        openView={openViewTeam}
        closeView={handleCloseViewTeam}
      />

      {/* Integrate CreateInvite Modal */}
      <CreateInvite
        teamId={selectedTeamId}
        open={openInviteModal}
        onClose={() => setOpenInviteModal(false)} // Close the modal
      />
      <Snackbar
        open={openSnackbar}
        autoHideDuration={2000}
        onClose={() => setOpenSnackbar(false)}
        message={snackbarMessage}
      />
    </>
  );
}

export default ListTeams;