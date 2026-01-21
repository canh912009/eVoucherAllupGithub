import * as yup from 'yup';
import { REGEX_LETTERS_NUMBERS_SPACES_SOME_CHARACTERS, SMS } from '@/utils/constants';
import { DeliveryReceiveType } from '@/types';

export const deliveryValidationSchema = yup.object().shape({
    publishName: yup.string().required('form:error-delivery-publishName-required'),
    smsType: yup.string().nullable().required('form:error-delivery-smsType-required'),
    bookingYn: yup.string().nullable().required('form:error-delivery-bookingYn-required'),
    numberOfVouchers: yup.number().when('smsType', {
        is: (smsType: DeliveryReceiveType) => smsType === DeliveryReceiveType.DOWNLOAD || smsType === DeliveryReceiveType.PAPER,
        then: yup.number().positive('Field must be a positive number').nullable()
            .transform((_, val) => (val !== '' ? Number(val) : null))
            .max(1000, `Value must be less than or equal to 1000`)
            .required('Field is required'),
        otherwise: yup.number().nullable().transform((_, val) => (val !== '' ? Number(val) : null))
    }),
    contentLink: yup.string().nullable().url(),
    messageSubject: yup.string().max(64, 'Delivery Message must be at most 64 characters').required('form:error-delivery-messageSubject-required'),
    messageContent: yup.string().required('form:error-delivery-messageContent-required'),
    // uploadType: yup.string().nullable().required('form:error-delivery-uploadType-required'),
    uploadType: yup.string().when('smsType', {
        is: (smsType: DeliveryReceiveType) => smsType !== DeliveryReceiveType.DOWNLOAD && smsType !== DeliveryReceiveType.PAPER,
        then: yup.string().nullable().required('form:error-delivery-uploadType-required'),
        otherwise: yup.string().nullable()
    }),
    sellPrice: yup.string().typeError('Must be a number').required(),
    sellListPrice: yup.string().typeError('Must be a number').required(),
    code_settlement: yup.object().nullable().required('form:error-delivery-code_settlement-required'),
    sellDiscountAmount: yup.string().typeError("This field must be a number"),
    sellCommissionRate: yup.number().typeError("This field must be a number")
        // .nullable()
        // .moreThan(0, "This field cannot be negative")
        .transform((_, val) => (val !== "" ? Number(val) : null)),
    sellVatIncludeYn: yup.string().nullable().required('form:error-delivery-sellVatIncludeYn-required'),
    senderName: yup.string()
        .max(SMS.SENDER_NAME_MAX_LENGTH, `The sender's name has a maximum of ${SMS.SENDER_NAME_MAX_LENGTH} characters`)
        .matches(REGEX_LETTERS_NUMBERS_SPACES_SOME_CHARACTERS, 'Only letters, numbers, spaces and , - _ () are allowed')
        .required('form:error-delivery-senderName-required'),
});
