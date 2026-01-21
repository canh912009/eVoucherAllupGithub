import { AQUA_EMAIL, AQUA_HOTLINE, renderMessage } from "../../../utils/utils";

export const TELCO_PREFIX_NUM = {
    VIETTEL: ['032', '033', '034', '035', '036', '037', '038', '039', '086', '096', '097', '098'],
    MOBIFONE: ['070', '076', '077', '078', '079', '089', '090', '093'],
    VINAPHONE: ['081', '082', '083', '084', '085', '088', '091', '094'],
    VIETNAMMOBILE: ['052', '056', '058', '092'],
    GTEL: ['059', '099'],
    WINTEL: ['055'],
    ITELECOM: ['087']
}

export const TELCO_NAMES = {
    VIETTEL: 'Viettel',
    MOBIFONE: 'Mobifone',
    VINAPHONE: 'Vinaphone',
    VIETNAMMOBILE: 'Vietnamobile',
    GTEL: 'Gtel',
    WINTEL: 'Wintel',
    ITELECOM: 'Itelecom'
}

export const PROVIDERS = {
    VIETTEL: 'VTT',
    VIETTEL_DATA: 'VTTDATA3',
    MOBIFONE: 'VMS',
    MOBIFONE_DATA: 'VMSDATA2',
    VINAPHONE: 'VNP',
    VINAPHONE_DATA: 'VNPDATA',
    GTEL: 'BEE',
    WINTEL: 'WINTEL',
    VIETNAMMOBILE: 'VNM',
}

// Define constants or enums for options
export const TOPUP_OPTIONS = {
    CARDCODE: 'CARDCODE',
    TOPUP: 'TOPUP',
};

export const ALLOWED_ACTIONS = {
    CARDCODE_AND_TOPUP: 'CARDCODE_AND_TOPUP',
    TOPUP: 'TOPUP',
    CARDCODE: 'CARDCODE',
}

// After Redeem action CARDCODE successfully
export const TYPE_CARD_CODE = {
    CARD_MOBILE: 'CARD_MOBILE',
    CARD_MOBILE_DATA: 'CARD_MOBILE_DATA',
    CARD_GAME: 'CARD_GAME',
}

export const TYPE_PROVIDERS_MOBILE = [
    PROVIDERS.VIETTEL, PROVIDERS.VINAPHONE, PROVIDERS.MOBIFONE, PROVIDERS.VIETNAMMOBILE, PROVIDERS.WINTEL, PROVIDERS.GTEL
]

export const TYPE_PROVIDERS_MOBILEDATA = [
    PROVIDERS.VIETTEL_DATA, PROVIDERS.VINAPHONE_DATA, PROVIDERS.MOBIFONE_DATA
]

const getTypeCardByProviderCode = (providerCode) => {
    if (TYPE_PROVIDERS_MOBILE.includes(providerCode)) {
        return TYPE_CARD_CODE.CARD_MOBILE;
    } else if (TYPE_PROVIDERS_MOBILEDATA.includes(providerCode)) {
        return TYPE_CARD_CODE.CARD_MOBILE_DATA;
    } else {
        return TYPE_CARD_CODE.CARD_GAME;
    }
}

export const getTypeCardCodeByProduct = (vnptProduct) => {
    const providerCode = vnptProduct?.providerCode;
    if (!providerCode) {
        return null;
    }

    return getTypeCardByProviderCode(providerCode);
}

export const getTypeCardCode = (data) => {
    const providerCode = data?.selectedAction === TOPUP_OPTIONS.CARDCODE ? data?.cardCodeResult?.providerCode : null;
    if (!providerCode) {
        return null;
    }

    return getTypeCardByProviderCode(providerCode);
}

export const topupSyntaxExpress = (dataVoucher) => {
    const code = dataVoucher?.goods?.vnpt?.cardCodeResult?.code;
    const typeCardCode = getTypeCardCode(dataVoucher?.goods?.vnpt);
    console.log(`typeCardCode ${typeCardCode}, code ${code}`);

    if (!code || !typeCardCode) {
        return '';
    }

    const providerCode = dataVoucher?.goods?.vnpt?.cardCodeResult?.providerCode;
    console.log(`providerCode ${providerCode}`);

    if (TYPE_CARD_CODE.CARD_MOBILE === typeCardCode) {
        if (PROVIDERS.WINTEL === providerCode) {
            return `tel:*055*${code}%23`;
        } else {
            return `tel:*100*${code}%23`;
        }
    } else if (TYPE_CARD_CODE.CARD_MOBILE_DATA === typeCardCode) {
        if (PROVIDERS.VIETTEL_DATA === providerCode) {
            return `tel:*191*68*${code}%23`;
        } else if (PROVIDERS.MOBIFONE_DATA === providerCode) {
            return `tel:*090*9*${code}%23`;
        } else if (PROVIDERS.VINAPHONE_DATA === providerCode) {
            return `tel:*100*${code}%23`;
        } else {
            return ``;
        }
    } else if (TYPE_CARD_CODE.CARD_GAME === typeCardCode) {
        return ``;
    }
}

