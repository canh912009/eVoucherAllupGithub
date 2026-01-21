import ConfirmationInputSimpleCard from '@/components/common/confirmation-input-simple-card';
import { WarningCircleTriangleIcon } from '@/components/icons/warning-circle-triangle';
import {
  useModalAction,
  useModalState,
} from '@/components/ui/modal/modal.context';
import {useApproveExtendCs, } from '@/data/cs';
import { useState } from 'react';

const RequestExtendCsPopup = () => {
  const { mutate: requestExtendVoucher, isLoading: loading } = useApproveExtendCs(true);
  const { data } = useModalState();
  const { closeModal } = useModalAction();
  const [memo, setMemo] = useState('');

  function handleStatus() {
    if ( (!memo || memo.trim().length === 0) ) {
      alert('Please enter memo ');
      return;
    }
    requestExtendVoucher({
      ev: data?.voucherUUID,
      memo: memo,
    });
    closeModal();
  }

  function getOrdinalSuffix(n: number) {
    const s = ["th", "st", "nd", "rd"],
      v = n % 100;
    return n + (s[(v - 20) % 10] || s[v] || s[0]);
  }

  return (
    <ConfirmationInputSimpleCard
      onCancel={closeModal}
      onSubmit={handleStatus}
      submitBtnLoading={loading}
      title={"VOUCHER EXPIRE DATE EXTENSION"}
      description={data?.requestCount === 0
      ? `First-time extension, no payment is needed`
      : `This is the ${getOrdinalSuffix(data?.requestCount + 1)} extension, a <b>5% fee</b> must be collected from the customer. Is it confirmed?`}
      description2={`The current voucher expire date is: <b>${data?.endDate}</b>. \n
                     The voucher expiration date will be extended <b>one more month</b>`}
      titleInput={"Memo"}
      inputRequired={true}
      onInputChange={(value) => setMemo(value)}
      submitBtnText={"Confirm"}
      cancelBtnClassName="w-52 bg-white focus:outline-none hover:!bg-slate-200 focus:!bg-slate-200-hover !text-black"
      submitBtnClassName="w-52 !bg-red-600 focus:outline-none hover:!bg-red-700 focus:!bg-red-700"
    />
  );
};

export default RequestExtendCsPopup;
