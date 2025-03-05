import React, { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { Box, Button, TextField } from "@mui/material";
import ListBlocks from './ListBlocks';
import CreateBlock from './CreateBlock';
import Box from "@mui/material/Box";
import { fetchPanels, handleAddBlock } from '../../taskmasterApi';

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

    return (
        <div>
            <h1>Panel View</h1>
            <ListBlocks blocks={blocks} key={blocks.length} />
            <Box>
                <CreateBlock createBlock={(newBlock) => addNewBlock(newBlock, panelid)} />
            </Box>
        </div >
    );
}

export default PanelView;