import { useState } from 'react';
import Button from '@mui/material/Button';
import TextField from '@mui/material/TextField';
import Dialog from '@mui/material/Dialog';
import DialogActions from '@mui/material/DialogActions';
import DialogContent from '@mui/material/DialogContent';
import DialogTitle from '@mui/material/DialogTitle';

export default function CreatePanel({ createPanel }) {

    const [open, setOpen] = useState(false);

    const [panel, setPanel] = useState({
        panelName: '',
        description: '',
    })

    const handleClickOpen = () => {
        setOpen(true);
    };

    const handleClose = () => {
        setOpen(false);
    };

    const handleSave = () => {
        createPanel(panel);
        setPanel({ panelName: "", description: "" })
        handleClose();
    }

    return (
        <>
            <Button variant='contained' color='success' onClick={handleClickOpen}>
                Add Panel
            </Button>
            <Dialog
                open={open}
                onClose={handleClose}
            >
                <DialogTitle>Add new Panel</DialogTitle>
                <DialogContent>
                    <TextField
                        margin='dense'
                        label='Panel name'
                        value={panel.panelName}
                        onChange={e => setPanel({ ...panel, panelName: e.target.value })}
                        fullWidth
                        variant='standard'
                    />
                    <TextField
                        margin='dense'
                        label='Description'
                        value={panel.description}
                        onChange={e => setPanel({ ...panel, description: e.target.value })}
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