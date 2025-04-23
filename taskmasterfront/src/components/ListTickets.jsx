import React, { useState, useRef } from "react";
import { draggable } from "@atlaskit/pragmatic-drag-and-drop/element/adapter";
import { dropTargetForElements } from "@atlaskit/pragmatic-drag-and-drop/element/adapter";
import {
  attachClosestEdge,
  extractClosestEdge,
} from "@atlaskit/pragmatic-drag-and-drop-hitbox/closest-edge";
import { DropIndicator } from "@atlaskit/pragmatic-drag-and-drop-react-drop-indicator/box";
import { reorder } from "@atlaskit/pragmatic-drag-and-drop/reorder";
import { getReorderDestinationIndex } from "@atlaskit/pragmatic-drag-and-drop-hitbox/util/get-reorder-destination-index";
import Paper from "@mui/material/Paper";
import Box from "@mui/material/Box";
import Typography from "@mui/material/Typography";
import Divider from "@mui/material/Divider";
import ViewTicket from "./ViewTicket";
import EditTicket from "./EditTicket";
import { Snackbar } from "@mui/material";
import { deleteTicket } from "../../taskmasterApi";

export default function ListTickets({
  tickets,
  setBlocks,
  blockId,
  reorderTickets,
  moveTicket,
}) {
  const [selectedTicket, setSelectedTicket] = useState(null);
  const [openView, setOpenView] = useState(false);
  const [openEdit, setOpenEdit] = useState(false);
  const [openSnackbar, setOpenSnackbar] = useState(false);
  const [snackbarMessage, setSnackbarMessage] = useState("");
  const [closestEdges, setClosestEdges] = useState({});

  const setClosestEdgeForTicket = (ticketId, edge) => {
    setClosestEdges((prev) => ({
      ...prev,
      [ticketId]: edge,
    }));
  };

  const registeredElements = useRef(new WeakSet());

  const handleTicketClick = (ticket) => {
    setSelectedTicket(ticket);
    setOpenView(true);
  };

  const handleEditClick = () => {
    setOpenEdit(true);
    setOpenView(false);
  };

  const handleClose = () => {
    setOpenView(false);
    setOpenEdit(false);
  };

  const calculateDaysUntilDue = (dueDateString) => {
    const dueDate = new Date(dueDateString);
    const today = new Date();

    const timeLeft = dueDate - today;
    //Converting milliseconds to days
    const daysLeft = Math.ceil(timeLeft/(1000 * 60 * 60 *24));

    return daysLeft;
  }
  const getTicketColor = (dueDateString) => {
    const remainingDays = calculateDaysUntilDue(dueDateString)
    if ( remainingDays <= 0 ){
      return "rgba(255, 0, 0, 0.65)";
    } else if ( remainingDays <= 3 ){
      return "rgba(255, 87, 0, 0.7)";
    } else if (remainingDays <= 7){
      return "rgba(255, 223, 0, 0.7)";
    }else{
      return null;
    }
  }

  const handleDelete = (ticketId) => {
    const confirmed = window.confirm("Are you sure you want to delete this ticket?");
    if (confirmed) {
      deleteTicket(ticketId)
        .then(() => {
          setBlocks(prevBlocks =>
            prevBlocks.map(block => ({
              ...block,
              tickets: block.tickets.filter(t => t.ticketId !== ticketId),
            }))
          );
          setOpenView(false);
          setSnackbarMessage('Ticket deleted successfully');
          setOpenSnackbar(true);      //Opens snackbar to show success message
        })
        .catch((err) => {
          console.error("Failed to delete ticket:", err);
          setSnackbarMessage('Error deleting ticket');
          setOpenSnackbar(true);
        });
    }
  };

  const handleSaveEdit = (updatedTicket) => {
    setBlocks(prevBlocks =>
      prevBlocks.map(block => ({
        ...block,
        tickets: block.tickets.map(ticket =>
          ticket.ticketId === selectedTicket.ticketId
            ? { ...ticket, ...updatedTicket }
            : ticket
        ),
      }))
    );
  };



  const handleDrop = ({ source, self }) => {
    if (source.data.type === "ticket") {
      const sourceTicketId = source.data.ticketId;
      
      const targetTicketId = self.data.ticketId;

      // Determine the closest edge of the target ticket
      const closestEdge = extractClosestEdge(self.data);

      // If the ticket is being moved to a different block
      if (source.data.blockId !== blockId) {
        console.log(
          `Moving ticket ${sourceTicketId} from block ${source.data.blockId} to block ${blockId}`
        );
        moveTicket(sourceTicketId, targetTicketId, blockId, closestEdge);
      } else {
        // Reorder tickets within the same block
        console.log(
          `Reordering ticket ${sourceTicketId} within block ${blockId}`
        );
        handleReorder(sourceTicketId, targetTicketId, closestEdge);
      }
    }

    setClosestEdgeForTicket(self.data.ticketId, null);
  };

  const handleReorder = (sourceTicketId, targetTicketId, closestEdge) => {
    console.log("handleReorder called with:", {
      sourceTicketId,
      targetTicketId,
      closestEdge,
    });
    setBlocks((prevBlocks) => {
      console.log("prevBlocks:", prevBlocks); // Log prevBlocks here

      return prevBlocks.map((block) => {
        console.log(
          "Checking block:",
          block.blockId,
          "against blockId:",
          blockId
        );

        if (block.blockId !== blockId) return block;

        const sourceIndex = block.tickets.findIndex(
          (t) => t.ticketId === sourceTicketId
        );
        const targetIndex = block.tickets.findIndex(
          (t) => t.ticketId === targetTicketId
        );

        console.log("Source Index:", sourceIndex, "Target Index:", targetIndex);

        const destinationIndex = getReorderDestinationIndex({
          startIndex: sourceIndex,
          indexOfTarget: targetIndex,
          closestEdgeOfTarget: closestEdge,
          axis: "vertical",
        });

        console.log("Destination Index:", destinationIndex);

        const reorderedTickets = reorder({
          list: block.tickets,
          startIndex: sourceIndex,
          finishIndex: destinationIndex,
        });

        // Update sortOrder for each ticket
        const updatedTickets = reorderedTickets.map((ticket, index) => ({
          ...ticket,
          sortOrder: index,
        }));

        console.log(
          "Reordered Tickets with Updated SortOrder:",
          updatedTickets
        );

        console.log("Reordered Tickets:", reorderedTickets);
        reorderTickets(blockId, reorderedTickets);

        return { ...block, tickets: updatedTickets };
      });
    });
  };

  return (
    <>
      <Box component="ul" sx={{ padding: 0, margin: 0, listStyleType: "none", minHeight: "100%", // Ensure the ul has a minimum height
    minWidth: "100%", }}
    >
        {tickets
          .slice() // Create a shallow copy to avoid mutating the original array
          .sort((a, b) => a.sortOrder - b.sortOrder) // Sort tickets by sortOrder
          .map((ticket) => (
            <Box
              component="li"
              key={ticket.ticketId}
              sx={{
                marginBottom: 1,
                position: "relative", // Ensure relative positioning for DropIndicator
              }}
              ref={(el) => {
                if (el && !registeredElements.current.has(el)) {
                  registeredElements.current.add(el);
                  draggable({
                    element: el,
                    getInitialData: () => ({
                      type: "ticket",
                      ticketId: ticket.ticketId,
                      blockId,
                    }),
                  });
                  dropTargetForElements({
                    element: el,
                    getData: ({ input, element }) => {
                      if (!input || !element) {
                        console.error(
                          "Input or element is missing in getData!"
                        );
                        return null;
                      }

                      const data = {
                        type: "ticket",
                        ticketId: ticket.ticketId,
                        blockId,
                      };

                      // Attach closest edge data
                      return attachClosestEdge(data, {
                        input,
                        element,
                        allowedEdges: ["top", "bottom"],
                        hitboxPadding: 20,
                      });
                    },
                    onDragEnter: (args) => {
                      if (args.source.data.ticketId !== ticket.ticketId) {
                        const edge = extractClosestEdge(args.self.data);
                        console.log("Closest edge on drag enter:", edge);
                        setClosestEdgeForTicket(ticket.ticketId, edge);
                      }
                    },
                    onDrag: (args) => {
                      if (args.source.data.ticketId !== ticket.ticketId) {
                        const edge = extractClosestEdge(args.self.data);
                        console.log("Closest edge on drag:", edge);
                        setClosestEdgeForTicket(ticket.ticketId, edge);
                      }
                    },
                    onDragLeave: () => {
                      console.log("Drag left ticket:", ticket.ticketId);
                      setClosestEdgeForTicket(ticket.ticketId, null);
                    },
                    onDrop: handleDrop,
                    /*onDrop: ({ source, self }) => {
                    
                    if (source.data.type === "ticket") {
                      const sourceTicketId = source.data.ticketId;
                      const targetTicketId = self.data.ticketId;

                      // Get the index of the source and target tickets
                      const sourceIndex = tickets.findIndex(
                        (t) => t.ticketId === sourceTicketId
                      );
                      const targetIndex = tickets.findIndex(
                        (t) => t.ticketId === targetTicketId
                      );

                      // Determine the closest edge of the target ticket
                      const closestEdge = extractClosestEdge(self.data);

                      // Calculate the destination index
                      const destinationIndex = getReorderDestinationIndex({
                        startIndex: sourceIndex,
                        indexOfTarget: targetIndex,
                        closestEdgeOfTarget: closestEdge,
                        axis: "vertical", // Assuming a vertical list
                      });

                      console.log(
                        "Reordering ticket:",
                        sourceTicketId,
                        "From index:",
                        sourceIndex,
                        "to index:",
                        destinationIndex
                      );

                      handleReorder(
                        sourceTicketId,
                        targetTicketId,
                        closestEdge
                      );
                    }

                    setClosestEdgeForTicket(ticket.ticketId, null);
                  },*/
                  });
                }
              }}
            >
              <Paper
                elevation={2}
                sx={{
                  padding: 3,
                  cursor: "grab",
                  wordWrap: "break-word",
                  overflow: "hidden",
                  backgroundColor: getTicketColor(ticket.dueDate)
                }}
                onClick={() => handleTicketClick(ticket)}
              >
                <Typography
                  sx={{
                    wordWrap: "break-word",
                    overflow: "hidden",
                    textOverflow: "ellipsis",
                  }}
                  variant="body1"
                >
                  {ticket.ticketName}
                </Typography>
                <Divider />
                <Typography
                  sx={{
                    wordWrap: "break-word",
                    overflow: "hidden",
                    textOverflow: "ellipsis",
                  }}
                  variant="body2"
                >
                  {ticket.description}
                </Typography>
              </Paper>
              {closestEdges[ticket.ticketId] && (
                <DropIndicator
                  edge={closestEdges[ticket.ticketId]}
                  gap="8px"
                  style={{
                    backgroundColor: "rgba(0, 123, 255, 0.5)",
                    height: "4px",
                    width: "100%",
                  }}
                />
              )}
            </Box>
          ))}
      </Box>

      {selectedTicket && (
        <ViewTicket
          ticket={selectedTicket}
          open={openView}
          onClose={handleClose}
          handleDelete={handleDelete}
          onEditClick={handleEditClick}
        />
      )}
      {selectedTicket && (
        <EditTicket
          open={openEdit}
          ticket={selectedTicket}
          onClose={handleClose}
          onSave={handleSaveEdit}
        />
      )}
      <Snackbar
        open={openSnackbar}
        message={snackbarMessage}
        autoHideDuration={2000}
        onClose={() => setOpenSnackbar(false)}
      />
    </>
  );
}
