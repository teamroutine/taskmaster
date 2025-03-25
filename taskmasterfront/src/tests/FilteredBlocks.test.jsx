import SearchBar from "../components/SearchBar";
import PanelView from "../components/PanelView";
import { test, expect } from "vitest";
import { render, screen, fireEvent } from '@testing-library/react'
import '@testing-library/jest-dom';

const filterBlocks = (blocks, searchQuery) => {
    searchQuery = searchQuery.toLowerCase();
    return blocks
        .map(block => {
            const filteredTickets = block.tickets?.filter(ticket =>
                ticket.ticketName.toLowerCase().includes(searchQuery) ||
                ticket.description.toLowerCase().includes(searchQuery)
            ) || [];

            if (block.blockName.toLowerCase().includes(searchQuery) || filteredTickets.length > 0) {
                return { ...block, tickets: filteredTickets };
            }

            return null;
        })
        .filter(block => block !== null);
};

describe('filterBlocks function', () => {
    test('filters blocks and tickets correctly', () => {
        const blocks = [
            {
                blockName: 'Block A',
                tickets: [
                    { ticketName: 'Ticket 1', description: 'Issue with login' },
                    { ticketName: 'Ticket 2', description: 'Payment failure' }
                ]
            },
            {
                blockName: 'Block B',
                tickets: [
                    { ticketName: 'Bug Report', description: 'UI glitch' }
                ]
            }
        ];

        let searchQuery = 'login';
        let result = filterBlocks(blocks, searchQuery);
        expect(result).toHaveLength(1);
        expect(result[0].tickets).toHaveLength(1);
        expect(result[0].tickets[0].ticketName).toBe('Ticket 1');

        searchQuery = 'Block B';
        result = filterBlocks(blocks, searchQuery);
        expect(result).toHaveLength(1);
        expect(result[0].blockName).toBe('Block B');

        searchQuery = 'nonexistent';
        result = filterBlocks(blocks, searchQuery);
        expect(result).toHaveLength(0);
    });
});

