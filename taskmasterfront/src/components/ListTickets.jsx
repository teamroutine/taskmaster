import React, { useState } from 'react';
import Paper from '@mui/material/Paper';
import Box from '@mui/material/Box';
import Typography from '@mui/material/Typography';
import Divider from '@mui/material/Divider';
import ViewTicket from './ViewTicket';
import { deleteTicket } from '../../taskmasterApi';

export default function ListTickets({ tickets, setTickets }) {

    const [selectedTicket, setSelectedTicket] = useState(null);
    const [open, setOpen] = useState(false);


    const handleDelete = (ticketId) => {
        const confirmed = window.confirm("Are you sure you want to delete this ticket?");
        if (confirmed) {
            deleteTicket(ticketId)
                .then(() => {
                    setTickets(prevBlocks =>
                        prevBlocks.map(block => ({
                            ...block,
                            tickets: block.tickets.filter(t => t.ticketId !== ticketId)
                        }))
                    );
                    setOpen(false);
                })
                .catch((err) => {
                    console.error("Failed to delete ticket:", err);
                });
        }
    };

    const handleTicketClick = (ticket) => {
        setSelectedTicket(ticket);
        setOpen(true);
    };

    const handleClose = () => {
        setOpen(false);
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
                    open={open}
                    onClose={handleClose}
                    handleDelete={handleDelete}
                />
            )}
        </>
    );
}
