// import { useEffect, useState } from "react";
// import { useParams } from "react-router-dom";
// import { fetchBlocksById, handleAddTicket } from "../../taskmasterApi.js";
// import Paper from "@mui/material/Paper";
// import Box from "@mui/material/Box";
// import { Button } from "@mui/material";
// import Typography from "@mui/material/Typography";
// import ListTickets from "./ListTickets.jsx";
// import Divider from "@mui/material/Divider";
// import CreateTicket from "./CreateTicket.jsx";
// import EditBlock from "./EditBlock.jsx";

// function ListBlocks({ blocks }) {
//   const { panelid } = useParams();
//   const [blocks, setBlocks] = useState([]);
//   const [selectedBlock, setSelectedBlock] = useState(null);
//   const [error, setError] = useState(null);
//   const [open, setOpen] = useState(false)


//   useEffect(() => {
//     fetchBlocksById(panelid)
//       .then((data) => setBlocks(data.blocks))
//       .catch((err) => setError(err.message));
//   }, [panelid]);

//   //Handles the Edit button opening
//   const handleOpen = (block) => {
//     setSelectedBlock(block);
//     setOpen(true);
//   };

//   const handleClose = () => {
//     setOpen(false);
//   };

//   //Updates blocks in fronend after editing
//   const handleEditBlockSave = (updatedBlock) => {
//     setBlocks((prevBlocks) =>
//       prevBlocks.map((block) =>
//         block.blockId === selectedBlock.blockId
//           ? { ...block, ...updatedBlock }
//           : block
//       )
//     );
//   };

//   // Add new ticket function with the Blocks id
//   const addNewTicket = (newTicket, blockId) => {
//     handleAddTicket({ ...newTicket, blockId })
//       .then((addedTicket) => {
//         setBlocks((prevBlocks) =>
//           prevBlocks.map((block) =>
//             block.blockId === blockId
//               ? { ...block, tickets: [...block.tickets, addedTicket] }
//               : block
//           )
//         );
//       })
//       .catch((err) => {
//         console.error("Error adding ticket:", err);
//       });
//   };


//   if (error) {
//     return (
//       <Box>
//         Error: {error}
//         {panelid}
//       </Box>
//     );
//   }

//   return (
//     <Box>
//       <Box sx={{ overflowX: "auto", whiteSpace: "nowrap" }}>
//         <Box
//           component="ul"
//           sx={{
//             display: "inline-block",
//             padding: 0,
//             margin: 0,
//             listStyleType: "none",
//           }}
//         >
//           {blocks.map((block) => (
//             <Box
//               component="li"
//               key={block.blockId}
//               sx={{ display: "inline-block", marginRight: 2 }}
//             >
//               <Paper
//                 elevation={5}
//                 sx={{
//                   width: 300,
//                   height: 800,
//                   padding: 2,
//                   textAlign: "center",
//                   display: "flex",
//                   flexDirection: "column",
//                   justifyContent: "space-between",
//                 }}
//               >
//                 <Box>
//                   <Typography variant="h6">{block.blockName}</Typography>
//                   <Divider></Divider>
//                 </Box>
//                 <Box
//                   sx={{
//                     p: 1,
//                   }}>
//                   <ListTickets tickets={block.tickets} setBlocks={setBlocks} />
//                 </Box>
//                 <Box>
//                   <Divider></Divider>
//                   <CreateTicket createTicket={(newTicket) => addNewTicket(newTicket, block.blockId)} />

//                   <Button variant="contained" color="primary" onClick={() => handleOpen(block)}>
//                     Edit Block
//                   </Button>

//                 </Box>
//               </Paper>
//             </Box>
//           ))}
//         </Box>
//       </Box>
//       {selectedBlock && (
//         <EditBlock
//           block={selectedBlock}
//           onSave={handleEditBlockSave}
//           open={open}
//           onClose={handleClose}
//         />
//       )}
//     </Box>
//   );
// }

// export default ListBlocks;
import { useState } from "react";
import { useParams } from "react-router-dom";
import Paper from "@mui/material/Paper";
import Box from "@mui/material/Box";
import { Button } from "@mui/material";
import Typography from "@mui/material/Typography";
import ListTickets from "./ListTickets.jsx";
import Divider from "@mui/material/Divider";
import CreateTicket from "./CreateTicket.jsx";
import EditBlock from "./EditBlock.jsx";

function ListBlocks({ blocks }) { // blocks tulee nyt propsina PanelViewistä
  if (!blocks || blocks.length === 0) {
    return <Box>No blocks found.</Box>;
  }
  const { panelid } = useParams();
  const [selectedBlock, setSelectedBlock] = useState(null);
  const [error, setError] = useState(null);
  const [open, setOpen] = useState(false);

  //Handles the Edit button opening
  const handleOpen = (block) => {
    setSelectedBlock(block);
    setOpen(true);
  };

  const handleClose = () => {
    setOpen(false);
  };

  return (
    <Box>
      {error && <Box>Error: {error}</Box>}
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
                  <Divider />
                </Box>
                <Box sx={{ p: 1 }}>
                  <ListTickets tickets={block.tickets} />
                </Box>
                <Box>
                  <Divider />
                  <CreateTicket createTicket={(newTicket) => addNewTicket(newTicket, block.blockId)} />

                  <Button variant="contained" color="primary" onClick={() => handleOpen(block)}>
                    Edit Block
                  </Button>
                </Box>
              </Paper>
            </Box>
          ))}
        </Box>
      </Box>
      {selectedBlock && (
        <EditBlock block={selectedBlock} open={open} onClose={handleClose} />
      )}
    </Box>
  );
}


export default ListBlocks;