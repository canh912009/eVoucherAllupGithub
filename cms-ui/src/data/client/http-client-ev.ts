import axios, {AxiosError, AxiosResponse} from 'axios';
import Cookies from 'js-cookie';
import Router from 'next/router';
import invariant from 'tiny-invariant';
import {AUTH_CRED, TOKEN_KEY, USER_INFO} from '@/utils/constants';
import {Routes} from "@/config/routes";

invariant(
  process.env.NEXT_PUBLIC_REST_API_ENDPOINT_EV,
  'NEXT_PUBLIC_REST_API_ENDPOINT_EV is not defined, please define it in your .env file'
);
const Axios = createAxios(process.env.NEXT_PUBLIC_REST_API_ENDPOINT_EV);

interface AuthToken {
  [TOKEN_KEY: string]: string;
}

function createAxios(baseURL: string) {
  const AUTH_TOKEN_KEY = process.env.NEXT_PUBLIC_AUTH_TOKEN_KEY ?? AUTH_CRED;
  const DATA_ACCESS_ERROR = "DataAccess error!" ;
  const UNAUTHORIZED_USER_ERROR = "Unauthorized user access!" ;

  // Khởi tạo instance axios với config mặc định
  const axiosInstance = axios.create({
    baseURL,
    timeout: 120000,
    headers: {
      'Content-Type': 'application/json',
    },
  });

  // Request interceptor
  axiosInstance.interceptors.request.use((config) => {
    try {
      const authCookie = Cookies.get(AUTH_TOKEN_KEY);
      if (authCookie) {
        const { [TOKEN_KEY]: token } = JSON.parse(authCookie) as AuthToken;
        config.headers.Authorization = `Bearer ${token}`;
      }
      return config;
    } catch (error) {
      return Promise.reject(error);
    }
  });

  // Response interceptor
  axiosInstance.interceptors.response.use(
    (response) => response,
    (error) => {
      console.log("API Error:", error);

      // Check unauthorized errors
      const isUnauthorized =
        (!error.response && !error.request?.withCredentials) ||  //check serverAPI down
        error.response?.status === 401 ||
        error.response?.status === 403 ||
        [DATA_ACCESS_ERROR, UNAUTHORIZED_USER_ERROR].includes(error.response?.data?.message) ; //message name from server

      // Check server/network errors
      const isServerError =
        error.code === 'ECONNABORTED' ||
        error.code === 'ERR_NETWORK' ||
        !error.response ||
        error.response.status >= 500;

      if (isUnauthorized) {
        handleUnauthorized();
      }

      if (isServerError) {
        handleServerError(error);
      }

      return Promise.reject(error);
    }
  );

  return axiosInstance;
}

// Tách logic xử lý lỗi thành các hàm riêng
function handleUnauthorized() {
  console.log("Unauthorized - Clearing credentials");
  Cookies.remove(AUTH_CRED);
  Cookies.remove(USER_INFO);
  Router.push(Routes.dashboard);
}

function handleServerError(error: AxiosError) {
  console.log("Server error:", error.message);
  // Xử lý thêm logic khi server lỗi nếu cần
}

function formatBooleanSearchParam(key: string, value: boolean) {
  return value ? `${key}:1` : `${key}:`;
}

interface SearchParamOptions {
  categories: string;
  code: string;
  type: string;
  name: string;
  shop_id: string;
  is_approved: boolean;
  tracking_number: string;
  notice: string;
}

export class HttpClient {
  static async get<T>(url: string, params?: unknown) {
    const response = await Axios.get<T>(url, { params });
    return response.data;
  }

  static async post<T>(url: string, data: unknown, options?: any) {
    const response = await Axios.post<T>(url, data, options);
    return response.data;
  }

  static async put<T>(url: string, data: unknown) {
    const response = await Axios.put<T>(url, data);
    return response.data;
  }

  static async patch<T>(url: string, data: unknown) {
    const response = await Axios.patch<T>(url, data);
    return response.data;
  }

  static async delete<T>(url: string) {
    const response = await Axios.delete<T>(url);
    return response.data;
  }

  static async getFile<T>(url: string, params?: unknown) {
    const response: AxiosResponse<ArrayBuffer> = await Axios.get<ArrayBuffer>(url, {params, responseType: 'arraybuffer' });
    const buffer = Buffer.from(new Uint8Array(response.data)).toString('base64');
    const data = `data:${response.headers['content-type']};base64,${buffer}`;
    return data;
  }

  static formatSearchParams(params: Partial<SearchParamOptions>) {
    return Object.entries(params)
      .filter(([, value]) => Boolean(value))
      .map(([k, v]) =>
        ['type', 'categories', 'tags', 'author', 'manufacturer'].includes(k)
          ? `${k}.slug:${v}`
          : ['is_approved'].includes(k)
            ? formatBooleanSearchParam(k, v as boolean)
            : `${k}:${v}`
      )
      .join(';');
  }
}

export function getFormErrors(error: unknown) {
  if (axios.isAxiosError(error)) {
    return error.response?.data.message;
  }
  return null;
}

export function getFieldErrors(error: unknown) {
  if (axios.isAxiosError(error)) {
    return error.response?.data.errors;
  }
  return null;
}
