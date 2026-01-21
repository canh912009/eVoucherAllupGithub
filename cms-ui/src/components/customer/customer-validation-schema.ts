import * as yup from 'yup';

export const customerValidationSchema = yup.object().shape({
  taxcode: yup.string()
    .required('Tax code required')
    .max(30, 'Tax code must not exceed 30 characters'),

  id: yup.string()
    .required('Customer ID required')
    .max(20, 'Customer ID must not exceed 20 characters')
    .matches(/^[0-9a-zA-Z]+$/, 'Customer ID can only contain letters and numbers'),

  customerName: yup.string()
    .max(100, 'The customer name only allows a maximum of 100 characters')
    .test('no-dots', 'Customer name cannot contain a dot (".")', function (value) {
        if (value) {
            return !value.includes('.');
        }
        return true; // Return true for undefined or empty values
    }).required('Customer name is required'),
  bankName: yup.string().required('Bank account name required'),
  accountName: yup.string().required('Bank account name required'),
  accountNumber: yup.string().required('Bank account number required'),
  representativeMail: yup.string().required(' required').email(),
  representativeMobile: yup.string().required('Representative Mobile required'),
  managerName: yup.string().required(' required'),
  managerEmail: yup.string().required(' required').email(),
  managerMobileNo: yup.string().required('Manager phone number required'),
  customerType: yup.object().nullable().required(' required'),
  adminID: yup.string().required('Admin required'),
});
