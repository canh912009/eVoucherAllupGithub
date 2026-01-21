import * as yup from 'yup';

export const supplierValidationSchema = yup.object().shape({
    supplierName: yup.string().required('Supplier name required'),
    bankName: yup.string().required('Bank name required'),
    taxcode: yup.string()
        .required('Tax code required')
        .max(30, 'Tax code must not exceed 30 characters'),

    id: yup.string()
        .required('  ID required')
        .max(20, '  ID must not exceed 20 characters')
        .matches(/^[0-9a-zA-Z]+$/, 'Customer ID can only contain letters and numbers'),
    accountName: yup.string().required('Bank account name required'),
    accountNumber: yup.string().required('Bank account number required'),
    code_settlement: yup.object().nullable().required('Settlement method required'),
    managerMobileNumber: yup.string().required('required'),

    supplyDiscountRate: yup.number().required('This field is required')
        .typeError("This field must be a number")
        .nullable()
        // .moreThan(0, "This field cannot be negative")
        .min(0, 'Value must be greater than or equal to 0')
        .transform((_, val) => (val !== "" ? Number(val) : null)),
    supplyCommissionRate: yup.number().required('This field is required')
        .typeError("This field must be a number")
        .nullable()
        // .moreThan(0, "This field cannot be negative")
        .min(0, 'Value must be greater than or equal to 0')
        .transform((_, val) => (val !== "" ? Number(val) : null)),
    vatIncludeYn: yup.string().nullable().required('Including VAT required'),
    managerName: yup.string().required('form:required'),
    managerEmail: yup.string().required('form:required').email(),
    primaryContactName: yup.string().required('Primary contact name required'),
    primaryContactEmail: yup.string().required('Primary contact email required').email(),
    primaryContactMobile: yup.string().required('form:required'),
});
