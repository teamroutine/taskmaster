import { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import { fetchBlocksById } from "../../taskmasterApi.js";
import Paper from "@mui/material/Paper";
import Box from "@mui/material/Box";

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
                }}
              >
                {block.blockName}
              </Paper>
            </Box>
          ))}
        </Box>
      </Box>
    </Box>
  );
}

export default ListBlocks;
