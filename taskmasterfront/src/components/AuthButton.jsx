import React, { useState } from 'react';
import { Button, Snackbar } from '@mui/material';
import { useNavigate } from 'react-router-dom';

const AuthButton = () => {
    const navigate = useNavigate();
    const [openSnackbar, setOpenSnackbar] = useState(false);

    const handleAuthAction = () => {
        const token = localStorage.getItem('accessToken');
        if (token) {
            // Log out
            localStorage.removeItem('accessToken');
            localStorage.removeItem('tokenExpiresAt');
            setOpenSnackbar(true); 
            navigate('/');
        } else {
            // Log in
            navigate('/login');
        }
    };

    const isAuthenticated = !!localStorage.getItem('accessToken');

    return (
        <>
            <Button
                onClick={handleAuthAction}
                color="inherit"
                sx={{
                    marginRight: 2,
                    fontSize: '1.05rem',
                    '&:hover': {
                        color: '#1976d2',
                        backgroundColor: 'rgba(25, 118, 210, 0.1)',
                    },
                }}
            >
                {isAuthenticated ? 'Log Out' : 'Login'}
            </Button>

            <Snackbar
                open={openSnackbar}
                autoHideDuration={2000}
                onClose={() => setOpenSnackbar(false)}
                message="Logout successful"
            />
        </>
    );
};

export default AuthButton;
