import { ExternalPinUploadData, Good } from "@/types";
import { useTranslation } from 'next-i18next';
import { useRouter } from 'next/router';
import { useModalAction } from "../ui/modal/modal.context";
import { useForm } from "react-hook-form";
import { yupResolver } from "@hookform/resolvers/yup";
import { createExternalPinUploadMutation } from "@/data/external-pin-upload";
import { getErrorMessage } from "@/utils/form-error";
import Card from '../common/card';
import TextArea from "@/components/ui/text-area";
import Button from '@/components/ui/button';
import { useState } from "react";
import { useUploadDocumentMutation } from "@/data/upload";
import { toast } from "react-toastify";
import * as XLSX from 'xlsx';
import Papa from 'papaparse';
import { goodExternalPinUploadValidationSchema } from "./good-external-pin-upload-schema";
import { UploadIcon } from "../icons/upload-icon";
import { useDropzone } from 'react-dropzone';
import Loader from "@/components/ui/loader/loader";
import { compareDates, isValidDateFormat } from "@/utils/common-utils";

interface GoodUploadExternalPinPopupProps {
  good?: Good;
  displayType?: any
}

type FormValues = Partial<Good> & {
  uploadName: string;
  goodsId: number;
  uploadFilePath: string;
  uploadFileName: string;
  memo: string;
  pins: ExternalPinUploadData[]
};

