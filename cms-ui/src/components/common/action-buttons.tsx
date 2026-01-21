import { EditIcon } from '@/components/icons/edit';
import { TrashIcon } from '@/components/icons/trash';
import { Eye } from '@/components/icons/eye-icon';
import Link from '@/components/ui/link';
import { useTranslation } from 'next-i18next';
import { useModalAction } from '@/components/ui/modal/modal.context';
import { CheckMarkCircle } from '@/components/icons/checkmark-circle';
import { CloseFillIcon } from '@/components/icons/close-fill';

type Props = {
  id: string;
  editModalView?: string | any;
  deleteModalView?: string | any;
  editUrl?: string;
  detailsUrl?: string;
  customLocale?: string;
  approveModalView?: string | any;
  disapproveModalView?: string | any;
};

const ActionButtons = ({
  id,
  editModalView,
  deleteModalView,
  editUrl,
  detailsUrl,
  customLocale,
  approveModalView,
  disapproveModalView
}: Props) => {
  const { t } = useTranslation();
  const { openModal } = useModalAction();

  function handleDelete() {
    openModal(deleteModalView, id);
  }

  function handleEditModal() {
    openModal(editModalView, id);
  }

  function handleStatusApprove() {
    openModal(approveModalView, id);
  }

  function handleStatusDisapprove() {
    openModal(disapproveModalView, id);
  }

  return (
    <div className="gap-8 inline-flex w-auto items-center">
        {approveModalView && (
          <button
            onClick={handleStatusApprove}
            className="text-accent transition duration-200 hover:text-accent-hover focus:outline-none"
            title={t('common:text-approve')}
          >
            <CheckMarkCircle width={20} />
          </button>
        )}
        {disapproveModalView && (
          <button
            onClick={handleStatusDisapprove}
            className="text-red-500 transition duration-200 hover:text-red-600 focus:outline-none"
            title={t('common:text-disapprove')}
          >
            <CloseFillIcon width={20} />
          </button>
        )}
       {deleteModalView && (
        <button
          onClick={handleDelete}
          className="text-red-500 transition duration-200 hover:text-red-600 focus:outline-none"
          title={t('common:text-delete')}
        >
          <TrashIcon width={16} />
        </button>
      )}
      {editModalView && (
        <button
          onClick={handleEditModal}
          className="text-body transition duration-200 hover:text-heading focus:outline-none"
          title={t('common:text-edit')}
        >
          <EditIcon width={16} />
        </button>
      )}
      {editUrl && (
        <Link
          href={editUrl}
          className="text-base transition duration-200 hover:text-heading"
          title={t('common:text-edit')}
        >
          <EditIcon width={16} />
        </Link>
      )}
      {detailsUrl && (
        <Link
          href={detailsUrl}
          className="text-base transition duration-200 hover:text-heading"
          title={t('common:text-view')}
          locale={customLocale}
        >
          <Eye width={24} />
        </Link>
      )}
    </div>
  );
};

export default ActionButtons;
