import * as yup from 'yup';

export const menuGroupValidationSchema = yup.object().shape({
  menuGroupName: yup.string().required('form:error-required'),
  sortOrder: yup.number().required('form:error-required')
    .typeError("This field must be a number")
    .moreThan(0, "This field cannot be negative")
    .transform((_, val) => (val !== "" ? Number(val) : null)),
});
