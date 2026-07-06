import { createContext, useContext, useState, useMemo, useEffect } from "react";
import client, { attachToken } from "../api/client";

const AuthContext = createContext(null);

export function AuthProvider({ children }) {
  const [auth, setAuth] = useState(null); // { token, username, role }

  useEffect(() => {
    attachToken(() => auth?.token);
  }, [auth]);

  const login = async (username, password) => {
    const res = await client.post("/auth/login", { username, password });
    setAuth(res.data);
    return res.data;
  };

  const logout = () => setAuth(null);

  const value = useMemo(() => ({ auth, login, logout }), [auth]);

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
}

export function useAuth() {
  return useContext(AuthContext);
}
