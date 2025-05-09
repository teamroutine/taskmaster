import { useState } from "react";
import Button from "@mui/material/Button";
import TextField from "@mui/material/TextField";
import Dialog from "@mui/material/Dialog";
import DialogActions from "@mui/material/DialogActions";
import { DialogContent } from "@mui/material";
import DialogTitle from "@mui/material/DialogTitle";

export default function CreateTeam({ createTeam }) {
  const [open, setOpen] = useState(false);

  const [team, setTeam] = useState({
    teamName: "",
    description: "",
  });

  const handleClickOpen = () => {
    setOpen(true);
  };

  const handleClose = () => {
    setOpen(false);
  };

  const handleSave = () => {
    createTeam(team);
    setTeam({ teamName: "", description: "" });
    handleClose();
  };

  return (
    <>
      
      <Button variant="contained" color="success" onClick={handleClickOpen} sx={{ fontSize: '0.7em', padding: '0.4em 0.8em' }}>
        Add Team
      </Button>
      <Dialog open={open} onClose={handleClose}>
        <DialogTitle>Add new Team</DialogTitle>
        <DialogContent>
          <TextField
            margin="dense"
            label="Team name"
            value={team.teamName}
            onChange={(e) => setTeam({ ...team, teamName: e.target.value })}
            fullWidth
            variant="outlined"
          />
          <TextField
            margin="dense"
            label="Team description"
            value={team.description}
            onChange={(e) => setTeam({ ...team, description: e.target.value })}
            fullWidth
            multiline
            variant="outlined"
          />
        </DialogContent>
        <DialogActions>
          <Button onClick={handleClose}>Close</Button>{" "}
          <Button onClick={handleSave}>Save</Button>{" "}
        </DialogActions>
      </Dialog>
    </>
  );
}
