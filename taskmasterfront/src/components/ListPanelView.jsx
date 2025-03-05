import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import { fetchPanels } from '../../taskmasterApi';

function ListPanelView() {
    const [panels, setPanels] = useState([]);
    const [error, setError] = useState(null);

    useEffect(() => {
        fetchPanels()
            .then((data) => setPanels(data.panels))
            .catch((err) => setError("Error fetching panels: " + err.message));
    }, []);

    return (
        <div>
            <h1>All Panels</h1>
            {error && <p>{error}</p>}
            <ul>
                {panels.map((panel) => (
                    <li key={panel.id}>
                        <Link to={`/panels/${panel.id}`}>{panel.name}</Link> {/* Link to individual panel */}
                    </li>
                ))}
            </ul>
        </div>
    );
}

export default ListPanelView;