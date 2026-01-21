import Button from '@/components/ui/button';
import { useTranslation } from 'next-i18next';
import cn from 'classnames';
import React from 'react';

type ConfirmationInputCardProps = {
  onCancel: () => void;
  onSubmit: () => void;
  onInputChange?: (value: string) => void | any;
  title?: string;
  titleInput?: string;
  inputRequired?: boolean;
  icon?: any;
  description?: any;
  description2?: string;
  cancelBtnClassName?: string;
  submitBtnClassName?: string;
  cancelBtnText?: string;
  submitBtnText?: string;
  cancelBtnLoading?: boolean;
  submitBtnLoading?: boolean;
  showInput?: boolean;
};

const ConfirmationInputSimpleCard: React.FC<ConfirmationInputCardProps> = ({
  onCancel,
  onSubmit,
  onInputChange,
  icon,
  title = 'button-delete',
  titleInput,
  inputRequired = false,
  description,
  description2,
  cancelBtnText = 'button-cancel',
  submitBtnText = 'button-delete',
  cancelBtnClassName,
  submitBtnClassName,
  cancelBtnLoading,
  submitBtnLoading,
  showInput = true,
}) => {
  const { t } = useTranslation('common');
  const rootClassName =
    'ps-6 pe-4 h-12 flex items-center w-full rounded appearance-none transition duration-300 ease-in-out text-heading text-sm focus:outline-none focus:ring-0 border border-border-base focus:border-accent';

  const renderFormattedText = (text: string) => {
    return text.split('\n').map((line, index) => (
      <React.Fragment key={index}>
        <span dangerouslySetInnerHTML={{ __html: t(line) }} />
        {index < text.split('\n').length - 1 && <br />}
      </React.Fragment>
    ));
  };

  return (
    <div className="m-auto w-full max-w-3xl rounded-md bg-light p-4 pb-6 sm:w-[48rem] md:rounded-xl">
      <div className="h-full w-full">
        <div className="flex h-full flex-col justify-between px-6">
          {icon}
          <p className="mt-4 text-2xl font-bold text-heading text-red-500">{t(title)}</p>
          {description && (
            <p className="py-2 leading-relaxed text-body-dark dark:text-muted">
              {renderFormattedText(description)}
            </p>
          )}
          {description2 && (
            <p className="py-2 leading-relaxed text-body-dark dark:text-muted  ">
              {renderFormattedText(description2)}
            </p>
          )}

          {onInputChange && (
            <>
              {titleInput && (
                <div className="mb-2 mt-6 flex w-full space-s-2">
                  <span>{titleInput}</span> {inputRequired && <span className="text-red-500">*</span>}
                </div>
              )}
              {showInput && <div className="flex w-full items-center justify-between space-s-4">
                <input
                  type="text"
                  className={rootClassName}
                  onChange={(e) => onInputChange(e.target.value)}
                />
              </div>}
            </>
          )}

          <div className="mt-8 mb-2 flex w-full items-center space-s-4">
            <div className="flex w-full justify-end">
              <Button
                onClick={onCancel}
                loading={cancelBtnLoading}
                disabled={cancelBtnLoading}
                variant="outline"
                className={cn(
                  'mr-4 rounded bg-accent px-8 py-4 text-center text-base font-semibold text-light shadow-md transition duration-200 ease-in hover:bg-accent-hover focus:bg-accent-hover focus:outline-none',
                  cancelBtnClassName
                )}
              >
                {t(cancelBtnText)}
              </Button>
              <Button
                onClick={onSubmit}
                loading={submitBtnLoading}
                disabled={submitBtnLoading}
                variant="custom"
                className={cn(
                  'rounded bg-red-600 px-8 py-4 text-center text-base font-semibold text-light shadow-md transition duration-200 ease-in hover:bg-red-700 focus:bg-red-700 focus:outline-none',
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

export default ConfirmationInputSimpleCard;
