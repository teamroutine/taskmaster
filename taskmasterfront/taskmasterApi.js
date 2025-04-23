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
  return apiFetch(import.meta.env.VITE_API_URL + "/users");
}

export function fetchTags() {
  return apiFetch(import.meta.env.VITE_API_URL + "/tags");
}

export function createTag(newTag) {
  return apiFetch(import.meta.env.VITE_API_URL + "/tags", {
    method: "POST",
    body: JSON.stringify(newTag),
  });
}

export function updateTag(tagId, updatedTag) {
  return apiFetch(import.meta.env.VITE_API_URL + `/tags/${tagId}`, {
    method: "PUT",
    body: JSON.stringify(updatedTag),
  });
}

export function deleteTag(tagId) {
  return apiFetch(import.meta.env.VITE_API_URL + `/tags/${tagId}`, {
    method: "DELETE",
  });
}

export function addTagsToTicket(ticketId, tagIds) {
  return apiFetch(import.meta.env.VITE_API_URL + `/tickets/${ticketId}/addTags`, {
    method: "PUT",
    body: JSON.stringify(tagIds),
  });
}

export function removeTagsFromTicket(ticketId, tagIds) {
  return apiFetch(import.meta.env.VITE_API_URL + `/tickets/${ticketId}/removeTags`, {
    method: "PUT",
    body: JSON.stringify(tagIds),
  });
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

export const handleReorderTickets = (blockId, reorderedTickets) => {
  return apiFetch(`${import.meta.env.VITE_API_URL}/tickets/reorder?blockId=${blockId}`, {
    method: "PUT",
    body: JSON.stringify(reorderedTickets),
  });
};

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
  return apiFetch(import.meta.env.VITE_API_URL + "/auth/login", {
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

export const apiFetch = async (URL, options = {}) => {
  const token = localStorage.getItem("accessToken"); // Fetch the token from localStorage

  const isAuthRequired =
    !URL.includes("/api/auth/login") && !URL.includes("/api/users");

  if (isAuthRequired && !token) {
    throw new Error("You are not authenticated. Please log in");
  }

  const headers = {
    ...(isAuthRequired && { Authorization: "Bearer " + token }), // Attach token into the request
    "Content-Type": "application/json",
    ...options.headers,
  };

  try {
    const response = await fetch(URL, { ...options, headers });

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
