import { useState, useEffect, useRef } from "react";
import { useParams } from "react-router-dom";
import { deleteBlock, handleAddTicket, updateTicket } from "../../taskmasterApi.js";
import Paper from "@mui/material/Paper";
import Box from "@mui/material/Box";
import { Button, MenuItem, Snackbar } from "@mui/material";
import Typography from "@mui/material/Typography";
import ListTickets from "./ListTickets.jsx";
import Divider from "@mui/material/Divider";
import CreateTicket from "./CreateTicket.jsx";
import EditBlock from "./EditBlock.jsx";
import DropDown from "./DropDown.jsx";
import { dropTargetForElements } from "@atlaskit/pragmatic-drag-and-drop/element/adapter";

function ListBlocks({ blocks, setBlocks }) {
  const { panelid } = useParams();
  const [selectedBlock, setSelectedBlock] = useState(null);
  const [open, setOpen] = useState(false);
  const blockRefs = useRef(new Map());

  const [openSnackbar, setOpenSnackbar] = useState(false)
  const [snackbarMessage, setSnackbarMessage] = useState('');

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
    const confirmed = window.confirm(
      "Are you sure you want to delete block and all the tickets it contains?"
    );
    if (confirmed) {
      deleteBlock(blockId)
        .then(() => {
          setBlocks((prevBlocks) =>
            prevBlocks.filter((block) => block.blockId !== blockId)
          );
          setSnackbarMessage('Block deleted successfully');
          setOpenSnackbar(true);  //Opens snackbar to show success message
        })
        .catch((err) => {
          console.error("Error deleting block:", err);
          setSnackbarMessage('Error deleting block');
          setOpenSnackbar(true);
        });
    }
  };

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
              ? { ...block, tickets: [...(block.tickets || []), addedTicket] }
              : block
          );
          setSnackbarMessage('Ticket added successfully');
          setOpenSnackbar(true);
          return updatedBlocks;
        });
      })
      .catch((err) => {
        console.error("Error adding ticket:", err);
        setSnackbarMessage('Error adding ticket');
        setOpenSnackbar(true);
      });
  };


  const registeredBlocks = useRef(new Set());
  
  useEffect(() => {
    blocks.forEach((block) => {
      if (!blockRefs.current.has(block.blockId)) return;
      if (registeredBlocks.current.has(block.blockId)) return; // Avoid duplicate registration
  
      const el = blockRefs.current.get(block.blockId);
      if (!el) return;
  
      registeredBlocks.current.add(block.blockId); // Mark as registered
  
      dropTargetForElements({
        element: el,
        getData: () => ({ type: "block", blockId: block.blockId }),
        onDrop: ({ source }) => {
          console.log("Drop event received:", source.data);
          if (source.data.type === "ticket") {
            console.log("Moving ticket with ID:", source.data.ticketId, "to block", block.blockId);
            moveTicket(source.data.ticketId, block.blockId);
          } else {
            console.warn("Dropped item is not a ticket:", source.data);
          }
        },
      });
    });
  }, [blocks]);
  
  const moveTicket = (ticketId, targetBlockId) => {
    setBlocks((prevBlocks) => {
      let movedTicket = null;
  
      // Remove ticket from its original block
      const updatedBlocks = prevBlocks.map((block) => {
        // Ensure `tickets` is always treated as an array
        const tickets = block.tickets ?? []; // Default to empty array if null
  
        if (tickets.some((ticket) => ticket.ticketId === ticketId)) {
          movedTicket = tickets.find((ticket) => ticket.ticketId === ticketId);
          return {
            ...block,
            tickets: tickets.filter((ticket) => ticket.ticketId !== ticketId),
          };
        }
        return block;
      });
  
      // Add ticket to the target block
      const finalBlocks = updatedBlocks.map((block) => {
        if (block.blockId === targetBlockId && movedTicket) {
          return { ...block, tickets: [...(block.tickets ?? []), movedTicket] };
        }
        return block;
      });
  
      return finalBlocks;
    });
  
    // Sync with backend
    updateTicket(ticketId, { block: { blockId: targetBlockId } })
      .catch((error) => {
        console.error("Failed to update ticket:", error);
        // Optional: Implement rollback logic if needed
      });
  };

  return (
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
            ref={(el) => {
              if (el) blockRefs.current.set(block.blockId, el);
            }}
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
              <Box
                sx={{
                  display: "flex",
                  justifyContent: "space-between",
                  alignItems: "center",
                }}
              >
                <Typography
                  sx={{
                    whiteSpace: "nowrap",
                    overflow: "hidden",
                    textOverflow: "ellipsis",
                    maxWidth: "200px",
                  }}
                  variant="h6"
                >
                  {block.blockName}
                </Typography>
                <Divider />
                <DropDown>
                  <MenuItem>
                    <Button
                      variant="contained"
                      color="primary"
                      onClick={() => handleOpen(block)}
                    >
                      Edit Block
                    </Button>
                  </MenuItem>
                  <MenuItem>
                    <Button
                      variant="contained"
                      color="error"
                      onClick={() => handleBlockDelete(block.blockId)}
                    >
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
                <CreateTicket
                  createTicket={(newTicket) =>
                    addNewTicket(newTicket, block.blockId)
                  }
                />
              </Box>
            </Paper>
          </Box>
        ))}
      </Box>
      {selectedBlock && (
         <EditBlock
           block={selectedBlock}
           onSave={handleEditBlockSave}
           open={open}
           onClose={handleClose}
         />
       )}
      <Snackbar
        open={openSnackbar}
        message={snackbarMessage}
        autoHideDuration={2000}
        onClose={() => setOpenSnackbar(false)}
      />
    </Box>
  )

}

export default ListBlocks;
