import React, { useState, useEffect } from 'react';
import { useParams } from 'react-router-dom';
import ListBlocks from './ListBlocks';
import CreateBlock from './CreateBlock';
import { Box, Alert, Snackbar } from "@mui/material";
import { fetchPanels, handleAddBlock } from '../../taskmasterApi';
import SearchBar from './SearchBar';

function PanelView() {
    const { panelid } = useParams();
    const [blocks, setBlocks] = useState([]);
    const [panelNameData, setPanelNameData] = useState(null);
    const [descriptionData, setDescriptionData] = useState(null);
    const [error, setError] = useState(null);
    const [searchQuery, setSearchQuery] = useState("");

    const [openSnackbar, setOpenSnackbar] = useState(false);
    const [snackbarMessage, setSnackbarMessage] = useState('');

    // Fetch panel data
    useEffect(() => {
        fetchPanels(panelid)
            .then((data) => {
                const panel = data.find(p => p.panelId === Number(panelid));
    
                if (panel) {
                    setPanelNameData(panel.panelName);
                    setDescriptionData(panel.description);
                    
                    
                    const sortedBlocks = panel.blocks.sort((a, b) => {
                        if (a.blockName === "Done") return 1;  
                        if (b.blockName === "Done") return -1; 
                        return -1;  
                    });
    
                    setBlocks(sortedBlocks);
                } else {
                    console.error("Panel not found!");
                    setBlocks([]); 
                }
            })
            .catch((err) => setError(err.message));
    }, [panelid]);
    

    // Add new block
    const addNewBlock = (newBlock, panelId) => {
        handleAddBlock({ ...newBlock, panelId })
            .then((addedBlock) => {
                setBlocks((prevBlocks) => [addedBlock, ...prevBlocks]);
                setSnackbarMessage('Block added successfully!');
                setOpenSnackbar(true);
            })
            .catch((err) => {
                console.error("Error adding block:", err);
                setError("Error adding block");
                setSnackbarMessage('Error adding block.');
                setOpenSnackbar(true);
            });
    };

    // Update query when user writes in the InputBase


    // Filter blocks based on search query
    const filteredBlocks = blocks
        .map(block => {
            // Check if tickets exist and filter them
            const filteredTickets = block.tickets?.filter(ticket =>
                ticket.ticketName.toLowerCase().includes(searchQuery) ||
                ticket.description.toLowerCase().includes(searchQuery)
            ) || []; // Fallback to empty array if tickets is undefined or null

            if (block.blockName.toLowerCase().includes(searchQuery) || filteredTickets.length > 0) {
                return { ...block, tickets: filteredTickets }; // If query is similar to blocks or tickets, return a copy of the block/blocks and ticket/tickets
            }

            return null; // If blocks don't have similar tickets as the query, return null
        })
        .filter(block => block !== null); // Delete null values, so only the blocks with query hits remain

    return (
        <div>
            <h1>{panelNameData}</h1>
            <h3>{descriptionData}</h3>
            {error && <Alert severity="error">{error}</Alert>}
            {/* Search bar component for the frontend */}
            <Box>
                <SearchBar searchQuery={searchQuery} setSearchQuery={setSearchQuery} />
            </Box>
            {/*ListBlock component uses data filtered by filteredBLocks() */}
            <Box sx={{ marginTop: '10px' }}>
                <ListBlocks blocks={filteredBlocks} setBlocks={setBlocks} />
            </Box>
            {/*CreateBlock for creating a new Block */}
            <Box>
                <CreateBlock 
                    createBlock={(newBlock) => addNewBlock(newBlock, panelid)} 
                    existingBlockNames={blocks.map(block =>block.blockName.toLowerCase())}
                />
            </Box>
            <Snackbar
                open={openSnackbar}
                message={snackbarMessage}
                autoHideDuration={2000}
                onClose={() => setOpenSnackbar(false)}
            />
        </div >
    );
}

export default PanelView;
