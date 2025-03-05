import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { createPanel } from '../../taskmasterApi';
import Box from "@mui/material/Box";
import Button from "@mui/material/Button";
import TextField from "@mui/material/TextField";

function CreatePanel() {
    const [panelName, setPanelName] = useState('');
    const [error, setError] = useState(null);
    const navigate = useNavigate();

    const handleCreatePanel = async (e) => {
        e.preventDefault();
        if (!panelName.trim()) {
            setError("Panel name is required");
            return;
        }
        
        try {
            const newPanel = await createPanel({ name: panelName });
            navigate(`/panel/${newPanel.id}`); // Redirect to the new panel
        } catch (err) {
            console.error("Error creating panel:", err);
            setError("Failed to create panel");
        }
    };

    return (
        <Box className="create-panel-container">
            <h1>Create New Panel</h1>
            {error && <p className="error">{error}</p>}
            <form onSubmit={handleCreatePanel}>
                <TextField 
                    label="Panel Name" 
                    variant="outlined" 
                    fullWidth 
                    value={panelName} 
                    onChange={(e) => setPanelName(e.target.value)} 
                />
                <Button type="submit" variant="contained" color="primary" sx={{ mt: 2 }}>
                    Create Panel
                </Button>
            </form>
        </Box>
    );
}

export default CreatePanel;
