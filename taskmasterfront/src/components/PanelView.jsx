import React from 'react';
import { useState, useEffect } from 'react';
import { useParams } from 'react-router-dom';
import ListBlocks from './ListBlocks';
import CreateBlock from './CreateBlock';
import Box from "@mui/material/Box";
import { fetchPanels, handleAddBlock } from '../../taskmasterApi';

function PanelView() {
    const { panelid } = useParams();
    const [blocks, setBlocks] = useState([]);
    const [error, setError] = useState(null);


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