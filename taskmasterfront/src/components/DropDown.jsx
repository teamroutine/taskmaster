import React, { useState } from "react";
import { Menu, IconButton } from "@mui/material";
import MoreVertIcon from "@mui/icons-material/MoreVert";

// DropDown component to get children props from listblocks and render them to dropdown
export default function DropDown({ children }) {
  const [anchorEl, setAnchorEl] = useState(null);
  const open = Boolean(anchorEl);


  const handleClick = (event) => {
    setAnchorEl(event.currentTarget);
  };

  const handleClose = () => {
    setAnchorEl(null);
  };
  const handleSelect = () => {
    handleClose();
  };


  return (
    <div>
      {/* This is the icon og the dropdown */}
      <IconButton onClick={handleClick} style={{ outline: 'none' }}>
        <MoreVertIcon fontSize="inherit" />
      </IconButton>


      <Menu anchorEl={anchorEl} open={open} onClose={handleClose}>
        {/* Listing the options and when option is clicked the dropwdown will close */}
        {React.Children.map(children, (child) =>
          React.cloneElement(child, {
            onClick: () => {
              handleSelect();
              if (child.props.onClick) child.props.onClick();
            },
          })
        )}
      </Menu>
    </div>
  );
}
