import React, { useState } from 'react';
import Button from '@mui/material/Button';
import TextField from '@mui/material/TextField';
import Dialog from '@mui/material/Dialog';
import DialogActions from '@mui/material/DialogActions';
import DialogContent from '@mui/material/DialogContent';
import DialogTitle from '@mui/material/DialogTitle';



export default function Register({ registerUser }) {
    const [open, setOpen] = useState(false);

    const [user, setUser] = useState({
        firstName: '',
        lastName: '',
        email: '',
        phone: '',
        username: '',
        password: '',

    })
    const handleClickOpen = () => {
        setOpen(true);
    }

    const handleClickClose = () => {
        setOpen(false);
    }

    const handleSave = () => {
        registerUser(user);
        setUser({
            firstName: '',
            lastName: '',
            email: '',
            phone: '',
            username: '',
            password: '',
        });
        handleClickClose();
    }

    return (
        <>
            <Button variant='contained' color='success' onClick={handleClickOpen}>
                Register
            </Button>
            <Dialog
                open={open}
                onClose={handleClickClose}
            >
                <DialogTitle>Register new user</DialogTitle>
                <DialogContent>
                    <TextField
                        margin='dense'
                        label='First name'
                        value={user.firstName}
                        onChange={e => setUser({ ...user, firstName: e.target.value })}
                        fullWidth
                        variant='standard' e
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
                        label='username'
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
                    />
                </DialogContent>
                <DialogActions>
                    <Button onClick={handleClickClose}>Close</Button>
                    <Button onClick={handleSave}>Save</Button>
                </DialogActions>
            </Dialog>
        </>
    )

}
