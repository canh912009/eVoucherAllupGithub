import { DeliveryVoucher } from '@/types';
import { saveAs } from 'file-saver';
import JSZip from 'jszip';
import QRCode from 'qrcode';

export const downloadQRCode = async (
  data: string,
  filename: string = 'qrcode',
  width: number = 128
): Promise<void> => {
  try {
    const qrCodeDataURL = await QRCode.toDataURL(data, {
      type: 'image/png',
      errorCorrectionLevel: 'H',
      width: width,
    });
    const res = await fetch(qrCodeDataURL);
    const blob = await res.blob();
    saveAs(blob, filename);
  } catch (error) {
    console.error(`Error downloading QR code for ${filename}:`, error);
  }
};

export const downloadVouchersQRCodeZip = async (
  vouchers: DeliveryVoucher[],
  filename: string = 'vouchers_qrcode',
  width: number = 128
): Promise<void> => {
  const zip = new JSZip();

  // Function to generate and add QR code files to the zip
  const addQRCodeToZip = async (
    voucher: DeliveryVoucher,
    index: number
  ): Promise<void> => {
    const { ev, shortLink, otp } = voucher;
    let filename = `${index + 1}.png`;
    // if (otp) {
    //   filename = `${index + 1}_${otp}.png`;
    // }

    try {
      const qrCodeDataURL = await QRCode.toDataURL(shortLink, {
        type: 'image/png',
        errorCorrectionLevel: 'H',
        width: width,
      });
      const res = await fetch(qrCodeDataURL);
      const blob = await res.blob();
      zip.file(filename, blob); // Add the QR code image to the zip file
    } catch (error) {
      console.error(`Error generating QR code for ${filename}:`, error);
    }
  };

  // Generate and add QR code files to the zip for each voucher
  await Promise.all(
    vouchers.map((voucher, index) => addQRCodeToZip(voucher, index))
  );

  // Generate the zip file
  zip.generateAsync({ type: 'blob' }).then((blob) => {
    // Save the zip file
    saveAs(blob, `${filename}.zip`);
  });
};

export const downloadVouchersQRCodeBatch = (
  vouchers: DeliveryVoucher[],
  batchSize: number = 10,
  width: number = 128
): void => {
  const totalBatches = Math.ceil(vouchers.length / batchSize);

  // Function to process a batch of vouchers
  const processBatch = async (batchIndex: number): Promise<void> => {
    const start = batchIndex * batchSize;
    const end = Math.min((batchIndex + 1) * batchSize, vouchers.length);

    for (let i = start; i < end; i++) {
      const voucher = vouchers[i];
      const { serialNo, activationUrl } = voucher;
      const filename = `${serialNo}.png`;

      await downloadQRCode(activationUrl, filename, width);
    }
  };

  // Process batches sequentially
  for (let batchIndex = 0; batchIndex < totalBatches; batchIndex++) {
    processBatch(batchIndex);
  }
};
