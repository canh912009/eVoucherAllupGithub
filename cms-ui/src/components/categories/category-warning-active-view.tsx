import ConfirmationSimplePopup from '@/components/common/confirmation-simple';
import { useModalAction, useModalState } from '@/components/ui/modal/modal.context';

const CategoryWarningActiveView = () => {
  const { closeModal } = useModalAction();
  const { data } = useModalState();
  // console.log('data', data);

  function handleSubmit() {
    data?.triggerActiveYn(data?.validYn !== 'Y');
    closeModal();
  }

  function handleCancel() {
    data?.triggerActiveYn(data?.validYn === 'Y');
    closeModal();
  }

  return (
    <ConfirmationSimplePopup
      onCancel={handleCancel}
      onSubmit={handleSubmit}
      title="Update Valid Y/N"
      titleClassName="text-heading text-red-700 text-left !normal-case"
      descriptions={[
        'Updating this value might affect the goods and vouchers related to it.',
        'Are you sure want to change it?',
      ]}
      descriptionClassNameWrapper='text-left !pt-6'
    />
  );
};

export default CategoryWarningActiveView;
