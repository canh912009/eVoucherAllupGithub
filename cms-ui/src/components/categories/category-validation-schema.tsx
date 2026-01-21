import * as yup from 'yup';

export const categoryValidationSchema = yup.object().shape({
  categoryCode: yup
      .string()
      .required('form:error-category-categoryCode-required')
      .max(20, 'Category code must be at most 20 characters')
      .test('is-unique', 'Category code must be unique', async (value) => {
        // Implement uniqueness check here
        return true; // Placeholder
      }),
  categoryName: yup
      .string()
      .required('form:error-category-categoryName-required')
      .min(3, 'Category name must be at least 3 characters')
      .max(100, 'Category name must be at most 100 characters'),
  validYn: yup
      .string()
      .nullable()
      .required('form:error-category-validYn-required'),
  imagePath: yup
      .string()
      .nullable()
      .required('form:error-category-imagePath-required'),
});
