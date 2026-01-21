
import { Controller } from 'react-hook-form';
import { ExternalPinUploader } from './external-pin-uploader';

interface ExternalPinFileUploadProps {
  control: any;
  name: string;
  multiple?: boolean;
  acceptFile?: boolean;
  helperText?: string;
  defaultValue?: any;
}

const ExternalPinFileUpload = ({
  control,
  name,
  multiple = true,
  acceptFile = false,
  helperText,
  defaultValue = []
}: ExternalPinFileUploadProps) => {
  return (
    <Controller
      control={control}
      name={name}
      defaultValue={defaultValue}
      render={({ field: { ref, ...rest } }) => (
        <ExternalPinUploader
          {...rest}
          multiple={multiple}
          acceptFile={acceptFile}
          helperText={helperText}
        />
      )}
    />
  );
};

export default ExternalPinFileUpload;

