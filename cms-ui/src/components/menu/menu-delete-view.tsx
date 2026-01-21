import React from 'react';
import {useModalAction, useModalState} from "@/components/ui/modal/modal.context";
import ConfirmationCard from "@/components/common/confirmation-card";
import {useDeleteMenuMutation} from "@/data/menu";

const MenuDeleteView = () => {
  const { mutate: deleteMenu, isLoading: loading } =
    useDeleteMenuMutation();

  const { data } = useModalState();
  const { closeModal } = useModalAction();

  function handleDelete() {
    deleteMenu({
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

export default MenuDeleteView;
