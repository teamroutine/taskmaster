import { useEffect, useState } from 'react';
import {useParams} from "react-router"
import { fetchBlocks } from '../taskmasterApi';

function ListBlocks () {
    const [blocks, setBlocks] = useState([]);
    const [error, setError] = useState(null);

    useEffect(() => {
        fetchBlocks()
            .then(data => setBlocks(data))
            .catch(err => setError(err.message));
    }, []);

    if (error) {
        return <div>Error: {error}</div>;
    }

    return (
        <div>
            <h1>Blocks List</h1>
            <ul>
                {blocks.map(block => (
                    <li key={block.id}>{block.name}</li>
                ))}
            </ul>
        </div>
    );
};

export default ListBlocks;