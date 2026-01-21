import { Backdrop } from "@mui/material";
import moment from "moment/moment";
import React from 'react';
import { useTranslation } from "react-i18next";
import { MdClear } from "react-icons/md";
import styled from "styled-components";
import SystemType from "../../utils/SystemType";
import { ConvertNumber, getPriceProduct } from "../../utils/utils";
import { getTypeVoucherString } from "../../utils/VoucherType";

const Container = styled.div`
  .text_show_instruction {
    font-style: normal;
    font-weight: 700;
    font-size: 18px;
    color: #000000;
  }

  h2 {
    line-height: 28px;
  }

 `

const PopupProductGuide = ({ data, onClose }) => {
    const { t } = useTranslation()

    return (
        <Backdrop
            sx={{ color: '#fff', zIndex: 10 }}
            open={true}
        >
            <Container>
                <div className="pop_up_modal">
                    <MdClear onClick={onClose}
                        color={"#000"}
                        style={{
                            position: "absolute",
                            right: "18px",
                            cursor: "pointer",
                            zIndex: "100",
                            top: "18px"
                        }}
                        size={32} />
                    <div className="row_popup">
                        <h1>{t('voucher.product_guide')}</h1>
                        <h2>Voucher : {data?.id}</h2>
                        <h2>{t('voucher.expiryDate')}: {moment(data?.expireDate || null).format("DD/MM/YYYY")}</h2>

                        <h2>{t('voucher.productName')}: {data?.goods?.name}</h2>
                        <h2>{t('voucher.productType')}: {getTypeVoucherString(data?.voucherType) || 'NULL'}</h2>
                        <h2 style={{
                            display: "flex",
                            alignItems: "center",
                            alignContent: "center"
                        }}><span>{t('voucher.price')}</span> {getPriceProduct(data, t)}</h2>

                        {(data?.voucherType === "PP" || data?.system === SystemType.CHOICE || data?.system === SystemType.BULK) &&
                            <h2 className="price"
                                style={{ width: "max-content" }}>
                                {t('voucher.balance')}:{' '}
                                <span>
                                    {ConvertNumber(data?.balance)} VNĐ
                                </span>
                            </h2>
                        }


                        <div style={{
                            width: "100%",
                            margin: "auto",
                            borderTop: "0.5px dashed #e0e0e0",
                            marginTop: "12px"
                        }}></div>
                        {/*<div>*/}
                        {/*    {data.contentImagePath && <div style={{display: "flex", justifyContent: "center"}}>*/}
                        {/*        <img style={{width: "80%", marginTop: "24px"}} src={data?.contentImagePath} alt=""/>*/}
                        {/*    </div>}*/}
                        {/*    {data.contentLink && <h2 style={{textAlign: "center", marginTop: "12px"}}>*/}
                        {/*        {data?.contentLink}*/}
                        {/*    </h2>}*/}
                        {/*</div>*/}
                        {/*<div style={{*/}
                        {/*    width: "100%",*/}
                        {/*    margin: "auto",*/}
                        {/*    borderTop: "0.5px dashed #e0e0e0",*/}
                        {/*    marginTop: "12px"*/}
                        {/*}}></div>*/}
                        <div>
                            <h1>1. {t('voucher.brandDesc')}</h1>
                            <h2 style={{ whiteSpace: "pre-line" }}>
                                {data?.goods?.brand?.description}
                            </h2>
                            <h1>2. {t('voucher.produceDesc')}</h1>
                            <h2 style={{ whiteSpace: "pre-line" }} dangerouslySetInnerHTML={{ __html: data?.goods?.description }}>
                            </h2>
                        </div>
                    </div>
                </div>
            </Container>
        </Backdrop>
    );
};

export default PopupProductGuide;