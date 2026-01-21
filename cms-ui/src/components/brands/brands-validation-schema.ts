import * as yup from 'yup';

export const brandsValidationSchema = yup.object().shape({
    // supplierIdObject: yup.string().required('required'),
    supplier: yup.object().nullable().required('required'),
    brandName: yup.string().required(' required'),
    description: yup.string().required('required'),
    systemTypeCode: yup.mixed().required('required') ,
    brandImagePath: yup.string().nullable().required('required'),
    brandImageName: yup.string().nullable().required('required'),
    validYn: yup.string().nullable().required('required'),
    displayType: yup.string().nullable().required('required'),
});
