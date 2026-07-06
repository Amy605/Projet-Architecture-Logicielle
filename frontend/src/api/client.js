import axios from "axios";

const client = axios.create({
  baseURL: "/api",
});

// Injecte le jeton JWT (en memoire, jamais dans localStorage) sur chaque requete
export function attachToken(getToken) {
  client.interceptors.request.use((config) => {
    const token = getToken();
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  });
}

export default client;
