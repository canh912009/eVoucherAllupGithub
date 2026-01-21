import ConfirmationInputSimpleCard from '@/components/common/confirmation-input-simple-card';
import { WarningCircleTriangleIcon } from '@/components/icons/warning-circle-triangle';
import {
  useModalAction,
  useModalState,
} from '@/components/ui/modal/modal.context';
import { useUpdateCsResendMutation } from '@/data/cs';
import { useState } from 'react';

const CsStatusResendView = () => {
  const { mutate: resendVoucher, isLoading: loading } =
    useUpdateCsResendMutation();

  const { data } = useModalState();
  const { closeModal } = useModalAction();
  const [inputText, setInputText] = useState('');
  console.log("data", data?.emailType, data)

  function handleStatus() {
    if (!data?.emailType && (!inputText || inputText.trim().length === 0) ) {
      alert('Please enter your reason!');
      return;
    }
    resendVoucher({
      ev: data?.emailType ? data?.ev : data,
      reason: data?.emailType ? "Error 1002. Connection lost by the mail server" : inputText,
    });
    closeModal();
  }

  // @ts-ignore
  return (
    <ConfirmationInputSimpleCard
      onCancel={closeModal}
      onSubmit={handleStatus}
      submitBtnLoading={loading}
      icon={
        <WarningCircleTriangleIcon className="mt-4 h-10 w-10 text-red-500" />
      }
      title={data?.emailType ? "DELIVERY ERROR DETAILS" : "Resending a PIN Coupon (Voucher)"}
      description={data?.emailType
        ? "<strong>Error 1002.</strong>\nConnection lost by the mail server"
        : "Prior to proceeding, kindly note that resending a PIN coupon (voucher) to another user will transfer its use and ownership to the recipient. Proceed only if you are certain of this transfer."
      }
      submitBtnText={data?.emailType ? "Resend now" : "I wish to proceed"}
      cancelBtnClassName="w-52 bg-white focus:outline-none hover:!bg-slate-200 focus:!bg-slate-200-hover !text-black"
      submitBtnClassName="w-52 !bg-red-600 focus:outline-none hover:!bg-red-700 focus:!bg-red-700"

      // Conditionally add the following props only if emailType is false
      {...(!data?.emailType && {
        titleInput: "Tell us why do you want to resend it ?",
        inputRequired: true,
        onInputChange: (value) => setInputText(value)
      })}
    />

  );
};

export default CsStatusResendView;
