import { useState } from 'react';
import Button from '@mui/material/Button';
import TextField from '@mui/material/TextField';
import Dialog from '@mui/material/Dialog';
import DialogActions from '@mui/material/DialogActions';
import DialogContent from '@mui/material/DialogContent';
import DialogTitle from '@mui/material/DialogTitle';
import FormHelperText from '@mui/material/FormHelperText';
import FormControl from '@mui/material/FormControl';

export default function CreateBlock({ createBlock, existingBlockNames = [] }) {

    const [open, setOpen] = useState(false);

    const [block, setBlock] = useState({
        blockName: '',
        description: '',
        highlightColor: '',
    });

    const [errorMessage, setErrorMessage] = useState('');

    const handleClickOpen = () => {
        setOpen(true);
    };

    const handleClose = () => {
        setOpen(false);
        setErrorMessage('');  
    };

    const handleSave = () => {
        //Checking that a block with the same name doesn't already exist
        if (existingBlockNames.includes(block.blockName.toLowerCase())) {
            setErrorMessage('Block with this name already exists in this panel!');
            return;
        }

        createBlock(block);
        setBlock({ blockName: "", description: "", highlightColor: "" });
        setErrorMessage('');
        handleClose();
    };

    return (
        <>
            <Button variant='contained' color='success' onClick={handleClickOpen} sx={{ position:"absolute", top:"110px", right:"40px" }}>
                Add Block
            </Button>
            <Dialog open={open} onClose={handleClose}>
                <DialogTitle>Add new Block</DialogTitle>
                <DialogContent>
                    <FormControl fullWidth error={!!errorMessage}>
                        <TextField
                            margin='dense'
                            label='Block name'
                            value={block.blockName}
                            onChange={e => setBlock({ ...block, blockName: e.target.value })}
                            fullWidth
                            variant='standard'
                        />
                        {errorMessage && (
                            <FormHelperText sx={{ fontSize: '0.95rem' }}>{errorMessage}</FormHelperText>
                        )}
                    </FormControl>
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
                    <Button onClick={handleClose}>Close</Button>
                    <Button onClick={handleSave}>Save</Button>
                </DialogActions>
            </Dialog>
        </>
    );
}
