import React from "react";
import { render, screen } from "@testing-library/react";
import userEvent from "@testing-library/user-event";
import DropDown from "../components/DropDown";
import { MenuItem } from "@mui/material";
import '@testing-library/jest-dom';

test("Dropdown becomes visible when the icon is clicked", async () => {
    render(
        <DropDown>
            <MenuItem>Option 1</MenuItem>
            <MenuItem>Option 2</MenuItem>
        </DropDown>
    );

    // Aluksi menu ei ole näkyvissä
    expect(screen.queryByText("Option 1")).not.toBeInTheDocument();
    expect(screen.queryByText("Option 2")).not.toBeInTheDocument();

    // Käyttäjä klikkaa dropdown-ikonia
    const button = screen.getByRole("button");
    await userEvent.click(button);

    // Tarkistetaan, että menu tulee näkyviin
    expect(screen.getByText("Option 1")).toBeVisible();
    expect(screen.getByText("Option 2")).toBeVisible();
});