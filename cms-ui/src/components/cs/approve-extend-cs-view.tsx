import ConfirmationInputSimpleCard from '@/components/common/confirmation-input-simple-card';
import { WarningCircleTriangleIcon } from '@/components/icons/warning-circle-triangle';
import {
  useModalAction,
  useModalState,
} from '@/components/ui/modal/modal.context';
import {useApproveExtendCs, useUpdateCsDisableMutation} from '@/data/cs';
import { useState } from 'react';
import {CommonStatusCode} from "@/types";

const ApproveExtendCsPopup = () => {
  const { mutate: approveVoucher, isLoading: loading } = useApproveExtendCs();
  const { data } = useModalState();
  const approveAction = data?.approveAction
  const isApproved = (approveAction === CommonStatusCode.APPROVED)

  const { closeModal } = useModalAction();

  const [rejectMemo, setRejectMemo] = useState('');

  function handleStatus() {
    if (!isApproved && (!rejectMemo || rejectMemo.trim().length === 0) ) {
      alert('Please enter Reject memo ');
      return;
    }
    approveVoucher({
      reqId: data?.request?.reqId,
      reqStatus: approveAction,
      approveMemo: isApproved ? "" : rejectMemo,
    });
    closeModal();
  }

  function addDays(dateString: string, days: number): string {
    let date = new Date(dateString);
    date.setDate(date.getDate() + days);
    return date.toISOString().split('T')[0]
  }

  return (
    <ConfirmationInputSimpleCard
      onCancel={closeModal}
      onSubmit={handleStatus}
      submitBtnLoading={loading}
      title={approveAction + " EXTEND REQUEST"}
      description={`You are ${isApproved ? "approving" : "rejecting"} request extend expire date of voucher <b>${data?.request?.ev}</b> `}
      description2={isApproved
          ? `User will be able to use this voucher until <b>${addDays(data?.voucher?.expirationDate || "", 30)}</b>`
          : ``}
      titleInput={`${isApproved ? "" : "Reject memo"} `}
      inputRequired={isApproved ? false : true}
      onInputChange={(value) => setRejectMemo(value)}
      submitBtnText={isApproved ? "Confirm" : "Reject"}
      cancelBtnClassName="w-52 bg-white focus:outline-none hover:!bg-slate-200 focus:!bg-slate-200-hover !text-black"
      submitBtnClassName="w-52 !bg-red-600 focus:outline-none hover:!bg-red-700 focus:!bg-red-700"
      showInput={isApproved ? false : true}
    />
  );
};

export default ApproveExtendCsPopup;
