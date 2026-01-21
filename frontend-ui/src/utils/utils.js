import IMAGE_FAKE_MADONALD from "../images/imageVoucher.png";
import IMAGE_TIKER_USED from '../images/Sticker_EN/used.png'
import IMAGE_TIKER_DISABLE from '../images/Sticker_EN/disable.png'
import IMAGE_TIKER_EXPIRED from '../images/Sticker_EN/expired.png'
import IMAGE_TIKER_RECIEVED from '../images/Sticker_EN/received.png'
import IMAGE_TIKER_TRANSFERRED from '../images/Sticker_EN/transferred.png'
import IMAGE_VOUCHER_DEFAULT from '../images/imageVoucher.png'

import IMAGE_TIKER_USED_VN from '../images/Sticker_VN/used.png'
import IMAGE_TIKER_DISABLE_VN from '../images/Sticker_VN/disable.png'
import IMAGE_TIKER_EXPIRED_VN from '../images/Sticker_VN/expired.png'
import IMAGE_TIKER_RECIEVED_VN from '../images/Sticker_VN/received.png'
import IMAGE_TIKER_TRANSFERRED_VN from '../images/Sticker_VN/transferred.png'
import SystemType from "./SystemType";

import Cookies from 'universal-cookie';

import React from "react";
import moment from "moment/moment";
import VoucherStatus from "./VoucherStatus";

export const AQUA_HOTLINE = "024.3232.3839";
export const AQUA_EMAIL = "support@aquaretail.net";
export const REGEX_VIETNAMESE_PHONE_NUMBER = /^(0[235789][0-9]{8}|(\+84|0084)[235789][0-9]{8})$/;

export const VERSIONS = {
    V1: 'version_1',
    V2: 'version_2',
}

export function isVersionV1(data) {
    return data?.version === VERSIONS.V1;
}

export function isVersionV2(data) {
    return data?.version === VERSIONS.V2;
}


/**
 * Checks if a phone number is a valid Vietnamese phone number.
 * 
 * @param {string} phoneNumber - The phone number to be validated.
 * @returns {boolean} - Returns true if the phone number is valid, false otherwise.
 */
export function isValidVietnamesePhoneNumber(phoneNumber) {
    return REGEX_VIETNAMESE_PHONE_NUMBER.test(phoneNumber);
}

/**
 * Normalizes a Vietnamese phone number by converting:
 * - Numbers starting with '0084' or '+84' to the '0...' format.
 * Then, validates if it's a valid Vietnamese phone number.
 * 
 * @param {string} phoneNumber - The phone number to be normalized and validated.
 * @returns {string|null} - Returns the normalized phone number if valid, otherwise returns null.
 */
export function normalizeVietnamesePhoneNumber(phoneNumber) {
    // Check if the original phone number is valid
    if (!isValidVietnamesePhoneNumber(phoneNumber)) {
        return null;  // Return null if the phone number is not valid
    }

    // If it's valid, normalize '0084' or '+84' to '0'
    let normalizedNumber = phoneNumber;

    if (phoneNumber.startsWith('0084')) {
        normalizedNumber = phoneNumber.replace(/^0084/, '0');
    } else if (phoneNumber.startsWith('+84')) {
        normalizedNumber = phoneNumber.replace(/^\+84/, '0');
    }

    // Return the normalized number if it was valid
    return normalizedNumber;
}

export function IsNumeric(input) {
    return (input - 0) === input && ('' + input).trim().length > 0;
}

export function ConvertNumber(number) {
    // return number.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ".");
    try {
        if (typeof number !== 'number' || isNaN(number)) {
            return '';
        }
        return number.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ".");
    } catch (error) {
        return '';
    }
}


export const isExpiredVoucher = (dataVoucher) => {
    if (!dataVoucher) {
        console.log("Voucher doesn't exists");
        return false;
    }
    // Check by voucher status
    if (dataVoucher.voucherStatus === VoucherStatus.EXPIRE) {
        return true;
    }
    // check by voucher expire time
    //
    if (!dataVoucher.expireDate || dataVoucher === '') {
        console.log("Voucher doesn't have expire date -> not expired");
        return false;
    }
    const voucherExpireDate = dataVoucher.expireDate;
    const voucherExpireTime = moment(voucherExpireDate);
    const currentTime = moment();

    return voucherExpireTime.isBefore(currentTime);
}

