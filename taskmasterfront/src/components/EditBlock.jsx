import { useEffect, useState } from "react";
import { updateBlock } from "../../taskmasterApi";
import { Snackbar,Dialog, DialogContent, Box, DialogTitle, TextField, Button, DialogActions } from "@mui/material";

export default function EditBlock({ block, onSave, open, onClose }) {
    const [blockData, setBlockData] = useState({ blockName: '', description: '', highlightColor: '' });

    const [openSnackbar, setOpenSnackbar] = useState(false);
    const [snackbarMessage, setSnackbarMessage] = useState('');

    useEffect(() => {
        setBlockData({
            blockName: block.blockName,
            description: block.description,
            highlightColor: block.highlightColor,
        });
    }, [block]);


    const handleSave = () => {
        updateBlock(block.blockId, blockData)
            .then(() => {
                onSave(blockData);
                onClose();
                setSnackbarMessage('Block updated successfully!');
                setOpenSnackbar(true); 
            })
            .catch((err) => console.error("Error updating block:", err));
            setSnackbarMessage('Error updating block.');
            setOpenSnackbar(true);
    };

    return (
        <>
        <Dialog open={open} onClose={onClose}>
            <DialogTitle>Edit Block</DialogTitle>
            <DialogContent>
                <TextField
                    margin="dense"
                    label="Block name"
                    value={blockData.blockName}
                    onChange={(e) => setBlockData({ ...blockData, blockName: e.target.value })}
                    fullWidth
                    variant="standard"
                />
                <TextField
                    margin="dense"
                    label="Description"
                    value={blockData.description}
                    onChange={(e) => setBlockData({ ...blockData, description: e.target.value })}
                    fullWidth
                    variant="standard"
                />
                <TextField
                    margin="dense"
                    label="Highlight color"
                    value={blockData.highlightColor}
                    onChange={(e) => setBlockData({ ...blockData, highlightColor: e.target.value })}
                    fullWidth
                    variant="standard"
                />
            </DialogContent>
            <DialogActions>
                <Button onClick={onClose}>Close</Button>
                <Button onClick={handleSave}>Save</Button>
            </DialogActions>
        </Dialog>
        <Snackbar
            open={openSnackbar}
            message={snackbarMessage}
            autoHideDuration={2000}
            onClose={() => setOpenSnackbar(false)}
                />
        </>
    );
}
