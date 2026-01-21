import React from 'react';
import {useModalAction, useModalState} from "@/components/ui/modal/modal.context";
import ConfirmationCard from "@/components/common/confirmation-card";
import {useDeleteMenuGroupMutation} from "@/data/menu-group";

const MenuGroupDeleteView = () => {
  const { mutate: deleteMenuGroup, isLoading: loading } =
    useDeleteMenuGroupMutation();

  const { data } = useModalState();
  const { closeModal } = useModalAction();

  function handleDelete() {
    deleteMenuGroup({
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

export default MenuGroupDeleteView;
