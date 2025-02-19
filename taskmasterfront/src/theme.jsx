import { createTheme } from '@mui/material/styles';

export const themeOptions = {
  palette: {
    mode: 'dark',
    primary: {
      main: '#90caf9',
    },
    secondary: {
      main: '#ce93d8',
    },
    background: {
      default: '#121212',
      paper: '#121212',
    },
  },
};

const theme = createTheme(themeOptions);

export default theme;