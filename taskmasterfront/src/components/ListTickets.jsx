import React, { useState, useRef } from "react";
import { draggable } from "@atlaskit/pragmatic-drag-and-drop/element/adapter";
import Paper from "@mui/material/Paper";
import Box from "@mui/material/Box";
import Typography from "@mui/material/Typography";
import Divider from "@mui/material/Divider";
import ViewTicket from "./ViewTicket";
import EditTicket from "./EditTicket";
import { deleteTicket } from "../../taskmasterApi";
import { Snackbar } from '@mui/material';


export default function ListTickets({ tickets, setBlocks }) {
    const [selectedTicket, setSelectedTicket] = useState(null);
    const [openView, setOpenView] = useState(false);
    const [openEdit, setOpenEdit] = useState(false);
    const [openSnackbar, setOpenSnackbar] = useState(false)
    const [snackbarMessage, setSnackbarMessage] = useState('');

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

    return (
        <>
            <Box component="ul" sx={{ padding: 0, margin: 0, listStyleType: "none" }}>
                {tickets.map(ticket => (
                    <Box
                        component="li"
                        key={ticket.ticketId}
                        sx={{ marginBottom: 1 }}
                        ref={(el) => {
                            if (el && !registeredElements.current.has(el)) {
                                registeredElements.current.add(el); // ✅ Prevent duplicate registration
                                draggable({
                                    element: el,
                                    getInitialData: () => ({
                                        type: "ticket",
                                        ticketId: ticket.ticketId,
                                    }),
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
                            }}
                            onClick={() => handleTicketClick(ticket)}
                        >
                            <Typography  sx={{
                                wordWrap: "break-word",
                                overflow: "hidden",
                                textOverflow:'ellipsis'
                            }}
                            onClivariant="body1">{ticket.ticketName}</Typography>
                            <Divider />
                            <Typography  sx={{
                                wordWrap: "break-word",
                                overflow: "hidden",
                                textOverflow:'ellipsis'
                            }} variant="body2">{ticket.description}</Typography>
                        </Paper>
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
