import React, { useState } from 'react';
import Paper from '@mui/material/Paper';
import Box from '@mui/material/Box';
import Typography from '@mui/material/Typography';
import Divider from '@mui/material/Divider';
import ViewTicket from './ViewTicket';
import EditTicket from './EditTicket';
import { deleteTicket } from '../../taskmasterApi';

export default function ListTickets({ tickets, setBlocks }) {

    const [selectedTicket, setSelectedTicket] = useState(null);
    const [openView, setOpenView] = useState(false);
    const [openEdit, setOpenEdit] = useState(false);

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
                            tickets: block.tickets.filter(t => t.ticketId !== ticketId)
                        }))
                    );
                    setOpenView(false);
                })
                .catch((err) => {
                    console.error("Failed to delete ticket:", err);
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
                )
            }))
        );
    };

    return (
        <>
            <Box component="ul" sx={{ padding: 0, margin: 0, listStyleType: 'none' }}>
                {tickets.map(ticket => (
                    <Box component="li" key={ticket.ticketId} sx={{ marginBottom: 1 }}>
                        {/*WordWrap and Overflow makes the text not go over the borders */}
                        <Paper elevation={2} sx={{ padding: 3, cursor: 'pointer', wordWrap: 'break-word', overflow: 'hidden' }} onClick={() => handleTicketClick(ticket)}>
                            <Typography variant="body1">{ticket.ticketName}</Typography>
                            <Divider />
                            <Typography variant="body2">{ticket.description}</Typography>
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
        </>
    );
}
