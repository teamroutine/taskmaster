import React from 'react';
import { useState, useEffect } from 'react';
import { useParams } from 'react-router-dom';
import ListBlocks from './ListBlocks';
import CreateBlock from './CreateBlock';
import { Box, InputBase } from "@mui/material";
import { fetchPanels, handleAddBlock } from '../../taskmasterApi';
import { Search as SearchIcon } from '@mui/icons-material';
import { TextField } from '@mui/material';


function PanelView() {
    const { panelid } = useParams();
    const [blocks, setBlocks] = useState([]);
    const [error, setError] = useState(null);
    const [searchQuery, setSearchQuery] = useState("");


    useEffect(() => {
        fetchPanels(panelid)
            .then((data) => {
                setBlocks(data.panel.blocks);
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

    const handleSearchChange = (e) => {
        setSearchQuery(e.target.value);
    }

    return (
        <div>
            <h1>Panel View</h1>
            <Box
                sx={{
                    display: 'flex',
                    alignItems: 'center',
                    border: '1px solid #ccc', // Reunan väri
                    borderRadius: '4px', // Kulmien pyöristys
                    padding: '0 10px',
                    width: 300,
                }}
            >
                <SearchIcon sx={{ color: 'gray', marginRight: '8px' }} />
                <InputBase
                    sx={{
                        width: '100%',
                        color: 'black'
                    }}
                    placeholder="Search..."
                    value={searchQuery}
                    onChange={handleSearchChange}
                    autoFocus
                />
            </Box>
            <Box sx={{ marginTop: '10px' }}>
                <ListBlocks blocks={blocks} key={blocks.length} />
            </Box>
            <Box>
                <CreateBlock createBlock={(newBlock) => addNewBlock(newBlock, panelid)} />
            </Box>
        </div >
    );
}
export default PanelView;