export function getStatus(dataVoucher) {
    const cookies = new Cookies();
    const isVietnamese = cookies.get('locales') === "vi";
    if (dataVoucher && isExpiredVoucher(dataVoucher)) {
        if (dataVoucher.voucherStatus === "USED") {
            return isVietnamese ? IMAGE_TIKER_USED_VN : IMAGE_TIKER_USED;
        }
        return isVietnamese ? IMAGE_TIKER_EXPIRED_VN : IMAGE_TIKER_EXPIRED;
    } else if (dataVoucher && dataVoucher.voucherStatus === "NORMAL" && dataVoucher.transferStatus === "RECPTED") {
        return isVietnamese ? IMAGE_TIKER_RECIEVED_VN : IMAGE_TIKER_RECIEVED;
    } else if (dataVoucher && dataVoucher.voucherStatus === "USED") {
        if (dataVoucher.system === "VNPT_EPAY" && dataVoucher.transferStatus === "RECPTED") {
            return isVietnamese ? IMAGE_TIKER_RECIEVED_VN : IMAGE_TIKER_RECIEVED;
        }
        return isVietnamese ? IMAGE_TIKER_USED_VN : IMAGE_TIKER_USED;
    } else if (dataVoucher && dataVoucher.voucherStatus === "DISABLED") {
        if (dataVoucher.transferStatus === "TRANSFER") {
            return isVietnamese ? IMAGE_TIKER_TRANSFERRED_VN : IMAGE_TIKER_TRANSFERRED;
        } else {
            return isVietnamese ? IMAGE_TIKER_DISABLE_VN : IMAGE_TIKER_DISABLE;
        }
    }
}

export function getImage(dataVoucher) {
    if (dataVoucher && dataVoucher.voucherType === "SI") {
        return IMAGE_VOUCHER_DEFAULT
    } else if (dataVoucher && dataVoucher.voucherType === "PP") {
        return IMAGE_VOUCHER_DEFAULT
    } else if (dataVoucher && dataVoucher.voucherType === "DC") {
        return IMAGE_VOUCHER_DEFAULT
    } else {
        return IMAGE_FAKE_MADONALD
    }
}

