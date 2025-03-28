export function fetchBlocksById(panelid) {
    return fetch(import.meta.env.VITE_API_URL + "/panels/" + panelid)
        .then(response => {
            if (!response.ok)
                throw new Error("Error in fetch: " + response.statusText);

            return response.json();
        });
}

export function fetchPanels() {
    return fetch(import.meta.env.VITE_API_URL + "/panels")
        .then(response => {
            if (!response.ok)
                throw new Error("Error in fetch: " + response.statusText);

            return response.json();
        });
}

export function fetchTeams() {
    return fetch(import.meta.env.VITE_API_URL + "/teams")
        .then(response => {
            if (!response.ok)
                throw new Error("Error in fetch: " + response.statusText);

            return response.json();
        });
}

export function fetchTickets() {
    return fetch(import.meta.env.VITE_API_URL + "/tickets")
        .then(response => {
            if (!response.ok)
                throw new Error("Error in fetch: " + response.statusText);

            return response.json();
        });
}
export function fetchAppUsers(){
    return fetch(import.meta.env.VITE_API_URL + "/users")
    .then(response => {
        if(!response.ok)
            throw new Error("Error in fetch:" + response.statusText);

        return response.json();
    })
}
export const handleAddTicket = (newTicket) => {
    return fetch(import.meta.env.VITE_API_URL + "/tickets", {
        method: 'POST',
        headers: { 'Content-type': 'application/json' },
        body: JSON.stringify(newTicket)
    })
        .then(response => {
            if (!response.ok)
                throw new Error("Error when adding ticket: " + response.statusText);

            return response.json();

        });
}

export const handleAddBlock = (newBlock) => {
    return fetch(import.meta.env.VITE_API_URL + "/blocks", {
        method: 'POST',
        headers: { 'Content-type': 'application/json' },
        body: JSON.stringify(newBlock)
    })
        .then(response => {
            if (!response.ok) {
                throw new Error("Error when adding block: " + response.statusText);
            }
            return response.json();
        });
}

export const handleAddPanel = (newPanel) => {
    return fetch(import.meta.env.VITE_API_URL + "/panels", {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(newPanel)
    })
        .then(response => {
            if (!response.ok)
                throw new Error("Error when creating panel: " + response.statusText);

            return response.json();
        });
};

export function deleteTicket(ticketId) {
    return fetch(import.meta.env.VITE_API_URL + `/tickets/${ticketId}`, {
        method: "DELETE",
    })
        .then(response => {
            if (!response.ok) {
                throw new Error("Error when deleting ticket: " + response.statusText);
            }
            return response;
        });
}

export function updateTicket(ticketId, ticket) {
    return fetch(import.meta.env.VITE_API_URL + `/tickets/${ticketId}`, {
        method: "PUT",
        headers: {
            "Content-Type": "application/json",
        },
        body: JSON.stringify(ticket),
    })
        .then(async (response) => {
            const text = await response.text(); // Read response as text
            if (!response.ok) {
                console.error("Server Error:", text);
                throw new Error("Error when updating ticket: " + text);
            }

            try {
                return JSON.parse(text); // Parse response only if it's valid JSON
            } catch (error) {
                console.error("Invalid JSON response:", text);
                throw new Error("Invalid JSON from server");
            }
        })
        .catch(error => console.error("Update failed:", error));
}

export function updateBlock(blockId, block) {
    return fetch(import.meta.env.VITE_API_URL + `/blocks/${blockId}`, {
        method: "PUT",
        headers: {
            'Content-type': 'application/json'
        },
        body: JSON.stringify(block)
    })
        .then(response => {
            if (!response.ok) {
                throw new Error("Error when updating block: " + response.statusText)
            }
            return response;
        });
}

export function updatePanel(panelId, panel) {
    return fetch(import.meta.env.VITE_API_URL + `/panels/${panelId}`, {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(panel),
    })
        .then((response) => {
            if (!response.ok) {
                throw new Error("Error when updating panel: " + response.statusText);
            }
            return response;
        });
}
export function deleteBlock(blockId) {
    return fetch(import.meta.env.VITE_API_URL + `/blocks/${blockId}`, {
        method: 'DELETE',
    })
        .then(response => {
            if (!response.ok) {
                throw new Error("Error when deleting Block: " + response.statusText);
            }
            return response;
        });

}

export function deletePanel(panelId) {
    return fetch(import.meta.env.VITE_API_URL + `/panels/${panelId}`, {
        method: 'DELETE',
    })
        .then(response => {
            if (!response.ok) {
                throw new Error("Error deleting Panel: " + response.statusText);
            }
            return response;
        });
}