import React from 'react';
import { Dialog, DialogActions, DialogContent, DialogTitle, Button, Paper, Typography, Box, TextField } from '@mui/material';

export default function ViewTicket({ ticket, open, onClose, handleDelete }) {

    return (
        <Dialog open={open} onClose={onClose}>
            <DialogTitle>Ticket Details</DialogTitle>
            <DialogContent>
                <Paper
                    elevation={3}
                    sx={{
                        padding: 2,
                        width: 500,
                        height: 300,
                        display: 'flex',
                        flexDirection: 'column',
                        justifyContent: 'flex-start',
                        wordWrap: 'break-word',
                        overflow: 'hidden'
                    }}
                >
                    <Box mb={3}>
                        <Typography variant="h5">{ticket.ticketName}</Typography>
                    </Box>

                    <Box mb={3}>
                        <Typography variant="h6">Description:</Typography>
                        <TextField
                            value={ticket.description}
                            multiline
                            rows={6}
                            fullWidth
                            variant="outlined"
                            InputProps={{
                                readOnly: true,
                            }}
                            sx={{
                                marginTop: 1,
                                borderRadius: 2,
                            }}
                        />
                    </Box>

                    <Box
                        sx={{
                            display: 'flex',
                            justifyContent: 'flex-end',
                            alignItems: 'center'
                        }}
                    >
                        <Typography variant="body2" color="textSecondary">
                            Created on: {ticket.created}
                        </Typography>
                    </Box>
                </Paper>
            </DialogContent>
            <DialogActions>
                <Button sx={{ position: 'absolute', bottom: 10, left: 10, }}
                    color="error"
                    variant="outlined"
                    onClick={() => handleDelete(ticket.ticketId)}
                >Delete</Button>
                <Button variant="outlined" onClick={onClose}>Close</Button>
            </DialogActions>
        </Dialog>
    );
}
