import moment from "moment/moment";
import Cookies from 'universal-cookie';
import { encryptPhone } from "./utils";

const TIME_EXPIRE_VOUCHER_DAYS = 30;
const PARAM_PREFIX_VOUCHER = 'v_';
const PARAM_PREFIX_MOBILE = 'm_';

const cookies = new Cookies();

/**
 * Set a cookie with the specified name, value, and expiration time in milliseconds.
 * @param {string} name - The name of the cookie.
 * @param {string} value - The value of the cookie.
 * @param {number} time - The expiration time in milliseconds.
 * @param {Object} [options] - Additional options like path, secure, etc.
 */
export const setCookie = (name, value, time, options = {}) => {
    const expires = new Date();
    expires.setTime(expires.getTime() + time);
    cookies.set(name, value, { path: '/', expires, ...options });
};

/**
 * Get the value of a cookie by name.
 * @param {string} name - The name of the cookie.
 * @returns {string|null} - The value of the cookie if found, or null if not.
 */
export const getCookie = (name) => {
    return cookies.get(name) || null;
};

/**
 * Remove a cookie by name.
 * @param {string} name - The name of the cookie.
 * @param {Object} [options] - Additional options like path, domain, etc.
 */
export const removeCookie = (name, options = {}) => {
    cookies.remove(name, { path: '/', ...options });
};

export const setCookieVoucher = (voucherId, value, days = TIME_EXPIRE_VOUCHER_DAYS) => {
    const evCookie = getCookieVoucher(voucherId);
    // If exists cookie, no need to set
    if (evCookie) {
        console.log(`Exists cookie: ${JSON.stringify(evCookie)}, no need to set`);
        return;
    }

    setCookie(`${PARAM_PREFIX_VOUCHER}${voucherId}`, value, days * 24 * 60 * 60 * 1000);
}

export const getCookieVoucher = (voucherId) => {
    return getCookie(`${PARAM_PREFIX_VOUCHER}${voucherId}`);
}

export const removeCookieVoucher = (voucherId) => {
    removeCookie(`${PARAM_PREFIX_VOUCHER}${voucherId}`);
}

export const setCookieMobile = (mobileNumber, value, days = TIME_EXPIRE_VOUCHER_DAYS) => {
    setCookie(`${PARAM_PREFIX_MOBILE}${mobileNumber}`, value, 2 * days * 24 * 60 * 60 * 1000);
}

export const getCookieMobile = (mobileNumber) => {
    return getCookie(`${PARAM_PREFIX_MOBILE}${mobileNumber}`);
}

export const removeCookieMobile = (mobileNumber) => {
    removeCookie(`${PARAM_PREFIX_MOBILE}${mobileNumber}`);
}

export const getOtpExpireDtNew = () => {
    return moment().add(TIME_EXPIRE_VOUCHER_DAYS + 1, 'days').format('YYYY-MM-DD')
}

export const setCookieMobileEncode = async (mobileNumber, { otp, otpExpireDt }) => {
    const mobileNumberEncoded = await encryptPhone(mobileNumber);
    if (!mobileNumberEncoded) {
        console.warn(`Can't encode mobile number: ${mobileNumber}`);
        return;
    }

    // Retrieve the current cookie value for this mobile number
    let currentCookieValue = getCookieMobile(encodeURIComponent(mobileNumberEncoded));

    let cookieData = {};
    if (currentCookieValue) {
        try {
            // Parse existing cookie data if it exists
            cookieData = JSON.parse(decodeURIComponent(currentCookieValue));
        } catch (error) {
            console.error('Error parsing existing cookie data', error);
        }
    }

    // Update the `otp` only if it exists in the provided object
    if (otp !== undefined) {
        cookieData.otp = otp;
    }

    // Update the `otpExpireDt` only if it exists in the provided object
    if (otpExpireDt !== undefined) {
        cookieData.otpExpireDt = otpExpireDt;

        // cookieData.otpExpireDt = moment(otpExpireDt)
        //     .add(1, 'days')
        //     .format('YYYY-MM-DD');
    }

    // Save the updated cookie data
    setCookieMobile(encodeURIComponent(mobileNumberEncoded), JSON.stringify(cookieData));
};

export const setCookieMobileEncodeUpdate = async (mobileNumber, otp) => {
    // Validate required parameters
    if (!mobileNumber || !otp) {
        console.warn('Mobile number and OTP are required');
        return;
    }

    const mobileNumberEncoded = await encryptPhone(mobileNumber);
    if (!mobileNumberEncoded) {
        console.warn(`Can't encode mobile number: ${mobileNumber}`);
        return;
    }

    // Get current cookie value
    let currentCookieValue = getCookieMobile(encodeURIComponent(mobileNumberEncoded));
    let currentCookieData = {};

    if (currentCookieValue) {
        try {
            currentCookieData = JSON.parse(decodeURIComponent(currentCookieValue));
        } catch (error) {
            console.error('Error parsing existing cookie data', error);
        }
    }

    // Check if OTP cookie exists and is still valid
    if (currentCookieData?.otp && currentCookieData?.otpExpireDt) {
        const isOtpMatch = currentCookieData.otp === otp;
        const isOtpValid = moment(currentCookieData.otpExpireDt).isAfter(moment());

        if (isOtpMatch && isOtpValid) {
            console.log('OTP still valid, no update needed');
            return;
        }
    }

    // Prepare new cookie data
    const cookieData = {
        otp,
        otpExpireDt: getOtpExpireDtNew()
    };

    // Save the cookie with new expiration (31 days)
    setCookieMobile(
        encodeURIComponent(mobileNumberEncoded),
        JSON.stringify(cookieData)
    );
};

export const getCookieMobileEncode = async (mobileNumber) => {
    const mobileNumberEncoded = await encryptPhone(mobileNumber);
    if (!mobileNumberEncoded) {
        console.warn(`Can't encode mobile number: ${mobileNumber}`);
        return;
    }

    return getCookieMobile(encodeURIComponent(mobileNumberEncoded));
}

