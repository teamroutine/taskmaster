import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import { fetchPanels, createPanel } from '../../taskmasterApi';  // Assuming you have this API call
import CreatePanel from './CreatePanel';


function ListPanelView() {
    const [panels, setPanels] = useState([]);
    const [error, setError] = useState(null);

    useEffect(() => {
        fetchPanels()
            .then((data) => {
                console.log(data);
                setPanels(data || []);
            })
            .catch((err) => {
                setError("Error fetching panels: " + err.message);
            });
    }, []);

    const addNewPanel = async (newPanel) => {
        try {
            const createdPanel = await createPanel(newPanel);
            if (createdPanel) {
                setPanels((prevPanels) => [...prevPanels, createdPanel]);
            }
        } catch (err) {
            console.error(err);
            setError("Error creating panel: " + err.message);
        }
    };
    return (
        <div>
            <h1>All Panels</h1>
            {error && <p>{error}</p>}
            {panels.length === 0 ? (
                <p>No panels available.</p>
            ) : (
                <ul>
                    {panels.map((panel) => (
                        <li key={panel.panelId}>
                            <Link to={`/panels/${panel.panelId}`}>{panel.panelName || "Unnamed Panel"}</Link>
                        </li>
                    ))}
                </ul>
            )}

            <CreatePanel createPanel={addNewPanel} />
        </div>
    );
}

export default ListPanelView;