export function renderMessage(code, t) {
    console.log("Message code", code);
    if (code === 10001) {
        return t('voucher.UNKNOWN_ERROR')
    } else if (code === 1001) {
        return t('voucher.VOUCHER_NOT_FOUND')
    } else if (code === 1002) {
        return t('voucher.EXCHANGE_FAIL')
    } else if (code === 1003) {
        return t('voucher.VOUCHER_USED')
    } else if (code === 1004) {
        return t('voucher.VOUCHER_CANNOT_TRANSFER')
    } else if (code === 1005) {
        return t('voucher.VOUCHER_TRANSFER_PROCESSING')
    } else if (code === 1006) {
        return t('voucher.VOUCHER_LIMIT_AMOUNT')
    } else if (code === 1007) {
        return t('voucher.OTP_INVALID_OR_EXPIRE')
    } else if (code === 1008) {
        return t('voucher.GENERATE_OTP_FAIL')
    } else if (code === 1009) {
        return t('voucher.VOUCHER_REMAINING_SMALL')
    } else if (code === 1010) {
        return t('voucher.VOUCHER_CANNOT_RECEIPT')
    } else if (code === 1011) {
        return t('voucher.VOUCHER_NOT_WAITING_RECEIPT')
    } else if (code === 1012) {
        return t('voucher.CAN_NOT_FIND_HISTORY_TRANSFER')
    } else if (code === 1013) {
        return t('voucher.CAN_NOT_FIND_OLD_VOUCHER')
    } else if (code === 1014) {
        return t('voucher.CANCEL_PAYMENT_EXPIRE')
    } else if (code === 1015) {
        return t('voucher.CANCEL_VOUCHER_PAYMENT_NOT_USE')
    } else if (code === 1016) {
        return t('voucher.CANCEL_VOUCHER_NOT_USE')
    } else if (code === 1017) {
        return t('voucher.PAYMENT_HISTORY_NOT_FOUND')
    } else if (code === 1018) {
        return t('voucher.CANCEL_STORE_NOT_SAME')
    } else if (code === 1019) {
        return t('voucher.CANCEL_AMOUNT_GREATER')
    } else if (code === 1020) {
        return t('voucher.VOUCHER_IS_EXPIRE')
    } else if (code === 1021) {
        return t('voucher.STORE_NOT_FOUND')
    } else if (code === 1022) {
        return t('voucher.VOUCHER_IS_DISABLE')
    } else if (code === 1023) {
        return t('voucher.VOUCHER_IS_TRANSFER')
    } else if (code === 1024) {
        return t('voucher.SELF_TRANSFER_IS_NOT_ALLOWED')
    } else if (code === 1026) {
        return t('voucher.PRODUCT_QUANTITY_NOT_ENOUGH')
    } else if (code === 1027) {
        return t('voucher.CAN_NOT_TRANSFER_CHOICE_VOUCHER')
    } else if (code === 1028) {
        return t('voucher.ERROR_CREATE_VOUCHER_CHOICE')
    } else if (code === 1088) {
        return t('voucher.CAN_NOT_FIND_PUBLISH')
    } else if (code === 1089) {
        return t('voucher.CAN_NOT_FIND_USER')
    } else if (code === 1090) {
        return t('voucher.CAN_NOT_FIND_CATEGORY')
    } else if (code === 1091) {
        return t('voucher.CAN_NOT_FIND_BRAND')
    } else if (code === 1092) {
        return t('voucher.CAN_NOT_FIND_GOODS')
    } else if (code === 1093) {
        return t('voucher.CAN_NOT_FIND_SUPPLIER')
    } else if (code === 1096) {
        return t('voucher.VNPT_PROVIDER_NOT_FOUND')
    } else if (code === 1097) {
        return t('voucher.BULK_CATEGORY_IS_EMPTY')
    } else if (code === 1098) {
        return t('voucher.VNPT_PROVIDER_IS_INVALID')
    } else if (code === 1201) {
        return t('voucher.RECEIVER_PHONE_NUMBER_IS_REQUIRED_FOR_TOPUP')
    } else if (code === 1209) {
        return t('voucher.CAN_NOT_PURCHASE_FROM_VNPT_EPAY')
    } else if (code === 1210) {
        return t('voucher.EXCEPTION_WHILE_REQUESTING_TO_BE')
    } else if (code === 1300) {
        return t('voucher.STORE_INVALID_OR_NO_PERMISSION')
    } else if (code === 1400) {
        return t('voucher.OTP_IS_REQUIRED')
    } else if (code === 1401) {
        return t('voucher.OTP_MISMATCH')
    } else if (code === 1500) {
        return t('voucher.VNPT_REQUEST_IN_PROGRESS')
    } else if (code === 4041) {
        return t('voucher.SERIAL_NUMBER_DOES_NOT_MATCH')
    } else if (code === 6000) {
        return t('voucher.INVALID_REQUEST_DATA');
    } else if (code === 6001) {
        return t('voucher.TRANSACTION_IN_PROGRESS');
    } else if (code === 6002) {
        return t('voucher.TRANSACTION_TIMEOUT');
    } else if (code === 6003) {
        return t('voucher.TRANSACTION_FAILED');
    } else if (code === 6004) {
        return t('voucher.PRODUCT_NOT_FOUND');
    } else if (code === 6005) {
        return t('voucher.PRODUCT_NOT_SUPPORTED');
    } else if (code === 6006) {
        return t('voucher.TRANSACTION_ALREADY_EXISTS');
    } else if (code === 6007) {
        return t('voucher.INTERNAL_PAYMENT_FAILED');
    } else if (code === 6008) {
        return t('voucher.VOUCHER_GENERATION_ERROR');
    } else if (code === 6009 || code === 6010) {
        return t('voucher.SYSTEM_ERROR');
    } else if (code === 6011) {
        return t('voucher.WATANE_REQUEST_ERROR');
    } else if (code === 6099) {
        return t('voucher.WATANE_API_ERROR');
    } else if (code === 8888) {
        return t('voucher.VOUCHER_IS_BEING_PROCESSED')
    } else if (code === 9898) {
        return t('voucher.EXCEPTION_WHILE_REQUESTING_FE')
    } else if (code === 10010) {
        return t('voucher.INVALID_SHORT_LINK')
    } else if (code === 10011) {
        return t('voucher.INACTIVE_BRAND')
    } else if (code === 10012) {
        return t('voucher.INACTIVE_GOOD')
    } else if (code === 10013) {
        return t('voucher.CHOICE_GOOD_ID_NULL')
    } else if (code === 10014) {
        return t('voucher.PRODUCT_IS_EXPIRED')
    } else return t('voucher.UNKNOWN_ERROR')
}

export function getPrice(dataVoucher, t) {
    const Render = () => {
        if (dataVoucher && dataVoucher.voucherType === "SI") {
            return <div className="item_right">
                <h1>{t('voucher.priceVoucher')}</h1>
                <h2>{dataVoucher ? ConvertNumber(dataVoucher?.voucherPrice) : ''} VNĐ</h2>
            </div>
        } else if (dataVoucher && dataVoucher.voucherType === "PP") {
            return <div className="item_right">
                <h1>{t('voucher.balance')}</h1>
                <h2>{dataVoucher ? ConvertNumber(dataVoucher?.balance) : ''} VNĐ</h2>
            </div>
        } else if (dataVoucher && dataVoucher.voucherType === "DC") {
            return <div className="item_right">
                <h1>{t('voucher.discountRate')}</h1>
                <h2>{dataVoucher ? ConvertNumber(dataVoucher?.discountRate) : ''}%
                    (Limit: {ConvertNumber(dataVoucher?.discountLimitPrice)} VNĐ)</h2>
            </div>
        } else {
            return <div className="item_right">
                <h1>{t('voucher.priceVoucher')}</h1>
                <h2>{dataVoucher ? ConvertNumber(dataVoucher?.voucherPrice) : ''} VNĐ</h2>
            </div>
        }
    }

    return (
        <Render />
    )
}

