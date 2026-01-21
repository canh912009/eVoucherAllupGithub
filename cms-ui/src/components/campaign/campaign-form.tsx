import { campaignValidationSchema } from '@/components/campaign/campaign-validation-schema';
import Card from '@/components/common/card';
import Button from '@/components/ui/button';
import { DatePicker } from '@/components/ui/date-picker';
import ValidationError from '@/components/ui/form-validation-error';
import Loader from '@/components/ui/loader/loader';
import SelectInput from '@/components/ui/select-input-autocomplete';
import TextArea from '@/components/ui/text-area';
import {
  useCreateCampaignMutation,
  useUpdateCampaignMutation,
} from '@/data/campaign';
import { customerClient } from '@/data/client/crud-client';
import { useCustomersQuery } from '@/data/customer';
import { useCustomerContractsQuery } from '@/data/customer-contract';
import { getUrlPublicAsset } from '@/data/download';
import { useMessageTemplatesQuery } from '@/data/message-template';
import { useUploadImageMutation } from '@/data/upload';
import {
  Campaign,
  CommonApproveStatusAction,
  CommonStatusCode,
  CustomerContract,
  CustomerTypeCode,
  Good,
  MessageTemplate,
  PathOptions,
} from '@/types';
import { formatDate } from '@/utils/common-utils';
import { PAGE_SIZE, SMS, SYSTEM_BRAND_TYPE } from '@/utils/constants';
import { getErrorMessage } from '@/utils/form-error';
import useInputTimeout from '@/utils/use-input-timeout';
import { yupResolver } from '@hookform/resolvers/yup';
import { useTranslation } from 'next-i18next';
import { useRouter } from 'next/router';
import { ChangeEvent, useEffect, useState } from 'react';
import { Controller, useForm } from 'react-hook-form';
import { toast } from 'react-toastify';
import StatusCodeBadge from '../common/status-code-badge';
import { useModalAction } from '../ui/modal/modal.context';
import CampaignGoodsList from './campaign-product-list';
import {Switch} from "@headlessui/react";
import SwitchDisplay from "@/components/ui/switch-display";
import SwitchInput from "@/components/ui/switch-input";
import GoodsListCustomPagination from "@/components/good/product-list-custome-pagination";

type FormValues = Partial<Campaign> & {
  messageTemplateDescription: string;
};

type IProps = {
  initialValues?: Campaign | null;
};