const GoodUploadExternalPinPopup: React.FC<GoodUploadExternalPinPopupProps> = ({ good, displayType }) => {

  const { t } = useTranslation();
  const router = useRouter();
  const { closeModal } = useModalAction();

  const rootClassName =
    'ps-4 pe-4 h-12 flex items-center w-full md:w-3/4 rounded appearance-none transition duration-300 ease-in-out text-heading text-sm focus:outline-none focus:ring-0 border border-border-base focus:border-accent';

  const {
    register,
    handleSubmit,
    control,
    setError,
    setValue,
    watch,
    formState: { errors },
  } = useForm<FormValues>({
    resolver: yupResolver(goodExternalPinUploadValidationSchema),
  });

  // console.log('control = ', control, typeof control);

  const uploadFileName = watch('uploadFileName');
  const allowedExtensions = ['csv', 'xls', 'xlsx'];

  const [uploadErrorMessage, setUploadErrorMessage] = useState<string>();
  const { mutate: uploadDocument, isLoading: uploadingDocument } = useUploadDocumentMutation();

  const [listPins, setListPins] = useState<ExternalPinUploadData[]>([]);


  const { getRootProps, getInputProps } = useDropzone(
    {
      onDrop(acceptedFiles, fileRejections, event) {
        // console.log('acceptedFiles ', acceptedFiles);
        // console.log('fileRejections ', fileRejections);
        // console.log('event ', event);
        
        // Clear before set
        setListPins([]);
        setUploadErrorMessage('');

        // acceptedFiles.forEach((file) => {
        const file = acceptedFiles[0];

        console.log('file ', file)
        if (file) {
          const fileExtension = file?.name.split('.').pop()?.toLowerCase();
          if (!allowedExtensions.includes(fileExtension!)) {
            setUploadErrorMessage('Please input a csv or excel file');
            return;
          }

          // Process file upload
          if ('csv' === fileExtension) {
            handleProcessFileCsv(file);
          } else if (['xls', 'xlsx'].includes(fileExtension!)) {
            handleProcessFileExcel(file);
          }

          uploadDocument(file, {
            onSuccess: (data: any) => {
              // console.log('data', data);
              setValue('uploadFilePath', data?.path);
              setValue('uploadFileName', file.name);
            },
            onError: (error: any) => {
              toast.error('Error:' + error?.response?.data.message);
            },
          });
        };
        // });

        fileRejections.forEach((file) => {
          file?.errors?.forEach((error) => {
            if (error?.code === 'file-too-large') {
              setUploadErrorMessage(t('error-file-too-large'));
            } else if (error?.code === 'file-invalid-type') {
              setUploadErrorMessage(t('error-invalid-file-type'));
            }
          });
        });
      },
    }
  );

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
      processExternalPinFile(objDataFile);
    };

    // reader.readAsBinaryString(file);
    reader.readAsArrayBuffer(file);
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
      processExternalPinFile(objDataFile);
    };
    reader.readAsText(file, 'ISO-8859-1');
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

    // Parse parsedData into an array of type DeliveryUser
    const externalPinUploadList: ExternalPinUploadData[] = [];
    let errorMessage: string = '';
    data.forEach((data: any) => {
      const { externalPinNo, expireTime, password } = data;
      // console.log('file data ', externalPinNo, expireTime);

      // Create a new object with properties of DeliveryUser and values from the parsed data
      if (externalPinNo) {
        const expireTimeTemp: string = isValidDateFormat(expireTime) ? expireTime.trim() : '';
        if (expireTimeTemp == '') {
          errorMessage = `Row: pin ${externalPinNo} has column expireTime no data`;
          return;
        } else if (compareDates(expireTimeTemp) < 1) {
          errorMessage = `Row: pin ${externalPinNo} has column expireTime earlier than the current date`;
          return;
        }

        // Check duplicate PIN
        const existPinNo = externalPinUploadList.some(
          (pin) => pin.externalPinNo === externalPinNo
        );
        if (existPinNo) {
          errorMessage = `Row: pin ${externalPinNo} has duplicate data`;
          return;
        }

        let exPinNo: ExternalPinUploadData = {
          id: '',
          externalPinNo: externalPinNo,
          goodsId: '',
          externalPinUpload: '',
          publishDetailId: '',
          status: '',
          expireTime: expireTimeTemp,
          password: password,
        }
        externalPinUploadList.push(exPinNo);
      }
    });
    
    if (errorMessage != '') {
      return { errorCode: 1, errorMessage: errorMessage };
    }
    if (externalPinUploadList.length === 0) {
      return { errorCode: 1, errorMessage: 'No valid data' };
    }
    
    return { errorCode: 0, data: externalPinUploadList };
  };

  /**
   * Process data after read from file
   * @param objDataFile
   * @returns
   */
  const processExternalPinFile = (objDataFile: any) => {
    // If error, show message
    // console.log('objDataFile ', objDataFile);
    if (objDataFile.errorCode !== 0) {
      setUploadErrorMessage(objDataFile.errorMessage!);
      return;
    }

    // If ok, add to list
    setListPins(objDataFile.data);
  };

  const { mutate: createExternalPinUpload, isLoading: creating } =
    createExternalPinUploadMutation();

  const onSubmit = async (values: FormValues) => {
    // console.log('[GoodUploadExternalPinPopup] onSubmit ... values ', values);
    // console.log('[GoodUploadExternalPinPopup] onSubmit ... listPins ', listPins);
    
    if (listPins.length == 0) {
      return;
    }

    let inputValues = {
      uploadName: values.uploadName,
      goodsId: good?.id as unknown as string,
      uploadFilePath: values.uploadFilePath,
      uploadFileName: values.uploadFileName,
      memo: values.memo,
      pins: listPins,
      displayType: displayType,
    }
    // console.log('[GoodUploadExternalPinPopup] onSubmit ... inputValues ', inputValues);
    // return;

    try {
      createExternalPinUpload({
        ...inputValues,
      });
      closeModal();
    } catch (error) {
      const serverErrors = getErrorMessage(error);
      Object.keys(serverErrors?.validation).forEach((field: any) => {
        setError(field.split('.')[1], {
          type: 'manual',
          message: serverErrors?.validation[field][0],
        });
      });
    }
  }

  return (
    <form onSubmit={handleSubmit(onSubmit)}>
      <div className="flex flex-wrap">
        <Card className="w-full sm:w-full md:w-full md:rounded-xl">
          <article className="relative z-[51] w-full h-full max-w-6xl bg-light md:rounded-xl xl:min-w-[622px]">

            <div className="mb-2 flex flex-wrap">
              <div className="flex w-full flex-wrap font-semibold px-4 py-2">
                <h1 className="text-lg uppercase">
                  {t('Upload External PIN')}
                </h1>
              </div>
            </div>

            <div className="mb-2 flex flex-wrap">
              <div className="flex w-full flex-wrap px-4 md:w-full">
                <label className="w-full py-2 text-heading md:w-1/4">
                  {t('Upload Name ')}<span className="text-red-500">*</span>
                </label>
                <input
                  className={rootClassName}
                  type="text"
                  // disabled={initialValues ? false : true}
                  id="uploadName"
                  {...register('uploadName')}
                  placeholder={t('Type text here')}
                />
                <span className="w-full text-xs text-red-500 text-start md:w-3/4 md:pl-[25%]">
                  {t(errors.uploadName?.message!)}
                </span>
              </div>
            </div>

            <div className="mb-2 flex flex-wrap">
              <div className="flex w-full flex-wrap px-4 md:w-full">
                <label className="w-full py-2 text-heading md:w-1/4">
                  {t('Memo Description ')}
                </label>
              </div>
            </div>


            <div className="mb-3 flex flex-wrap">
              <div className="flex w-full flex-wrap px-4 md:w-full">
                <TextArea
                  className={'w-full'}
                  id="memo"
                  {...register('memo')}
                  placeholder={t('Enter a memo description...')}
                  variant="outline"
                  autoComplete="off"
                />
                <span className="w-full text-xs text-red-500 text-start md:w-3/4 md:pl-[25%]">
                  {t(errors.memo?.message!)}
                </span>
              </div>
            </div>

            <div className="flex flex-wrap">
              <div className="flex w-full flex-wrap px-4 md:w-full">
                {/* <label className="w-full py-2 text-heading md:w-1/4">
                  {t('Upload PIN File ')}<span className="text-red-500">*</span>
                </label> */}

                <div className="w-full py-1 md:w-3/4">
                  <label className="py-2 text-heading">
                    {t('Upload PIN File ')}<span className="text-red-500">*</span>
                    &nbsp; (
                    <a
                      href="/files/ExternalPin File Template.csv"
                      className="e_link"
                    >
                      Csv
                    </a>
                    &nbsp;|&nbsp;
                    <a
                      href="/files/ExternalPin File Template.xlsx"
                      className="e_link"
                    >
                      Excel
                    </a>
                    )
                    <span className="ml-2 w-full text-xs text-body">
                      {t('Click here to download the template')}
                    </span>
                  </label>
                </div>
                <span className="w-full text-xs text-red-500 text-start md:w-3/4 md:pl-[18%]">
                  {t(errors.uploadFileName?.message!)}
                </span>
              </div>
            </div>

            <div className="mb-3 px-4 flex flex-wrap w-full">
              {uploadFileName && <p>Selected file: {uploadFileName}</p>}
            </div>

            <div className="flex flex-wrap mb-5 px-4 w-full">
              <div className="p-3 items-center border border-border-base md:w-3/4">
                {uploadingDocument &&
                  <Loader uploadFile={true} showText={false} />
                }
                {!uploadingDocument &&
                  <UploadIcon className="mb-2 w-full text-muted-light" />
                }
                <section className="upload">
                  <div
                    {...getRootProps({
                      className:
                        'border-dashed border-2 border-border-base rounded flex flex-col justify-center items-center cursor-pointer focus:border-accent-400 focus:outline-none',
                    })}
                  >
                    <input {...getInputProps()} />
                    <p className="m-1 text-center text-sm text-body">
                      <span className="font-semibold text-accent">
                        {t('Drop your upload file here')}
                      </span>{' '}<br />
                      <span className="text-xs text-body">{t('(Cvs or Excel file format)')}</span>
                    </p>
                    {uploadErrorMessage && (
                      <p className="text-center text-sm text-body text-red-600">
                        {uploadErrorMessage}
                      </p>
                    )}
                  </div>
                </section>
              </div>
            </div>

            <div className="mb-2 text-end">
              <Button
                variant="outline"
                size="medium"
                className="bg-red-600 hover:bg-red-600"
                onClick={closeModal}
                type="button"
              >
                {t('Close')}
              </Button>

              <Button
                size="medium"
                className="ml-4 me-4 bg-red-600 hover:bg-red-700"
              >
                {t('Upload')}
              </Button>
            </div>

          </article>
        </Card>
      </div>
    </form>
  );
}

export default GoodUploadExternalPinPopup;