import ConfirmationInputCard from '@/components/common/confirmation-input-card';
import {
  useModalAction,
  useModalState,
} from '@/components/ui/modal/modal.context';
import { usePatchDeliveryMutation } from '@/data/delivery';
import { CommonApproveStatusAction } from '@/types';
import { useState } from 'react';

const DeliveryStatusRejectView = () => {
  const { mutate: changeStatusDelivery, isLoading: loading } =
    usePatchDeliveryMutation();

  const { data } = useModalState();
  const { closeModal } = useModalAction();

  const [inputText, setInputText] = useState('');

  function handleStatus() {
    changeStatusDelivery({
      id: data,
      approveStatusCode: CommonApproveStatusAction.REJCT,
      rejectReason: inputText,
    });
    closeModal();
  }

  return (
    <ConfirmationInputCard
      onCancel={closeModal}
      onSubmit={handleStatus}
      submitBtnLoading={loading}
      title="Why are you rejecting this delivery"
      titleInput="Other reason (max 1000 characters)"
      onInputChange={(value) => setInputText(value)}
      submitBtnText="Reject"
      submitBtnClassName="bg-white focus:outline-none hover:!bg-slate-200 focus:!bg-slate-200-hover !text-black"
      cancelBtnClassName="!bg-red-600 focus:outline-none hover:!bg-red-700 focus:!bg-red-700"
    />
  );
};

export default DeliveryStatusRejectView;
