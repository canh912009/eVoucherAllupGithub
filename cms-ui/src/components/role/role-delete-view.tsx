import React from 'react';
import {useModalAction, useModalState} from "@/components/ui/modal/modal.context";
import ConfirmationCard from "@/components/common/confirmation-card";
import {useDeleteRoleMutation} from "@/data/role";

const RoleDeleteView = () => {
  const { mutate: deleteRole, isLoading: loading } =
    useDeleteRoleMutation();

  const { data } = useModalState();
  const { closeModal } = useModalAction();

  function handleDelete() {
    deleteRole({
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

export default RoleDeleteView;
