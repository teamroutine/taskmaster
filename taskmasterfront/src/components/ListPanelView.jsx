import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import { fetchPanels, handleAddPanel } from '../../taskmasterApi';
import CreatePanel from './CreatePanel';
import EditPanel from './EditPanel';
import { Button, Paper, Box, Typography, Divider } from '@mui/material';

function ListPanelView() {
    const [panels, setPanels] = useState([]);
    const [error, setError] = useState(null);
    const [selectedPanel, setSelectedPanel] = useState(null);
    const [open, setOpen] = useState(false);

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
        } catch (err) {
            setError("Failed to create panel: " + err.message);
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
                            <Typography variant="h6">
                                <Link to={`/panels/${panel.panelId}`} style={{ textDecoration: "none", color: "inherit" }}>
                                    {panel.panelName || "Unnamed Panel"}
                                </Link>
                            </Typography>

                            <Divider sx={{ marginY: 1 }} />

                            <Box sx={{ display: "flex", justifyContent: "space-between", marginTop: 2 }}>
                                <Button variant="outlined" color="primary" onClick={() => handleOpenEdit(panel)}>
                                    Edit
                                </Button>
                            </Box>
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
        </Box>
    );
}

export default ListPanelView;