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
        setTicket({ ticketName: "", description: "" });
        handleClose();
    }

    return (
        <>
            {/*Button for adding new Ticket*/}
            <Button variant='contained' color='success' onClick={handleClickOpen}>
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
                        label='Ticket name'
                        value={ticket.ticketName}
                        onChange={e => setTicket({ ...ticket, ticketName: e.target.value })}
                        fullWidth
                        variant='standard'
                    />
                    <TextField
                        margin='dense'
                        label='Ticket description'
                        value={ticket.description}
                        onChange={e => setTicket({ ...ticket, description: e.target.value })}
                        fullWidth
                        variant='standard'
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