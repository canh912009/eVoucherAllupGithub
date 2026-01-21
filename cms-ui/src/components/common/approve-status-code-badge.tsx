import Badge from '@/components/ui/badge/badge';

// REQ, APPRV, REJCT
export const APPROVE_STATUS_CODES = [
  { code: '', text: 'All', color: 'bg-white' },
  { code: 'REQ', text: 'Requesting', color: 'bg-yellow-600' },
  { code: 'APPRV', text: 'Approved', color: 'bg-accent' },
  { code: 'REJCT', text: 'Rejected', color: 'bg-red-800' },
  { code: 'EMPTY', text: 'Empty', color: 'bg-amber-200' },
];

type ApproveStatusCodeProps = {
  approveStatusCode?: string;
  className?: string;
};

const ApproveStatusCodeBadge = ({
  approveStatusCode,
  className,
}: ApproveStatusCodeProps) => {
  const code = approveStatusCode
    ? APPROVE_STATUS_CODES.find((status) => status.code === approveStatusCode)
    : null;
  if (!code) return null;

  const { text, color } = code;
  return <Badge text={text} color={color} className={className} />;
};

export default ApproveStatusCodeBadge;
