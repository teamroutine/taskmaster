import React, { useEffect, useState } from "react";
import { Dialog, DialogActions, DialogContent, DialogTitle, Button, Paper, Typography, Box, TextField, IconButton, Stack, Checkbox } from "@mui/material";
import { Edit, Delete } from "@mui/icons-material";
import { fetchTags, createTag, updateTag, deleteTag } from "../../taskmasterApi";

export default function TagListView({ open, onClose, selectable = false, selected = [], onSelectTags = () => { }, disabledTagIds = [], }) {
    const [tags, setTags] = useState([]);
    const [selectedTags, setSelectedTags] = useState([]);
    const [newTag, setNewTag] = useState({ name: "", color: "#000000" });
    const [editTagData, setEditTagData] = useState(null);

    useEffect(() => {
        fetchTags().then(setTags);
    }, []);

    useEffect(() => {
        if (selectable) {
            const initial = selected.map(s => (typeof s === "object" ? s.id : s));
            setSelectedTags(initial);
        }
    }, [selected, selectable]);

    const handleAddTag = async () => {
        if (!newTag.name || !newTag.color) return;
        const saved = await createTag(newTag);
        setTags([...tags, saved]);
        setNewTag({ name: "", color: "#000000" });
    };

    const handleDeleteTag = async (id) => {
        await deleteTag(id);
        setTags((prev) => prev.filter((tag) => tag.id !== id));
        setSelectedTags((prev) => prev.filter((tagId) => tagId !== id));
    };

    const handleEditSave = async () => {
        const updated = await updateTag(editTagData.id, editTagData);
        setTags(tags.map(tag => tag.id === updated.id ? updated : tag));
        setEditTagData(null);
    };

    const isDisabled = (id) => disabledTagIds.includes(id);

    const toggleTagSelection = (id) => {
        if (isDisabled(id)) return;
        setSelectedTags((prev) =>
            prev.includes(id) ? prev.filter((t) => t !== id) : [...prev, id]
        );
    };

    const handleDone = () => {
        const selectedObjects = tags.filter((tag) => selectedTags.includes(tag.id));
        onSelectTags(selectedObjects);
        onClose();
    };

    return (
        <Dialog open={open} onClose={onClose} fullWidth maxWidth="sm">
            <DialogTitle sx={{ backgroundColor: "#2c2c2c", color: "#fff" }}>
                <Typography variant="h5">
                    {selectable ? "Select Tags" : "Manage Tags"}
                </Typography>
            </DialogTitle>

            <DialogContent sx={{ backgroundColor: "#2c2c2c" }}>
                <Stack spacing={2}>
                    {tags.map((tag) => (
                        <Paper
                            key={tag.id}
                            elevation={2}
                            sx={{
                                padding: 2,
                                display: "flex",
                                alignItems: "center",
                                justifyContent: "space-between",
                                backgroundColor: "#424242",
                                color: "#fff",
                                opacity: isDisabled(tag.id) ? 0.5 : 1,
                            }}
                        >
                            <Box sx={{ display: "flex", alignItems: "center", gap: 2 }}>
                                {selectable && (
                                    <Checkbox
                                        checked={selectedTags.includes(tag.id)}
                                        onChange={() => toggleTagSelection(tag.id)}
                                        sx={{ color: "#fff" }}
                                        disabled={isDisabled(tag.id)}
                                    />
                                )}
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
                            {!selectable && (
                                <Box>
                                    <IconButton onClick={() => setEditTagData(tag)}>
                                        <Edit htmlColor="#fff" />
                                    </IconButton>
                                    <IconButton onClick={() => handleDeleteTag(tag.id)}>
                                        <Delete htmlColor="#fff" />
                                    </IconButton>
                                </Box>
                            )}
                        </Paper>
                    ))}

                    {!selectable && (
                        <Box sx={{ display: "flex", gap: 2 }}>
                            <TextField
                                label="Tag Name"
                                value={newTag.name}
                                onChange={(e) => setNewTag({ ...newTag, name: e.target.value })}
                                fullWidth
                                InputProps={{ style: { color: "#fff" } }}
                                InputLabelProps={{ style: { color: "#ccc" } }}
                                sx={{ backgroundColor: "#333", borderRadius: 1 }}
                            />
                            <input
                                type="color"
                                value={newTag.color}
                                onChange={(e) => setNewTag({ ...newTag, color: e.target.value })}
                                style={{ width: 50, height: 50, border: "none", background: "transparent" }}
                            />
                            <Button variant="contained" onClick={handleAddTag} sx={{ fontSize: '0.7em' }}>Add Tag</Button>
                        </Box>
                    )}
                </Stack>
            </DialogContent>

            <DialogActions sx={{ backgroundColor: "#2c2c2c" }}>
                {selectable ? (
                    <>
                        <Button onClick={onClose}>Cancel</Button>
                        <Button onClick={handleDone} variant="contained">Done</Button>
                    </>
                ) : (
                    <Button onClick={onClose} variant="contained" color="secondary" sx={{ fontSize: '0.7em', marginRight: 2 }}>Close</Button>
                )}
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
                                InputProps={{ style: { color: "#fff" } }}
                                InputLabelProps={{ style: { color: "#ccc" } }}
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