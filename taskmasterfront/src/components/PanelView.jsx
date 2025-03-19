import React, { useState, useEffect } from 'react';
import { useParams } from 'react-router-dom';
import ListBlocks from './ListBlocks';
import CreateBlock from './CreateBlock';
import { Box, Alert } from "@mui/material";
import { fetchPanels, handleAddBlock } from '../../taskmasterApi';
import SearchBar from './SearchBar';

function PanelView() {
    const { panelid } = useParams();
    const [blocks, setBlocks] = useState([]);
    const [error, setError] = useState(null);
    const [searchQuery, setSearchQuery] = useState("");

    // Fetch panel data
    useEffect(() => {
        fetchPanels(panelid)
            .then((data) => {

                const panel = data.find(p => p.panelId === Number(panelid));

                if (panel) {
                    setBlocks(panel.blocks);
                } else {
                    console.error("Panel not found!");
                    setBlocks([]); // Clear blocks if the corresponding panel is not found
                }
            })
            .catch((err) => setError(err.message));
    }, [panelid]);

    // Add new block
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
                return { ...block, tickets: filteredTickets }; // If query is similar to blocks tickets, return a copy of the block/blocks
            }

            return null; // If blocks don't have similar tickets as the query, return null
        })
        .filter(block => block !== null); // Delete null values, so only the blocks with query hits remain

    return (
        <div>
            <h1>Panel View</h1>
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
                <CreateBlock createBlock={(newBlock) => addNewBlock(newBlock, panelid)} />
            </Box>
        </div >
    );
}

export default PanelView;
