import * as yup from 'yup';

export const storeValidationSchema = yup.object().shape({
  storeName: yup.string().required('form:error-store-storeName-required'),
  mapCode: yup.string()
    .test('mapCode-length', 'Map Code must be less than 20 digits',
      (value) => {
        if (!value) return true; // Allow empty values
        return value.length <= 20;
      })
    .required('form:error-store-mapCode-required'),
  supplier: yup.object().nullable().required('form:error-store-supplier-required'),
  brand: yup.object().nullable().required('form:error-store-brand-required'),
  region: yup.string().required('form:error-store-region-required'),
  fullAddress: yup.string().required('form:error-store-fullAddress-required'),
  telephoneNumber: yup.string().required('form:error-store-telephoneNumber-required'),
  validYn: yup.string().nullable().required('form:error-store-validYn-required'),
  storeImagePath: yup.string().nullable().required('form:error-store-storeImagePath-required'),
});
