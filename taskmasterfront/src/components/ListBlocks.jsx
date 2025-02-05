import { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import { fetchBlocksById } from "../../taskmasterApi.js";
import Paper from "@mui/material/Paper";
import Box from "@mui/material/Box";
import Typography from "@mui/material/Typography";
import ListTickets from "./ListTickets.jsx";
import Divider from "@mui/material/Divider";

function ListBlocks() {
  const { panelid } = useParams();
  const [blocks, setBlocks] = useState([]);
  const [error, setError] = useState(null);

  useEffect(() => {
    fetchBlocksById(panelid)
      .then((data) => setBlocks(data.blocks))
      .catch((err) => setError(err.message));
  }, [panelid]);

  if (error) {
    return (
      <Box>
        Error: {error}
        {panelid}
      </Box>
    );
  }

  return (
    <Box>
      <h1>Blocks List</h1>
      <Box sx={{ overflowX: "auto", whiteSpace: "nowrap" }}>
        <Box
          component="ul"
          sx={{
            display: "inline-block",
            padding: 0,
            margin: 0,
            listStyleType: "none",
          }}
        >
          {blocks.map((block) => (
            <Box
              component="li"
              key={block.blockId}
              sx={{ display: "inline-block", marginRight: 2 }}
            >
              <Paper
                elevation={5}
                sx={{
                  width: 300,
                  height: 800,
                  padding: 2,
                  textAlign: "center",
                  display: "flex",
                  flexDirection: "column",
                  justifyContent: "space-between",
                }}
              >
                <Box>
                <Typography variant="h6">{block.blockName}</Typography>
                <Divider></Divider>
                </Box>
                <Box
                 sx={{ 
                    p: 1,
                 }}>
                  <ListTickets tickets={block.tickets} />
                </Box>
                <Box>
                  <Divider></Divider>
                  <Typography variant="body2">
                    Add Ticket +
                  </Typography>
                </Box>
              </Paper>
            </Box>
          ))}
        </Box>
      </Box>
    </Box>
  );
}

export default ListBlocks;
