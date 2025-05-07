import { useState, useEffect, useRef } from "react";
import { useParams } from "react-router-dom";
import {
  deleteBlock,
  handleAddTicket,
  updateTicket,
  handleReorderTickets,
  handleReorderBlocks,
} from "../../taskmasterApi.js";
import Paper from "@mui/material/Paper";
import Box from "@mui/material/Box";
import { Button, MenuItem, Snackbar } from "@mui/material";
import Typography from "@mui/material/Typography";
import ListTickets from "./ListTickets.jsx";
import Divider from "@mui/material/Divider";
import CreateTicket from "./CreateTicket.jsx";
import EditBlock from "./EditBlock.jsx";
import DropDown from "./DropDown.jsx";

import { draggable } from "@atlaskit/pragmatic-drag-and-drop/element/adapter";
import { dropTargetForElements } from "@atlaskit/pragmatic-drag-and-drop/element/adapter";
import { extractClosestEdge } from "@atlaskit/pragmatic-drag-and-drop-hitbox/closest-edge";
import { reorder } from "@atlaskit/pragmatic-drag-and-drop/reorder";
import { getReorderDestinationIndex } from "@atlaskit/pragmatic-drag-and-drop-hitbox/util/get-reorder-destination-index";

function ListBlocks({ blocks, setBlocks }) {
  const { panelid } = useParams();
  const [selectedBlock, setSelectedBlock] = useState(null);
  const [open, setOpen] = useState(false);
  const blockRefs = useRef(new Map());
  const registeredBlocks = useRef(new Set());
  const [openSnackbar, setOpenSnackbar] = useState(false);
  const [snackbarMessage, setSnackbarMessage] = useState("");

  useEffect(() => {
    console.log("Updated blocks state:", blocks);
  }, [blocks]);

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
      "Are you sure you want to delete this block and all the tickets it contains?"
    );
    if (confirmed) {
      deleteBlock(blockId)
        .then(() => {
          setBlocks((prevBlocks) =>
            prevBlocks.filter((block) => block.blockId !== blockId)
          );
          setSnackbarMessage("Block deleted successfully");
          setOpenSnackbar(true); //Opens snackbar to show success message
        })
        .catch((err) => {
          console.error("Error deleting block:", err);
          setSnackbarMessage("Error deleting block");
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
          setSnackbarMessage("Ticket added successfully");
          setOpenSnackbar(true);
          return updatedBlocks;
        });
      })
      .catch((err) => {
        console.error("Error adding ticket:", err);
        setSnackbarMessage("Error adding ticket");
        setOpenSnackbar(true);
      });
  };

  const moveBlockLeft = (blockId) => {
    setBlocks((prevBlocks) => {
      const index = prevBlocks.findIndex((block) => block.blockId === blockId);
      const reorderedBlocks = reorder({
        list: prevBlocks,
        startIndex: index,
        finishIndex: index - 1, // Ensure the index doesn't go below 0
      });

      // Sync with backend
      handleReorderBlocks(panelid, reorderedBlocks)
        .then(() => {
          const updatedBlocks = reorderedBlocks.map((Block, index) => ({
            ...Block,
            sortOrder: index,
          }));
          console.log("Blocks reordered successfully" + updatedBlocks);
          setBlocks(updatedBlocks);
        })
        .catch((err) => {
          console.error("Error reordering blocks:", err);
        });

      return reorderedBlocks;
    });
  };

  const moveBlockRight = (blockId) => {
    setBlocks((prevBlocks) => {
      const index = prevBlocks.findIndex((block) => block.blockId === blockId);
      const reorderedBlocks = reorder({
        list: prevBlocks,
        startIndex: index,
        finishIndex: index + 1, // Ensure the index doesn't exceed the last position
      });
      console.log(reorderedBlocks);
      // Sync with backend
      handleReorderBlocks(panelid, reorderedBlocks)
        .then(() => {
          const updatedBlocks = reorderedBlocks.map((Block, index) => ({
            ...Block,
            sortOrder: index,
          }));
          console.log("Blocks reordered successfully" + updatedBlocks);
          setBlocks(updatedBlocks);
        })
        .catch((err) => {
          console.error("Error reordering blocks:", err);
        });

      return reorderedBlocks;
    });
  };

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
          if (block.tickets?.length === 0) {
            // Handle drop only if the block is empty
            if (source.data.type === "ticket") {
              const sourceTicketId = source.data.ticketId;
              console.log(
                `Adding ticket ${sourceTicketId} to empty block ${block.blockId}`
              );
              moveTicket(sourceTicketId, null, block.blockId, null); // No targetTicketId or closestEdge needed
            }
          }
        },
      });
    });
  }, [blocks]);

  const moveTicket = (ticketId, targetTicketId, targetBlockId, closestEdge) => {
    setBlocks((prevBlocks) => {
      let movedTicket = null;

      // Remove ticket from its original block
      const updatedBlocks = prevBlocks.map((block) => {
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

      // Add ticket to the target block and reorder
      const finalBlocks = updatedBlocks.map((block) => {
        if (block.blockId === targetBlockId && movedTicket) {
          const targetTickets = block.tickets ?? [];
          // Handle empty block case
          if (targetTickets.length === 0) {
            const updatedTickets = [{ ...movedTicket, sortOrder: 0 }];

            // Sync with backend
            updateTicket(ticketId, { block: { blockId: targetBlockId } })
              .then(() => {
                reorderTickets(targetBlockId, updatedTickets);
              })
              .catch((error) => {
                console.error("Failed to update ticket:", error);
              });

            return { ...block, tickets: updatedTickets };
          }

          // Find the index of the target ticket
          const targetIndex = targetTickets.findIndex(
            (ticket) => ticket.ticketId === targetTicketId
          );

          // Calculate the destination index
          const destinationIndex = getReorderDestinationIndex({
            startIndex: targetTickets.length, // Assume it's being added at the end initially
            indexOfTarget: targetIndex,
            closestEdgeOfTarget: closestEdge,
            axis: "vertical",
          });

          const reorderedTickets = reorder({
            list: [...targetTickets, movedTicket], // Add the moved ticket to the list
            startIndex: targetTickets.length, // Moved ticket is added at the end
            finishIndex: destinationIndex, // Move it to the correct position
          });

          // Update sortOrder for all tickets in the target block
          const updatedTickets = reorderedTickets.map((ticket, index) => ({
            ...ticket,
            sortOrder: index,
          }));

          updateTicket(ticketId, { block: { blockId: targetBlockId } })
            .then(() => {
              reorderTickets(targetBlockId, updatedTickets);
            })
            .catch((error) => {
              console.error("Failed to update ticket:", error);
            });

          return { ...block, tickets: updatedTickets };
        }
        return block;
      });

      return finalBlocks;
    });
  };

  const reorderTickets = (blockId, reorderedTickets) => {
    handleReorderTickets(blockId, reorderedTickets)
      .then(() => {
        console.log("Tickets reordered successfully");
      })
      .catch((err) => {
        console.error("Error reordering tickets:", err);
      });
  };

  return (
    <Box sx={{ whiteSpace: "nowrap" }}>
      <Box
        component="ul"
        sx={{
          display: "inline-block",
          padding: 0,
          margin: 0,
          listStyleType: "none",
        }}
      >
        {blocks
          .slice()
          .sort((a, b) => a.sortOrder - b.sortOrder)
          .map((block) => (
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
                  height: 600,
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
                  <DropDown>
                    <MenuItem>
                      <Button
                        variant="contained"
                        color="primary"
                        onClick={() => handleOpen(block)}
                        disabled={block.blockName === "Done"}
                      >
                        Edit Block
                      </Button>
                    </MenuItem>
                    <MenuItem>
                      <Button
                        variant="contained"
                        color="error"
                        onClick={() => handleBlockDelete(block.blockId)}
                        disabled={block.blockName === "Done"}
                      >
                        Delete Block
                      </Button>
                    </MenuItem>
                    <MenuItem>
                      <Button
                        variant="contained"
                        color="secondary"
                        onClick={() => moveBlockLeft(block.blockId)}
                        disabled={block.sortOrder === 0} // Disable if it's the first block
                      >
                        Move Left
                      </Button>
                    </MenuItem>
                    <MenuItem>
                      <Button
                        variant="contained"
                        color="secondary"
                        onClick={() => moveBlockRight(block.blockId)}
                        disabled={block.sortOrder === blocks.length - 1} // Disable if it's the last block
                      >
                        Move Right
                      </Button>
                    </MenuItem>
                  </DropDown>
                </Box>
                <Divider />
                <Box sx={{ p: 1, flexGrow: 1, overflowY: "auto" }}>
                  <ListTickets
                    tickets={block.tickets}
                    setBlocks={setBlocks}
                    blockId={block.blockId}
                    reorderTickets={reorderTickets}
                    moveTicket={moveTicket}
                  />
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
  );
}

export default ListBlocks;