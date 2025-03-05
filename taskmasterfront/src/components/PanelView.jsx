import React, { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { Box, Button, TextField } from "@mui/material";
import ListBlocks from './ListBlocks';
import CreateBlock from './CreateBlock';
import { fetchPanels, updatePanelName, handleAddBlock } from '../../taskmasterApi';

function PanelView() {
    const { panelid } = useParams();
    const navigate = useNavigate();
    const [panel, setPanel] = useState(null); 
    const [blocks, setBlocks] = useState([]);
    const [newPanelName, setNewPanelName] = useState('');
    const [error, setError] = useState(null);

    useEffect(() => {
        fetchPanels(panelid)
            .then((data) => {
                setPanel(data.panel); 
                setBlocks(data.panel.blocks); 
                setNewPanelName(data.panel.name); 
            })
            .catch((err) => setError(err.message));
    }, [panelid]);

    const addNewBlock = (newBlock, panelId) => {
        handleAddBlock({ ...newBlock, panelId })
            .then((addedBlock) => {
                setBlocks((prevBlocks) => [...prevBlocks, addedBlock]);
            })
            .catch((err) => {
                console.error("Error adding block:", err);
                setError("Error adding block");
            });
    };

    const handleUpdatePanelName = () => {
        if (!newPanelName.trim()) {
            setError("Panel name cannot be empty");
            return;
        }

        updatePanelName(panelid, { name: newPanelName }) // Call to update panel name
            .then(() => {
                setPanel(prev => ({ ...prev, name: newPanelName })); // Update local state after successful update
            })
            .catch((err) => {
                console.error("Error updating panel:", err);
                setError("Error updating panel");
            });
    };

    return (
        <div>
            <h1>Panel View</h1>
            {error && <p style={{ color: 'red' }}>{error}</p>}
            
            {/* Panel Name Edit */}
            <Box sx={{ mb: 2 }}>
                <TextField 
                    label="Panel Name"
                    variant="outlined" 
                    value={newPanelName} 
                    onChange={(e) => setNewPanelName(e.target.value)} 
                    fullWidth
                />
                <Button variant="contained" color="primary" onClick={handleUpdatePanelName} sx={{ mt: 1 }}>
                    Save Panel Name
                </Button>
            </Box>

            <ListBlocks blocks={blocks} />

            <Box>
                <CreateBlock createBlock={(newBlock) => addNewBlock(newBlock, panelid)} />
            </Box>
        </div>
    );
}

export default PanelView;