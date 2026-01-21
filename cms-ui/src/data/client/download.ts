import { API_FILE_ENDPOINTS } from './api-endpoints';
import { HttpClient } from './http-client-ev';

export const downloadClient = {
  downloadFile<T>(path: string) {
    return HttpClient.getFile<T>(`${API_FILE_ENDPOINTS.ATTACHMENTS_DOWNLOAD}`, {
      file: path,
    });
  },
};
