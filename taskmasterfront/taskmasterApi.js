export function fetchBlocksById(panelid) {
  return apiFetch(import.meta.env.VITE_API_URL + "/panels/" + panelid);
}

export function fetchPanels() {
  return apiFetch(import.meta.env.VITE_API_URL + "/panels");
}

export function fetchTeams() {
  return apiFetch(import.meta.env.VITE_API_URL + "/teams");
}

export function fetchTickets() {
  return apiFetch(import.meta.env.VITE_API_URL + "/tickets");
}

export function fetchAppUsers() {
  return apiFetch("/users");
}

export const handleAddTicket = (newTicket) => {
  return apiFetch(import.meta.env.VITE_API_URL + "/tickets", {
    method: "POST",
    body: JSON.stringify(newTicket),
  });
};

export const handleAddBlock = (newBlock) => {
  return apiFetch(import.meta.env.VITE_API_URL + "/blocks", {
    method: "POST",
    body: JSON.stringify(newBlock),
  });
};

export const handleAddPanel = (newPanel) => {
  return apiFetch(import.meta.env.VITE_API_URL + "/panels", {
    method: "POST",
    body: JSON.stringify(newPanel),
  });
};

export const handleAddTeam = (newTeam) => {
  return apiFetch(import.meta.env.VITE_API_URL + "/teams", {
    method: "POST",
    body: JSON.stringify(newTeam),
  });
};

export function deleteTicket(ticketId) {
  return apiFetch(import.meta.env.VITE_API_URL + `/tickets/${ticketId}`, {
    method: "DELETE",
  });
}

export function updateTicket(ticketId, ticket) {
  return apiFetch(import.meta.env.VITE_API_URL + `/tickets/${ticketId}`, {
    method: "PUT",
    body: JSON.stringify(ticket),
  });
}

export function updateBlock(blockId, block) {
  return apiFetch(import.meta.env.VITE_API_URL + `/blocks/${blockId}`, {
    method: "PUT",
    body: JSON.stringify(block),
  });
}

export function updatePanel(panelId, panel) {
  return apiFetch(import.meta.env.VITE_API_URL + `/panels/${panelId}`, {
    method: "PUT",
    body: JSON.stringify(panel),
  });
}

export function deleteBlock(blockId) {
  return apiFetch(import.meta.env.VITE_API_URL + `/blocks/${blockId}`, {
    method: "DELETE",
  });
}

export function deletePanel(panelId) {
  return apiFetch(import.meta.env.VITE_API_URL + `/panels/${panelId}`, {
    method: "DELETE",
  });
}

export const userRegister = async (newUser) => {
  return apiFetch(import.meta.env.VITE_API_URL + "/users", {
    method: "POST",
    body: JSON.stringify(newUser),
  });
};

export const userLogin = async (user) => {
  return apiFetch("/api/auth/login", {
    method: "POST",
    body: JSON.stringify(user),
  });
};

export const generateInvite = (teamId, durationHours) => {
  return apiFetch(`${import.meta.env.VITE_API_URL}/invites/generateInvite`, {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify({ teamId, durationHours }),
  });
};

// Dynamically set the base URL based on the environment
const BASE_URL = process.env.NODE_ENV === "production"
  ? "https://taskmaster-git-ohjelmistoprojekti-2-taskmaster.2.rahtiapp.fi"
  : "http://localhost:8080";

// Centralized API fetch function
export const apiFetch = async (URL, options = {}) => {
  const token = localStorage.getItem("accessToken"); // Fetch the token from localStorage

  const isAuthRequired =
    !URL.includes("/auth/login") && !URL.includes("/users");

  if (isAuthRequired && !token) {
    throw new Error("You are not authenticated. Please log in");
  }

  const headers = {
    ...(isAuthRequired && { Authorization: "Bearer " + token }), // Attach token into the request
    "Content-Type": "application/json",
    ...options.headers,
  };

  try {
    const response = await fetch(`${BASE_URL}${URL}`, {
      credentials: "include",
      ...options,
      headers
    });

    if (!response.ok) {
      const errorText = await response.text();
      throw new Error(
        "Error: " +
        response.status +
        " - " +
        response.statusText +
        "-" +
        errorText
      );
    }

    const contentType = response.headers.get("content-type");
    if (response.status === 204 || !contentType || !contentType.includes("application/json")) {
      return null;
    }

    return response.json();
  } catch (error) {
    console.error("Fetch error: ", error);
    throw error;
  }
};
