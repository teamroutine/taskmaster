import { useState } from "react";
import Button from "@mui/material/Button";
import TextField from "@mui/material/TextField";
import Dialog from "@mui/material/Dialog";
import DialogActions from "@mui/material/DialogActions";
import DialogContent from "@mui/material.DialogContent";
import DialogTitle from "@mui/material/DialogTitle";

export default function CreateTeam({ createTeam }) {
  const [open, setOpen] = useState(false);

  const [team, setTeam] = useState({
    teamName: "",
    description: "",
  });

  // Open new Team modal
  const handleClickOpen = () => {
    setOpen(true);
  };

  // Close new Team modal
  const handleClose = () => {
    setOpen(false);
  };

  // Save new Team information
  const handleSave = () => {
    createTeam(team);
    setTeam({ teamName: "", description: "" });
    handleClose();
  };

  return (
    <>
      {/* Button for adding new Team */}
      <Button variant="contained" color="success" onClick={handleClickOpen}>
        Add Team
      </Button>
      <Dialog open={open} onClose={handleClose}>
        {/* Modal for all the mandatory attributes for new Team */}
        <DialogTitle>Add new Team</DialogTitle>
        <DialogContent>
          <TextField
            margin="dense"
            label="Team name"
            value={team.teamName}
            onChange={(e) => setTeam({ ...team, teamName: e.target.value })}
            fullWidth
            variant="standard"
          />
          <TextField
            margin="dense"
            label="Team description"
            value={team.description}
            onChange={(e) => setTeam({ ...team, description: e.target.value })}
            fullWidth
            variant="standard"
          />
        </DialogContent>
        <DialogActions>
          <Button onClick={handleClose}>Close</Button>{" "}
          {/* Button for closing modal */}
          <Button onClick={handleSave}>Save</Button>{" "}
          {/* Button for saving Team information */}
        </DialogActions>
      </Dialog>
    </>
  );
}
