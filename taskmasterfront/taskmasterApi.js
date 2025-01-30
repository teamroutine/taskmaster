export function fetchBlocks() {
    return fetch(import.meta.env.VITE_API_URL + "/blocks")
        .then(response => {
            if (!response.ok) 
                throw new Error("Error in fetch: " + response.statusText);
            
            return response.json();
        });
}