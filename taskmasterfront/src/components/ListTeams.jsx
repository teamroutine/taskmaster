import React, { useState, useEffect } from "react";
import { useLocation } from "react-router-dom";
import {
  Paper,
  Box,
  Typography,
  Divider,
  Button,
  MenuItem,
  Snackbar,
} from "@mui/material";
import {
  fetchMyTeams,
  handleAddPanel,
  handleAddTeam,
  handleAddBlock,
  deleteTeam,
} from "../../taskmasterApi";
import ListPanels from "./ListPanels";
import CreatePanel from "./CreatePanel";
import CreateTeam from "./CreateTeam";
import ViewTeam from "./ViewTeam";
import DropDown from "./DropDown";
import JoinTeamDialog from "./JoinTeamDialog";
import CreateInvite from "./CreateInvite"; // Import CreateInvite component

function ListTeams({ username }) {
  const [teams, setTeams] = useState([]);
  const [openSnackbar, setOpenSnackbar] = useState(false);
  const [snackbarMessage, setSnackbarMessage] = useState("");
  const [selectedTeam, setSelectedTeam] = useState(null);
  const [openViewTeam, setOpenViewTeam] = useState(false);
  const [openInviteModal, setOpenInviteModal] = useState(false); // State for invite modal
  const [selectedTeamId, setSelectedTeamId] = useState(null); // State for selected team ID
  const [joinDialogOpen, setJoinDialogOpen] = useState(false);

  useEffect(() => {
    fetchMyTeams()
      .then((data) => {
        setTeams(data || []);
      })
      .catch((err) => {
        console.error("Error fetching teams: " + err.message);
      });
  }, []);

  const location = useLocation();

  useEffect(() => {
    if (location.state?.message) {
      setSnackbarMessage(location.state.message);
      setOpenSnackbar(true);
    }
  }, [location.state]);

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
        }).then((addedBlock) => {
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

  const handleDeleteTeam = (teamId) => {
    if (
      window.confirm(
        "Are you sure you want to delete this team and all its contents?"
      )
    ) {
      deleteTeam(teamId)
        .then(() => {
          setTeams((prevTeams) =>
            prevTeams.filter((team) => team.teamId !== teamId)
          );

          setSnackbarMessage("Team deleted successfully!");
          setOpenSnackbar(true);
        })
        .catch((err) => {
          console.error("Error deleting team:", err);
          setSnackbarMessage("Error deleting team.");
          setOpenSnackbar(true);
        });
    }
  };

  return (
    <>
      <Box sx={{ mt: 2, pt: 0, mb: 2 }}>
        <Typography variant="h4" sx={{ marginBottom: 2, marginTop: 2 }}>
          Your Teams
        </Typography>
        {username && (

          <Box sx={{ display: "flex", alignItems: "center", gap: 1, marginBottom: 2 }}>
            <CreateTeam createTeam={createTeam} />
            <Button
              variant="contained"
              onClick={() => setJoinDialogOpen(true)}
              sx={{ marginLeft: '2%', fontSize: '0.7em', padding: '0.4em 0.8em' }}
            >
              Join Team
            </Button>
          </Box>


        )}
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
                  width: 240,
                  minHeight: 465,
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
                      fontSize: '0.7em'
                    }}
                    variant="h6"
                  >
                    {team.teamName}
                  </Typography>
                  <Typography
                    sx={{
                      textAlign: "left",
                      opacity: 0.7,
                      fontSize: "0.7em",
                      marginLeft: 1,
                      marginBottom: 1
                    }}
                  >
                    {team.createdBy === username && " (Owner)"}
                  </Typography>
                  <DropDown>
                    <MenuItem>
                      <Button
                        variant="contained"
                        color="primary"
                        sx={{ width: "100%", fontSize: '0.7em' }}
                        onClick={() => handleInviteUsers(team.teamId)} // Open invite modal
                      >
                        Invite Users
                      </Button>
                    </MenuItem>
                    <MenuItem>
                      <Button
                        variant="contained"
                        color="info"
                        sx={{ width: "100%", fontSize: '0.7em' }}
                        onClick={() => handleTeamClick(team)}
                      >
                        Info
                      </Button>
                    </MenuItem>
                    <MenuItem>
                      <Button
                        variant="contained"
                        color="error"
                        sx={{ width: "100%", fontSize: '0.7em' }}
                        onClick={() => handleDeleteTeam(team.teamId)}
                        disabled={team.createdBy !== username}
                      >
                        Delete Team
                      </Button>
                    </MenuItem>
                  </DropDown>
                </Box>
                <Divider />
                <Box sx={{ p: 1, flexGrow: 1, overflowY: "auto" }}>
                  <ListPanels panels={team.panels} setTeams={setTeams} />
                </Box>
                <Box>
                  <Divider sx={{ marginBottom: 2 }} />

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

      <JoinTeamDialog
        open={joinDialogOpen}
        onClose={() => setJoinDialogOpen(false)}
        onSuccess={() => {
          fetchMyTeams().then((data) => {
            setTeams(data || []);
          });
        }}
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
