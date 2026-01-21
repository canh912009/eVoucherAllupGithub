import Button from '@/components/ui/button';
import cn from 'classnames';
import { useTranslation } from 'next-i18next';

type ConfirmationInputCardProps = {
  onCancel: () => void;
  onSubmit: () => void;
  title?: string;
  titleClassName?: string;
  icon?: any;
  descriptions?: string[];
  descriptionClassNameWrapper?: string;
  descriptionClassName?: string;
  cancelBtnClassName?: string;
  submitBtnClassName?: string;
  cancelBtnText?: string;
  submitBtnText?: string;
  cancelBtnLoading?: boolean;
  submitBtnLoading?: boolean;
};

const ConfirmationSimplePopup: React.FC<ConfirmationInputCardProps> = ({
  onCancel,
  onSubmit,
  icon,
  title,
  titleClassName,
  descriptions = [],
  descriptionClassNameWrapper,
  descriptionClassName,
  cancelBtnText = 'button-cancel',
  submitBtnText = 'OK',
  cancelBtnClassName,
  submitBtnClassName,
  cancelBtnLoading,
  submitBtnLoading,
}) => {
  const { t } = useTranslation('common');
  return (
    <div className="m-auto w-full max-w-xl rounded-md bg-light p-4 pb-6 sm:w-[32rem] md:rounded-xl">
      <div className="h-full w-full text-center">
        <div className="flex h-full flex-col justify-between">
          {icon}
          <p
            className={cn(
              'mt-4 px-2 text-xl font-bold uppercase text-heading',
              titleClassName
            )}
          >
            {t(title)}
          </p>
          {descriptions.length > 0 && (
            <div className={cn('px-2 py-2', descriptionClassNameWrapper)}>
              {descriptions.map((description) => (
                <p key={description} className={cn(descriptionClassName)}>
                  {t(description)}
                </p>
              ))}
            </div>
          )}

          <div className="mt-6 flex w-full items-center justify-between space-s-4">
            <div className="flex w-full justify-end">
              <Button
                onClick={onCancel}
                loading={cancelBtnLoading}
                disabled={cancelBtnLoading}
                variant="custom"
                className={cn(
                  'rounded border border-gray-300 bg-white px-4 py-2 text-center text-base font-semibold text-gray-600 shadow-md transition duration-200 ease-in hover:bg-gray-300 focus:bg-gray-300 focus:outline-none',
                  cancelBtnClassName
                )}
              >
                {t(cancelBtnText)}
              </Button>
              &nbsp;
              <Button
                onClick={onSubmit}
                loading={submitBtnLoading}
                disabled={submitBtnLoading}
                variant="custom"
                className={cn(
                  'w-20 rounded bg-red-600 px-4 py-2 text-center text-base font-semibold text-light shadow-md transition duration-200 ease-in hover:bg-red-700 focus:bg-red-700 focus:outline-none',
                  submitBtnClassName
                )}
              >
                {t(submitBtnText)}
              </Button>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default ConfirmationSimplePopup;
