import React, { useState, useEffect } from 'react';
import { useParams } from 'react-router-dom';
import ListBlocks from './ListBlocks';
import CreateBlock from './CreateBlock';
import { Box, Alert, Snackbar, Typography } from "@mui/material";
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
                    setBlocks(panel.blocks);
                } else {
                    console.error("Panel not found!");
                    setBlocks([]);
                }
            })
            .catch((err) => setError(err.message));
    }, [panelid]);

    const addNewBlock = (newBlock, panelId) => {
        handleAddBlock({ ...newBlock, panelId })
            .then((addedBlock) => {
                setBlocks((prevBlocks) => [...prevBlocks, addedBlock]);
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

    // Filter blocks based on search query
    const filteredBlocks = blocks
        .map(block => {
            // Check if tickets exist and filter them
            const filteredTickets = block.tickets?.filter(ticket =>
                ticket.ticketName.toLowerCase().includes(searchQuery) ||
                ticket.description.toLowerCase().includes(searchQuery)
            ) || [];

            if (block.blockName.toLowerCase().includes(searchQuery) || filteredTickets.length > 0) {
                return { ...block, tickets: filteredTickets }; // If query is similar to blocks or tickets, return a copy of the block/blocks and ticket/tickets
            }

            return null;
        })
        .filter(block => block !== null); // Delete null values, so only the blocks with query hits remain

    return (
        <Box sx={{ marginTop: 0 }} >
            <Box>
                <Typography variant="h4" component="h1" sx={{ marginTop: 2 }}>
                    {panelNameData}
                </Typography>
                <Typography variant="body1" component="h3" sx={{}}>
                    {descriptionData}
                </Typography>
            </Box>
            {error && <Alert severity="error">{error}</Alert>}
            <Box>
                <SearchBar searchQuery={searchQuery} setSearchQuery={setSearchQuery} />
            </Box>
            <Box sx={{ marginTop: 1 }}>
                <CreateBlock
                    createBlock={(newBlock) => addNewBlock(newBlock, panelid)}
                    existingBlockNames={blocks.map(block => block.blockName.toLowerCase())}
                />
            </Box>
            <Box sx={{ marginTop: 1 }}>
                <ListBlocks blocks={filteredBlocks} setBlocks={setBlocks} />
            </Box>

            <Snackbar
                open={openSnackbar}
                message={snackbarMessage}
                autoHideDuration={2000}
                onClose={() => setOpenSnackbar(false)}
            />
        </Box>
    );
}

export default PanelView;
