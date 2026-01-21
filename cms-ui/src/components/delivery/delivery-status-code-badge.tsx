import Badge from '@/components/ui/badge/badge';

export const DELIVERY_STATUS_CODES = [
  { code: '', text: 'All', color: 'bg-white' },
  { code: 'GENERATING', text: 'Generating', color: 'bg-yellow-600' },
  { code: 'FAIL_GENERATING', text: 'Fail Generating', color: 'bg-red-700' },
  { code: 'WAIT_APPRV', text: 'Wait Approve', color: 'bg-yellow-400' },
  { code: 'APPROVED', text: 'Approved', color: 'bg-accent' },
  { code: 'CANCEL_APPRV', text: 'Cancel Approve', color: 'bg-slate-400' },
  { code: 'CANCEL', text: 'Cancel', color: 'bg-red-900' },
  { code: 'REJECTED', text: 'Rejected', color: 'bg-slate-950' },
  { code: 'FINISHED', text: 'Finished', color: 'bg-gray-500' },
  { code: 'PUBLISHING', text: 'Publishing', color: 'bg-accent' },
  { code: 'FAIL_PUBLISHING', text: 'Fail Publishing', color: 'bg-red-800' },
  { code: 'SENDING', text: 'Sending', color: 'bg-fuchsia-500' },
  { code: 'FAIL_SENDING', text: 'Fail Sending', color: 'bg-red-600' },
  {
    code: 'WAIT_FOR_SEND_RESULT',
    text: 'Wait Send Result',
    color: 'bg-rose-700',
  },
];

export const TARGET_SMS_TYPE = [
  { code: '', text: 'All', color: 'bg-white' },
  { code: 'ZALO', text: 'Zalo', color: 'bg-yellow-400' },
  { code: 'SMS', text: 'SMS', color: 'bg-red-700' },
  { code: 'DOWNLOAD', text: 'Download', color: 'bg-yellow-600' },
  { code: 'PAPER', text: 'Paper', color: 'bg-orange-500' },
  { code: 'EMAIL', text: 'Email', color: 'bg-orange-500' },
];

type DeliveryStatusCodeProps = {
  statusCode?: string;
  className?: string;
};

const DeliveryStatusCodeBadge = ({
  statusCode,
  className,
}: DeliveryStatusCodeProps) => {
  const code = statusCode
    ? DELIVERY_STATUS_CODES.find((status) => status.code === statusCode)
    : null;
  if (!code) return null;

  const { text, color } = code;
  return <Badge text={text} color={color} className={className} />;
};

export default DeliveryStatusCodeBadge;
