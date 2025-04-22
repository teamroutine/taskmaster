import React, { useEffect, useState } from "react";
import {
    Dialog,
    DialogActions,
    DialogContent,
    DialogTitle,
    Button,
    Paper,
    Typography,
    Box,
    TextField,
    IconButton,
    Stack,
} from "@mui/material";
import { Edit, Delete } from "@mui/icons-material";
import { 
    fetchTags,
    createTag,
    updateTag,
    deleteTag
} from "../../taskmasterApi";

export default function TagListView({ open, onClose }) {
    const [tags, setTags] = useState([]);
    const [newTag, setNewTag] = useState({ name: "", color: "#000000" });
    const [editTagData, setEditTagData] = useState(null);

    useEffect(() => {
        fetchTags().then(setTags);
    }, []);

    const handleAddTag = async () => {
        if (!newTag.name || !newTag.color) return;
        const saved = await createTag(newTag);
        setTags([...tags, saved]);
        setNewTag({ name: "", color: "#000000" });
    };

    const handleDeleteTag = async (id) => {
        await deleteTag(id);
        setTags(tags.filter((tag) => tag.tagId !== id));
    };

    const handleEditSave = async () => {
        const updated = await updateTag(editTagData.tagId, editTagData);
        setTags(tags.map(tag => tag.tagId === updated.tagId ? updated : tag));
        setEditTagData(null);
    };

    return (
        <Dialog open={open} onClose={onClose} fullWidth maxWidth="sm">
            <DialogTitle sx={{ backgroundColor: "#2c2c2c", color: "#fff" }}>
                <Typography variant="h5">Manage Tags</Typography>
            </DialogTitle>

            <DialogContent sx={{ backgroundColor: "#2c2c2c" }}>
                <Stack spacing={2}>
                    {tags.map((tag) => (
                        <Paper
                            key={tag.tagId}
                            elevation={2}
                            sx={{
                                padding: 2,
                                display: "flex",
                                alignItems: "center",
                                justifyContent: "space-between",
                                backgroundColor: "#424242", // Dark background for tags
                                color: "#fff" // White text for readability
                            }}
                        >
                            <Box sx={{ display: "flex", alignItems: "center", gap: 2 }}>
                                <Box
                                    sx={{
                                        width: 24,
                                        height: 24,
                                        backgroundColor: tag.color,
                                        borderRadius: "50%",
                                        border: "1px solid #ccc",
                                    }}
                                />
                                <Typography>{tag.name}</Typography>
                            </Box>
                            <Box>
                                <IconButton onClick={() => setEditTagData(tag)}><Edit htmlColor="#fff" /></IconButton>
                                <IconButton onClick={() => handleDeleteTag(tag.tagId)}><Delete htmlColor="#fff" /></IconButton>
                            </Box>
                        </Paper>
                    ))}

                    <Box sx={{ display: "flex", gap: 2 }}>
                        <TextField
                            label="Tag Name"
                            value={newTag.name}
                            onChange={(e) => setNewTag({ ...newTag, name: e.target.value })}
                            fullWidth
                            InputProps={{
                                style: { color: "#fff" } // White text
                            }}
                            InputLabelProps={{
                                style: { color: "#ccc" } // Light label color
                            }}
                            sx={{ backgroundColor: "#333", borderRadius: 1 }} // Dark background for the text field
                        />
                        <input
                            type="color"
                            value={newTag.color}
                            onChange={(e) => setNewTag({ ...newTag, color: e.target.value })}
                            style={{ width: 50, height: 50, border: "none", background: "transparent" }}
                        />
                        <Button variant="contained" onClick={handleAddTag}>Add</Button>
                    </Box>
                </Stack>
            </DialogContent>

            <DialogActions sx={{ backgroundColor: "#2c2c2c" }}>
                <Button onClick={onClose} variant="contained" color="secondary">
                    Close
                </Button>
            </DialogActions>

            {editTagData && (
                <Dialog open={!!editTagData} onClose={() => setEditTagData(null)}>
                    <DialogTitle sx={{ backgroundColor: "#2c2c2c", color: "#fff" }}>
                        Edit Tag
                    </DialogTitle>
                    <DialogContent sx={{ backgroundColor: "#2c2c2c" }}>
                        <Box sx={{ display: "flex", gap: 2, mt: 1 }}>
                            <TextField
                                label="Tag Name"
                                value={editTagData.name}
                                onChange={(e) =>
                                    setEditTagData({ ...editTagData, name: e.target.value })
                                }
                                fullWidth
                                InputProps={{
                                    style: { color: "#fff" }
                                }}
                                InputLabelProps={{
                                    style: { color: "#ccc" }
                                }}
                                sx={{ backgroundColor: "#333", borderRadius: 1 }}
                            />
                            <input
                                type="color"
                                value={editTagData.color}
                                onChange={(e) =>
                                    setEditTagData({ ...editTagData, color: e.target.value })
                                }
                                style={{ width: 50, height: 50, border: "none", background: "transparent" }}
                            />
                        </Box>
                    </DialogContent>
                    <DialogActions sx={{ backgroundColor: "#2c2c2c" }}>
                        <Button onClick={() => setEditTagData(null)}>Cancel</Button>
                        <Button onClick={handleEditSave} variant="contained">Save</Button>
                    </DialogActions>
                </Dialog>
            )}
        </Dialog>
    );
}