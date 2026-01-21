import React from 'react';
import QRCode from '@/components/ui/qrcode/QRCode';

type StoreQRCodeBackgroundProps = {
  // Image QRCode
  qrText: string;
  qrCodeSize?: number;

  // Description
  title?: string;
  storeName?: string;

  // Image background
  imgBgSrc?: string;
  imgBgSize?: number;

  // Image logo
  logoSrc?: string;
  logoSize?: number;
};

function StoreQRCodeBackground({
  qrText,
  qrCodeSize = 300,
  title = 'Scan me',
  storeName,
  imgBgSrc = '/images/qrcode/store-qr-bg.png',
  imgBgSize = 500,
  logoSrc = '/images/logo/logo_aqua_square_100x91.png',
  logoSize = 90,
}: StoreQRCodeBackgroundProps) {
  const containerStyle: React.CSSProperties = {
    position: 'relative',
    width: imgBgSize,
    // height: imgBgSize,
    // border: '1px solid red'
  };

  const contentStyle: React.CSSProperties = {
    position: 'absolute',
    width: imgBgSize,
    top: '50%',
    left: '50%',
    transform: 'translate(-50%, -50%)',
    zIndex: 1,
  };

  const headingStyle: React.CSSProperties = {
    position: 'relative',
    display: 'flex',
    justifyContent: 'center',
    alignItems: 'center',
    // zIndex: 1,
    fontSize: '28px',
    fontWeight: 400,
    textTransform: 'uppercase',
    color: '#ffffff',
    width: '100%',
    textAlign: 'center',
    bottom: '20px',
  };

  const qrCodeStyle: React.CSSProperties = {
    position: 'relative',
    // width: qrCodeSize,
    display: 'flex', // Add flex display
    justifyContent: 'center', // Center horizontally
    alignItems: 'center', // Center vertically
    // paddingTop: '20px', // Add some space between the headings and QR code
    // paddingBottom: '10px',
  };

  const bottomHeadingStyle: React.CSSProperties = {
    position: 'relative',
    display: 'flex',
    justifyContent: 'center',
    alignItems: 'center',
    // zIndex: 1,
    fontSize: '28px',
    fontWeight: 400,
    textTransform: 'uppercase',
    color: '#ffffff',
    width: '100%',
    textAlign: 'center',
    top: '10px',
  };

  return (
    <div style={containerStyle}>
      <img
        src={imgBgSrc}
        alt="Store Background QRCode"
        width={imgBgSize}
        height={imgBgSize}
      />
      <div style={contentStyle}>
        <div style={headingStyle}>
          <span>{title}</span>
        </div>

        <div style={qrCodeStyle}>
          <QRCode
            qrText={qrText}
            imgSize={qrCodeSize}
            logoSrc={logoSrc}
            logoSize={logoSize}
          />
        </div>

        {storeName ? (
          <div style={bottomHeadingStyle}>
            <span>{storeName}</span>
          </div>
        ) : (
          ''
        )}
      </div>
    </div>
  );
}

export default StoreQRCodeBackground;
