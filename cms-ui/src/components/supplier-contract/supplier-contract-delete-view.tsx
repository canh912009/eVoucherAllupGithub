import ConfirmationCard from '@/components/common/confirmation-card';
import {
  useModalAction,
  useModalState,
} from '@/components/ui/modal/modal.context';
import { useDeleteSupplierContractMutation } from '@/data/supplier-contract';

const SupplierContractDeleteView = () => {
  const { mutate: deleteSupplierContract, isLoading: loading } =
  useDeleteSupplierContractMutation();

  const { data } = useModalState();
  const { closeModal } = useModalAction();

  function handleDelete() {
    deleteSupplierContract({
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

export default SupplierContractDeleteView;
