import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { createPanel } from '../../taskmasterApi';
import Box from "@mui/material/Box";
import Button from "@mui/material/Button";
import TextField from "@mui/material/TextField";

// function CreatePanel() {
//     const [panelName, setPanelName] = useState('');
//     const [error, setError] = useState(null);
//     const navigate = useNavigate();

//     const handleCreatePanel = async (e) => {
//         e.preventDefault();
//         if (!panelName.trim()) {
//             setError("Panel name is required");
//             return;
//         }

//         try {
//             const newPanel = await createPanel({ name: panelName });
//             navigate('/panels'); // Redirect to the panels listing page after creation
//         } catch (err) {
//             console.error("Error creating panel:", err);
//             setError("Failed to create panel");
//         }
//     };

//     return (
//         <Box className="create-panel-container">
//             <h1>Create New Panel</h1>
//             {error && <p className="error">{error}</p>}
//             <form onSubmit={handleCreatePanel}>
//                 <TextField
//                     label="Panel Name"
//                     variant="outlined"
//                     fullWidth
//                     value={panelName}
//                     onChange={(e) => setPanelName(e.target.value)}
//                 />
//                 <Button type="submit" variant="contained" color="primary" sx={{ mt: 2 }}>
//                     Create Panel
//                 </Button>
//             </form>
//         </Box>
//     );
// }

// export default CreatePanel;

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
            <Button variant='contained' color='success' onClick={handleClickOpen}
                sx={{ position: "absolute", top: "110px", right: "40px" }}>Add Panel</Button>
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