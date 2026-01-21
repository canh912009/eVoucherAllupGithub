import { useForm } from 'react-hook-form';
import Button from '@/components/ui/button';
import Card from '@/components/common/card';
import { useRouter } from 'next/router';
import Checkbox from '@/components/ui/checkbox/checkbox';
import Radio from '@/components/ui/radio/radio';
import SelectInput from '@/components/ui/select-input';
import { useTranslation } from 'next-i18next';
import { yupResolver } from '@hookform/resolvers/yup';
import * as XLSX from 'xlsx';
import Papa from 'papaparse';
import {
  Campaign,
  Code,
  CommonApproveStatusAction,
  CommonYesNoEnum,
  Delivery,
  DeliveryReceiveType,
  DeliveryUploadType,
  DeliveryUser,
  DeliveryUserInput,
  User,
  CommonStatusCode,
  MappedPaginatorInfoEV,
  PaginatorInfoEV, Good,
} from '@/types';
import { getErrorMessage } from '@/utils/form-error';
import { useEffect, useState, ChangeEvent, useRef } from 'react';
import {
  CODE_GROUP,
  SMS,
  PAGE_SIZE,
  PERMISSIONS_EV as p,
} from '@/utils/constants';
import {
  useCreateDeliveryMutation,
  useUpdateDeliveryMutation,
} from '@/data/delivery';
import { deliveryValidationSchema } from '@/components/delivery/delivery-validation-schema';
import { useUploadDocumentMutation, useUploadImageMutation } from '@/data/upload';
import { useCodeGroupQuery } from '@/data/code-group';
import ValidationError from '../ui/form-validation-error';
import TextArea from '../ui/text-area';
import DeliveryStatusCodeBadge from './delivery-status-code-badge';
import DeliveryUsersList from './delivery-users-list';
import { Table } from '@/components/ui/table';
import {
  isValidDateFormat,
  extractNumbers,
  reformatTextSms, formatNumber,
} from '@/utils/common-utils';
import { isPermitted } from '@/utils/auth-utils';
import { getUrlPublicAsset } from '@/data/download';
import { toast } from 'react-toastify';
import Loader from '@/components/ui/loader/loader';
import { mapPaginatorData } from '@/utils/data-mappers-ev';
import {handlePriceInputChange} from "@/components/good/good-form";
import SwitchInput from "@/components/ui/switch-input";
import SwitchDisplay from "@/components/ui/switch-display";

type FormValues = Partial<Delivery> & {
  code_settlement: Code;
  isDuplicateAllow: boolean;
  numberOfTarget: number;
};

type IProps = {
  initialValues?: Delivery | null;
  campaign?: Campaign | null;
};

