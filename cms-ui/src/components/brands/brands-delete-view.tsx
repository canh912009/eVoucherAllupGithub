import ConfirmationCard from '@/components/common/confirmation-card';
import {
    useModalAction,
    useModalState,
} from '@/components/ui/modal/modal.context';
import { useDeleteBrandMutation } from '@/data/brands';

const BrandDeleteView = () => {
    const { mutate: deleteBrand, isLoading: loading } =
        useDeleteBrandMutation();

    const { data } = useModalState();
    const { closeModal } = useModalAction();

    function handleDelete() {
        deleteBrand({
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

export default BrandDeleteView;
