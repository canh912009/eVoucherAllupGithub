import * as yup from 'yup';

export const menuValidationSchema = yup.object().shape({
  // menuGroupId: yup.number().required('form:error-menuGroupId-required')
  //   .typeError("This field must be a number")
  //   .moreThan(0, "This field cannot be negative")
  //   .transform((_, val) => (val !== "" ? Number(val) : null)),
  menuName: yup.string().required('form:error-menuName-required'),
  sortOrder: yup.number().required('form:error-sortOrder-required')
    .typeError("This field must be a number")
    .moreThan(0, "This field cannot be negative")
    .transform((_, val) => (val !== "" ? Number(val) : null)),
  menuUrl: yup.string()
    .required('form:error-menuUrl-required')
    .matches(/^\//, "The field must start with a '/' character."),
});
