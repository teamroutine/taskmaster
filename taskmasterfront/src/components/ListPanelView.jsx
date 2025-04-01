import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import { deletePanel, fetchPanels, handleAddPanel } from '../../taskmasterApi';
import CreatePanel from './CreatePanel';
import EditPanel from './EditPanel';
import { Button, Paper, Box, Typography, Divider, Snackbar, MenuItem} from '@mui/material';
import DropDown from './DropDown';

function ListPanelView() {
    const [panels, setPanels] = useState([]);
    const [error, setError] = useState(null);
    const [selectedPanel, setSelectedPanel] = useState(null);
    const [open, setOpen] = useState(false);
    const [openSnackbar, setOpenSnackbar] = useState(false);
    const [snackbarMessage, setSnackbarMessage] = useState('');

    useEffect(() => {
        fetchPanels()
            .then((data) => {
                console.log(data);
                setPanels(data || []);
            })
            .catch((err) => {
                setError("Error fetching panels: " + err.message);
            });
    }, []);

    const addNewPanel = async (newPanel) => {
        try {
            const createdPanel = await handleAddPanel(newPanel);
            setPanels(prevPanels => [...prevPanels, createdPanel]);
            setSnackbarMessage('Panel created successfully!');
            setOpenSnackbar(true);
        } catch (err) {
            setError("Failed to create panel: " + err.message);
            setSnackbarMessage('Error creating panel.');
            setOpenSnackbar(true);
        }
    };

    const handleOpenEdit = (panel) => {
        setSelectedPanel(panel);
        setOpen(true);
    };

    const handleCloseEdit = () => {
        setOpen(false);
    };

    const handleEditPanelSave = (updatedPanel) => {
        setPanels(prevPanels =>
            prevPanels.map(panel =>
                panel.panelId === selectedPanel.panelId
                    ? { ...panel, ...updatedPanel }
                    : panel
            )
        );
    };

    const handleDeletePanel = (panelId) => {
        const confirmed = window.confirm("Are you sure you want to delete this panel and all its contents?");
        if (confirmed) {
            deletePanel(panelId)
                .then(() => {
                    setPanels((prevPanels) =>
                        prevPanels.filter((panel) => panel.panelId !== panelId)
                    );
                    setSnackbarMessage('Panel deleted successfully!');
                    setOpenSnackbar(true);
                })
                .catch((err) => {
                    console.error("Error deleting panel:", err);
                    setSnackbarMessage('Error deleting panel.');
                    setOpenSnackbar(true);
                });
        }
    };

    return (
        <Box sx={{ padding: 3 }}>
            <Typography variant="h4" sx={{ marginBottom: 2 }}>All Panels</Typography>

            {error && <Typography color="error">{error}</Typography>}

            <Box sx={{ display: "flex", flexWrap: "wrap", gap: 2 }}>
                {panels.length === 0 ? (
                    <Typography>No panels available.</Typography>
                ) : (
                    panels.map((panel) => (
                        <Paper key={panel.panelId} elevation={5} sx={{ width: 300, padding: 2 }}>
                             <Box sx={{ display: "flex", justifyContent: "space-between", alignItems: "center" }}>
                                <Typography  sx={{ whiteSpace: "nowrap", overflow: "hidden",textOverflow: "ellipsis"}} variant="h6" >
                                    <Link to={`/panels/${panel.panelId}`} style={{ textDecoration: "none", color: "inherit" }}>
                                        {panel.panelName || "Unnamed Panel"}
                                    </Link>
                                </Typography>

                                {/* Dropdown on the right */}
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

                            <Typography variant="body2" color="textSecondary" sx={{ marginTop: 1, overflow: "hidden",textOverflow: "ellipsis"}}>
                                {panel.description || "No description available"}
                            </Typography>

                            <Divider sx={{ marginY: 1 }} />
                        </Paper>
                    ))
                )}
            </Box>

            <Box sx={{ marginTop: 3 }}>
                <CreatePanel createPanel={addNewPanel} />
            </Box>

            {selectedPanel && (
                <EditPanel
                    panel={selectedPanel}
                    onSave={handleEditPanelSave}
                    open={open}
                    onClose={handleCloseEdit}
                />
            )}
            <Snackbar
                open={openSnackbar}
                message={snackbarMessage}
                autoHideDuration={2000}
                onClose={() => setOpenSnackbar(false)}
            />
        </Box>
    );
}

export default ListPanelView;