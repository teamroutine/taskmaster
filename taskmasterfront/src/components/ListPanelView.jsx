import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import { fetchPanels } from '../../taskmasterApi';  // Assuming you have this API call

function ListPanelView() {
    const [panels, setPanels] = useState([]);
    const [error, setError] = useState(null);

    useEffect(() => {
        fetchPanels()  // Fetch all panels
            .then((data) => {
                console.log(data);  // Log the data to check its structure
                setPanels(data || []);  // Directly set the data array (no need for `data.panels`)
            })
            .catch((err) => {
                setError("Error fetching panels: " + err.message);
            });
    }, []);

    return (
        <div>
            <h1>All Panels</h1>
            {error && <p>{error}</p>}
            {panels.length === 0 ? (
                <p>No panels available.</p>  // Show a message when no panels exist
            ) : (
                <ul>
                    {panels.map((panel) => (
                        <li key={panel.panelId}>
                            <Link to={`/panels/${panel.panelId}`}>{panel.panelName || "Unnamed Panel"}</Link> {/* Link to individual panel */}
                        </li>
                    ))}
                </ul>
            )}
        </div>
    );
}

export default ListPanelView;