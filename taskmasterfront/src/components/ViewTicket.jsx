import React, { useState } from 'react';
import { Dialog, DialogActions, DialogContent, DialogTitle, Button, Paper, Typography, Box, MenuItem, TextField, Divider } from '@mui/material';
import DropDown from './DropDown';
import TagListView from './TagListView';
export default function ViewTicket({ ticket, open, onClose, handleDelete, onEditClick }) {
    const [tagModalOpen, setTagModalOpen] = useState(false);

    const formatDate = (dateString) => {
        const date = new Date(dateString);
        return date.toLocaleDateString("fi-FI");
    };

    return (
        <>
            <Dialog open={open} onClose={onClose}>
                <DialogTitle
                    sx={{
                        padding: 2,
                        display: "flex",
                        justifyContent: "space-between",
                        alignItems: "center"
                    }}
                >
                    <Typography variant="h5" >
                        Ticket Details
                    </Typography>
                    <DropDown>
                        <MenuItem>
                            <Button sx={{ backgroundColor: '#64B5F6' }} onClick={onEditClick} variant="contained" color="primary">
                                Edit
                            </Button>
                        </MenuItem>
                        <MenuItem>
                            <Button sx={{ backgroundColor: '#D32F2F' }} variant="contained" onClick={() => handleDelete(ticket.ticketId)}>
                                Delete
                            </Button>
                        </MenuItem>
                    </DropDown>
                </DialogTitle>
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
                        <Box mb={3} sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
                            <Typography variant="h5">{ticket.ticketName}</Typography>
                            <Box sx={{ textAlign: 'right' }}>
                                <Typography variant="body2" color="textSecondary">Due Date</Typography>
                                <Typography variant="body1" sx={{ color: 'textPrimary', fontSize: '1em' }}>
                                    {formatDate(ticket.dueDate)}
                                </Typography>
                            </Box>
                        </Box>

                        <Box mb={3}>
                            <TextField
                                label="Description"
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
                                justifyContent: 'space-between',
                                alignItems: 'center'
                            }}
                        >
                            <Typography variant="body2" color="textSecondary">
                                Created on: {formatDate(ticket.created)}
                            </Typography>
                            <Button
                                variant="outlined"
                                size="small"
                                sx={{ ml: 2 }}
                                onClick={() => setTagModalOpen(true)}
                            >
                                Manage Tags
                            </Button>
                            <DialogActions sx={{ display: "flex", justifyContent: "flex-start" }} >

                                <Button sx={{ fontSize: '0.7em' }} variant="contained" color="secondary" onClick={onClose}>Close</Button>
                            </DialogActions>
                        </Box>
                    </Paper>
                </DialogContent>

            </Dialog>

            {tagModalOpen && (
                <TagListView
                    ticket={ticket}
                    open={tagModalOpen}
                    onClose={() => setTagModalOpen(false)}
                />
            )}
        </>
    );
}
