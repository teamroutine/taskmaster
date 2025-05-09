import React, { useState, useEffect } from 'react';
import Button from '@mui/material/Button';
import TextField from '@mui/material/TextField';
import Dialog from '@mui/material/Dialog';
import DialogActions from '@mui/material/DialogActions';
import DialogContent from '@mui/material/DialogContent';
import DialogTitle from '@mui/material/DialogTitle';
import { userLogin } from '../../taskmasterApi';
import { Visibility, VisibilityOff } from '@mui/icons-material';
import { InputAdornment } from '@mui/material';
import IconButton from '@mui/material/IconButton';
import { useNavigate } from 'react-router-dom';
import { useContext } from "react";
import { AuthContext } from "../context/AuthProvider";


export default function Login() {
    const { login } = useContext(AuthContext);
    const [open, setOpen] = useState(false);

    const [showPassword, setShowPassword] = useState('');

    const [user, setUser] = useState({
        username: '',
        password: ''
    });

    // Open modal right after login button has been clicked
    useEffect(() => {
        setOpen(true);
    }, []);

    // Close the modal
    const handleClickClose = () => {
        setOpen(false);
        navigate('/')
    }

    const navigate = useNavigate();

    const handleLogin = async () => {
        try {
            const response = await userLogin(user); // Has username and password
            const { accessToken, expiresAt } = response;

            // Save token into localStorage
            localStorage.setItem('accessToken', accessToken);
            localStorage.setItem('tokenExpiresAt', expiresAt);

            login();

            // Navigate to panels
            navigate('/teams');
        } catch (error) {
            alert(error.message);
        }
    };

    const handleClickShowPassword = () =>
        setShowPassword((show) => !show);

    const handleMouseDownPassword = (event) => {
        event.preventDefault();
    };

    const handleMouseUpPassword = (event) => {
        event.preventDefault();
    };

    return (


        <Dialog
            open={open}
            onClose={handleClickClose}
            autoFocus
        >
            <DialogTitle>Login</DialogTitle>
            <DialogContent>
                <TextField
                    margin='dense'
                    label='Username'
                    value={user.username}
                    onChange={e => setUser({ ...user, username: e.target.value })}
                    fullWidth
                    variant='standard'
                    autoFocus
                />
                <TextField
                    margin='dense'
                    label='Password'
                    value={user.password}
                    onChange={e => setUser({ ...user, password: e.target.value })}
                    fullWidth
                    variant='standard'
                    type={showPassword ? 'text' : 'password'}
                    slotProps={{
                        input: {
                            endAdornment: (
                                <InputAdornment position='end'>
                                    <IconButton
                                        aria-label={showPassword ? "Hide the password" : 'Show the password'}
                                        onClick={handleClickShowPassword}
                                        onMouseDown={handleMouseDownPassword}
                                        onMouseUp={handleMouseUpPassword}
                                        edge='end'
                                    >
                                        {showPassword ? <VisibilityOff /> : <Visibility />}
                                    </IconButton>
                                </InputAdornment>
                            )
                        }
                    }}
                />
            </DialogContent>
            <DialogActions>
                <Button onClick={handleClickClose}>Close</Button>
                <Button onClick={handleLogin}>Login</Button>
            </DialogActions>
        </Dialog>
    )

}