import * as yup from 'yup';
import {REGEX_LETTERS_NUMBERS_SPACES_SOME_CHARACTERS, SMS} from "@/utils/constants";

export const campaignValidationSchema = yup.object().shape({
  customer: yup.object().nullable().required('form:error-campaign-customer-required'),
  customerContract: yup.object().nullable().required('form:error-campaign-customerContract-required'),
  campaignName: yup.string().required('form:error-campaign-campaignName-required'),
  startDate: yup.date().required('form:error-campaign-startDate-required'),
  endDate: yup.date().required('form:error-campaign-endDate-required'),
  messageSubject: yup.string().nullable().max(64, 'Voucher subject must be at most 64 characters'),
  messageContent: yup.string().nullable() ,
  senderName: yup.string()
      .max(SMS.SENDER_NAME_MAX_LENGTH, `The sender's name has a maximum of ${SMS.SENDER_NAME_MAX_LENGTH} characters`)
      .matches(REGEX_LETTERS_NUMBERS_SPACES_SOME_CHARACTERS, 'Only letters, numbers, spaces and , - _ () are allowed')
      .required('form:error-delivery-senderName-required'),
  // messageTemplate: yup.object().nullable().required('form:error-campaign-messageTemplate-required'),
  contentLink: yup.string().nullable().url(),
});
