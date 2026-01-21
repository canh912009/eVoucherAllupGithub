import ConfirmationCard from '@/components/common/confirmation-card';
import { CloseFillIcon } from '@/components/icons/close-fill';
import {
  useModalAction,
  useModalState,
} from '@/components/ui/modal/modal.context';
import { usePatchSupplierMutation } from '@/data/supplier';

const SupplierStatusDisapproveView = () => {
  const { mutate: changeStatusSupplier, isLoading: loading } =
    usePatchSupplierMutation();

  const { data } = useModalState();
  const { closeModal } = useModalAction();

  function handleStatus() {
    changeStatusSupplier({
      id: data,
      approveStatusCode: 'REJCT',
    });
    closeModal();
  }

  return (
    <ConfirmationCard
      onCancel={closeModal}
      onDelete={handleStatus}
      deleteBtnLoading={loading}
      icon={<CloseFillIcon className="m-auto mt-4 h-10 w-10 text-red-500" />}
      title="Disapprove Supplier"
      description="Are you sure?"
      deleteBtnText="Submit"
      deleteBtnClassName="!bg-accent focus:outline-none hover:!bg-accent-hover focus:!bg-accent-hover"
      cancelBtnClassName="!bg-red-600 focus:outline-none hover:!bg-red-700 focus:!bg-red-700"
    />
  );
};

export default SupplierStatusDisapproveView;
