import React, { useState, useEffect } from 'react';
import { Dialog,Typography, DialogActions, DialogContent, DialogTitle, Button, TextField,Paper, Box } from '@mui/material';
import { updateTicket } from '../../taskmasterApi';

export default function EditTicket({ open, ticket, onClose, onSave }) {
    const [ticketData, setTicketData] = useState({
        ticketName: '',
        description: '',
    });

    useEffect(() => { 
            setTicketData({
                ticketName: ticket.ticketName,
                description: ticket.description,
            });
        
    }, [ticket]);

    const handleSave = () => {
        updateTicket(ticket.ticketId, ticketData)
            .then(() => {
                onSave(ticketData);
                onClose();
            })
            .catch((err) => {
                console.error("Error updating ticket:", err);
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

                            <TextField  sx={{ marginTop: 1, borderRadius: 2, fontSize:"20px"}}
                                margin="dense"
                                label ="Ticket Name"
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
                    </Paper>
                </DialogContent>

                <DialogActions sx={{ display: 'flex', justifyContent: 'space-between', padding: 2 }}>
                    <Button onClick={onClose} variant="outlined">
                        Cancel
                    </Button>
                    <Button onClick={handleSave} color="primary" variant="contained">
                        Save
                    </Button>
                </DialogActions>

            </Dialog>

        </>
    );

}