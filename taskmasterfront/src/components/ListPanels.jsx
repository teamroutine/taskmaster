import React, { useState } from "react";
import { Paper, Box, Typography, Divider, Button, Snackbar, MenuItem } from "@mui/material";
import { Link } from "react-router-dom";
import { deletePanel } from "../../taskmasterApi";
import EditPanel from "./EditPanel";
import DropDown from "./DropDown";

export default function ListPanels({ panels, setTeams }) {
    const [selectedPanel, setSelectedPanel] = useState(null);
    const [openEdit, setOpenEdit] = useState(false);
    const [openSnackbar, setOpenSnackbar] = useState(false);
    const [snackbarMessage, setSnackbarMessage] = useState("");

    const handleOpenEdit = (panel) => {
        setSelectedPanel(panel);
        setOpenEdit(true);
    };

    const handleCloseEdit = () => {
        setOpenEdit(false);
    };

    const handleEditPanelSave = (updatedPanel) => {
        setTeams((prevTeams) =>
            prevTeams.map((team) => ({
                ...team,
                panels: team.panels.map((panel) =>
                    panel.panelId === selectedPanel.panelId ? { ...panel, ...updatedPanel } : panel
                ),
            }))
        );
        setSnackbarMessage("Panel updated successfully!");
        setOpenSnackbar(true);
    };

    const handleDeletePanel = (panelId) => {
        if (window.confirm("Are you sure you want to delete this panel and all its contents?")) {
            deletePanel(panelId)
                .then(() => {
                    setTeams((prevTeams) =>
                        prevTeams.map((team) => ({
                            ...team,
                            panels: team.panels.filter((panel) => panel.panelId !== panelId),
                        }))
                    );

                    setSnackbarMessage("Panel deleted successfully!");
                    setOpenSnackbar(true);
                })
                .catch((err) => {
                    console.error("Error deleting panel:", err);
                    setSnackbarMessage("Error deleting panel.");
                    setOpenSnackbar(true);
                });
        }
    };

    return (
        <>
            <Box sx={{ display: "flex", flexWrap: "wrap", gap: 2 }}>
                {panels.map((panel) => (
                    <Paper key={panel.panelId} elevation={5} sx={{ width: 300, padding: 2, display: "flex", flexDirection: "column", justifyContent: "space-between", overflow: "hidden", maxHeight: 80 }}>
                        <Box sx={{ display: "flex", justifyContent: "space-between", alignItems: "center" }}>
                            <Typography sx={{ whiteSpace: "nowrap", overflow: "hidden", textOverflow: "ellipsis", fontSize: '0.7em' }} variant="h6">
                                <Link to={`/panels/${panel.panelId}`} style={{ textDecoration: "none", color: "inherit" }}>
                                    {panel.panelName || "Unnamed Panel"}
                                </Link>
                            </Typography>

                            <DropDown>
                                <MenuItem>
                                    <Button variant="contained" color="primary" onClick={() => handleOpenEdit(panel)}>
                                        Edit Panel
                                    </Button>
                                </MenuItem>
                                <MenuItem>
                                    <Button variant="contained" color="error" onClick={() => handleDeletePanel(panel.panelId)}>
                                        Delete Panel
                                    </Button>
                                </MenuItem>
                            </DropDown>
                        </Box>

                        <Typography variant="body2" color="textSecondary" sx={{ marginTop: 1, overflow: "hidden", textOverflow: "ellipsis", textAlign: 'left', fontSize: '0.7em' }}>
                            {panel.description || "No description available"}
                        </Typography>

                        <Divider sx={{ marginY: 1 }} />
                    </Paper>
                ))}
            </Box>

            {selectedPanel && (
                <EditPanel panel={selectedPanel} onSave={handleEditPanelSave} open={openEdit} onClose={handleCloseEdit} />
            )}

            <Snackbar open={openSnackbar} message={snackbarMessage} autoHideDuration={2000} onClose={() => setOpenSnackbar(false)} />
        </>
    );
}
