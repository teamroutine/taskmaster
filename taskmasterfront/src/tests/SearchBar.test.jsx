import SearchBar from "../components/SearchBar";
import PanelView from "../components/PanelView";
import { test, expect } from "vitest";
import { render, screen, fireEvent } from '@testing-library/react'
import '@testing-library/jest-dom';


test("Renders page header", () => {
    render(<PanelView />)
    const button = screen.getByText(/Add Block/i); // Make sure that test suite find the header of the page
    expect(button).toBeInTheDocument();

});

test('SearchBar updates searchQuery state when user types', () => {
    const setSearchQuery = vi.fn();  // Mocking setSearchQuery function
    const searchQuery = '';

    render(<SearchBar searchQuery={searchQuery} setSearchQuery={setSearchQuery} />);

    // Get the input field in the search bar
    const searchInput = screen.getByPlaceholderText('Search...');

    // Simulate typing into the search input
    fireEvent.change(searchInput, { target: { value: 'new query' } });

    // Verify if the setSearchQuery function has been called with the correct value
    expect(setSearchQuery).toHaveBeenCalledWith('new query');
});
