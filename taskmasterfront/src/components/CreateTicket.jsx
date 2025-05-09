import { useState } from 'react';
import Button from '@mui/material/Button';
import TextField from '@mui/material/TextField';
import Dialog from '@mui/material/Dialog';
import DialogActions from '@mui/material/DialogActions';
import DialogContent from '@mui/material/DialogContent';
import DialogTitle from '@mui/material/DialogTitle';

export default function CreateTicket({ createTicket }) {

    const [open, setOpen] = useState(false);

    const [ticket, setTicket] = useState({
        ticketName: '',
        description: '',
        dueDate: new Date().toISOString().split('T')[0], // initializing with today's date
    })

    const handleClickOpen = () => {
        setOpen(true);
    };

    const handleClose = () => {
        setOpen(false);
    };

    const handleSave = () => {
        createTicket(ticket);
        setTicket({ ticketName: "", description: "", dueDate: new Date().toISOString().split('T')[0] });
        handleClose();
    }

    return (
        <>
            <Button variant='contained' color='success' onClick={handleClickOpen}>
                Add Ticket
            </Button>
            <Dialog
                open={open}
                onClose={handleClose}
            >
                <DialogTitle>Add new Ticket</DialogTitle>
                <DialogContent>
                    <TextField
                        margin='dense'
                        placeholder='Ticket Name'
                        value={ticket.ticketName}
                        onChange={e => setTicket({ ...ticket, ticketName: e.target.value })}
                        fullWidth
                        variant='outlined'
                        InputLabelProps={{  // label above input
                            shrink: true, 
                        }}
                    />
                    <TextField
                        margin='dense'
                        placeholder='Add a description'
                        value={ticket.description}
                        onChange={e => setTicket({ ...ticket, description: e.target.value })}
                        fullWidth
                        multiline
                        rows={3}
                        variant='outlined'
                        InputLabelProps={{
                            shrink: true, 
                        }}
                    />
                    <TextField
                        margin='dense'
                        label='Due Date'
                        type='date'
                        value={ticket.dueDate}
                        onChange={e => setTicket({ ...ticket, dueDate: e.target.value })}
                        fullWidth
                        variant='outlined'
                        InputLabelProps={{
                            shrink: true,
                        }}
                    />
                </DialogContent>
                <DialogActions>
                    <Button onClick={handleClose}>Close</Button>
                    <Button onClick={handleSave}>Save</Button>
                </DialogActions>
            </Dialog>
        </>
    );

}