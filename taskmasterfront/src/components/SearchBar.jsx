import React from "react";
import { Box, InputBase } from "@mui/material";
import { Search as SearchIcon } from "@mui/icons-material";

export default function SearchBar({ searchQuery, setSearchQuery }) {
    const handleSearchChange = (e) => {
        setSearchQuery(e.target.value.toLowerCase());

    };
    return (
        <Box
            sx={{
                display: 'flex',
                alignItems: 'center',
                border: '1px solid #ccc',
                borderRadius: '4px',
                padding: '0 10px',
                width: '300px',
            }}
        >
            <SearchIcon sx={{ color: 'gray', marginRight: '8px' }} />
            <InputBase
                sx={{ width: '100%', color: 'black' }}
                placeholder="Search..."
                value={searchQuery}
                onChange={handleSearchChange}
            />
        </Box>
    );
}