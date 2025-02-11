
import React from 'react';
import { useState, useEffect } from 'react';
import { useParams } from 'react-router-dom';
import ListBlocks from './ListBlocks';
import CreateBlock from './CreateBlock';
import Box from "@mui/material/Box";
import { fetchPanels, handleAddBlock } from '../../taskmasterApi';

const PanelView = () => {
    const { panelid } = useParams();
    const [panel, setPanel] = useState();
    const [error, setError] = useState(null);

    useEffect(() => {
        fetch(`/api/panel/${panelid}`)
            .then(response => response.json())
            .then(data => setPanel(data))
            .catch(error => console.error("Error fetching panel:", error));
    }, [panelid]);

    const addNewBlock = (newBlock, panelId) => {
        handleAddBlock({ ...newBlock, panelId })
            .then((addedBlock) => {
                setPanel((prevPanel) => prevPanel
                    ? { ...prevPanel, blocks: [...prevPanel.blocks, addedBlock] }
                    : { blocks: [addedBlock] }

                );
            })
            .catch((err) => {
                console.error("Error adding block:", err);
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