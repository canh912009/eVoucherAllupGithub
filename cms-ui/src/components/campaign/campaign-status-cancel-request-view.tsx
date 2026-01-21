import ConfirmationCard from '@/components/common/confirmation-card';
import { CheckMarkCircle } from '@/components/icons/checkmark-circle';
import {
  useModalAction,
  useModalState,
} from '@/components/ui/modal/modal.context';
import { usePatchCampaignMutation } from '@/data/campaign';
import { CommonApproveStatusAction } from '@/types';

const CampaignStatusCancelRequestView = () => {
  const { mutate: changeStatusCampaign, isLoading: loading } =
    usePatchCampaignMutation();

  const { data } = useModalState();
  const { closeModal } = useModalAction();

  function handleStatus() {
    changeStatusCampaign({
      id: data,
      approveStatusCode: CommonApproveStatusAction.CANCEL_REQ,
    });
    closeModal();
  }

  return (
    <ConfirmationCard
      onCancel={closeModal}
      onDelete={handleStatus}
      deleteBtnLoading={loading}
      icon={<CheckMarkCircle className="m-auto mt-4 h-10 w-10 text-accent" />}
      title="Cancel Request"
      description="Are you sure?"
      deleteBtnText="Submit"
      deleteBtnClassName="!bg-accent focus:outline-none hover:!bg-accent-hover focus:!bg-accent-hover"
      cancelBtnClassName="!bg-red-600 focus:outline-none hover:!bg-red-700 focus:!bg-red-700"
    />
  );
};

export default CampaignStatusCancelRequestView;
