import React, { useState, useEffect } from 'react';
import { Snackbar, Dialog, Typography, DialogActions, DialogContent, DialogTitle, Button, TextField, Paper, Box, Divider } from '@mui/material';
import { updateTicket } from '../../taskmasterApi';

export default function EditTicket({ open, ticket, onClose, onSave }) {
    const [ticketData, setTicketData] = useState({
        ticketName: '',
        description: '',
        dueDate:'',
    });

    const [openSnackbar, setOpenSnackbar] = useState(false);
    const [snackbarMessage, setSnackbarMessage] = useState('');

    useEffect(() => {
        setTicketData({
            ticketName: ticket.ticketName,
            description: ticket.description,
            dueDate: ticket.dueDate
        });

    }, [ticket]);

    const handleSave = () => {
        updateTicket(ticket.ticketId, ticketData)
            .then(() => {
                onSave(ticketData);
                onClose();
                setSnackbarMessage('Ticket updated successfully!');
                setOpenSnackbar(true); // Shows success message
            })
            .catch((err) => {
                console.error("Error updating ticket:", err);
                setSnackbarMessage('Error updating ticket.');
                setOpenSnackbar(true); // Shows error message
            });
    };



    return (
        <>
            <Dialog open={open} onClose={onClose}>
                <DialogTitle>Edit Ticket</DialogTitle>
                <DialogContent>
                    <Paper
                        elevation={3}
                        sx={{
                            padding: 2,
                            width: 500,
                            height: "auto",
                            display: 'flex',
                            flexDirection: 'column',
                            justifyContent: 'flex-start',
                            wordWrap: 'break-word',
                            overflow: 'hidden'
                        }}
                    >
                        <Box mb={3}>

                            <TextField sx={{ marginTop: 1, borderRadius: 2, fontSize: "20px" }}
                                margin="dense"
                                label="Ticket Name"
                                value={ticketData.ticketName}
                                onChange={e => setTicketData({ ...ticketData, ticketName: e.target.value })}
                                fullWidth
                                variant="standard"
                                InputProps={{
                                    style: { fontSize: '20px' }
                                }}

                            />
                        </Box>
                        
                        <Box mb={3}>
                            <TextField
                                margin="dense"
                                label="Description"
                                value={ticketData.description}
                                onChange={e => setTicketData({ ...ticketData, description: e.target.value })}
                                fullWidth
                                multiline
                                rows={6}
                                variant="outlined"
                                InputProps={{
                                    style: { fontSize: '20px' }
                                }}
                                sx={{ marginTop: 1, borderRadius: 2, }}
                            />
                        </Box>

                        <Box mb={3}>
                            <TextField
                                margin="dense"
                                label="Due Date"
                                type='date'
                                value={ticketData.dueDate}
                                onChange={e => setTicketData({ ...ticketData, dueDate: e.target.value })}
                                fullWidth
                                variant="outlined"
                                InputProps={{
                                    style: { fontSize: '20px' }
                                }}
                            />
                        </Box>
                    </Paper>
                </DialogContent>

                <DialogActions sx={{ display: 'flex', justifyContent: 'space-between', padding: 2 }}>
                    <Button sx={{ backgroundColor: "#9E9E9E" }} onClick={onClose} variant="contained">
                        Cancel
                    </Button>
                    <Button onClick={handleSave} variant="contained" sx={{ backgroundColor: "#4CAF50" }}>
                        Save
                    </Button>
                </DialogActions>


            </Dialog>
            <Snackbar
                open={openSnackbar}
                message={snackbarMessage}
                autoHideDuration={2000}
                onClose={() => setOpenSnackbar(false)}
            />

        </>
    );

}