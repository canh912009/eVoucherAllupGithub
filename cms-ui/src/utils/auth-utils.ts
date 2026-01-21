import Cookie from 'js-cookie';
// @ts-ignore
import SSRCookie from 'cookie';
import {
  AUTH_CRED, EMAIL_VERIFIED, PERMISSIONS_KEY,
  PERMISSIONS_EV as p,
  TOKEN_KEY, USER_INFO
} from './constants';
import { User } from '@/types';

// export const allowedRoles = [p.SUPER_ADMIN, p.ADMIN, p.STORE_OWNER, p.STAFF, p.CUSTOMER];
export const allowedRolesEV = [p.ROLE_ADMIN, p.ROLE_OPERATOR,   p.ROLE_CUSTOMER,   p.ROLE_SUPPLIER, p.ROLE_BRAND, p.ROLE_STORE];
export const adminAndOwnerOnly = [p.ROLE_ADMIN, p.ROLE_OPERATOR, p.ROLE_STORE];
export const adminOwnerAndStaffOnly = [p.ROLE_ADMIN, p.ROLE_OPERATOR, p.ROLE_STORE/* , p.STAFF */];
export const adminOnly = [p.ROLE_ADMIN, p.ROLE_OPERATOR];
export const customerOnly = [p.ROLE_CUSTOMER];
export const supplierOnly = [p.ROLE_SUPPLIER];
export const brandOnly = [p.ROLE_BRAND];
export const storeOnly = [p.ROLE_STORE];

export function setAuthCredentials(token: string, permissions: any) {
  Cookie.set(AUTH_CRED, JSON.stringify({ token, permissions }));
}
export function setEmailVerified(emailVerified: boolean) {
  Cookie.set(EMAIL_VERIFIED, JSON.stringify({ emailVerified }));
}
export function getEmailVerified(): {
  emailVerified: boolean;
} {
  const emailVerified = Cookie.get(EMAIL_VERIFIED);
  return emailVerified ? JSON.parse(emailVerified) : false;
}

export function getAuthCredentials(context?: any): {
  token: string | null;
  permissions: string[] | null;
} {
  let authCred;
  if (context) {
    authCred = parseSSRCookie(context)[AUTH_CRED];
  } else {
    authCred = Cookie.get(AUTH_CRED);
  }
  if (authCred) {
    return JSON.parse(authCred);
  }
  return { token: null, permissions: null };
}

export function parseSSRCookie(context: any) {
  return SSRCookie.parse(context.req.headers.cookie ?? '');
}

export function hasAccess(
  _allowedRoles: string[],
  _userPermissions: string[] | undefined | null
) {
  if (_userPermissions) {
    return Boolean(
      _allowedRoles?.find((aRole) => _userPermissions.includes(aRole))
    );
  }
  return false;
}

export function isAuthenticated(_cookies: any) {
  return (
    !!_cookies[TOKEN_KEY] &&
    Array.isArray(_cookies[PERMISSIONS_KEY]) &&
    !!_cookies[PERMISSIONS_KEY].length
  );
}

/**
 * Checks whether at least one role from the provided roles array exists in the user's permissions.
 * @param roles - An array of roles to check against the user's permissions.
 * @returns {boolean} - True if at least one role exists, otherwise false.
 */
export function isPermitted(roles: string[]): boolean {
  const { permissions } = getAuthCredentials();
  return hasAccess(roles, permissions);
}

export function setUserInfo(user: User) {
  Cookie.set(USER_INFO, JSON.stringify(user));
}

export function getUserInfo() {
  const userStr = Cookie.get(USER_INFO);
  return userStr ? JSON.parse(userStr) : null;
}