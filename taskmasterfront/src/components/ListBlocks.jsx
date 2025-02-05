import { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import { fetchBlocksById } from "../taskmasterApi";
import Paper from "@mui/material/Paper";
import Box from "@mui/material/Box";

const ListBlocks = () => {
  const { panelid } = useParams();
  const [blocks, setBlocks] = useState([]);
  const [error, setError] = useState(null);

  useEffect(() => {
    fetchBlocksById(panelid)
      .then((data) => setBlocks(data))
      .catch((err) => setError(err.message));
  }, [panelid]);

  if (error) {
    return <Box>Error: {error}</Box>;
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
              key={block.blockid}
              sx={{ display: "inline-block", marginRight: 2 }}
            >
              <Paper
                elevation={3}
                sx={{
                  width: 300,
                  height: 800,
                  padding: 2,
                  textAlign: "center",
                }}
              >
                {block.name}
              </Paper>
            </Box>
          ))}
        </Box>
      </Box>
    </Box>
  );
};

export default ListBlocks;