export const getProvider = (phoneNumber) => {
    if (!phoneNumber) {
        return null;
    }

     // Check if the phone number starts with '0084' or '+84' and replace with '0'
     if (phoneNumber.startsWith('0084')) {
        phoneNumber = '0' + phoneNumber.slice(4);
    } else if (phoneNumber.startsWith('+84')) {
        phoneNumber = '0' + phoneNumber.slice(3);
    }

    const prefix = phoneNumber.slice(0, 3);

    for (const provider in TELCO_PREFIX_NUM) {
        if (TELCO_PREFIX_NUM[provider].includes(prefix)) {
            const providerDataCode = PROVIDERS[`${provider}_DATA`] || null;
            return { providerCode: PROVIDERS[provider], providerNm: TELCO_NAMES[provider], providerDataCode };
        }
    }

    return null;
}

export const getKeyProvider = (providerDataCode) => {
    if (!providerDataCode) {
        return null;
    }
    for (const key in PROVIDERS) {
        if (key.endsWith('_DATA') && PROVIDERS[key] === providerDataCode) {
            return key.replace('_DATA', '');
        }
    }
    return null;
};

export const getTelcoName = (providerDataCode) => {
    const key = getKeyProvider(providerDataCode);
    if (!key) {
        return null;
    }

    return TELCO_NAMES[key];
}

export const VNPTEPAY_ERROR_CODE = {
    INCORRECT_PHONE: -1,

    PROVIDER_NOT_FOUND: 1096,
    PROVIDER_INVALID: 1098,
    RECEIVER_PHONE_IS_REQUIRED: 1201,
    CAN_NOT_PURCHASE_FROM_VNPT_EPAY: 1209,
}


export const getErrorDetails = (t, phone, typeProvider) => {
    return {
        [VNPTEPAY_ERROR_CODE.INCORRECT_PHONE]: {
            title: 'topup.error_incorrect_phone',
            message: 'topup.error_incorrect_phone_message',
            notes: ['topup.error_note_check_phone'],
            extraFields: { phone: phone },
        },
        [VNPTEPAY_ERROR_CODE.PROVIDER_NOT_FOUND]: {
            title: 'topup.error_provider_notfound',
            message: 'topup.error_provider_notfound_message',
            notes: ['topup.error_note_check_phone'],
            extraFields: { phone: phone, provider: typeProvider },
        },
        [VNPTEPAY_ERROR_CODE.PROVIDER_INVALID]: {
            title: 'topup.error_provider_mismatch',
            message: 'topup.error_provider_mismatch_message',
            notes: ['topup.error_note_check_phone'],
            extraFields: { phone: phone, provider: typeProvider },
        },
        [VNPTEPAY_ERROR_CODE.RECEIVER_PHONE_IS_REQUIRED]: {
            title: 'topup.error_receiver_phone_is_required',
            message: 'topup.error_receiver_phone_is_required_message',
            notes: ['topup.error_note_check_phone'],
            extraFields: {},
        },
        [VNPTEPAY_ERROR_CODE.CAN_NOT_PURCHASE_FROM_VNPT_EPAY]: {
            title: 'topup.error_request_to_3rd',
            message: 'topup.error_request_to_3rd_message',
            notes: [
                'topup.error_note_support',
                `${t('voucher.hotline')}: ${AQUA_HOTLINE}`,
                `${t('voucher.email')}: ${AQUA_EMAIL}`
            ],
            extraFields: {},
        },
    };
};


export const getDataByErrorCode = (errorCode, errorMessage, t, phone, typeProvider) => {
    // const details = getErrorDetails(t, phone, typeProvider)[errorCode];

    // if (!details) {
    //     return {
    //         errorCode,
    //         title: renderMessage(errorCode, t),
    //         message: renderMessage(errorCode, t),
    //     }
    // }

    // return {
    //     errorCode,
    //     title: t(details.title),
    //     message: t(details.message),
    //     notes: details.notes.map(note => t(note)),
    //     ...details.extraFields,
    // };

    // Check if the error code matches the pattern "20xxx"
    if (errorCode && /^20\d{3}$/.test(errorCode)) {
        // If the error code matches the pattern "20xxx"
        console.error(`Error Code: ${errorCode}, Message: ${errorMessage}`);

        return {
            errorCode: errorCode,
            title: t('topup.error_request_to_3rd'),
            message: errorMessage,
            notes: [
                t('topup.error_note_support'),
                `${t('voucher.hotline')}: ${AQUA_HOTLINE}`,
                `${t('voucher.email')}: ${AQUA_EMAIL}`
            ],
        }
    } else if (errorCode === VNPTEPAY_ERROR_CODE.INCORRECT_PHONE) {
        return {
            errorCode: errorCode,
            title: t('topup.error_incorrect_phone'),
            message: t('topup.error_incorrect_phone_message'),
            notes: [t('topup.error_note_check_phone')],
            phone: phone
        }
    } else {
        return {
            errorCode,
            title: `${t('voucher.ERROR')} ${errorCode}`,
            message: renderMessage(errorCode, t),
            notes: [
                t('topup.error_note_support'),
                `${t('voucher.hotline')}: ${AQUA_HOTLINE}`,
                `${t('voucher.email')}: ${AQUA_EMAIL}`
            ]
        }
    }
};
