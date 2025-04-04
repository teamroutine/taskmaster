import React, { useState, useEffect } from 'react';
import Button from '@mui/material/Button';
import TextField from '@mui/material/TextField';
import Dialog from '@mui/material/Dialog';
import DialogActions from '@mui/material/DialogActions';
import DialogContent from '@mui/material/DialogContent';
import DialogTitle from '@mui/material/DialogTitle';
import { useNavigate } from 'react-router-dom';
import { userRegister } from '../../taskmasterApi';
import { InputAdornment } from '@mui/material';
import IconButton from '@mui/material/IconButton';
import { Visibility, VisibilityOff } from '@mui/icons-material';



export default function Register() {
    const [open, setOpen] = useState(false);


    const [error, setError] = useState("");

    const [confirmPassword, setConfirmPassword] = useState('');

    const [showPassword, setShowPassword] = useState(false);

    const [showConfirmPassword, setShowConfirmPassword] = useState(false);

    const navigate = useNavigate();

    const [user, setUser] = useState({
        firstName: '',
        lastName: '',
        email: '',
        phone: '',
        username: '',
        password: '',

    })

    // Open modal right after register button has been clicked
    useEffect(() => {
        setOpen(true);
    }, []);

    // Close the modal
    const handleClickClose = () => {
        setOpen(false);
        navigate(-1);
    }

    // Save the user data and send it to back end
    const handleRegister = async () => {
        setError("");

        if (user.password != confirmPassword) {
            setError("Passwords do not match!");
            console.log("Error: Passwords do not match!");
            return;
        }

        try {
            await userRegister(user);
            handleClickClose();
            console.log("Registration successfull");

        } catch (error) {
            setError("Registration failed: " + error.message);
        }
    };

    // Show/Hide the text in password field
    const handleClickShowPassword = () => setShowPassword((show) => !show);

    // Show/Hide the text in confirmPassword field
    const handleClickShowConfirmPassword = () => setShowConfirmPassword((show) => !show);

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
            <DialogTitle>Register new user</DialogTitle>
            <DialogContent>
                <TextField
                    margin='dense'
                    label='First name'
                    value={user.firstName}
                    onChange={e => setUser({ ...user, firstName: e.target.value })}
                    fullWidth
                    variant='standard'
                    autoFocus
                />
                <TextField
                    margin='dense'
                    label='Last name'
                    value={user.lastName}
                    onChange={e => setUser({ ...user, lastName: e.target.value })}
                    fullWidth
                    variant='standard'
                />
                <TextField
                    margin='dense'
                    label='Email'
                    value={user.email}
                    onChange={e => setUser({ ...user, email: e.target.value })}
                    fullWidth
                    variant='standard'
                />
                <TextField
                    margin='dense'
                    label='Phone number'
                    value={user.phone}
                    onChange={e => setUser({ ...user, phone: e.target.value })}
                    fullWidth
                    variant='standard'
                />
                <TextField
                    margin='dense'
                    label='Username'
                    value={user.username}
                    onChange={e => setUser({ ...user, username: e.target.value })}
                    fullWidth
                    variant='standard'
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
                                        aria-label={showPassword ? "Hide the password" : "Show the password"}
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
                <TextField
                    margin='dense'
                    label='Confirm password'
                    value={confirmPassword}
                    onChange={e => setConfirmPassword(e.target.value)}
                    fullWidth
                    variant='standard' error={!!error}
                    helperText={error}
                    type={showConfirmPassword ? 'text' : 'password'}
                    slotProps={{
                        input: {
                            endAdornment: (
                                <InputAdornment position='end'>
                                    <IconButton
                                        aria-label={showConfirmPassword ? "Hide the password" : "Show the password"}
                                        onClick={handleClickShowConfirmPassword}
                                        onMouseDown={handleMouseDownPassword}
                                        onMouseUp={handleMouseUpPassword}
                                        edge='end'
                                    >
                                        {showConfirmPassword ? <VisibilityOff /> : <Visibility />}
                                    </IconButton>
                                </InputAdornment>
                            )
                        }
                    }}
                />
            </DialogContent>
            <DialogActions>
                <Button onClick={handleClickClose}>Close</Button>
                <Button onClick={handleRegister}>Save</Button>
            </DialogActions>
        </Dialog>

    )

}
