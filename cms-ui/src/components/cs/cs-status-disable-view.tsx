import ConfirmationInputSimpleCard from '@/components/common/confirmation-input-simple-card';
import { WarningCircleTriangleIcon } from '@/components/icons/warning-circle-triangle';
import {
  useModalAction,
  useModalState,
} from '@/components/ui/modal/modal.context';
import { useUpdateCsDisableMutation } from '@/data/cs';
import { useState } from 'react';

const CsStatusDisableView = () => {
  const { mutate: disableVoucher, isLoading: loading } =
    useUpdateCsDisableMutation();

  const { data } = useModalState();
  const { closeModal } = useModalAction();

  const [inputText, setInputText] = useState('');

  function handleStatus() {
    // console.log('inputText', inputText);
    // console.log('data', data);
    // return;
    if (!inputText || inputText.trim().length === 0) {
      alert('Please enter your reason!');
      return;
    }
    disableVoucher({
      ev: data,
      reason: inputText,
    });
    closeModal();
  }

  return (
    <ConfirmationInputSimpleCard
      onCancel={closeModal}
      onSubmit={handleStatus}
      submitBtnLoading={loading}
      icon={
        <WarningCircleTriangleIcon className="mt-4 h-10 w-10 text-red-500" />
      }
      title="Disabling a PIN Coupon (Voucher)"
      description="Before proceeding, please be aware that disabling a PIN coupon (voucher) will render it invalid and unusable. Ensure that you no longer require its functionality before taking this action."
      titleInput="Tell us why do you want to disable it ?"
      inputRequired={true}
      onInputChange={(value) => setInputText(value)}
      submitBtnText="I wish to proceed"
      cancelBtnClassName="w-52 bg-white focus:outline-none hover:!bg-slate-200 focus:!bg-slate-200-hover !text-black"
      submitBtnClassName="w-52 !bg-red-600 focus:outline-none hover:!bg-red-700 focus:!bg-red-700"
    />
  );
};

export default CsStatusDisableView;