export default function CreateOrUpdateCampaignForm({
  initialValues,
}: Readonly<IProps>) {
  // console.log('initialValues', initialValues);
  const rootClassName =
    'ps-6 pe-4 h-12 flex items-center w-full md:w-3/4 rounded appearance-none transition duration-300 ease-in-out text-heading text-sm focus:outline-none focus:ring-0 border border-border-base focus:border-accent';

  const router = useRouter();
  const { t } = useTranslation();
  const { openModal, closeModal } = useModalAction();

  const {
    register,
    handleSubmit,
    control,
    watch,
    setValue,
    setError,
    formState: { errors },
  } = useForm<FormValues>({
    // @ts-ignore
    defaultValues: initialValues
      ? {
          ...initialValues,
          startDate: initialValues.startDate
            ? new Date(initialValues.startDate)
            : '',
          endDate: initialValues.endDate ? new Date(initialValues.endDate) : '',
          customerContractId: initialValues.customerContract?.id,
        }
      : {},
    resolver: yupResolver(campaignValidationSchema),
  });

  // default value
  useEffect(() => {
    setValue('messageSubject', "Mời bạn trải nghiệm mua sắm thú vị cùng aQuà voucher");
    setValue('messageContent', "aQuà voucher gửi tặng bạn một món quà đặc biệt.\n" +
      "Hãy nhập mã này khi thanh toán để nhận được ưu đãi\n" +
      "Chúng tôi mong rằng bạn sẽ có trải nghiệm mua sắm tuyệt vời!");
  }, []);

  /**
   * Customer
   */
  const customerId = watch('customerId');
  const customer = watch('customer');

  const { inputText: customerName, onInputChange: handleInputChangeCustomer } =
    useInputTimeout();
  const { customers, loading: loadingCustomers } = useCustomersQuery(
    { page: 1, pageSize: PAGE_SIZE },
    {
      customerName: customerName,
      approveStatusCode: CommonApproveStatusAction.APPRV,
    }
  );

  const handleChangeCustomer = (option: any) => {
    setValue('customer', option);
    setValue('senderName', option?.customerName);
    setValue('customerContract', null as unknown as CustomerContract);
  };

  /**
   * Customer Contract
   */
  const {
    inputText: customerContractName,
    onInputChange: handleInputChangeCustomerContract,
  } = useInputTimeout();

  const { customerContracts, loading: loadingCustomerContracts } =
    useCustomerContractsQuery(
      { page: 1, pageSize: PAGE_SIZE },
      {
        contractName: customerContractName,
        customerId: customer?.id,
        approveStatusCode: CommonApproveStatusAction.APPRV,
      }
    );

  const handleChangeCustomerContract = (option: any) => {
    setValue('customerContract', option);
    setValue('customerContractId', option?.id);

    if (option?.startDate) {
      setValue('startDate', new Date(option?.startDate) as any);
    }
    if (option?.endDate) {
      setValue('endDate', new Date(option?.endDate) as any);
    }

    if (option?.customerId) {
      setValue('customerId', option?.customerId);
    } else {
      // @ts-ignore
      setValue('customerId', '');
    }
  };

  /**
   * Image
   */
  const contentImageName = watch('contentImageName');
  const contentImagePath = watch('contentImagePath');

  const { mutate: uploadImage, isLoading: uploadingImage } =
    useUploadImageMutation();

  const handleFileChange = (e: ChangeEvent<HTMLInputElement>) => {
    setValue('contentImageName', '');
    setValue('contentImagePath', '');

    if (e.target.files) {
      const file = e.target.files[0];

      if (file.size > 200 * 1024) {
        // 200KB
        // Hiển thị thông báo hoặc xử lý khi tệp tin vượt quá dung lượng cho phép
        alert('File size exceeds 200KB limit!');
        // Xoá tập tin đã chọn (nếu muốn)
        // @ts-ignore
        e.target.value = null;
        return;
      }

      if (file) {
        uploadImage(file, {
          onSuccess: (data: any) => {
            // console.log('data', data);
            setValue('contentImageName', file.name);
            setValue('contentImagePath', data?.path);
          },
          onError: (error: any) => {
            toast.error('Error:' + error?.response?.data.message);
          },
        });
      }
    }
  };

  const deleteFile = () => {
    setValue('contentImageName', '');
    setValue('contentImagePath', '');
  };

  /**
   * Product
   */
  const [selectedProducts, setSelectedProducts] = useState<Good[]>(
    initialValues?.listGoods ?? []
  );
  const [errorMessageGoods, setErrorMessageGoods] = useState<string>();
  // console.log('errorMessageGoods', errorMessageGoods)

  const [systemType, setSystemType] = useState<string>('')

  const determineSystemType = (products: Good[]): string => {
    const hasChoice = products.some(
      (product) => product.system === SYSTEM_BRAND_TYPE.CHOICE
    );
    const hasBulk = products.some(
      (product) => product.system === SYSTEM_BRAND_TYPE.BULK
    );

    if (hasChoice && hasBulk) {
      return `${SYSTEM_BRAND_TYPE.CHOICE},${SYSTEM_BRAND_TYPE.BULK}`;
    } else if (hasChoice) {
      return SYSTEM_BRAND_TYPE.CHOICE;
    } else if (hasBulk) {
      return SYSTEM_BRAND_TYPE.BULK;
    } else {
      return SYSTEM_BRAND_TYPE.NORMAL;
    }
  };

  const warningCustomerLength =
    systemType !== SYSTEM_BRAND_TYPE.NORMAL &&
    customer?.customerName?.length! >
      SMS.CUSTOMER_WITH_GOOD_TYPE_CHOICE_MAX_LENGTH
      ? `Customer name length must be less than or equal ${SMS.CUSTOMER_WITH_GOOD_TYPE_CHOICE_MAX_LENGTH} with product ${systemType}`
      : '';

  const addGood = (option: Good) => {
    // Clear warning
    setErrorMessageGoods('');

    if (option?.id) {
      // Check if the option with the same id already exists in the selectedProducts array
      const goodExists = selectedProducts.some(
        (product) => product.id === option.id
      );

      if (!goodExists) {
        // setSelectedProducts([...selectedProducts, option]);
        setSelectedProducts((prevProducts) => [...prevProducts, option]);
      }
    }
  };

  const removeGood = (option: Good) => {
    if (option?.id) {
      // setSelectedProducts(selectedProducts.filter((product) => product.id !== option.id));
      setSelectedProducts((prevProducts) =>
        prevProducts.filter((product) => product.id !== option.id)
      );
    }
  };

  const removeAllGood = () => {
    setValue('messageTemplate', null as unknown as MessageTemplate);
    setValue('messageTemplateDescription', null as unknown as string);
    setSelectedProducts([]);
  };

  function handlePopup(modalView: any) {
    openModal(modalView, { handleSaveItems, selectedProducts });
  }

  function handleSaveItems(items: any) {
    // console.log('items', items);
    items.forEach((item: any) => addGood(item));
    closeModal();
  }

  /**
   * Message Template
   */
  const messageTemplate = watch('messageTemplate');
  const { messageTemplates, loading: messageTemplateLoading } = useMessageTemplatesQuery(
    null as unknown as PathOptions,
    { system: SYSTEM_BRAND_TYPE.NORMAL, }
  );
  const handleChangeMessageTemplate = (option: any) => {
    setValue('messageTemplate', option);
    if (option) {
      setValue('messageTemplateDescription', option.messageString);
    } else {
      setValue('messageTemplateDescription', '');
    }
  };

  /**
   * Event onClick button
   */
  const [actionType, setActionType] = useState<CommonApproveStatusAction | ''>(
    ''
  );

  const handleClick = (action: any) => {
    setActionType(action);
  };

  const { mutate: createCampaign, isLoading: creating } =
    useCreateCampaignMutation();
  const { mutate: updateCampaign, isLoading: updating } =
    useUpdateCampaignMutation();

  /**
   * Set customer if choose customer contract
   */
  useEffect(() => {
    const fetchData = async () => {
      try {
        if (!customerId) {
          return;
        }

        const response = await customerClient.get(customerId);
        // console.log('cusomter', response.data);
        setValue('customer', response.data);
      } catch (error) {
        console.error('Error fetching data:', error);
      }
    };

    fetchData();
  }, [customerId]);

  /**
   * Show warning customer length
   */
  useEffect(() => {
    setError('customer', {
      type: 'manual',
      message: warningCustomerLength,
    });
  }, [warningCustomerLength]);

  useEffect(() => {
    if (initialValues?.messageTemplate && messageTemplates) {
      let messageTemplate = messageTemplates.find(
        (item) => item.id === initialValues.messageTemplate.id
      );
      if (messageTemplate) {
        setValue('messageTemplate', messageTemplate);
      }
    }
  }, [messageTemplates, setValue]);

  useEffect(() => {
    if (!selectedProducts || selectedProducts.length < 1) {
      setValue('messageTemplate', null as unknown as MessageTemplate);
      setValue('messageTemplateDescription', null as unknown as string);
      setSystemType('');
    }
    if (selectedProducts && selectedProducts.length > 0) {
      const systemType = determineSystemType(selectedProducts);
      setSystemType(systemType);
      // console.log('System Type:', systemType);
    }
  }, [selectedProducts]);

  /**
   * Submit
   */
  const onSubmit = async (values: FormValues) => {
    // console.log('values', values);
    // console.log('selectedProducts', selectedProducts);
    // console.log('actionType', actionType, selectedCustomerId);
    // return;

    /**
     * Validate length customer if selected product is type CHOICE
     */
    if (warningCustomerLength !== '') {
      setError('customer', {
        type: 'manual',
        message: warningCustomerLength,
      });
      return;
    }

    /**
     * Validate goods
     */
    if (!selectedProducts || selectedProducts.length < 1) {
      setErrorMessageGoods('Please select product');
      return;
    }
    const productsId: { id: number }[] = selectedProducts.map((good) => ({
      id: good.id,
    }));
    // console.log('productsId', productsId)
    // return;

    /**
     * Validate Message Subject
     */
    if (
      values.customer?.customerTypeCode !== CustomerTypeCode.CHANNEL &&
      !values.messageSubject
    ) {
      setError('messageSubject', {
        type: 'manual',
        message: 'Voucher subject is required',
      });
      return;
    }
    if (
      values.customer?.customerTypeCode !== CustomerTypeCode.CHANNEL &&
      !values.messageContent
    ) {
      setError('messageContent', {
        type: 'manual',
        message: 'Voucher content is required',
      });
      return;
    }

    /**
     * Validate Message Template
     */
    if (!values.messageTemplate) {
      setError('messageTemplate', {
        type: 'manual',
        message: 'Message template is required',
      });
      return;
    }

    let inputValues = {
      customerId: values.customer?.id,
      customerContractId: values.customerContractId,
      campaignName: values.campaignName,
      startDate: values.startDate ? formatDate(values.startDate) : '',
      endDate: values.endDate ? formatDate(values.endDate) : '',
      messageTemplateId: values.messageTemplate?.id,
      messageSubject:
        values.customer?.customerTypeCode !== CustomerTypeCode.CHANNEL
          ? values.messageSubject
          : '',
      senderName: values.senderName,
      messageContent:
        values.customer?.customerTypeCode !== CustomerTypeCode.CHANNEL
          ? values.messageContent
          : '',
      contentLink: values.contentLink,
      contentImageName: values.contentImageName,
      contentImagePath: values.contentImagePath,
      goods: productsId,
      showPopupYn: values.showPopupYn ? "Y" : "N"
    };
    // console.log('inputValues', inputValues);
    // return;

    try {
      if (!initialValues) {
        if (actionType === '') {
          // Save Draff
          createCampaign({
            ...inputValues,
          });
        } else {
          // Save Register
          createCampaign({
            ...inputValues,
            approveStatusCode: actionType,
          });
        }
      } else {
        if (actionType === '') {
          // Update
          updateCampaign({
            ...inputValues,
            id: initialValues.id,
          });
        } else {
          // Update register
          updateCampaign({
            ...inputValues,
            id: initialValues.id,
            approveStatusCode: actionType,
          });
        }
      }
    } catch (error) {
      const serverErrors = getErrorMessage(error);
      Object.keys(serverErrors?.validation).forEach((field: any) => {
        setError(field.split('.')[1], {
          type: 'manual',
          message: serverErrors?.validation[field][0],
        });
      });
    }
  };
  return (
    <form onSubmit={handleSubmit(onSubmit)}>
      <div className="my-5 flex flex-wrap sm:my-8">
        <Card className="w-full sm:w-full md:w-full">
          <div className="mb-5 flex flex-wrap">
            <div className="flex w-full flex-wrap px-4 py-2">
              <h1 className="text-lg font-semibold uppercase">
                {t('Campaign Information')}
              </h1>
            </div>
          </div>

          {initialValues && (
            <div className="flex flex-wrap">
              <div className="mb-5 px-4 sm:w-full md:w-1/2">
                {t('Status')}:{' '}
                {<StatusCodeBadge statusCode={initialValues?.statusCode} />}
              </div>
            </div>
          )}

          <div className="mb-5 flex flex-wrap">
            <div className="flex w-full flex-wrap px-4 md:w-1/2">
              <label className="w-full py-2 text-heading md:w-1/4">
                {t('Customer Name')} <span className="text-red-500">*</span>
              </label>
              <div className="w-full md:w-3/4">
                <SelectInput
                  name="customer"
                  options={customers}
                  isLoading={loadingCustomers}
                  getOptionLabel={(option: any) =>
                    option.customerName + ' - ' + option.id
                  }
                  getOptionValue={(option: any) => option.id}
                  onInputChange={handleInputChangeCustomer}
                  onChange={handleChangeCustomer}
                  placeholder={t('common:filter-by-group-placeholder')}
                  control={control}
                  isClearable={true}
                />
                <ValidationError message={t(errors.customer?.message)} />
              </div>
            </div>
            <div className="flex w-full flex-wrap px-4 md:w-1/2">
              <label className="w-full py-2 text-heading md:w-1/4">
                {t('Contract Name')} <span className="text-red-500">*</span>
              </label>
              <div className="w-full md:w-3/4">
                <SelectInput
                  name="customerContract"
                  options={customerContracts}
                  isLoading={loadingCustomerContracts}
                  getOptionLabel={(option: any) =>
                    option.contractName + ' - ' + option.id
                  }
                  getOptionValue={(option: any) => option.id}
                  onInputChange={handleInputChangeCustomerContract}
                  onChange={handleChangeCustomerContract}
                  placeholder={t('common:filter-by-group-placeholder')}
                  control={control}
                  isClearable={true}
                />
                <ValidationError
                  message={t(errors.customerContract?.message)}
                />
              </div>
            </div>
          </div>

          <div className="mb-5 flex flex-wrap">
            <div className="flex w-full flex-wrap px-4 md:w-1/2">
              <label className="w-full py-2 text-heading md:w-1/4">
                {t('Campaign name')} <span className="text-red-500">*</span>
              </label>
              <input
                className={rootClassName}
                type="text"
                // disabled={initialValues ? true : false}
                id="campaignName"
                {...register('campaignName')}
                placeholder={t('Campaign name')}
                autoComplete="off"
              />
              <span className="w-full text-xs text-red-500 text-start md:w-3/4 md:pl-[25%]">
                {t(errors.campaignName?.message!)}
              </span>
            </div>
            <div className="flex w-full flex-wrap px-4 md:w-1/2">
              <label className="w-full py-2 text-heading md:w-1/4">
                {t('Contract ID')} <span className="text-red-500">*</span>
              </label>
              <input
                className={`${rootClassName} ${'bg-gray-100'}`}
                type="text"
                disabled={true}
                id="customerContractId"
                {...register('customerContractId')}
                placeholder={t('Contract ID')}
                autoComplete="off"
              />
              <span className="w-full text-xs text-red-500 text-start md:w-3/4 md:pl-[25%]">
                {t(errors.customerContractId?.message!)}
              </span>
            </div>
          </div>

          <div className="mb-5 flex flex-wrap">
            <div className="flex w-full flex-wrap px-4 md:w-1/2">
              <label className="w-full py-2 text-heading md:w-1/4">
                {t('Start date')} <span className="text-red-500">*</span>
              </label>
              <div className="w-full md:w-3/4">
                <Controller
                  control={control}
                  name="startDate"
                  render={({ field: { onChange, onBlur, value } }) => (
                    <DatePicker
                      dateFormat="yyyy-MM-dd"
                      onChange={onChange}
                      onBlur={onBlur}
                      //@ts-ignore
                      selected={value}
                      selectsStart
                      startDate={new Date()}
                      className="border border-border-base"
                    />
                  )}
                />
                <ValidationError message={t(errors.startDate?.message!)} />
              </div>
            </div>
            <div className="flex w-full flex-wrap px-4 md:w-1/2">
              <label className="w-full py-2 text-heading md:w-1/4">
                {t('End date')} <span className="text-red-500">*</span>
              </label>
              <div className="w-full md:w-3/4">
                <Controller
                  control={control}
                  name="endDate"
                  render={({ field: { onChange, onBlur, value } }) => (
                    <DatePicker
                      dateFormat="yyyy-MM-dd"
                      onChange={onChange}
                      onBlur={onBlur}
                      //@ts-ignore
                      selected={value}
                      selectsEnd
                      startDate={new Date()}
                      className="border border-border-base"
                    />
                  )}
                />
                <ValidationError message={t(errors.endDate?.message!)} />
              </div>
            </div>
          </div>

          {/*Product Information*/}
          <div className=" flex flex-wrap">
            <div className="flex w-full flex-wrap px-4 py-2">
              <label className="w-full text-lg font-semibold uppercase text-heading md:w-2/12">
                {t('Product Information')}
              </label>
              <Button
                size="small"
                className="bg-red-700 hover:bg-red-800"
                onClick={(event) => {
                  event.preventDefault();
                  return handlePopup('PRODUCT_LIST_CAMPAIGN');
                }}
              >
                {t('Add Products')}
              </Button>
              <div>
                <span className="w-full px-4 text-xs text-red-500 text-start  ">
                  {errorMessageGoods}
                </span>
              </div>
            </div>
          </div>
          <div className=" flex flex-wrap">
            <div className="flex w-full flex-wrap px-4">
              {selectedProducts.length > 0 && (
                <label className="w-full italic text-heading text-red-500 md:w-9/12">
                  {t(
                    '** If you want to add products other than the current SYSTEM, you need to delete the entire old list'
                  )}
                </label>
              )}
            </div>
            { selectedProducts.length == 0 && <div className=" flex flex-wrap mb-4">
              <span className="w-full px-4 italic text-heading text-start  ">
                  No products selected. Please select at least one product
                </span>
            </div> }
          </div>
          {selectedProducts.length > 0 && <div className="mb-5 flex flex-wrap">
            <div className="flex w-full flex-wrap px-4 py-2 md:w-full ">
              <GoodsListCustomPagination
                allGoods={selectedProducts}
                actionRemoveItem={removeGood}
                removeAll={removeAllGood}
              />
            </div>
          </div> }

          {/*SMS TEMPLATE*/}
          <div className=" flex flex-wrap">
            <div className="flex w-full flex-wrap px-4 py-2">
              <h1 className="text-lg font-semibold uppercase">
                {t('SMS TEMPLATE')}
              </h1>
            </div>
          </div>
          {selectedProducts.length > 0 && (
            <div className=" flex flex-wrap">
              <div className="flex w-full flex-wrap px-4">
                <label className="w-full  text-heading md:w-2/12">
                  {t('Message Template')}{' '} <span className="text-red-500">*</span>
                </label>
                <div className="w-full md:w-9/12 ">
                  <SelectInput
                    name="messageTemplate"
                    control={control}
                    getOptionLabel={(option: any) => option?.name}
                    getOptionValue={(option: any) => option?.id}
                    onChange={handleChangeMessageTemplate}
                    options={messageTemplates ?? []}
                    isLoading={messageTemplateLoading}
                  />
                  <ValidationError
                    message={t(errors.messageTemplate?.message)}
                  />
                </div>
              </div>
              {messageTemplate && (
                <div className="flex w-full flex-wrap px-4">
                  <label className="w-full  text-heading md:w-2/12">
                    {t('Message Template Description')}
                  </label>
                  <div className="w-full md:w-9/12 ">
                    <TextArea
                      placeholder={t('Message Template description')}
                      {...register('messageTemplateDescription')}
                      variant="outline"
                      disabled={true}
                      className="w-full"
                    />
                  </div>
                </div>
              )}
            </div>
          )}
          <div className="mb-5 flex flex-wrap">
            <div className="flex w-full flex-wrap px-4">
              <label className="w-full  text-heading md:w-2/12">
                {t('Sender name')} <span className="text-red-500">*</span>
              </label>
              <input
                className={`${rootClassName} md:w-9/12`}
                type="text"
                id="senderName"
                {...register('senderName')}
                placeholder={t('Sender name')}
                autoComplete="off"
              />
              <span className="w-full text-xs text-red-500 text-start md:w-full">
                {t(errors.senderName?.message!)}
              </span>
            </div>
            </div>

          {/*gift message*/}
          <div className=" flex flex-wrap">
            <div className="flex w-full flex-wrap px-4 py-2">
              <h1 className="text-lg font-semibold uppercase">
                {t('gift message')}
              </h1>
            </div>
          </div>
          <div className="flex w-full flex-wrap px-4">
            <label className="w-full  text-heading md:w-2/12">
              {t('Enable popup')}
            </label>
            <div className="flex items-center space-x-2 md:w-9/12 my-2">
              <SwitchInput name="showPopupYn" control={control} />
              <span className="text-sm text-gray-500 italic">
                {t('Enable to show pop up message when user views the voucher for the first time')}
              </span>
            </div>
          </div>
          {customer?.customerTypeCode !== CustomerTypeCode.CHANNEL && (
            <div className=" flex flex-wrap">
              <div className="flex w-full flex-wrap px-4">
                <label className="w-full  text-heading md:w-2/12">
                  {t('Voucher subject')} <span className="text-red-500">*</span>
                </label>
                <input
                  className={`${rootClassName} md:w-9/12`}
                  type="text"
                  id="messageSubject"
                  {...register('messageSubject')}
                  placeholder={t('Voucher subject')}
                  autoComplete="off"
                />
                <span className="w-full text-xs text-red-500 text-start md:w-full">
                  {t(errors.messageSubject?.message!)}
                </span>
              </div>
              <div className="flex w-full flex-wrap px-4 mt-2">
                <label className="w-full  text-heading md:w-2/12">
                  {t('Voucher content')} <span className="text-red-500">*</span>
                </label>
                <TextArea
                  placeholder={t('Voucher content')}
                  {...register('messageContent')}
                  variant="outline"
                  className="w-full md:w-9/12"
                />
                <span className="w-full text-xs text-red-500 text-start md:w-full">
                  {t(errors.messageContent?.message!)}
                </span>
              </div>
            </div>
          )}
          <div className="mb-5 flex flex-wrap">
            <div className="flex w-full flex-wrap px-4">
              <label className="w-full  text-heading md:w-2/12">
                {t('Content Link')}
              </label>
              <input
                className={`${rootClassName} md:w-9/12`}
                type="text"
                id="contentLink"
                {...register('contentLink')}
                placeholder={t('Content link')}
                autoComplete="off"
              />
              <span className="w-full text-xs text-red-500 text-start md:w-3/4 md:pl-[25%]">
                {t(errors.contentLink?.message!)}
              </span>
            </div>
          </div>
          <div className="mb-5 flex flex-wrap">
            <div className="flex w-full flex-wrap px-4">
              <label className="w-full  text-heading md:w-2/12">
                {t('Content Image')}
              </label>
              <div className="w-full pt-1 md:w-9/12">
                <input
                  id="file_input"
                  type="file"
                  accept=".jpg, .jpeg, .png, .gif, .svg"
                  className={'e_hide-text'}
                  onChange={handleFileChange}
                  style={{ width: '100px' }}
                />
                {contentImagePath && (
                  <Button
                    size="small"
                    onClick={deleteFile}
                    className="mt-2 w-full bg-red-700 hover:bg-slate-700 md:w-auto md:ms-6"
                    type="button"
                  >
                    {t('Delete')}
                  </Button>
                )}
                {uploadingImage && (
                  <Loader uploadFile={true} text={t('common:text-loading')} />
                )}
                {contentImageName && <p>Selected file: {contentImageName}</p>}
              </div>
              <span className="w-full text-xs text-red-500 text-start md:w-3/4 md:pl-[25%]">
                {t(errors.contentImagePath?.message!)}
              </span>
            </div>
            {contentImagePath && (
              <div className="flex w-full flex-wrap px-4 mt-3 ">
                <div className="w-full py-2 md:w-2/12" />
                <img
                  src={getUrlPublicAsset(contentImagePath) ?? ''}
                  alt={'Content Image'}
                  width={300}
                  height={300}
                />
              </div>
            )}
          </div>
        </Card>
      </div>
      <div className="mb-4 text-end">
        <Button
          variant="outline"
          onClick={router.back}
          className="me-4 hover:bg-red-800"
          type="button"
        >
          {t('form:button-label-back')}
        </Button>

        {/** Page create */}
        {!initialValues && (
          <>
            <Button variant="outline" className="me-4" loading={creating}>
              {t('Save')}
            </Button>
            <Button
              className="bg-red-700 me-4 hover:bg-red-800"
              loading={creating}
              onClick={() => handleClick(CommonApproveStatusAction.REQ)}
            >
              {t('Register')}
            </Button>
          </>
        )}

        {/** Page update */}
        {initialValues && (
          <>
            {!initialValues.statusCode && (
              <>
                <Button variant="outline" className="me-4" loading={updating}>
                  {t('Save')}
                </Button>
                <Button
                  className="bg-red-700 me-4 hover:bg-red-800"
                  loading={updating}
                  onClick={() => handleClick(CommonApproveStatusAction.REQ)}
                >
                  {t('Register')}
                </Button>
              </>
            )}
            {initialValues.statusCode === CommonStatusCode.REJECTED && (
              <Button
                className="bg-red-700 me-4 hover:bg-red-800"
                loading={updating}
                onClick={() => handleClick(CommonApproveStatusAction.REQ)}
              >
                {t('Re-register')}
              </Button>
            )}
            {initialValues.statusCode === CommonStatusCode.CANCEL_APPRV && (
              <Button
                className="bg-red-700 me-4 hover:bg-red-800"
                loading={updating}
                onClick={() => handleClick(CommonApproveStatusAction.REQ)}
              >
                {t('Re-register')}
              </Button>
            )}

            {initialValues.statusCode === CommonStatusCode.END &&
              initialValues.customer?.customerTypeCode ===
                CustomerTypeCode.CHANNEL && (
                <Button
                  className="bg-red-700 me-4 hover:bg-red-800"
                  loading={updating}
                  onClick={() => handleClick(CommonApproveStatusAction.REQ)}
                >
                  {t('Re-register')}
                </Button>
              )}
          </>
        )}
      </div>
    </form>
  );
}
