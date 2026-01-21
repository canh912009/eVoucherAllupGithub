import React from 'react';
import { useQRCode } from 'next-qrcode';
import styles from './QRCode.module.css';

type QRCodeProps = {
  qrText: string;
  imgSize?: number;
  logoSrc?: string;
  logoSize?: number;
};

/**
 * This component generates a QRCode with an optional logo in the center.
 * If you intend to use a logo from next-qrcode's Canvas, be aware that downloading the image by converting HTML (using the html2canvas library) might not work as expected.
 *
 * @author: dd.tung
 * @returns JSX element
 */
function QRCode({
  qrText,
  imgSize = 300,
  logoSrc,
  logoSize = 60,
}: QRCodeProps) {
  // console.log('qrText', qrText);

  const { Image } = useQRCode();

  return (
    <div className={styles.qrCodeContainer}>
      <Image
        text={qrText}
        options={{
          errorCorrectionLevel: 'L',
          margin: 1,
          scale: 1,
          width: imgSize,
          color: {
            dark: '#000000',
            light: '#FFFFFF',
          },
        }}
      />
      {logoSrc ? (
        <img
          className={styles.logo}
          src={logoSrc}
          width={logoSize}
          // height={logoSize}
          alt="Logo"
        ></img>
      ) : undefined}
    </div>
  );
}

export default QRCode;
