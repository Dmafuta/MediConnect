import axiosInstance from './axiosInstance';

export interface MeUser {
  id: number;
  username: string;
  email: string;
  isActive: boolean;
  needsPasswordUpdate: boolean;
  roles: string[];
  permissions: string[];
}

export const login = async (username: string, password: string): Promise<string> => {
  const response = await axiosInstance.post<{ token: string }>('/auth/login', { username, password });
  return response.data.token;
};

export const fetchMe = async (): Promise<MeUser> => {
  const response = await axiosInstance.get<MeUser>('/auth/me');
  return response.data;
};

export const logout = async (): Promise<void> => {
  await axiosInstance.post('/auth/logout').catch(() => {});
};
