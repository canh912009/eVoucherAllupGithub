import { URL_ASSET, URL_ASSET_LOCAL } from './client/api-endpoints';

/**
 * Return link public asset
 * @param path : (ex: '/static/images/abc.png')
 * @returns
 */
export const getUrlPublicAsset = (path: string) => {
  if (!path) {
    return null;
  }
  
  // Check if the path starts with "http://" or "https://"
  if (path.startsWith("http://") || path.startsWith("https://")) {
    return path;
  }

  return `${URL_ASSET}${path}`;
};

/**
 * Return link local asset (For deploy to PRODUCT)
 * @param path : (ex: '/static/images/abc.png')
 * @returns
 */
export const getUrlLocalAsset = (path: string) => {
  if (!path) {
    return null;
  }
  return `${URL_ASSET_LOCAL}${path}`;
};
