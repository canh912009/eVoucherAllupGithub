import ConfirmationCard from '@/components/common/confirmation-card';
import {
  useModalAction,
  useModalState,
} from '@/components/ui/modal/modal.context';
import { useDeleteCustomerContractMutation } from '@/data/customer-contract';

const CustomerContractDeleteView = () => {
  const { mutate: deleteCustomerContract, isLoading: loading } =
    useDeleteCustomerContractMutation();

  const { data } = useModalState();
  const { closeModal } = useModalAction();

  function handleDelete() {
    deleteCustomerContract({
      id: data,
    });
    closeModal();
  }

  return (
    <ConfirmationCard
      onCancel={closeModal}
      onDelete={handleDelete}
      deleteBtnLoading={loading}
    />
  );
};

export default CustomerContractDeleteView;
