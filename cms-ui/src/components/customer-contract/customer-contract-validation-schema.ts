import * as yup from 'yup';

export const customerContractValidationSchema = yup.object().shape({
  contractName: yup
    .string()
    .required('form:error-customerContract-contractName-required'),
  customer: yup
    .object()
    .nullable()
    .required('form:error-customerContract-customer-required'),
  startDate: yup.date().required('Start date is required'),
  endDate: yup
    .date()
    .when('startDate', (startDate, schema) => {
      return startDate
        ? schema
            .min(
              startDate,
              'End date must be greater than or equal to the start date'
            )
            .test({
              name: 'is-future',
              test: (endDate: any) => {
                console.log('endDate', endDate);
                const currentDate = new Date();
                currentDate.setHours(0, 0, 0, 0);
                console.log('currentDate', currentDate);
                return endDate && endDate >= currentDate;
              },
              message: 'End date must be in the future',
            })
        : schema;
    })
    .required('End date is required'),
  code_settlement: yup.object().nullable(),
  sellDiscountRate: yup
    .number()
    .nullable()
    .transform((_, val) => (val !== '' ? Number(val) : null))
    // .positive('form:error-customerContract-sellDiscountRate-positive'),
    // .moreThan(0, "This field cannot be negative")
    .min(0, 'Value must be greater than or equal to 0'),
  sellDiscountAmount: yup
    .number()
    .nullable()
    .transform((_, val) => (val !== '' ? Number(val) : null))
    .min(0, 'Value must be greater than or equal to 0'),
  sellCommissionRate: yup
    .number()
    .nullable()
    .transform((_, val) => (val !== '' ? Number(val) : null))
    .min(0, 'Value must be greater than or equal to 0'),
});
