import React from 'react';
import { Dialog, DialogActions, DialogContent, DialogTitle, Button, Paper, Typography, Box, TextField } from '@mui/material';

export default function ViewTicket({ ticket, open, onClose, handleDelete, onEditClick }) {

    return (
        <Dialog open={open} onClose={onClose}>
            <DialogTitle sx={{ padding: 2 }}>Ticket Details</DialogTitle>
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
                                style: { fontSize: '20px' }
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
                <Button sx={{ position: 'absolute', bottom: 10, left: 10, backgroundColor: '#D32F2F' }}
                    variant="contained"
                    onClick={() => handleDelete(ticket.ticketId)}
                >Delete</Button>
                <Button sx={{ position: 'absolute', top: 15, right: 10, backgroundColor: '#64B5F6' }} onClick={onEditClick} variant="contained" color="primary">
                    Edit
                </Button>
                <Button sx={{ backgroundColor: "#9E9E9E" }} variant="contained" onClick={onClose}>Close</Button>
            </DialogActions>
        </Dialog>
    );
}
