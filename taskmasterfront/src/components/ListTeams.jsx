import React, { useState, useEffect } from "react";
import { Paper, Box, Typography, Divider } from "@mui/material";
import { fetchTeams, handleAddPanel } from "../../taskmasterApi";
import ListPanels from "./ListPanels";
import CreatePanel from "./CreatePanel";
import ViewTeam from "./ViewTeam";

function ListTeams() {
    const [teams, setTeams] = useState([]);
    const [openSnackbar, setOpenSnackbar] = useState(false);
    const [snackbarMessage, setSnackbarMessage] = useState('');
    const [selectedTeam, setSelectedTeam] = useState(null);
    const [openViewTeam, setOpenViewTeam] = useState(false);

    useEffect(() => {
        fetchTeams()
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
            })
            .catch((err) => {
                console.error("Error adding panel:", err);
                setSnackbarMessage("Error adding panel");
                setOpenSnackbar(true);
            });
    };

    return (
        <>
            <h1 sx={{ marginBottom: 2 }}>Your Teams</h1>
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
                                        justifyContent:"space-between",
                                        alignItems: "center",
                                    }}
                                >
                                <Typography
                                 sx={{
                                    whiteSpace: "nowrap",
                                    overflow: "hidden",
                                    textOverflow: "ellipsis",
                                    maxWidth: "200px",
                                    marginBottom:1,
                                    cursor:'pointer'
                                  }}
                                    variant="h6"
                                    onClick={() => handleTeamClick(team)}
                                >
                                    {team.teamName}
                                </Typography>
                                </Box>
                                <Divider />
                                <Box sx={{ p: 1, flexGrow: 1, overflowY: "auto"}}>
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
        </>
    );
}

export default ListTeams;
