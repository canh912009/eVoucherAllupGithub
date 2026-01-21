import ConfirmationCard from '@/components/common/confirmation-card';
import { CheckMarkCircle } from '@/components/icons/checkmark-circle';
import {
  useModalAction,
  useModalState,
} from '@/components/ui/modal/modal.context';
import { usePatchDeliveryMutation } from '@/data/delivery';
import { CommonApproveStatusAction } from '@/types';
import { useEffect, useState } from 'react';

const DeliveryStatusApproveView = () => {
  const { mutate: changeStatusDelivery, isLoading: loading } =
    usePatchDeliveryMutation();

  const { data } = useModalState();
  const { closeModal } = useModalAction();
  const [isSubmitting, setIsSubmitting] = useState(false);

  function handleStatus() {
    setIsSubmitting(true);
    changeStatusDelivery({
      id: data,
      approveStatusCode: CommonApproveStatusAction.APPRV,
    });
  }

  useEffect(() => {
    if (!loading && isSubmitting) {
      closeModal();
      setIsSubmitting(false);
    }
  }, [loading, isSubmitting, closeModal]);

  return (
    <ConfirmationCard
      onCancel={closeModal}
      onDelete={handleStatus}
      deleteBtnLoading={loading}
      icon={<CheckMarkCircle className="m-auto mt-4 h-10 w-10 text-accent" />}
      title="Approve Delivery"
      description="Are you sure?"
      deleteBtnText="Submit"
      deleteBtnClassName="!bg-accent focus:outline-none hover:!bg-accent-hover focus:!bg-accent-hover"
      cancelBtnClassName="!bg-red-600 focus:outline-none hover:!bg-red-700 focus:!bg-red-700"
    />
  );
};

export default DeliveryStatusApproveView;
