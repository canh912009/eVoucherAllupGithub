import * as yup from 'yup';

export const vnptEpayProviderValidationSchema = yup.object().shape({
  providerNm: yup
    .string()
    .required('form:error-category-categoryName-required') ,
  validYn: yup
    .string()
    .nullable()
    .required('form:error-category-validYn-required'),
});
