import { HttpClient } from './http-client-ev';
import { API_FILE, API_FILE_ENDPOINTS } from './api-endpoints';
import { UploadResponse } from '@/types';

export const uploadClient = {
  uploadImage: async (attachment: any) => {
    let formData = new FormData();
    formData.append('image', attachment);
    const options = {
      headers: {
        'Content-Type': 'multipart/form-data',
      },
    };
    return HttpClient.post<UploadResponse>(
      API_FILE.BASE_URL + API_FILE_ENDPOINTS.IMAGES_UPLOAD,
      formData,
      options
    );
  },

  uploadImages: async (attachments: any) => {
    let formData = new FormData();
    attachments.forEach((attachment: any) => {
      formData.append('images', attachment);
    });

    const options = {
      headers: {
        'Content-Type': 'multipart/form-data',
      },
    };
    return HttpClient.post<UploadResponse>(
      API_FILE.BASE_URL + API_FILE_ENDPOINTS.IMAGES_UPLOADS,
      formData,
      options
    );
  },

  uploadFile: async (file: any) => {
    let formData = new FormData();
    formData.append('file', file);
    const options = {
      headers: {
        'Content-Type': 'multipart/form-data',
      },
    };
    return HttpClient.post<UploadResponse>(
      API_FILE.BASE_URL + API_FILE_ENDPOINTS.ATTACHMENTS_UPLOAD,
      formData,
      options
    );
  },

  uploadFiles: async (files: any) => {
    let formData = new FormData();
    files.forEach((file: any) => {
      formData.append('files', file);
    });
    const options = {
      headers: {
        'Content-Type': 'multipart/form-data',
      },
    };
    return HttpClient.post<UploadResponse>(
      API_FILE.BASE_URL + API_FILE_ENDPOINTS.ATTACHMENTS_UPLOAD,
      formData,
      options
    );
  },

  downloadFiles: async (path: string) => {
    return HttpClient.post<any>(
      API_FILE.BASE_URL + API_FILE_ENDPOINTS.ATTACHMENTS_DOWNLOAD , {path})
  },
};
