import ConfirmationCard from '@/components/common/confirmation-card';
import { CheckMarkCircle } from '@/components/icons/checkmark-circle';
import {
  useModalAction,
  useModalState,
} from '@/components/ui/modal/modal.context';
import { usePatchCustomerContractMutation } from '@/data/customer-contract';
import { CommonApproveStatusAction } from '@/types';

const CustomerContractStatusApproveView = () => {
  const { mutate: changeStatusCustomerContract, isLoading: loading } =
    usePatchCustomerContractMutation();

  const { data } = useModalState();
  const { closeModal } = useModalAction();

  function handleStatus() {
    changeStatusCustomerContract({
      id: data,
      approveStatusCode: CommonApproveStatusAction.APPRV,
    });
    closeModal();
  }

  return (
    <ConfirmationCard
      onCancel={closeModal}
      onDelete={handleStatus}
      deleteBtnLoading={loading}
      icon={<CheckMarkCircle className="m-auto mt-4 h-10 w-10 text-accent" />}
      title="Approve Customer Contract"
      description="Are you sure?"
      deleteBtnText="Submit"
      deleteBtnClassName="!bg-accent focus:outline-none hover:!bg-accent-hover focus:!bg-accent-hover"
      cancelBtnClassName="!bg-red-600 focus:outline-none hover:!bg-red-700 focus:!bg-red-700"
    />
  );
};

export default CustomerContractStatusApproveView;
