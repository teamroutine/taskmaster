import { useState } from 'react';
import Button from '@mui/material/Button';
import TextField from '@mui/material/TextField';
import Dialog from '@mui/material/Dialog';
import DialogActions from '@mui/material/DialogActions';
import DialogContent from '@mui/material/DialogContent';
import DialogTitle from '@mui/material/DialogTitle';

export default function CreateBlock({ createBlock }) {

    const [open, setOpen] = useState(false);

    const [block, setBlock] = useState({
        blockName: '',
        description: '',
        highlightColor: '',
    })

    const handleClickOpen = () => {
        setOpen(true);
    };

    const handleClose = () => {
        setOpen(false);
    };

    const handleSave = () => {
        createBlock(block)
            .then(() => {
                setBlock({ blockName: "", description: "", highlightColor: "" });
                handleClose();
            })
            .catch(err => console.error("Error adding block:", err));
    };

    return (
        <>
            <Button variant='contained' color='success' onClick={handleClickOpen}>
                Add Block
            </Button>
            <Dialog
                open={open}
                onClose={handleClose}
            >
                <DialogTitle>Add new Block</DialogTitle>
                <DialogContent>
                    <TextField
                        margin='dense'
                        label='Block name'
                        value={block.blockName}
                        onChange={e => setBlock({ ...block, blockName: e.target.value })}
                        fullWidth
                        variant='standard'
                    />
                    <TextField
                        margin='dense'
                        label='Description'
                        value={block.description}
                        onChange={e => setBlock({ ...block, description: e.target.value })}
                        fullWidth
                        variant='standard'
                    />
                    <TextField
                        margin='dense'
                        label='Highlight color'
                        value={block.highlightColor}
                        onChange={e => setBlock({ ...block, highlightColor: e.target.value })}
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

