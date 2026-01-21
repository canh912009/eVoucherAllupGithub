import * as yup from 'yup';
import {SYSTEM_BRAND_TYPE} from "@/utils/constants";
import {VOUCHER_TYPE_CODES} from "@/components/common/status-code-badge";

export const goodValidationSchema = yup.object().shape({
  goodsName: yup.string().required('form:error-good-goodName-required'),
  supplierGoodsId: yup.string()
      // .matches(/^[a-zA-Z0-9]+$/, 'Product Code must input alphanumeric')
      .test('productCode-length', 'Product Code must be less than 30 digits',
      (value) => {
        if (!value) return true; // Allow empty values
        return value.length <= 30;
      })
    .required('form:error-good-supplierGoodsId-required'),
  supplier: yup.object().required('form:error-good-supplierIdObject-required'),
  supplierContract: yup.object().required('form:error-good-supplierContractId-required'),

  periodType: yup.string().when('system', {
    is: SYSTEM_BRAND_TYPE.INTERNAL,
    then: yup.string().nullable().required('You must need to provide Period Type when System is INTERNAL'),
    otherwise: yup.string().nullable()
  }),

  goodsDescription: yup.string().required('form:error-good-goodsDescription-required'),
  goodsTypeCode: yup.mixed().when('system', {
    is: (value: any) => value === SYSTEM_BRAND_TYPE.CHOICE || value === SYSTEM_BRAND_TYPE.BULK,
    then: yup.string().nullable(),
    otherwise: yup.object().required('form:error-good-goodsType-required')
  }),
  brand: yup.object().required('form:error-good-brandId-required'),

  goodsImgPath: yup.string().required('form:error-good-goodsImgName-required'),
  validYn: yup.string().nullable().required('form:error-good-validYn-required'),
  listPrice: yup.mixed().when('system', {
    is: (value: any) => value === SYSTEM_BRAND_TYPE.VNPT_EPAY || value === SYSTEM_BRAND_TYPE.XPAY,
    then: yup.object().required('form:error-good-listPrice-required'),
    otherwise: yup.string().required('form:error-good-listPrice-required')
  }),
  sellPrice: yup
      .string()
      .required('form:error-good-sellPrice-required'),
      // .min(0, 'form:error-sellPrice-non-negative'),

  settlementMethodTypeCode: yup.object().required('form:error-good-settlementMethodCode-required'),
  supplyDiscountAmount: yup
    .string()
    .required('form:error-DiscountAmount-required'),
    // .min(0, 'form:error-DiscountAmount-non-negative'),
  supplyCommissionRate: yup
      .number().required('form:error-CommissionRate-required')
      .min(0, 'form:error-CommissionRate-non-negative'),

  vatIncludeYn: yup.string().nullable().required('form:error-good-vatIncludeYn-required'),

  startDate: yup.date().required('form:error-good-startDate-required'),
  endDate: yup.date().required('form:error-good-endDate-required'),

  usageCount: yup.mixed().when('goodsTypeCode', {
    is: (value: any) => value && value.codeId === VOUCHER_TYPE_CODES.LC,
    then: yup.number()
              .typeError('Usage Count must be a number')
              .integer('Usage Count must be an integer')
              .min(1, 'Must in range 1 to 100')
              .max(100, 'Must in range 1 to 100')
              .required('Usage Count is required'),
    otherwise: yup.string().nullable()
  }),

});
