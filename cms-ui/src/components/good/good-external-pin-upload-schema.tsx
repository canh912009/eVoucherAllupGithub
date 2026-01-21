import * as yup from 'yup';

export const goodExternalPinUploadValidationSchema = yup.object().shape({
  uploadName: yup.string().required('You must need to provide External Pin Upload Name'),
  uploadFileName: yup.string().required('You must need to provide Upload File'),

});