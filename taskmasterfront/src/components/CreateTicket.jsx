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
        dueDate: new Date().toISOString().split('T')[0],//initializinf with today's date
    })

    // Open new Ticket modal
    const handleClickOpen = () => {
        setOpen(true);
    };

    // Close new Ticket modal
    const handleClose = () => {
        setOpen(false);
    };

    // Save new Ticket information 
    const handleSave = () => {
        createTicket(ticket);
        setTicket({ ticketName: "", description: "", dueDate: new Date().toISOString().split('T')[0] });
        handleClose();
    }

    return (
        <>
            {/*Button for adding new Ticket*/}
            <Button variant='contained' color='success' onClick={handleClickOpen} sx={{ marginTop: 2, fontSize: '0.5em' }}>
                Add Ticket
            </Button>
            <Dialog
                open={open}
                onClose={handleClose}
            >
                {/*Modal for all the mandatory atrributes for new Ticket */}
                <DialogTitle>Add new Ticket</DialogTitle>
                <DialogContent>
                    <TextField
                        margin='dense'
                        placeholder='Ticket Name'
                        value={ticket.ticketName}
                        onChange={e => setTicket({ ...ticket, ticketName: e.target.value })}
                        fullWidth
                        variant='outlined'
                        InputLabelProps={{
                            shrink: true, // label above input
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
                            shrink: true, // Ensures the label is above the input
                        }}
                    />
                </DialogContent>
                <DialogActions>
                    <Button onClick={handleClose}>Close</Button> {/*Button for closing modal */}
                    <Button onClick={handleSave}>Save</Button>  {/*Button for saving Ticket information */}
                </DialogActions>
            </Dialog>
        </>
    );

}