export default function CreateOrUpdateDeliveryForm({
  initialValues,
  campaign,
}: Readonly<IProps>) {
  // console.log('initialValues', initialValues);
  // console.log('campaign', campaign);

  /**
   * Codes, Code Groups
   */
  const { codeGroup, loading: codeLoading } = useCodeGroupQuery(
    CODE_GROUP.SETTLEMENT_METHOD_CD
  );

  const router = useRouter();
  const { t } = useTranslation();

  const rootClassName =
    'ps-6 pe-4 h-12 flex items-center w-full md:w-3/4 rounded appearance-none transition duration-300 ease-in-out text-heading text-sm focus:outline-none focus:ring-0 border border-border-base focus:border-accent';

  const smallClassName =
    'h-12 flex items-center w-full md:w-1/2 rounded appearance-none transition duration-300 ease-in-out text-heading text-sm focus:outline-none focus:ring-0 border border-border-base focus:border-accent';

  const columnsGoods = [
    {
      title: '',
      dataIndex: 'id',
      key: 'id',
      width: 30,
      align: 'center',
      ellipsis: true,
      render: (id: number) => (
        <Radio
          id={`good_${id}`}
          {...register('goodsId')}
          onClick={() => {
            setErrorMessageGoodsId('');
            // @ts-ignore
            setValue('sellPrice', initialValues
                ? initialValues?.campaign?.listGoods?.filter(item => item.id === id)[0]?.sellPrice
                : campaign?.listGoods?.filter(item => item.id === id)[0]?.sellPrice );
            // @ts-ignore
            setValue('sellListPrice', initialValues
                ? initialValues?.campaign?.listGoods?.filter(item => item.id === id)[0]?.listPrice
                : campaign?.listGoods?.filter(item => item.id === id)[0]?.listPrice )
          }}
          value={id}
        />
      ),
    },
    {
      title: <span className="uppercase">Product ID</span>,
      dataIndex: 'id',
      key: 'id',
      align: 'center',
      width: 100,
    },
    {
      title: <span className="uppercase">Product name</span>,
      dataIndex: 'goodsName',
      key: 'goodsName',
      width: 120,
      align: 'center',
      ellipsis: true,
      render: (goodsName: string) => (
        <span className="truncate whitespace-nowrap">{goodsName}</span>
      ),
    },
    {
      title: <span className="uppercase">Supplier ID</span>,
      dataIndex: 'supplierId',
      key: 'supplierId',
      width: 120,
      align: 'center',
      ellipsis: true,
      render: (supplierId: string, record: Good) => (
          <span className="truncate whitespace-nowrap">{record?.supplier?.id}</span>
      ),
    },
    {
      title: <span className="uppercase">Brand ID</span>,
      dataIndex: 'brandId',
      key: 'brandId',
      width: 120,
      align: 'center',
      ellipsis: true,
      render: (brandId: string, record: Good) => (
          <span className="truncate whitespace-nowrap">{record?.brand?.id}</span>
      ),
    },
    {
      title: <span className="uppercase">Exchange cost</span>,
      dataIndex: 'sellPrice',
      key: 'sellPrice',
      width: 120,
      align: 'center',
      ellipsis: true,
      render: (sellPrice: string) => (
        <span className="truncate whitespace-nowrap">{sellPrice}</span>
      ),
    },
    {
      title: <span className="uppercase">Settlement method</span>,
      dataIndex: 'settlementMethodCode',
      key: 'settlementMethodCode',
      width: 140,
      align: 'center',
      ellipsis: true,
      render: (settlementMethodCode: string) => (
        <span className="truncate whitespace-nowrap">
          {settlementMethodCode}
        </span>
      ),
    },
    {
      title: <span className="uppercase">Registration time</span>,
      dataIndex: 'startDate',
      key: 'startDate',
      width: 140,
      align: 'center',
      ellipsis: true,
      render: (startDate: string) => (
        <span className="truncate whitespace-nowrap">{startDate}</span>
      ),
    },
  ];

  const {
    register,
    handleSubmit,
    setValue,
    control,
    watch,
    setError,
    formState: { errors },
  } = useForm<FormValues>({
    // @ts-ignore
    defaultValues: initialValues
      ? {
          ...initialValues,
          campaignName: initialValues.campaign?.campaignName,
          campaignId: initialValues.campaign?.id,
          isDuplicateAllow:
            initialValues.receiverNoDuplicateAllowYn === CommonYesNoEnum.YES,
          goodsId: initialValues.goods?.id.toString(),
        }
      : {
          campaignName: campaign?.campaignName,
          campaignId: campaign?.id,
          messageSubject: campaign?.messageSubject,
          messageContent: campaign?.messageContent,
          smsType: DeliveryReceiveType.SMS,
          bookingYn: CommonYesNoEnum.YES,
          contentLink: campaign?.contentLink,
          contentImageName: campaign?.contentImageName,
          contentImagePath: campaign?.contentImagePath,
          senderName: campaign?.senderName,
          code_settlement: codeGroup?.codes.length
            ? codeGroup?.codes?.find(
                (code) =>
                  campaign?.customerContract?.sellSettlementMethodCode ===
                  code.codeId
              )
            : '',
          sellDiscountAmount: campaign?.customerContract?.sellDiscountAmount,
          sellCommissionRate: campaign?.customerContract?.sellCommissionRate,
          sellVatIncludeYn: campaign?.customerContract?.sellVatIncludeYn,
        },
    resolver: yupResolver(deliveryValidationSchema),
  });

  const targetType = watch('smsType');
  const targetEmail = (targetType === DeliveryReceiveType.EMAIL)
  const bookingYn = watch('bookingYn');

  useEffect(() => {
    // @ts-ignore
    setValue('bookingYn', CommonYesNoEnum.NO);
  }, [targetType === DeliveryReceiveType.DOWNLOAD || targetType === DeliveryReceiveType.PAPER]);

  const [errorMessageBookingDate, setErrorMessageBookingDate] =
    useState<string>('');

  const handleChangeBookingDate = (event: any) => {
    setErrorMessageBookingDate('');
  };

  /**
   * Check duplicate or check exist phone number
   */
  const [isAllowDuplicate, setAllowDuplicate] = useState<boolean>(
    initialValues?.receiverNoDuplicateAllowYn === CommonYesNoEnum.YES
  );
  // console.log('isAllowDuplicate', isAllowDuplicate);

  const handleChangeDuplicate = (event: any) => {
    const isChecked = event.target.checked;
    // console.log('isChecked', isChecked);

    if (isChecked) {
      setValue('isDuplicateAllow', true);
      setAllowDuplicate(true);
      setErrorMessageUserDuplicate('');
    } else {
      const listPhoneDuplicated = checkDuplicatePhoneNumbers(listUsers);
      const listEmailDuplicated = checkDuplicateEmails(listUsers);

      if (!targetEmail && listPhoneDuplicated.length > 0) {
        setValue('isDuplicateAllow', true);
        setAllowDuplicate(true);
        setErrorMessageUserDuplicate(
          `Phone numbers duplicated: ${listPhoneDuplicated.join(', ')}`
        );
        return
      }
      if (targetEmail && listEmailDuplicated.length > 0) {
        setValue('isDuplicateAllow', true);
        setAllowDuplicate(true);
        setErrorMessageUserDuplicate(
          `Emails duplicated: ${listEmailDuplicated.join(', ')}`
        );
        return
      }
      setValue('isDuplicateAllow', false);
      setAllowDuplicate(false);
      setErrorMessageUserDuplicate('');
    }
  };

  /**
   * Content Image
   */
  const contentImageName = watch('contentImageName');
  const contentImagePath = watch('contentImagePath');

  const { mutate: uploadImage, isLoading: uploadingImage } =
    useUploadImageMutation();

  const handleFileChangeContentImage = (e: ChangeEvent<HTMLInputElement>) => {
    setValue('contentImageName', '');
    setValue('contentImagePath', '');

    if (e.target.files) {
      const file = e.target.files[0];
      // console.log('file', file);
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

  const deleteFileContentImage = () => {
    setValue('contentImageName', '');
    setValue('contentImagePath', '');
  };

  /**
   * File
   */
  const uploadType = watch('uploadType');
  const allowedExtensions = ['csv', 'xls', 'xlsx'];
  const [errorFile, setErrorFile] = useState('');

  const [uploadFilePath, setUploadFilePath] = useState(
    initialValues?.uploadFilePath
  );
  const [uploadFileName, setUploadFileName] = useState(
    initialValues?.uploadFileName
  );

  const { mutate: uploadDocument } = useUploadDocumentMutation();

  const handleChangeTargetNumber = (event: any) => {
    // setListUsers([]);
    clearErrorMessageUser();
  };

  useEffect(() => {
    // setUploadFilePath(undefined);
    // setUploadFileName(undefined);
    setErrorFile('');
    clearErrorMessageUser()
    resetFileInput();
    setListUsers([]);
  }, [targetType]);

  const resetFileInput = () => {
  const fileInput = document.getElementById('file_input_csv') as HTMLInputElement;
    if (fileInput) {
      fileInput.value = '';
    }
  };

  const handleChangeFile = (e: ChangeEvent<HTMLInputElement>) => {
    setUploadFilePath('');
    setUploadFileName('');
    setErrorFile('');

    if (e.target.files && e.target.files.length > 0) {
      const inputFile = e.target.files[0];
      // console.log('inputFile', inputFile);

      if (inputFile) {
        // const fileExtension = inputFile?.type.split('/')[1];
        const fileExtension = inputFile?.name.split('.').pop()?.toLowerCase(); // Extract extension from file name
        if (!allowedExtensions.includes(fileExtension!)) {
          setErrorFile('Please input a csv or excel file');
          return;
        }

        // Process file upload
        if ('csv' === fileExtension) {
          handleProcessFileCsv(inputFile);
        } else if (['xls', 'xlsx'].includes(fileExtension!)) {
          handleProcessFileExcel(inputFile);
        }

        // Validate success will upload file to server
        uploadDocument(inputFile, {
          onSuccess: (data: any) => {
            setUploadFilePath(data?.path);
            setUploadFileName(inputFile?.name);
          },
        });
        // Reset input to be able to select the old file again
        e.target.value = '';
      }
    }
  };

  /**
   * Read file csv
   * @param file
   */
  const handleProcessFileCsv = (file: File) => {
    const reader = new FileReader();
    reader.onload = async (e: ProgressEvent<FileReader>) => {
      const target = e.target as FileReader;
      const csv = Papa.parse(target.result as string, { header: true });
      const objDataFile = parseDataUsers(csv.data);
      processListUsersFile(objDataFile);
    };
    reader.readAsText(file, 'ISO-8859-1');
  };

  /**
   * Read file Excel
   * @param file
   */
  const handleProcessFileExcel = (file: File) => {
    const reader = new FileReader();
    reader.onload = async (e) => {
      const data = new Uint8Array(e.target?.result as ArrayBuffer);
      const workbook = XLSX.read(data, { type: 'array' });
      // console.log('workbook', workbook);
      const sheetName = workbook.SheetNames[0];
      // console.log('sheetName', sheetName);
      const sheetData = XLSX.utils.sheet_to_json(workbook.Sheets[sheetName]);
      // console.log(sheetData);
      const objDataFile = parseDataUsers(sheetData);
      processListUsersFile(objDataFile);
    };

    // reader.readAsBinaryString(file);
    reader.readAsArrayBuffer(file);
  };

  /**
   * Parse data of file
   * @param data
   * @returns
   */
  const parseDataUsers = (data: any) => {
    // console.log('data', data);

    // Validate data in file
    if (!data || data.length < 1) {
      // setErrorFile('Invalid data of file');
      return { errorCode: 1, errorMessage: 'Invalid data of file' };
    }

    // Validate number of fields in file
    // const firstObject = data[0] as DeliveryUserInput;
    // const keysOfFirstObject = Object.keys(firstObject);
    // const lengthOfFirstObject = keysOfFirstObject.length;
    // console.log('lengthOfFirstObject', lengthOfFirstObject);
    // if (lengthOfFirstObject < 6) {
    //   // setErrorFile('Invalid data of file, miss field');
    //   // return;
    //   return {
    //     errorCode: 1,
    //     errorMessage: 'Invalid data of file, missing some field',
    //   };
    // }

    // Parse parsedData into an array of type DeliveryUser
    const deliveryUsers: DeliveryUser[] = [];
    data.forEach((data: any, index: number) => {
      const { phone, name, gender, birthday, address, email } = data;
      const userMobileNum = extractNumbers(phone?.toString());
      const userName = reformatUsername(name);

      if(targetEmail && !email) {
        toast.error('Have empty email in file') ;
        return
      }
      if (targetEmail && !emailRegex.test(email.trim()) ) {
        toast.error('Check invalid email : ' + email) ;
        return
      }
      if (!targetEmail && (!phone || !userMobileNum || !userName) ) {
        return;
      }

      // Create a new object with properties of DeliveryUser and values from the parsed data
      const deliveryUser: DeliveryUser = {
        id: index + 1 + '-' + new Date().getTime().toString(),
        userMobileNum: userMobileNum,
        userNm: userName,
        gender,
        birthday:
          birthday && isValidDateFormat(birthday.trim()) ? birthday.trim() : '',
        address,
        email: email?.trim(),
      };

      deliveryUsers.push(deliveryUser);
      return false;
    });

    if (deliveryUsers.length === 0) {
      return { errorCode: 1, errorMessage: 'No valid data' };
    }
    return { errorCode: 0, data: deliveryUsers };
  };

  /**
   * Process data after read from file
   * @param objDataFile
   * @returns
   */
  const processListUsersFile = (objDataFile: any) => {
    // If error, show message
    if (objDataFile.errorCode !== 0) {
      setErrorFile(objDataFile.errorMessage!);
      return;
    }

    // Check phone numbers exists
    if (!isAllowDuplicate) {
      const listUsersTemp = [...objDataFile.data!, ...listUsers];
      const listPhoneDuplicated = checkDuplicatePhoneNumbers(listUsersTemp);
      const listEmailDuplicated = checkDuplicateEmails(listUsersTemp);

      if (!targetEmail && listPhoneDuplicated.length > 0) {
        setErrorMessageUserDuplicate(
          `Phone numbers duplicated: ${listPhoneDuplicated.join(', ')}`
        );
        // No add data to list
        return;
      }
      if (targetEmail && listEmailDuplicated.length > 0) {
        setErrorMessageUserDuplicate(
          `Emails duplicated: ${listEmailDuplicated.join(', ')}`
        );
        // No add data to list
        return;
      }
    }

    // If ok, add to list
    setListUsers([...objDataFile.data!, ...listUsers]);
  };

  /**
   * Users
   */
  const userPhoneRef = useRef<HTMLInputElement | null>(null);
  const userNameRef = useRef<HTMLInputElement | null>(null);
  const userGenderRef = useRef<HTMLSelectElement | null>(null);
  const userBirthdayRef = useRef<HTMLInputElement | null>(null);
  const userAddressRef = useRef<HTMLInputElement | null>(null);
  const userEmailRef = useRef<HTMLInputElement | null>(null);

  const [listUsers, setListUsers] = useState<DeliveryUser[]>([]);
  const [userSlices, setUserSlices] = useState<DeliveryUser[]>([]); // Display users (10) in once page
  const IS_PAGINATION = true;
  const [page, setPage] = useState(1);
  const [paginatorInfo, setPaginatorInfo] = useState<MappedPaginatorInfoEV>();

  function handlePagination(current: number) {
    // console.log('current', current);
    setPage(current);
  }

  function updatePaginator(users: DeliveryUser[]) {
    // console.log('updatePaginator', users.length);

    /**
     * Slice list data by page current
     * */
    let pathOps = { page: page, pageSize: PAGE_SIZE };
    const sliceData = users.slice(
      (pathOps.page - 1) * pathOps.pageSize,
      pathOps.page * pathOps.pageSize
    );
    // console.log('updatePaginator sliceData', sliceData);
    setUserSlices(sliceData);

    /**
     * Set paginator info
     **/
    let obj: PaginatorInfoEV<any> = {} as PaginatorInfoEV<DeliveryUser>;
    obj.data = sliceData;
    obj.totalCount = users.length;
    let pageInfo = mapPaginatorData(pathOps, obj);
    // console.log('updatePaginator pageInfo', pageInfo);
    setPaginatorInfo(pageInfo!);
  }

  const [errorMessageUsers, setErrorMessageUsers] = useState<string>('');
  const [errorMessageUserPhone, setErrorMessageUserPhone] = useState<string>('');
  const [errorMessageEmail, setErrorMessageEmail] = useState<string>('');
  const [errorMessageUserDuplicate, setErrorMessageUserDuplicate] =
    useState<string>('');
  const [errorMessageUserName, setErrorMessageUserName] = useState<string>('');
  const [errorMessageUserBirthday, setErrorMessageUserBirthday] =
    useState<string>('');

  const clearErrorMessageUser = () => {
    setErrorMessageUsers('');
    setErrorMessageUserName('');
    setErrorMessageUserPhone('');
    setErrorMessageUserDuplicate('');
    setErrorMessageUserBirthday('');
    setErrorMessageEmail('');
  };

  const reformatUsername = (inputText: string) => {
    if (!inputText) return '';
    return reformatTextSms(inputText, SMS.USERNAME_MAX_LENGTH).trim();
  };

  const emailRegex = /^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,6}$/;
  const addUser = () => {
    clearErrorMessageUser();

    const userPhone = extractNumbers(userPhoneRef.current?.value.trim()!);
    const userName = reformatUsername(userNameRef.current?.value.trim()!);
    const userGender = userGenderRef.current?.value.trim();
    const userBirthday = userBirthdayRef.current?.value.trim();
    const userAddress = userAddressRef.current?.value.trim();
    const userEmail = userEmailRef.current?.value.trim() ?? "";

    if (!targetEmail) {
      if(!userPhone) {
        setErrorMessageUserPhone('Invalid phone number!');
        return;
      }
      if(userEmail.length > 0 && !emailRegex.test(userEmail ?? "") ) {
        setErrorMessageEmail('Invalid email');
        return;
      }
    }

    if (targetEmail && !emailRegex.test(userEmail ?? "")) {
      setErrorMessageEmail('Invalid email');
      return;
    }

    // console.log('userPhone', userPhone);
    // console.log('listUsers', listUsers);

    if (userPhone.length > 20) {
      setErrorMessageUserPhone('Phone number must be at most 20 characters');
      return;
    }

    // Check if the object with the given phone number exists in the array
    const existPhone = listUsers.some(
      (user) => user.userMobileNum === userPhone
    );
    const existEmail = listUsers.some(
      (user) => user.email === userEmail
    );
    console.log('isAllowDuplicate', isAllowDuplicate);
    if (!isAllowDuplicate && existPhone && !targetEmail) {
      setErrorMessageUserPhone('Exist phone number!');
      return;
    }
    if (!isAllowDuplicate && existEmail && targetEmail) {
      setErrorMessageEmail('Exist email!');
      return;
    }

    if (!userName && !targetEmail) {
      setErrorMessageUserName('Invalid username!');
      return;
    }

    if (userBirthday && !isValidDateFormat(userBirthday)) {
      setErrorMessageUserBirthday('Invalid date format');
      return;
    }

    let user: DeliveryUser = {
      id: new Date().getTime().toString(),
      userMobileNum: userPhone,
      userNm: userName,
      gender: userGender,
      birthday: userBirthday,
      address: userAddress,
      email: userEmail,
    };

    setListUsers((prevUsers) => [user, ...prevUsers]);
  };

  const removeUser = (option: DeliveryUser) => {
    clearErrorMessageUser();

    if (option?.id) {
      setListUsers((prevUsers) =>
        prevUsers.filter((user) => user.id !== option.id)
      );
    }
  };

  const removeUsers = () => {
    setListUsers([]);
  };

  // Function to check for duplicate phone numbers
  const checkDuplicatePhoneNumbers = (data: Array<DeliveryUser>) => {
    const phoneNumbersSet = new Set<string>();
    const duplicatePhoneNumbers: string[] = [];

    data.forEach((user: DeliveryUser) => {
      if (phoneNumbersSet.has(user.userMobileNum)) {
        duplicatePhoneNumbers.push(user.userMobileNum);
      } else {
        phoneNumbersSet.add(user.userMobileNum);
      }
    });

    return duplicatePhoneNumbers;
  };

  const checkDuplicateEmails = (data: Array<DeliveryUser>) => {
    const emailsSet = new Set<string>();
    const duplicateEmails: string[] = [];

    data.forEach((user: DeliveryUser) => {
      if (emailsSet.has(user.email)) {
        duplicateEmails.push(user.email);
      } else {
        emailsSet.add(user.email);
      }
    });

    return duplicateEmails;
  };

  /**
   * Product
   */
  const [errorMessageGoodsId, setErrorMessageGoodsId] = useState<string>();

  /**
   * Event onClick button
   */
  const [actionType, setActionType] = useState<CommonApproveStatusAction | ''>(
    ''
  );

  const handleClick = (action: any) => {
    setActionType(action);
  };

  const { mutate: createDelivery, isLoading: creating } =
    useCreateDeliveryMutation();
  const { mutate: updateDelivery, isLoading: updating } =
    useUpdateDeliveryMutation();

  const onSubmit = async (values: FormValues) => {
    // console.log('onSubmit', values);
    // console.log('listUsers', listUsers);
    // return;

    const smsTypeInput = values.smsType;

    // Validate boodingDate by bookingyn
    let bookingDateInput = '';
    if (smsTypeInput !== DeliveryReceiveType.DOWNLOAD && smsTypeInput !== DeliveryReceiveType.PAPER) {
      if (values.bookingYn === CommonYesNoEnum.YES) {
        if (!values.bookingDate) {
          setErrorMessageBookingDate('Booking date is empty');
          return;
        }

        bookingDateInput = values.bookingDate
          ? values.bookingDate.replace('T', ' ') + ':00'
          : '';
      }

      // Validate users
      if (!listUsers || listUsers.length < 1) {
        setErrorMessageUsers('Please add users');
        return;
      }
    }

    // Validate goodsId
    if (!values.goodsId) {
      setErrorMessageGoodsId('Please select one product');
      return;
    }

    const inputValues = {
      campaignId: values.campaignId,
      publishName: values.publishName,
      smsType: smsTypeInput,
      ...(smsTypeInput === DeliveryReceiveType.DOWNLOAD || smsTypeInput === DeliveryReceiveType.PAPER
        ? {numberOfVouchers: values.numberOfVouchers,}
        : ({} as object)),

      bookingYn: values.bookingYn,
      // Param bookingDate is depend on bookingYn
      ...(values.bookingYn === 'Y'
        ? { bookingDate: bookingDateInput }
        : ({} as object)),

      messageSubject: values.messageSubject,
      messageContent: values.messageContent,
      receiverNoDuplicateAllowYn: values.isDuplicateAllow
        ? CommonYesNoEnum.YES
        : CommonYesNoEnum.NO,
      uploadType: values.uploadType,
      uploadFileName: uploadFileName,
      uploadFilePath: uploadFilePath,
      endUsers: listUsers,
      goodsId: values.goodsId,
      // @ts-ignore
      sellPrice: parseInt(values?.sellPrice?.replace(/,/g, '')),
      // @ts-ignore
      sellListPrice: parseInt(values?.sellListPrice?.replace(/,/g, '')),
      sellSettlementMethodCode: values.code_settlement?.codeId,
      // @ts-ignore
      sellDiscountAmount: parseInt(values?.sellDiscountAmount?.replace(/,/g, '')),
      sellCommissionRate: values.sellCommissionRate,
      sellVatIncludeYn: values.sellVatIncludeYn,
      // approveStatusCode: CommonApproveStatusAction.REQ,
      contentLink: values.contentLink,
      contentImageName: values.contentImageName,
      contentImagePath: values.contentImagePath,
      senderName: values.senderName,
    };

    try {
      // console.log('inputValues', inputValues, actionType);
      // return;

      if (!initialValues) {
        createDelivery({
          ...inputValues,
          approveStatusCode: CommonApproveStatusAction.REQ,
        });
      } else {
        if (actionType === '') {
          updateDelivery({
            ...inputValues,
            id: initialValues.id,
          });
        } else {
          updateDelivery({
            ...inputValues,
            id: initialValues.id,
            approveStatusCode: actionType,
          });
        }
      }

      // Clear actionType
      setActionType('');

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

  useEffect(() => {
    if(initialValues?.bookingYn === "Y") {
      setValue('bookingYn', initialValues?.bookingYn)
    }
  }, [initialValues, setValue]);

  useEffect(() => {
    if (initialValues?.endUsers) {
      let endUsersIds = initialValues.endUsers.map((user, index) => ({
        ...user,
        id: index + 1 + '-' + new Date().getTime().toString(),
      }));
      setListUsers(endUsersIds);
    }
  }, []);

  // This effect will run whenever 'listUsers' is updated
  useEffect(() => {
    setValue('numberOfTarget', listUsers.length);
    if (IS_PAGINATION) {
      updatePaginator(listUsers);
    }
  }, [listUsers, page, setValue]);

  // This effect will run whenever get 'codeGroup' success
  useEffect(() => {
    // console.log('codeGroup', codeGroup);
    if (codeGroup?.codes) {
      let codeSettelement = initialValues?.sellSettlementMethodCode
        ? codeGroup.codes.find(
            (code) => code.codeId === initialValues.sellSettlementMethodCode
          )
        : codeGroup.codes.find(
            (code) =>
              code.codeId ===
              campaign?.customerContract?.sellSettlementMethodCode
          );
      if (codeSettelement) {
        setValue('code_settlement', codeSettelement);
      }
    }
  }, [codeGroup, setValue]);

  function getElement() {
    return <>
      <div className="mb-5 flex flex-wrap">
        <div className="flex w-full flex-wrap px-2 md:w-1/2">
          <label className="w-full  text-heading md:w-1/4">
            {t('Allow Duplication')}
          </label>
          <div className="flex w-full md:w-3/4">
            <Checkbox
              {...register('isDuplicateAllow')}
              label={t('Allowed')}
              className="flex items-center font-semibold"
              onChange={handleChangeDuplicate}
            />
          </div>
        </div>
      </div>
      <div className="mb-5 flex flex-wrap">
        <div className="flex w-full flex-wrap px-2 md:w-1/2">
          <label className="w-full py-2 text-heading md:w-1/4">
            {t('Target details')} <span className="text-red-500">*</span>
          </label>
          <div className="flex w-full md:w-3/4">
            <Radio
              className="flex w-full md:w-1/2"
              label={t('File')}
              {...register('uploadType')}
              id="uploadType_file"
              value={DeliveryUploadType.FILE}
              onClick={handleChangeTargetNumber}
            />
            <Radio
              className="flex w-full md:w-1/2"
              label={t('Text')}
              {...register('uploadType')}
              id="uploadType_text"
              value={DeliveryUploadType.TEXT}
              onClick={handleChangeTargetNumber}
            />
          </div>
          <span className="w-full text-xs text-red-500 text-start md:w-3/4 md:pl-[25%]">
            {t(errors.uploadType?.message!)}
          </span>
        </div>
        {uploadType == DeliveryUploadType.FILE && (
          <div className="flex w-full flex-wrap px-2 md:w-1/2">
            <label className="w-full py-2 text-heading md:w-1/4">
              {t('File Upload')} <span className="text-red-500">*</span>
              &nbsp; (
              <a
                href="/files/Delivery Users File Template.csv"
                className="e_link"
              >
                Csv
              </a>
              &nbsp;|&nbsp;
              <a
                href="/files/Delivery Users File Template.xlsx"
                className="e_link"
              >
                Excel
              </a>
              )
            </label>
            <div className="flex w-full md:w-3/4">
              <input
                id="file_input_csv"
                type="file"
                accept=".csv, .xlsx, .xls"
                className={'e_hide-text'}
                onChange={handleChangeFile}
              />
              {uploadFileName ? (
                <span>Selected file: {uploadFileName}</span>
              ) : (
                ''
              )}
              {errorFile && (
                <span className="w-full text-xs text-red-500 text-start md:w-3/4 md:pl-[25%]">
                  {errorFile}
                </span>
              )}
            </div>
          </div>
        )}
      </div>


      <div className="mb-5 mt-6 flex flex-wrap">
        <div className="flex w-full flex-wrap px-2 py-2 md:w-3/4">
          <h1 className="text-lg font-semibold uppercase text-cyan-600 text-heading">
            {t('Target details')}
          </h1>
        </div>
        <div className="flex-reverse flex w-full flex-wrap px-2 md:w-1/4">
          <label className="w-full py-2 text-heading md:w-1/2">
            {t('Number of Target')}
          </label>
          <input
            className={`${smallClassName} ${'cursor-not-allowed bg-gray-100'}`}
            type="text"
            id="numberOfTarget"
            {...register('numberOfTarget')}
            disabled={true}
            autoComplete="off"
          />
        </div>
      </div>

      {uploadType == DeliveryUploadType.TEXT && (
        <>
          <div className="mb-5 flex flex-wrap md:w-[90%]">
            <div className="flex w-full flex-wrap px-2 md:w-1/4">
              <label className="w-full py-2 text-heading md:w-1/2">
                {t('Phone number')} {targetEmail ? "" : <span className="text-red-500">*</span>}
              </label>
              <input
                className={smallClassName}
                type="text"
                id="user_userMobileNum"
                autoComplete="off"
                ref={userPhoneRef}
              />
              <span className="w-full text-xs text-red-500 text-start md:pl-[50%]">
                {errorMessageUserPhone}
              </span>
            </div>
            <div className="flex w-full flex-wrap px-2 md:w-1/4">
              <label className="w-full px-4 py-2 text-heading md:w-1/2">
                {t('Username')} {targetEmail ? "" : < span className="text-red-500">*</span>}
              </label>
              <input
                className={smallClassName}
                type="text"
                id="user_userNm"
                autoComplete="off"
                ref={userNameRef}
              />
              <span className="w-full text-xs text-red-500 text-start md:pl-[50%]">
                {errorMessageUserName}
              </span>
            </div>
            <div className="flex w-full flex-wrap px-2 md:w-1/4">
              <label className="w-full py-2 text-heading md:w-1/2">
                {t('Gender')}
              </label>
              <select
                id="user_gender"
                ref={userGenderRef}
                className={`${smallClassName} ps-8`}
              >
                {/* <option value=""></option> */}
                <option value="MEN">MEN</option>
                <option value="WOMEN">WOMEN</option>
                <option value="KIDS">KIDS</option>
                <option value="MISCELL">MISCELL</option>
              </select>
            </div>
            <div className="flex w-full flex-wrap px-2 md:w-1/4">
              <label className="w-full px-4 py-2 text-heading md:w-1/2">
                {t('Birthday')}
              </label>
              <input
                className={smallClassName}
                type="text"
                id="user_birthday"
                autoComplete="off"
                placeholder="yyyy-MM-dd"
                ref={userBirthdayRef}
              />
              <span className="w-full text-xs text-red-500 text-start md:pl-[50%]">
                {errorMessageUserBirthday}
              </span>
            </div>
          </div>

          <div className="mb-5 flex flex-wrap">
            <div className="flex w-full flex-wrap px-2 md:w-[45%]">
              <label className="w-full py-2 text-heading md:w-1/4">
                {t('Address')}
              </label>
              <input
                className={rootClassName}
                type="text"
                id="user_address"
                autoComplete="off"
                ref={userAddressRef}
              />
            </div>
            <div className="flex w-full flex-wrap px-2 md:w-[45%]">
              <label className="w-full py-2 text-heading md:w-1/4">
                {t(targetEmail ? "Email " : 'Manager email ')}{targetEmail ? <span className="text-red-500">*</span> : "" }
              </label>
              <input
                className={rootClassName}
                type="text"
                id="user_email"
                autoComplete="off"
                ref={userEmailRef}
              />
              <span className="w-full text-xs text-red-500 text-start md:pl-[50%]">
                {errorMessageEmail}
              </span>
            </div>
            <div className="flex w-full flex-wrap md:w-[10%]  ">
              <Button
                size="small"
                onClick={addUser}
                className="mt-1 bg-red-600 hover:bg-red-700 md:w-auto  px-7 py-5 rounded-md"
                type="button"
              >
                {t('Add')}
              </Button>
            </div>
          </div>
        </>
      )}

      <div className="flex flex-wrap">
                <span className="w-full text-xs text-red-500 text-start">
                  {errorMessageUsers}
                  &nbsp;
                  {errorMessageUserDuplicate}
                </span>
      </div>

      <div className="mb-6">
        <DeliveryUsersList
          data={IS_PAGINATION ? userSlices : listUsers}
          removeAll={removeUsers}
          removeOne={removeUser}
          isSmsStatus={!!initialValues}
          isPagination={IS_PAGINATION}
          paginatorInfo={paginatorInfo ?? null}
          onPagination={handlePagination}
        />
      </div>
    </>;
  }

  return (
    <form onSubmit={handleSubmit(onSubmit)}>
      <div className="my-5 flex flex-wrap sm:my-6">
        <Card className="w-full !p-4 sm:w-full md:w-full">
          <div className="mb-5 flex flex-wrap">
            <div className="flex w-full flex-wrap px-2 py-2">
              <h1 className="text-lg font-semibold uppercase text-cyan-600 text-heading">
                {t('Delivery Information')}
              </h1>
            </div>
          </div>

          {initialValues && (
            <div className="flex flex-wrap">
              <div className="mb-5 px-2 sm:w-full md:w-1/2">
                {t('Status')}:{' '}
                {
                  <DeliveryStatusCodeBadge
                    statusCode={initialValues?.statusCode}
                  />
                }
              </div>
            </div>
          )}

          <div className="mb-5 flex flex-wrap">
            <div className="flex w-full flex-wrap px-2 md:w-1/2">
              <label className="w-full py-2 text-heading md:w-1/4">
                {t('Campaign name')} <span className="text-red-500">*</span>
              </label>
              <input
                className={`${rootClassName} ${'cursor-not-allowed bg-gray-100'}`}
                disabled={true}
                type="text"
                id="campaignName"
                {...register('campaignName')}
                autoComplete="off"
              />
              <span className="w-full text-xs text-red-500 text-start md:w-3/4 md:pl-[25%]">
                {t(errors.campaignName?.message!)}
              </span>
            </div>
            <div className="flex w-full flex-wrap px-2 md:w-1/2">
              <label className="w-full py-2 text-heading md:w-1/4">
                {t('Campaign ID')} <span className="text-red-500">*</span>
              </label>
              <input
                className={`${rootClassName} ${'cursor-not-allowed bg-gray-100'}`}
                disabled={true}
                type="text"
                id="campaignId"
                {...register('campaignId')}
                autoComplete="off"
              />
              <span className="w-full text-xs text-red-500 text-start md:w-3/4 md:pl-[25%]">
                {t(errors.campaignId?.message!)}
              </span>
            </div>
          </div>

          <div className="mb-5 flex flex-wrap">
            <div className="flex w-full flex-wrap px-2 md:w-1/2">
              <label className="w-full py-2 text-heading md:w-1/4">
                {t('Delivery name')} <span className="text-red-500">*</span>
              </label>
              <input
                className={rootClassName}
                type="text"
                id="publishName"
                {...register('publishName')}
                placeholder={t('Delivery name')}
                autoComplete="off"
              />
              <span className="w-full text-xs text-red-500 text-start md:w-3/4 md:pl-[25%]">
                {t(errors.publishName?.message!)}
              </span>
            </div>
            {initialValues && (
              <div className="flex w-full flex-wrap px-2 md:w-1/2">
                <label className="w-full py-2 text-heading md:w-1/4">
                  {t('Delivery ID')} <span className="text-red-500">*</span>
                </label>
                <input
                  className={`${rootClassName} ${'bg-gray-100'}`}
                  type="text"
                  disabled={true}
                  id="id"
                  {...register('id')}
                  autoComplete="off"
                />
              </div>
            )}
          </div>

          <div className="mb-5 flex flex-wrap">
            <div className="flex w-full flex-wrap px-2 py-2">
              <h1 className="text-lg font-semibold uppercase">
                {t('gift message')}
              </h1>
            </div>
          </div>
          <div className="flex w-full flex-wrap px-4">
            <label className="w-full  text-heading md:w-[12.5%]">
              {t('Enable popup')}
            </label>
            <div className="flex items-center space-x-2 md:w-9/12 my-2">
              <SwitchDisplay checked={campaign?.showPopupYn === 'Y'} disabled={true}/>
              <span className="text-sm text-gray-500 italic">
                {t('Enable to show pop up message when user views the voucher for the first time')}
              </span>
            </div>
          </div>
          <div className="mb-5 flex flex-wrap">
            <div className="flex w-full flex-wrap px-4">
              <label className="w-full  text-heading md:w-[12.5%]">
                {t('Sender Name')} <span className="text-red-500">*</span>
              </label>
              <input
                className={rootClassName}
                type="text"
                id="senderName"
                {...register('senderName')}
                placeholder={t('Sender Name')}
                autoComplete="off"
              />
              <span className="w-full text-xs text-red-500 text-start md:w-full">
                {t(errors.senderName?.message!)}
              </span>
            </div>
            <div className="flex w-full flex-wrap px-4 py-1 ">
              <label className="w-full  text-heading md:w-[12.5%]">
                {t('Voucher subject')} <span className="text-red-500">*</span>
              </label>
              <input
                className={rootClassName}
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
            <div className="flex w-full flex-wrap px-4 py-1 ">
              <label className="w-full  text-heading md:w-[12.5%]">
                {t('Voucher content')} <span className="text-red-500">*</span>
              </label>
              <TextArea
                placeholder={t('Voucher content')}
                {...register('messageContent')}
                variant="outline"
                className="w-full md:w-3/4"
              />
              <span className="w-full text-xs text-red-500 text-start md:w-full">
                {t(errors.messageContent?.message!)}
              </span>
            </div>
            <div className="flex w-full flex-wrap px-4   ">
              <label className="w-full  text-heading md:w-[12.5%]">
                {t('Content Link')}
              </label>
              <input
                className={rootClassName}
                type="text"
                id="contentLink"
                {...register('contentLink')}
                placeholder={t('Content link')}
                autoComplete="off"
              />
              <span className="w-full text-xs text-red-500 text-start md:w-full">
                {t(errors.contentLink?.message!)}
              </span>
            </div>
            <div className="flex w-full flex-wrap px-4 py-1">
              <label className="w-full  text-heading md:w-[12.5%]">
                {t('Content Image')}
              </label>
              <div className="w-full pt-1 md:w-3/4">
                <input
                  id="file_input"
                  type="file"
                  className={'e_hide-text'}
                  onChange={handleFileChangeContentImage}
                  style={{width: '100px'}}
                />
                {contentImagePath && (
                  <Button
                    size="small"
                    onClick={deleteFileContentImage}
                    className="mt-2 w-full bg-red-700 hover:bg-slate-700 md:w-auto md:ms-6"
                    type="button"
                  >
                    {t('Delete')}
                  </Button>
                )}
                {uploadingImage && (
                  <Loader uploadFile={true} text={t('common:text-loading')}/>
                )}
                {contentImageName && <p>Selected file: {contentImageName}</p>}
              </div>
              {contentImagePath && (
                <div className="w-full md:w-3/4 md:pl-[25%]">
                  <img
                    src={getUrlPublicAsset(contentImagePath) ?? ''}
                    alt={'Content img'}
                    width={300}
                    height={300}
                  />
                </div>
              )}
              <span className="w-full text-xs text-red-500 text-start md:w-3/4 md:pl-[25%]">
                {t(errors.contentImageName?.message!)}
              </span>
            </div>
          </div>

          <div className="mb-5 mt-6 flex flex-wrap">
            <div className="flex w-full flex-wrap px-2 py-2 md:w-3/4">
              <h1 className="text-lg font-semibold uppercase text-cyan-600 text-heading">
                {t('Target information')}
              </h1>
            </div>
          </div>

          <div className="mb-5 flex flex-wrap">
            <div className="flex w-full flex-wrap px-2 ">
              <label className="w-full py-2 text-heading  md:w-[12.5%]">
                {t('Target type')} <span className="text-red-500">*</span>
              </label>
              <div className="flex flex-wrap w-full md:w-3/4 gap-2">
                <Radio
                  className="flex w-full md:w-1/6"
                  label={t('Zalo')}
                  {...register('smsType')}
                  id="smsType_zalo"
                  value={DeliveryReceiveType.ZALO}
                />
                <Radio
                  className="flex w-full md:w-1/6"
                  label={t('SMS')}
                  {...register('smsType')}
                  id="smsType_sms"
                  value={DeliveryReceiveType.SMS}
                />
                <Radio
                  className="flex w-full md:w-1/6"
                  label={t('Download')}
                  {...register('smsType')}
                  id="smsType_download"
                  value={DeliveryReceiveType.DOWNLOAD}
                />
                <Radio
                  className="flex w-full md:w-1/6"
                  label={t('Paper')}
                  {...register('smsType')}
                  id="smsType_paper"
                  value={DeliveryReceiveType.PAPER}
                />
                <Radio
                  className="flex w-full md:w-1/6"
                  label={t('Email')}
                  {...register('smsType')}
                  id="smsType_email"
                  value={DeliveryReceiveType.EMAIL}
                />
              </div>
              <span className="w-full text-xs text-red-500 text-start md:w-3/4 md:pl-[25%]">
                {t(errors.smsType?.message!)}
              </span>
            </div>
            <div className="flex w-full flex-wrap px-2 md:w-1/2">
              <label className="w-full py-2 text-heading md:w-1/4">
                {t('Delivery Type')} <span className="text-red-500">*</span>
              </label>
              <div className="flex w-full md:w-3/4">
                {targetType !== DeliveryReceiveType.DOWNLOAD
                  && targetType !== DeliveryReceiveType.PAPER &&
                  <Radio
                    className="flex w-full md:w-1/2"
                    label={t('Reservation')}
                    {...register('bookingYn')}
                    id="bookingYn_reservation"
                    value={CommonYesNoEnum.YES}
                  />
                }
                <Radio
                  className="flex w-full md:w-1/2"
                  label={t('Immediately')}
                  {...register('bookingYn')}
                  id="bookingYn_immediately"
                  value={CommonYesNoEnum.NO}
                />
              </div>
              <span className="w-full text-xs text-red-500 text-start md:w-3/4 md:pl-[25%]">
                {t(errors.bookingYn?.message!)}
              </span>
            </div>
            {(targetType === DeliveryReceiveType.DOWNLOAD || targetType === DeliveryReceiveType.PAPER) && (
              <div className="flex w-full flex-wrap px-2 ">
                <label className="w-full py-2 text-heading  md:w-[12.5%]">
                  {t('Number of vouchers')} <span className="text-red-500">*</span>
                </label>
                <input
                  className={rootClassName}
                  type="number"
                  id="numberOfVouchers"
                  {...register('numberOfVouchers')}
                  placeholder={t('Number Of Vouchers')}
                  autoComplete="off"
                />
                <span className="w-full text-xs text-red-500 text-start md:w-3/4 md:pl-[25%]">
                 {t(errors.numberOfVouchers?.message!)}
                </span>
              </div>
            )}
          </div>
          <div className="mb-5 flex flex-wrap">
            {bookingYn === 'Y' && (
              <div className="flex w-full flex-wrap px-2 md:w-1/2">
                <label className="w-full py-2 text-heading md:w-1/4">
                  {t('Delivery Schedule')}{' '}
                  <span className="text-red-500">*</span>
                </label>
                <input
                  className={rootClassName}
                  type="datetime-local"
                  id="bookingDate"
                  {...register('bookingDate')}
                  // placeholder='YYYY-MM-DD HH:mm:ss'
                  // data-date-format="YYYY-MM-DD HH:mm:ss"
                  onChange={handleChangeBookingDate}
                />
                <span className="w-full text-xs text-red-500 text-start md:w-3/4 md:pl-[25%]">
                  {errorMessageBookingDate}
                </span>
              </div>
            )}
          </div>
          {targetType !== DeliveryReceiveType.DOWNLOAD
            && targetType !== DeliveryReceiveType.PAPER && getElement()}

          <div className="mb-5 flex flex-wrap">
            <div className="flex w-full flex-wrap px-2 py-2">
              <h1 className="text-lg font-semibold uppercase text-cyan-600 text-heading">
                {t('Contract Information')}
              </h1>
            </div>
          </div>

          <div className="mb-5 flex flex-wrap">
            <div className="flex w-full flex-wrap px-2 md:w-1/2">
              <label className="w-full py-2 text-heading md:w-1/4">
                {t('Sales Price')} <span className="text-red-500">*</span>
              </label>
              <input
                className={rootClassName}
                type="text"
                id="sellPrice"
                {...register('sellPrice')}
                onChange={(e) => {
                  handlePriceInputChange(e, "sellPrice", setValue);
                }}
                placeholder={t('Sales Price')}
                autoComplete="off"
              />
              <span className="w-full text-xs text-red-500 text-start md:w-3/4 md:pl-[25%]">
                {t(errors.sellPrice?.message!)}
              </span>
            </div>
            <div className="flex w-full flex-wrap px-2 md:w-1/2">
              <label className="w-full py-2 text-heading md:w-1/4">
                {t('List Price')} <span className="text-red-500">*</span>
              </label>
              <input
                className={rootClassName}
                type="text"
                id="sellListPrice"
                {...register('sellListPrice')}
                placeholder={t('List Price')}
                autoComplete="off"
                onChange={(e) => {
                  handlePriceInputChange(e, "sellListPrice", setValue);
                }}
              />
              <span className="w-full text-xs text-red-500 text-start md:w-3/4 md:pl-[25%]">
                {t(errors.sellListPrice?.message!)}
              </span>
            </div>
          </div>

          <div className="mb-5 flex flex-wrap">
            <div className="flex w-full flex-wrap px-2 md:w-1/2">
              <label className="w-full py-2 text-heading md:w-1/4">
                {t('Settlement Method')} <span className="text-red-500">*</span>
              </label>
              <div className="w-full md:w-3/4 ">
                <SelectInput
                  name="code_settlement"
                  control={control}
                  getOptionLabel={(option: any) =>
                    option?.codeId + ' - ' + option?.codeName
                  }
                  getOptionValue={(option: any) => option?.codeId}
                  options={codeGroup?.codes ?? []}
                  isLoading={codeLoading}
                />
                <ValidationError message={t(errors.code_settlement?.message)}/>
              </div>
            </div>
            <div className="flex w-full flex-wrap px-2 md:w-1/2">
              <label className="w-full py-2 text-heading md:w-1/4">
                {t('Discount Amount')} <span className="text-red-500">*</span>
              </label>
              <input
                className={rootClassName}
                type="text"
                id="sellDiscountAmount"
                {...register('sellDiscountAmount')}
                onChange={(e) => {
                  handlePriceInputChange(e, "sellDiscountAmount", setValue);
                }}
                placeholder={t('Discount Amount')}
                autoComplete="off"
              />
              <span className="w-full text-xs text-red-500 text-start md:w-3/4 md:pl-[25%]">
                {t(errors.sellDiscountAmount?.message!)}
              </span>
            </div>
          </div>

          <div className="mb-5 flex flex-wrap">
            <div className="flex w-full flex-wrap px-2 md:w-1/2">
              <label className="w-full py-2 text-heading md:w-1/4">
                {t('Commission Rate')} <span className="text-red-500">*</span>
              </label>
              <input
                className={rootClassName}
                type="number"
                step="0.01"
                id="sellCommissionRate"
                {...register('sellCommissionRate')}
                placeholder={t('Commission Rate')}
                autoComplete="off"
              />
              <span className="w-full text-xs text-red-500 text-start md:w-3/4 md:pl-[25%]">
                {t(errors.sellCommissionRate?.message!)}
              </span>
            </div>
            <div className="flex w-full flex-wrap px-2 md:w-1/2">
              <label className="w-full py-2 text-heading md:w-1/4">
                {t('Including VAT')} <span className="text-red-500">*</span>
              </label>
              <div className="flex w-full md:w-3/4">
                <Radio
                  className="flex w-full md:w-1/2"
                  label={t('Yes')}
                  {...register('sellVatIncludeYn')}
                  id="sellVatIncludeYn_yes"
                  value={CommonYesNoEnum.YES}
                />
                <Radio
                  className="flex w-full md:w-1/2"
                  label={t('No')}
                  {...register('sellVatIncludeYn')}
                  id="sellVatIncludeYn_no"
                  value={CommonYesNoEnum.NO}
                />
              </div>
              <span className="w-full text-xs text-red-500 text-start md:w-3/4 md:pl-[25%]">
                {t(errors.sellVatIncludeYn?.message!)}
              </span>
            </div>
          </div>

          <div className="mb-5 mt-5 flex flex-wrap">
            <div className="flex w-full flex-wrap px-2 py-2">
              <h1 className="text-lg font-semibold uppercase text-cyan-600 text-heading">
                {t('Product Information')}
              </h1>
            </div>
          </div>

          <div className="flex flex-wrap">
            <span className="w-full text-xs text-red-500 text-start">
              {errorMessageGoodsId}
            </span>
          </div>

          <div className="mb-6 overflow-hidden rounded shadow">
            <Table
              //@ts-ignore
              columns={columnsGoods}
              emptyText={() => (
                <div className="flex flex-col items-center py-6">
                  <div className="pt-6 text-sm font-semibold">
                    {t('table:empty-table-data')}
                  </div>
                </div>
              )}
              data={
                initialValues?.campaign?.listGoods ?? campaign?.listGoods ?? []
              }
              rowKey="id"
              scroll={{x: 1000}}
            />
          </div>
        </Card>
      </div>
      <div className="mb-4 text-end">
        <Button
          variant="outline"
          onClick={router.back}
          className="me-4"
          type="button"
        >
          {t('Back')}
        </Button>

        {/* Page create */}
        {!initialValues && campaign && (
          <Button
            loading={creating}
            className="bg-red-700 me-4 hover:bg-red-800"
          >
            {t('Delivery Register')}
          </Button>
        )}

        {/* Page update */}
        {/* {initialValues && (
          <Button className="bg-red-700 hover:bg-red-800" loading={updating}>
            {t('Delivery Register')}
          </Button>
        )} */}

        {initialValues?.statusCode === CommonStatusCode.WAIT_APPRV &&
          isPermitted([p.ROLE_ADMIN]) && (
            <>
              <Button
                variant="outline"
                className="bg-red-700 me-4 hover:bg-red-800"
                loading={updating}
              >
                {t('Save')}
              </Button>
              <Button
                className="bg-red-700 me-4 hover:bg-red-800"
                loading={updating}
                onClick={() => handleClick(CommonApproveStatusAction.APPRV)}
              >
                {t('Approve')}
              </Button>
            </>
          )}
        {initialValues?.statusCode === CommonStatusCode.CANCEL && (
          <>
            <Button
              variant="outline"
              className="bg-red-700 me-4 hover:bg-red-800"
              loading={updating}
            >
              {t('Save')}
            </Button>
            <Button
              className="bg-red-700 me-4 hover:bg-red-800"
              loading={updating}
              onClick={() => handleClick(CommonApproveStatusAction.REQ)}
            >
              {t('Re-register')}
            </Button>
          </>
        )}
        {initialValues?.statusCode === CommonStatusCode.CANCEL_APPRV && (
          <Button
            className="bg-red-700 me-4 hover:bg-red-800"
            loading={updating}
            onClick={() => handleClick(CommonApproveStatusAction.REQ)}
          >
            {t('Re-register')}
          </Button>
        )}
        {initialValues?.statusCode === CommonStatusCode.REJECTED && (
          <Button
            className="bg-red-700 me-4 hover:bg-red-800"
            loading={updating}
            onClick={() => handleClick(CommonApproveStatusAction.REQ)}
          >
            {t('Re-register')}
          </Button>
        )}
      </div>
    </form>
  );
}
