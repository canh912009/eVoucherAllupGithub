import { validate as isUUID, version } from 'uuid';

export const checkUUID = (str) => {
    if (isUUID(str)) {
        // Check UUID version 4
        return version(str) === 4;
    }
    return false;
};

export const checkSerialNumber = (str) => !!(str && str.trim().length > 0);
export const checkVoucherId = (str) => !!(str && str.trim().length > 0);