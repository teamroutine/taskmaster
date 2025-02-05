/* eslint-disable no-unused-vars */
import React from 'react';
import { useParams } from 'react-router-dom';
import ListBlocks from './ListBlocks';

const PanelView = () => {
    const { panelid } = useParams(); 

    return (
        <div>
            <h1>Panel View</h1>
            <ListBlocks panelid={panelid} />
        </div>
    );
};

export default PanelView;