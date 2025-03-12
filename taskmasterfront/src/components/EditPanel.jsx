import { useEffect, useState } from "react";
import { updatePanel } from "../../taskmasterApi";
import { Dialog, DialogContent, Box, DialogTitle, Button, TextField, DialogActions } from "@mui/material";

export default function EditPanel({ panel, onSave, open, onClose }) {
    const [panelData, setPanelData] = useState({ panelName: '', description: '' });

    useEffect(() => {
        setPanelData({
            panelName: panel.panelData || '', // Set empty string, if panelName is undefined
            description: panel.description || '' // Set empty string, if description is undefined
        });
    }, [panel]);

    const handleSave = () => {
        updatePanel(panel.panelId, panelData)
            .then(() => {
                onSave(panelData);
                onClose();
            })
            .catch((err) => console.error("Error in editing panel:", err))
    };

    return (
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
    );
}