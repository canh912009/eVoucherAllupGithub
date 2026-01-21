import moment from "moment";
import 'moment/locale/vi';
import React from 'react';
import { useTranslation } from "react-i18next";
import LOGO from '../../images/Logo.png';
import IMAGE_POPUP_SUCCESS from "../../images/popupSuccess.png";
import { Container } from './styles';


const ResponseUseVoucher = () => {
    const { t, i18n } = useTranslation();

    const isVietnamese = i18n.language === 'vi';

    document.title = 'Response Use Voucher';
    return (
        <Container>
            <div className="row">
                <div className="header">
                    <img src={LOGO} alt="" />
                </div>
                <div className="pop_up_modal">
                    <div className="row_popup">
                        <img src={IMAGE_POPUP_SUCCESS} alt="" />
                        <h1>{t('voucher.titleResponseUsed')}</h1>
                        <h2>{t('voucher.descResponseUsed')}</h2>
                        <div className={`footer ${isVietnamese ? 'footer-vi' : ''}`}>
                            {moment().locale(isVietnamese ? 'vi' : 'en').format('LLLL')}
                        </div>
                    </div>
                </div>
            </div>
        </Container>
    );
};

export default ResponseUseVoucher;
