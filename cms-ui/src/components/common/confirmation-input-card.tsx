import Button from '@/components/ui/button';
import TextArea from '@/components/ui/text-area';
import { useTranslation } from 'next-i18next';
import cn from 'classnames';

type ConfirmationInputCardProps = {
  onCancel: () => void;
  onSubmit: () => void;
  onInputChange?: (value: string) => void;
  title?: string;
  titleInput?: string;
  icon?: any;
  description?: string;
  cancelBtnClassName?: string;
  submitBtnClassName?: string;
  cancelBtnText?: string;
  submitBtnText?: string;
  cancelBtnLoading?: boolean;
  submitBtnLoading?: boolean;
};

const ConfirmationInputCard: React.FC<ConfirmationInputCardProps> = ({
  onCancel,
  onSubmit,
  onInputChange,
  icon,
  title = 'button-delete',
  titleInput,
  description,
  cancelBtnText = 'button-cancel',
  submitBtnText = 'button-delete',
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
          <p className="mt-4 text-xl font-bold text-heading uppercase">{t(title)}</p>
          {description && 
            <p className="px-6 py-2 leading-relaxed text-body-dark dark:text-muted">
              {t(description)}
            </p>
          }

          {onInputChange && (
            <>
              {titleInput && (
                <div className="mt-8 flex w-full items-center justify-between space-s-4">
                  <span>{titleInput}</span>
                </div>
              )}
              <div className="flex w-full items-center justify-between space-s-4">
                <TextArea
                  name="inputText"
                  variant="outline"
                  className="w-full"
                  onChange={(e) => onInputChange(e.target.value)}
                />
              </div>
            </>
          )}

          <div className="mt-8 flex w-full items-center justify-between space-s-4">
            <div className="w-full flex flex-row-reverse">
              <Button
                onClick={onCancel}
                loading={cancelBtnLoading}
                disabled={cancelBtnLoading}
                variant="custom"
                className={cn(
                  ' rounded bg-accent px-4 py-2 text-center text-base font-semibold text-light shadow-md transition duration-200 ease-in hover:bg-accent-hover focus:bg-accent-hover focus:outline-none',
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
                  'rounded bg-red-600 px-4 py-2 text-center text-base font-semibold text-light shadow-md transition duration-200 ease-in hover:bg-red-700 focus:bg-red-700 focus:outline-none',
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

export default ConfirmationInputCard;
