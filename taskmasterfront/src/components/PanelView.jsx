import React, { useState, useEffect } from 'react';
import { useParams } from 'react-router-dom';
import ListBlocks from './ListBlocks';
import CreateBlock from './CreateBlock';
import { Box, InputBase, Alert } from "@mui/material";
import { Search as SearchIcon } from '@mui/icons-material';
import { fetchPanels, handleAddBlock } from '../../taskmasterApi';

function PanelView() {
    const { panelid } = useParams();
    const [blocks, setBlocks] = useState([]);
    const [error, setError] = useState(null);
    const [searchQuery, setSearchQuery] = useState("");

    // Fetch panel data
    useEffect(() => {
        fetchPanels(panelid)
            .then((data) => {
                console.log("Fetched panel data:", data);
                const panel = data.find(p => p.panelId === Number(panelid));

                if (panel) {
                    console.log("Found panel:", panel);
                    setBlocks(panel.blocks);
                } else {
                    console.error("Panel not found!");
                    setBlocks([]); // Tyhjennetään, jos panelia ei löydy
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

    // Handle search change
    const handleSearchChange = (e) => {
        setSearchQuery(e.target.value.toLowerCase());
    };

    // Filter blocks based on search query
    const filteredBlocks = blocks
        .map(block => {
            // Check if tickets exist and filter them safely
            const filteredTickets = block.tickets?.filter(ticket =>
                ticket.ticketName.toLowerCase().includes(searchQuery) ||
                ticket.description.toLowerCase().includes(searchQuery)
            ) || []; // Fallback to empty array if tickets is undefined or null

            if (block.blockName.toLowerCase().includes(searchQuery) || filteredTickets.length > 0) {
                return { ...block, tickets: filteredTickets };
            }

            return null;
        })
        .filter(block => block !== null);

    return (
        <div>
            <h1>Panel View</h1>
            {error && <Alert severity="error">{error}</Alert>}

            <Box
                sx={{
                    display: 'flex',
                    alignItems: 'center',
                    border: '1px solid #ccc',
                    borderRadius: '4px',
                    padding: '0 10px',
                    width: '300px',
                }}
            >
                <SearchIcon sx={{ color: 'gray', marginRight: '8px' }} />
                <InputBase
                    sx={{
                        width: '100%',
                        color: 'black'
                    }}
                    placeholder="Search..."
                    value={searchQuery} // Set current value
                    onChange={handleSearchChange} // Update the state while searching
                />
            </Box>

            <Box sx={{ marginTop: '10px' }}>
                <ListBlocks blocks={filteredBlocks} />
            </Box>

            <Box>
                <CreateBlock createBlock={(newBlock) => addNewBlock(newBlock, panelid)} />
            </Box>
        </div >
    );
}

export default PanelView;
