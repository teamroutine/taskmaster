/* eslint-disable react/prop-types */
import Paper from '@mui/material/Paper';
import Box from '@mui/material/Box';
import Typography from '@mui/material/Typography';
import Divider from '@mui/material/Divider';

const ListTickets = ({ tickets }) => {
    return (
        <Box component="ul" sx={{ padding: 0, margin: 0, listStyleType: 'none' }}>
            {tickets.map(ticket => (
                <Box component="li" key={ticket.ticketId} sx={{ marginBottom: 1 }}>
                    <Paper elevation={2} sx={{ padding: 1 }}>
                        <Typography variant="body1">{ticket.ticketName}</Typography>
                        <Divider></Divider>
                        <Typography variant="body2">{ticket.description}</Typography>
                    </Paper>
                </Box>
            ))}
        </Box>
    );
};

export default ListTickets;