import * as yup from 'yup';

export const adminValidationSchema = yup.object().shape({
  // brandName: yup.string().required('form:error-brands-brandName-required'),
  // supplierIdObject: yup.string().required('form:error-brands-supplierIdObject-required'),

  adminName: yup.string()
    .test('adminName-length', 'Admin Name must be less than 50 digits',
      (value) => {
        if (!value) return true; // Allow empty values
        return value.length <= 50;
      })
    .required('Please enter a username'),

  mobileNumber: yup.string()
    .test('mobileNo-length', 'Mobile Number must be less than 20 digits',
      (value) => {
        if (!value) return true; // Allow empty values
        return value.length <= 20;
      })
    .required('Please enter your Mobile Number'),

  email: yup.string().email().matches(/^(?!.*@[^,]*,)/).required('Please enter your email. Your email must match correct format'),
});
