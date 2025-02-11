
import React from 'react';
import { useState, useEffect } from 'react';
import { useParams } from 'react-router-dom';
import ListBlocks from './ListBlocks';
import CreateBlock from './CreateBlock';
import Box from "@mui/material/Box";
import { fetchPanels, handleAddBlock } from '../../taskmasterApi';

const PanelView = () => {
    const { panelid } = useParams();
    const [panel, setPanel] = useState(null);
    const [error, setError] = useState(null);

    useEffect(() => {
        fetchPanels(panelid)
            .then(data => setPanel(data))
            .catch(error => {
                console.error("Error fetching panel:", error);
                setError("Error fetching panel data");
            });
    }, [panelid]);

    const addNewBlock = (newBlock, panelId) => {
        handleAddBlock({ ...newBlock, panelId })
            .then(() => {
                fetchPanels(panelId)
                    .then(data => setPanel(data))
                    .catch((err) => {
                        console.error("Error fetching updated panel:", err);
                        setError("Error fetching updated panel");
                    });
            })
            .catch((err) => {
                console.error("Error adding block:", err);
                setError("Error adding block");
            });
    };


    return (
        <div>
            <h1>Panel View</h1>
            <ListBlocks blocks={panel?.blocks ?? []} />
            <Box>
                <CreateBlock createBlock={(newBlock) => addNewBlock(newBlock, panelid)} />
            </Box>
        </div>
    );
};

export default PanelView;