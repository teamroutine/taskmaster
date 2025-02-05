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

export const handleAddTicket = (newTicket) => {
    return fetch(import.meta.env.VITE_API_URL, {
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