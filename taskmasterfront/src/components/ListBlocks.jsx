
import { useState } from "react";
import { useParams } from "react-router-dom";
import { deleteBlock, handleAddTicket } from "../../taskmasterApi.js";
import Paper from "@mui/material/Paper";
import Box from "@mui/material/Box";
import { Button, MenuItem } from "@mui/material";
import Typography from "@mui/material/Typography";
import ListTickets from "./ListTickets.jsx";
import Divider from "@mui/material/Divider";
import CreateTicket from "./CreateTicket.jsx";
import EditBlock from "./EditBlock.jsx";
import DropDown from "./DropDown.jsx";

function ListBlocks({ blocks, setBlocks }) { // Blocks comes as a prop from PanelView

  const { panelid } = useParams();
  const [selectedBlock, setSelectedBlock] = useState(null);
  const [error, setError] = useState(null);
  const [open, setOpen] = useState(false);

  // Handles the Edit button opening
  const handleOpen = (block) => {
    setSelectedBlock(block);
    setOpen(true);
  };
  // Closes the modal after closing or saving
  const handleClose = () => {
    setOpen(false);
  };

  // Handle delete
  const handleBlockDelete = (blockId) => {
    const confirmed = window.confirm("Are you sure you want to delete block and all the tickets it contains?");
    if (confirmed) {
      deleteBlock(blockId)
        .then(() => {
          setBlocks((prevBlocks) =>
            prevBlocks.filter((block) => block.blockId !== blockId)
          );
        })
        .catch((err) => {
          console.error("Error deleting block:", err);
        });
    }
  }

  // Updates blocks in fronend after editing
  const handleEditBlockSave = (updatedBlock) => {
    setBlocks((prevBlocks) =>
      prevBlocks.map((block) =>
        block.blockId === selectedBlock.blockId
          ? { ...block, ...updatedBlock }
          : block
      )
    );
  };

  // Add new ticket function with the Blocks id
  const addNewTicket = (newTicket, blockId) => {
    handleAddTicket({ ...newTicket, blockId })
      .then((addedTicket) => {
        setBlocks((prevBlocks) => {
          const updatedBlocks = prevBlocks.map((block) =>
            block.blockId === blockId
              ? { ...block, tickets: [...block.tickets, addedTicket] }
              : block
          );
          return updatedBlocks;
        });
      })
      .catch((err) => {
        console.error("Error adding ticket:", err);
      });
  };


  if (error) {
    return (
      <Box>
        Error: {error}
        {panelid}
      </Box>
    );
  }

  return (
    <Box>
      {error && <Box>Error: {error}</Box>}
      <Box sx={{ overflowX: "auto", whiteSpace: "nowrap" }}>
        <Box
          component="ul"
          sx={{
            display: "inline-block",
            padding: 0,
            margin: 0,
            listStyleType: "none",
          }}
        >
          {blocks.map((block) => (
            <Box
              component="li"
              key={block.blockId}
              sx={{ display: "inline-block", marginRight: 2 }}
            >
              <Paper
                elevation={5}
                sx={{
                  width: 300,
                  height: 800,
                  padding: 2,
                  textAlign: "center",
                  display: "flex",
                  flexDirection: "column",
                  justifyContent: "space-between",
                }}
              >
                <Box sx={{ display: "flex", justifyContent: "space-between", alignItems: "center" }}>
                  <Typography sx={{ whiteSpace: "nowrap", overflow: "hidden", textOverflow: "ellipsis", maxWidth: "200px" }} variant="h6">
                    {block.blockName}</Typography>
                  <Divider></Divider>
                  <DropDown>
                    <MenuItem>
                      <Button variant="contained" color="primary" onClick={() => handleOpen(block)}>
                        Edit Block
                      </Button>
                    </MenuItem>
                    <MenuItem>
                      <Button variant="contained" color="error" onClick={() => handleBlockDelete(block.blockId)}>
                        Delete Block
                      </Button>
                    </MenuItem>
                  </DropDown>
                </Box>
                <Box sx={{ p: 1 }}>
                  <ListTickets tickets={block.tickets} setBlocks={setBlocks} />
                </Box>
                <Box>
                  <Divider />
                  <CreateTicket createTicket={(newTicket) => addNewTicket(newTicket, block.blockId)} />
                </Box>
              </Paper>
            </Box>
          ))}
        </Box>
      </Box>
      {selectedBlock && (
        <EditBlock
          block={selectedBlock}
          onSave={handleEditBlockSave}
          open={open}
          onClose={handleClose}
        />
      )}
    </Box>
  );
}


export default ListBlocks;