import { useEffect, useState } from "react";
import { updatePanel } from "../../taskmasterApi";
import { Dialog, DialogContent, Box, DialogTitle, Button, TextField, DialogActions, Snackbar } from "@mui/material";

export default function EditPanel({ panel, onSave, open, onClose }) {
    const [panelData, setPanelData] = useState({ panelName: '', description: '' });

    const [openSnackbar, setOpenSnackbar] = useState(false);
    const [snackbarMessage, setSnackbarMessage] = useState('');

    useEffect(() => {
        setPanelData({
            panelName: panel.panelName,
            description: panel.description
        });
    }, [panel]);

    const handleSave = () => {
        updatePanel(panel.panelId, panelData)
            .then(() => {
                onSave(panelData);
                onClose();
                setSnackbarMessage('Panel updated successfully!');
                setOpenSnackbar(true);
            })
            .catch((err) => console.error("Error in editing panel:", err))
        setSnackbarMessage('Error in updating panel.');
        setOpenSnackbar(true);
    };

    return (
        <>
            <Dialog open={open} onClose={onClose}>
                <DialogTitle>Edit Panel</DialogTitle>
                <DialogContent>
                    <TextField
                        margin="dense"
                        label="Panel name"
                        value={panelData.panelName}
                        onChange={(e) => setPanelData({ ...panelData, panelName: e.target.value })}
                        fullWidth
                        variant="standard"
                    />
                    <TextField
                        margin="dense"
                        label="Description"
                        value={panelData.description}
                        onChange={(e) => setPanelData({ ...panelData, description: e.target.value })}
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