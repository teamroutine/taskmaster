import React from "react";
import { Dialog, DialogActions, DialogContent, DialogTitle, Button, Paper, Typography, Box, MenuItem, TextField } from '@mui/material';

export default function ViewTeam({team, openView, closeView}){
    if (!team) return null;
    return(
        <Dialog open={openView} onClose={closeView}>
        <DialogTitle
            sx={{
                padding: 2,
                display: "flex",
                justifyContent: "space-between",
                alignItems: "center"
            }}
        >
            <Typography variant="h5">
                Team Details
            </Typography>
        </DialogTitle>
        <DialogContent>
            <Paper
                elevation={3}
                sx={{
                    padding: 2,
                    width: 500,
                    height:'auto',
                    display: 'flex',
                    flexDirection: 'column',
                    justifyContent:'flex-start',
                    wordWrap:'break-word',
                    overflow:'hidden'
                }}
            >
                <Box mb={2}>
                    <Typography variant="h6">{team.teamName}</Typography>
                </Box>

                <Box mb={2}>
                    <Typography variant="h6">Description:</Typography>
                    <TextField
                            value={team.description}
                            multiline
                            rows={4}
                            fullWidth
                            variant="outlined"
                            InputProps={{
                                readOnly: true,
                                style: { fontSize: '20px' }
                            }}
                            sx={{
                                marginTop: 1,
                                borderRadius: 2,
                            }}
                        />
                </Box>
                <Box mb={2}>
                    <Typography variant="h6">Members:</Typography>
                </Box>

            </Paper>
        </DialogContent>
        <DialogActions sx={{ justifyContent: "flex-start" }}>
            <Button sx={{ backgroundColor: "#9E9E9E" }} variant="contained" onClick={closeView}>Close</Button>
        </DialogActions>
    </Dialog>
    );
}