export function getPriceProduct(dataVoucher, t) {
    const Render = () => {
        // if (dataVoucher && dataVoucher.voucherType === "SI") {
        //     return <div className="item_right">
        //         <span>: {dataVoucher ? ConvertNumber(dataVoucher?.voucherPrice) : ''} VNĐ</span>
        //     </div>
        // } else if (dataVoucher && dataVoucher.voucherType === "PP") {
        //     return <div className="item_right">
        //         <span>: {dataVoucher ? ConvertNumber(dataVoucher?.balance) : ''} VNĐ</span>
        //     </div>
        // } else if (dataVoucher && dataVoucher.voucherType === "DC") {
        //     return <div className="item_right">
        //         <span>: {dataVoucher ? ConvertNumber(dataVoucher?.discountRate) : ''}%
        //             (Limit: {ConvertNumber(dataVoucher?.discountLimitPrice)} VNĐ)</span>
        //     </div>
        // } else {
        //     return <div className="item_right">
        //         <span>: {dataVoucher ? ConvertNumber(dataVoucher?.voucherPrice) : ''} VNĐ</span>
        //     </div>
        // }
        return <div className="item_right">
            <span>: {dataVoucher ? ConvertNumber(dataVoucher?.initialAmount) : ''} VNĐ</span>
        </div>
    }

    return (
        <Render />
    )
}

export function toTimestamp(strDate) {
    if (strDate) {
        const datum = Date.parse(strDate);
        return datum / 1000;
    }
}

export function formatTimeSeconds(seconds) {
    return `${Math.floor(seconds / 60).toString().padStart(2, '0')}:${(seconds % 60).toString().padStart(2, '0')}`;
}

export const encryptData = async (value) => {
    // console.log(`encryptData input: ${value}`)
    const key = process.env.REACT_APP_KEY_ENCRYPT
    const initVector = process.env.REACT_APP_INIT_VECTOR
    // Replace with your actual initialization vector

    if (value && value !== "") {
        try {
            const encoder = new TextEncoder();
            const data = encoder.encode(value.toUpperCase());
            const keyBuffer = await crypto.subtle.importKey('raw', encoder.encode(key), 'AES-CBC', false, ['encrypt']);
            const encryptedBuffer = await crypto.subtle.encrypt({
                name: 'AES-CBC',
                iv: encoder.encode(initVector)
            }, keyBuffer, data);

            const encryptedValue = btoa(String.fromCharCode(...new Uint8Array(encryptedBuffer)));
            // console.log(`encryptData encryptedValue: ${encryptedValue}`)
            return encryptedValue;
        } catch (ex) {
            console.error(ex.message, ex);
        }
    }
    return null;
};


export const decryptData = async (encryptedValue) => {
    const key = process.env.REACT_APP_KEY_ENCRYPT
    const initVector = process.env.REACT_APP_INIT_VECTOR
    if (encryptedValue && encryptedValue !== "") {
        try {
            const decoder = new TextDecoder();
            const encryptedBuffer = new Uint8Array(atob(encryptedValue).split('').map(c => c.charCodeAt(0)));
            const keyBuffer = await crypto.subtle.importKey('raw', new TextEncoder().encode(key), 'AES-CBC', false, ['decrypt']);
            const decryptedBuffer = await crypto.subtle.decrypt({
                name: 'AES-CBC',
                iv: new TextEncoder().encode(initVector)
            }, keyBuffer, encryptedBuffer);

            const decryptedValue = decoder.decode(decryptedBuffer).toLowerCase(); // Decode and convert to lowercase if needed
            return decryptedValue;
        } catch (ex) {
            console.error(ex.message, ex);
        }
    }
    return null;
};

export const encryptPhone = async (phone) => {
    const encryptedPhone = await encryptData(normalizeVietnamesePhoneNumber(phone));
    return encryptedPhone;
};

export function getImageSrc(imagePath) {
    if (!imagePath) {
        return '';
    }
    return (imagePath.startsWith('http://') || imagePath.startsWith('https://'))
        ? imagePath
        : process.env.REACT_APP_IMAGE_URL + imagePath;
}

export function isUnlimitedProduct(item) {
    if (!item) return false;
    return item.system === SystemType.INTERNAL || item.system === SystemType.GIFTPOP || item.system === SystemType.UR_BOX || item.system === SystemType.VNPT_EPAY || item.system === SystemType.WATANE;
}
