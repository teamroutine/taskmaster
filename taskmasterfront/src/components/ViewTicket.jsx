import React, { useState } from 'react';
import { Dialog, DialogActions, DialogContent, DialogTitle, Button, Paper, Typography, Box, MenuItem, TextField } from '@mui/material';
import DropDown from './DropDown';
import TagListView from './TagListView';
export default function ViewTicket({ ticket, open, onClose, handleDelete, onEditClick }) {
    const [tagModalOpen, setTagModalOpen] = useState(false);


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
                            justifyContent: 'space-between',
                            alignItems: 'center'
                        }}
                    >
                        <Typography variant="body2" color="textSecondary">
                            Created on: {ticket.created}
                        </Typography>
                        <Button
                            variant="outlined"
                            size="small"
                            sx={{ ml: 2 }}
                            onClick={() => setTagModalOpen(true)}
                        >
                            Manage Tags
                        </Button>
                    </Box>
                </Paper>
            </DialogContent>
            <DialogActions sx={{ display: "flex", justifyContent: "flex-start" }} >

                <Button sx={{ backgroundColor: "#9E9E9E" }} variant="contained" onClick={onClose}>Close</Button>
            </DialogActions>
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
