import React from 'react';
import { Dialog, DialogActions, DialogContent, DialogTitle, Button, Paper, Typography, Box } from '@mui/material';

export default function ViewTicket({ ticket, open, onClose }) {
    return (
        <Dialog open={open} onClose={onClose}>
            <DialogTitle>View Ticket Details</DialogTitle>
            <DialogContent>
                <Paper elevation={3} sx={{width:400, height:300, textAlign: "center", justifyContent: "space-between", wordWrap: 'break-word', overflow: 'hidden'}}>
                    <Box mb={3}>
                        <Typography variant="h6">Ticket Name:</Typography>
                        <Typography variant="body1">{ticket.ticketName}</Typography>
                    </Box>

                    <Box mb={3}>
                        <Typography variant="h6">Description:</Typography>
                        <Typography variant="body2">{ticket.description}</Typography>
                    </Box>

                    <Box mb={3}>
                        <Typography variant="h6">Created on:</Typography>
                        <Typography variant="body2">{ticket.created}</Typography>
                    </Box>
                </Paper>
            </DialogContent>
            <DialogActions>
                <Button onClick={onClose}>Close</Button>
            </DialogActions>
        </Dialog>
    );
}
