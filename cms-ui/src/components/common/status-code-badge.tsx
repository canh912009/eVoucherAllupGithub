import Badge from '@/components/ui/badge/badge';
import {SYSTEM_BRAND_TYPE} from "@/utils/constants";

// EMPTY, PROCESSING, WAIT_APPRV, APPROVED, CANCEL_APPRV, REJECTED, END
export const STATUS_CODES = [
  { code: '', text: 'All', color: 'bg-white' },
  { code: 'EMPTY', text: 'Empty', color: 'bg-amber-200' },
  { code: 'PROCESSING', text: 'Processing', color: 'bg-yellow-400' },
  { code: 'WAIT_APPRV', text: 'Wait Approve', color: 'bg-yellow-600' },
  { code: 'APPROVED', text: 'Approved', color: 'bg-accent' },
  { code: 'CANCEL_APPRV', text: 'Cancel Approve', color: 'bg-slate-400' },
  { code: 'REJECTED', text: 'Rejected', color: 'bg-red-800' },
  { code: 'END', text: 'End', color: 'bg-slate-950' },
];
export const RAW_SETTLEMENT_COMPANY_TYPE = [
  { code: '', text: 'All', color: 'bg-white' },
  { code: 'SUPPLIER', text: 'SUPPLIER', color: 'bg-yellow-600' },
  { code: 'CUSTOMER', text: 'CUSTOMER', color: 'bg-accent' },
];
export const VOUCHER_TYPE_CODES = {
  SI: "SI",
  PP: "PP",
  LC: "LC",
  DC: "DC",
  CH: "CH",
  BK: "BK",
}
export const VOUCHER_TYPE = [
  { code: '', text: 'All', color: 'bg-white' },
  { code: 'SI', text: 'Single Item', color: 'bg-yellow-600' },
  { code: 'PP', text: 'Pre paid', color: 'bg-accent' },
  { code: 'LC', text: 'Limited Count', color: 'bg-accent' },
  { code: 'DC', text: 'Discount', color: 'bg-accent' },
  { code: 'CH', text: 'Choice', color: 'bg-accent' },
  { code: 'BK', text: 'Bulk', color: 'bg-accent' },
];
export const RAW_SETTLEMENT_METHOD = [
  { code: '', text: 'All', color: 'bg-white' },
  { code: 'PER_PUBLISH', text: 'PER_PUBLISH', color: 'bg-yellow-600' },
  { code: 'PER_EXCHANGE', text: 'PER_EXCHANGE', color: 'bg-accent' },
  { code: 'PER_USE_AMOUNT', text: 'PER_USE_AMOUNT', color: 'bg-accent' },
];
export const ACTIVE_STATUS_CODES = [
  { code: '', text: 'All', color: 'bg-white' },
  { code: 'Y', text: 'Yes', color: 'bg-yellow-600' },
  { code: 'N', text: 'No', color: 'bg-accent' },
];
export const SYSTEM_GROUP_IN_EX = [
  { code: SYSTEM_BRAND_TYPE.INTERNAL, text: SYSTEM_BRAND_TYPE.INTERNAL, color: 'bg-yellow-600' },
  { code: SYSTEM_BRAND_TYPE.EXTERNAL, text: SYSTEM_BRAND_TYPE.EXTERNAL, color: 'bg-accent' },
  { code: SYSTEM_BRAND_TYPE.GIFTPOP, text: SYSTEM_BRAND_TYPE.GIFTPOP, color: 'bg-accent' },
  { code: SYSTEM_BRAND_TYPE.UR_BOX, text: SYSTEM_BRAND_TYPE.UR_BOX, color: 'bg-accent' },
  { code: SYSTEM_BRAND_TYPE.WATANE, text: SYSTEM_BRAND_TYPE.WATANE, color: 'bg-accent' },
  { code: SYSTEM_BRAND_TYPE.VNPT_EPAY, text: SYSTEM_BRAND_TYPE.VNPT_EPAY, color: 'bg-accent' },
  { code: SYSTEM_BRAND_TYPE.XPAY, text: SYSTEM_BRAND_TYPE.XPAY, color: 'bg-accent' },
];
export const SYSTEM_GROUP_CHOICE_BULK = [
  { code: SYSTEM_BRAND_TYPE.CHOICE, text: SYSTEM_BRAND_TYPE.CHOICE, color: 'bg-accent' },
  { code: SYSTEM_BRAND_TYPE.BULK, text: SYSTEM_BRAND_TYPE.BULK, color: 'bg-accent' },
];
export const SYSTEM_GROUP_ALL = [
  { code: '', text: 'All', color: 'bg-white' },
  ...SYSTEM_GROUP_IN_EX,
  ...SYSTEM_GROUP_CHOICE_BULK
];
export const VOUCHER_STATUS_CODES = [
  { code: '', text: '', color: 'bg-white' },
  { code: 'DISABLED', text: 'DISABLED', color: 'bg-slate-600' },
  { code: 'EXPIRE', text: 'EXPIRE', color: 'bg-red-900' },
  { code: 'NORMAL', text: 'NORMAL', color: 'bg-accent' },
  { code: 'PART_USED', text: 'PART_USED', color: 'bg-yellow-400' },
  { code: 'USED', text: 'USED', color: 'bg-rose-700' },
];
export const VOUCHER_TRANSFER_STATUS_CODES = [
  { code: '', text: '', color: 'bg-white' },
  { code: 'TRANSFER', text: 'TRANSFER', color: 'bg-accent' },
  { code: 'RECPT_WAIT', text: 'RECPT_WAIT', color: 'bg-yellow-500' },
  { code: 'RECPTED', text: 'RECPTED', color: 'bg-fuchsia-800' },
  { code: 'RETURN', text: 'RETURN', color: 'bg-slate-800' },
];

type StatusCodeProps = {
  statusCode?: string;
  className?: string;
};


export const VoucherStatusCodeBadge = ({ statusCode, className }: StatusCodeProps) => {
  const code = statusCode
    ? VOUCHER_STATUS_CODES.find((status) => status.code === statusCode)
    : null;
  if (!code) {
    return statusCode ?? null;
  }

  const { text, color } = code;
  return <Badge text={text} color={color} className={className} />;
};

export const VoucherTransferStatusCodeBadge = ({ statusCode, className }: StatusCodeProps) => {
  const code = statusCode
    ? VOUCHER_TRANSFER_STATUS_CODES.find((status) => status.code === statusCode)
    : null;
  if (!code) {
    return statusCode ?? null;
  }

  const { text, color } = code;
  return <Badge text={text} color={color} className={className} />;
};


const StatusCodeBadge = ({ statusCode, className }: StatusCodeProps) => {
  const code = statusCode
    ? STATUS_CODES.find((status) => status.code === statusCode)
    : null;
  if (!code) return null;

  const { text, color } = code;
  return <Badge text={text} color={color} className={className} />;
};

export default StatusCodeBadge;
