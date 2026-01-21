import React, {useState} from 'react';
import {useTranslation} from "react-i18next";
import {Backdrop} from "@mui/material";
import {PopupNumberPhone} from "./styles";
import IMAGE_BACKGROUND_POPUP_NUMBERPHONE from "../../images/bg_popup_numberPhone.png";

const PopupEnterNumberPhone = ({onClose, onClickContinue, error}) => {
    const {t, i18n} = useTranslation()
    const [numberPhone, setNumberPhone] = useState('');
    return (
        <Backdrop
            sx={{color: '#fff', zIndex: 10}}
            open={true}
        >
            <PopupNumberPhone>
                <div className="modal-template">
                    <div className="header">
                        <div className="content">
                            <div onClick={onClose} className="circle-button" style={{cursor:"pointer"}}>
                                <svg
                                    className="icn-close"
                                    width="20"
                                    height="20"
                                    viewBox="0 0 20 20"
                                    fill="none"
                                    xmlns="http://www.w3.org/2000/svg"
                                >
                                    <path
                                        fill-rule="evenodd"
                                        clip-rule="evenodd"
                                        d="M16.709 4.70701C17.0995 4.31651 17.0995 3.68334 16.709 3.2928C16.3185 2.90226 15.6854 2.90223 15.2948 3.29274L10.0009 8.58615L4.70708 3.29274C4.31654 2.90223 3.68338 2.90226 3.29287 3.2928C2.90236 3.68334 2.90239 4.31651 3.29293 4.70701L8.58667 10.0003L3.29292 15.2936C2.90238 15.6841 2.90236 16.3173 3.29286 16.7078C3.68337 17.0984 4.31654 17.0984 4.70708 16.7079L10.0009 11.4145L15.2948 16.7079C15.6854 17.0984 16.3185 17.0984 16.709 16.7078C17.0995 16.3173 17.0995 15.6841 16.709 15.2936L11.4152 10.0003L16.709 4.70701Z"
                                        fill="#3D3D3D"
                                    />
                                </svg>
                            </div>
                        </div>
                    </div>
                    <div className="content-to-swap">
                        <div className="frame-1000003393">
                            <div className="content2">
                                <div className="image-container">
                                    <div className={"image"}>
                                        <img src={IMAGE_BACKGROUND_POPUP_NUMBERPHONE} alt=""/>
                                    </div>
                                </div>
                                <div className="text-and-supporting-text">
                                    <div className="text">{t('voucher.titleCheckNumberPhone')}</div>
                                    <div className="supporting-text">
                                        {t('voucher.descCheckNumberPhone')}
                                    </div>
                                </div>
                            </div>
                        </div>

                        <div className="otp">
                            <input type="number" value={numberPhone}
                                   onChange={(e) => {
                                       setNumberPhone(e.target.value)
                                   }} className={"text-input"}/>
                        </div>
                    </div>
                    <div className="buttons">
                        <button
                            onClick={() => {
                                onClickContinue(numberPhone)
                            }}
                            disabled={numberPhone.length === 0} style={{
                            width: "calc(100% - 32px)",
                            height: "40px",
                            borderRadius: "8px",
                            border: "unset",
                            backgroundColor: numberPhone.length > 0 ? "#FDDF47" : "gray",
                            fontSize: "15px",
                            fontWeight:'bold',
                            color: numberPhone.length > 0 ? "#383A42" : "white",
                            cursor: numberPhone.length > 0 ? "pointer" : "no-drop"
                        }}>
                            {t('voucher.continue')}
                        </button>
                    </div>
                </div>
            </PopupNumberPhone>
        </Backdrop>
    );
}
export default PopupEnterNumberPhone;