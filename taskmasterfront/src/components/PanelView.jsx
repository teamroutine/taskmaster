
import React from 'react';
import { useParams } from 'react-router-dom';
import ListBlocks from './ListBlocks';
import CreateBlock from './CreateBlock';
import Box from "@mui/material/Box";
import { handleAddBlock } from '../../taskmasterApi';

const PanelView = () => {
    const { panelid } = useParams();
    const {panels, setPanels} = useParams([]);

      const addNewBlock = (newBlock, panelId) => {
        handleAddBlock({ ...newBlock, panelId })
          .then((addedBlock) => {
            setPanels((prevPanels) =>
              prevPanels.map((panel) =>
                panel.panelId === panelId
                  ? { ...panel, blocks: [...panel.blocks, addedBlock] }
                  : panel
              )
            );
          })
          .catch((err) => {
            console.error("Error adding block:", err);
          });
      };
      


    return (
        <div>
            <h1>Panel View</h1>
            <ListBlocks panelid={panelid} />
            <Box>
                <CreateBlock createBlock={(newBlock) => addNewBlock(newBlock, panels.panelId)} />
            </Box>
        </div>
    );
};

export default PanelView;