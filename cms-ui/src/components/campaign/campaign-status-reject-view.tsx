import ConfirmationInputCard from '@/components/common/confirmation-input-card';
import { CloseFillIcon } from '@/components/icons/close-fill';
import {
  useModalAction,
  useModalState,
} from '@/components/ui/modal/modal.context';
import { usePatchCampaignMutation } from '@/data/campaign';
import { CommonApproveStatusAction } from '@/types';
import { useState } from 'react';

const CampaignStatusRejectView = () => {
  const { mutate: changeStatusCampaign, isLoading: loading } =
    usePatchCampaignMutation();

  const { data } = useModalState();
  const { closeModal } = useModalAction();

  // State to hold the textarea value
  const [inputText, setInputText] = useState('');

  function handleStatus() {
    // console.log('inputText', inputText);
    // console.log('data', data);
    // return;
    changeStatusCampaign({
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
      // icon={<CloseFillIcon className="m-auto mt-4 h-10 w-10 text-red-500" />}
      title="Why are you rejecting this campaign"
      // description="Are you sure?"
      titleInput='Other reason (max 1000 characters)'
      onInputChange={(value) => setInputText(value)}
      submitBtnText="Reject"
      submitBtnClassName="bg-white focus:outline-none hover:!bg-slate-200 focus:!bg-slate-200-hover !text-black"
      cancelBtnClassName="!bg-red-600 focus:outline-none hover:!bg-red-700 focus:!bg-red-700"
    />
  );
};

export default CampaignStatusRejectView;
