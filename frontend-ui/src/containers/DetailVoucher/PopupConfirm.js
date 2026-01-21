import React from 'react';
import ICON_QUES from "../../images/icon_ques.png";
import {Backdrop, Button} from "@mui/material";

const PopupConfirm = ({onTransfer, t, setIsConfirm, setUpdateInfo, otp, valueName}) => {
    const fromData = {
        "otp": otp,
        "name": valueName,
        "birthday": null,
        "gender": null,
        "province": null,
        "district": null
    }
    return (
        <Backdrop
            sx={{color: '#fff', zIndex: 10}}
            open={true}
        >
            <div className="pop_up_modal">
                <div className="row_popup">
                    <img src={ICON_QUES} alt=""/>
                    <h1>{t('voucher.updateYourInfor')}</h1>
                    <h2>{t('voucher.subupdateYourInfor')}</h2>
                    <div style={{
                        width: "100%",
                        margin: "auto",
                        borderTop: "0.5px dashed #e0e0e0",
                        marginTop: "12px"
                    }}></div>
                    <Button
                        onClick={() => {
                            setIsConfirm(false)
                            setUpdateInfo(true)
                        }}
                        sx={{backgroundColor: '#DC6803', width: "100%", margin: "20px 0"}}
                        variant='contained'>{t('voucher.buttonUpdate')}</Button>
                    <Button
                        onClick={()=>{
                            onTransfer(fromData)
                        }}
                        sx={{backgroundColor: '#039855', width: "100%"}}
                        variant='contained'>{t('voucher.buttonNoUpdate')}</Button>
                    <Button
                        onClick={() => {
                            setIsConfirm(false)
                        }}
                        sx={{backgroundColor: '#ee050c', width: "100%", marginTop: "20px"}}
                        variant='contained'>{t('voucher.cancel')}</Button>
                </div>
            </div>
        </Backdrop>
    );
};

export default PopupConfirm;