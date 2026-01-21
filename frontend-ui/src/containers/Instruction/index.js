import React, {useEffect, useState} from 'react';
import {Container} from './styles';
import LOGO from '../../images/Logo_mini.png';
import {BiArrowBack} from "react-icons/bi";
import {useNavigate} from "react-router-dom";
import {useTranslation} from "react-i18next";


const Instruction = () => {
    const {t} = useTranslation()


    document.title = 'Instruction';
    const history = useNavigate()
    const asPath = window.location.pathname;
    const lastIndex = asPath.lastIndexOf('/');
    const keyInstruction = asPath.substring(lastIndex + 1, asPath.length); // remove preview
    const [instruction, setInstruction] = useState(null);

    const LIST_INSTRUCTION = [
        {
            key: 'what_is_e-voucher',
            title: t('voucher.what_is_e-voucher'),
            ques: t('voucher.what_is_e-voucher'),
            sub: [
                t('voucher.what_is_e-voucher_text1'),
                t('voucher.what_is_e-voucher_text2')
            ],
        },
        {
            key: 'what_is_aQua',
            title: t('voucher.what_is_aQua'),
            ques: t('voucher.what_is_aQua'),
            sub: [
                t('voucher.what_is_aQua_text1'),
            ],
        },
        {
            key: 'what_are_the_precautions',
            title: t('voucher.what_are_the_precautions'),
            ques: t('voucher.what_are_the_precautions'),
            sub: [
                t('voucher.what_are_the_precautions_text1'),
            ],
        },
        {
            key: 'how_to_use_voucher',
            title: t('voucher.how_to_use_voucher'),
            ques: t('voucher.how_to_use_voucher'),
            sub: [
                t('voucher.how_to_use_voucher_text1'),
                t('voucher.how_to_use_voucher_text2')
            ],
        },
        {
            key: 'cannot_convert_to_cash',
            title: t('voucher.cannot_convert_to_cash'),
            ques: t('voucher.cannot_convert_to_cash'),
            sub: [
                t('voucher.cannot_convert_to_cash_text1'),
                t('voucher.cannot_convert_to_cash_text2'),
                t('voucher.cannot_convert_to_cash_text3'),
            ],
        },
        {
            key: 'sharing_with_people',
            title: t('voucher.sharing_with_people'),
            ques: t('voucher.sharing_with_people'),
            sub: [
                t('voucher.sharing_with_people_text1'),
                t('voucher.sharing_with_people_text2')
            ],
        },
        {
            key: 'who_is_aqua_retail',
            title: t('voucher.who_is_aqua_retail'),
            ques: t('voucher.who_is_aqua_retail'),
            sub: [
                t('voucher.who_is_aqua_retail_text1'),
                t('voucher.who_is_aqua_retail_text2'),
            ],
        },
        {
            key: 'hotline',
            title: t('voucher.question_hotline'),
            ques: t('voucher.question_hotline'),
            sub: [
                t('voucher.answer_hotline')
            ],
        },
    ];

    useEffect(() => {
        const listKey = LIST_INSTRUCTION.map(item => item.key);
        if (listKey.includes(keyInstruction)) {
            LIST_INSTRUCTION.map(item => {
                if (item.key === keyInstruction) {
                    setInstruction(item);
                }
            });
        }
    }, [keyInstruction]);

    return (
        <Container>
            <div className="row">
                <div className="header">
                    <div className="title">{LIST_INSTRUCTION.map((item) => {
                        if (item.key === keyInstruction) {
                            return item.title
                        }
                    })}</div>
                    <BiArrowBack onClick={() => {
                        history(-1)
                    }
                    } size={28} className={"icon_back"}/>
                </div>

                <div className="row_list_voucher">
                    <div className="item_voucher">
                        <div className="logo">
                            <img src={LOGO} alt=""/>
                        </div>
                        <div className="line_dashed"/>
                        <div className="line_dashed_absolute" style={{left: '-14px'}}/>
                        <div className="line_dashed_absolute" style={{right: '-14px'}}/>
                        {/*<div className="detail">{instruction && instruction.ques}</div>*/}
                        <div className="detail">{LIST_INSTRUCTION.map((item) => {
                            if (item.key === keyInstruction) {
                                return item.ques
                            }
                        })}</div>
                    </div>
                    <div style={{paddingTop: '20px'}}>
                        {LIST_INSTRUCTION.map((items) => {
                            if (items.key === keyInstruction) {
                                return items?.sub.map((item, index) => {
                                    if (index !== items?.sub.length - 1) {
                                        return (
                                            <>
                                                <div className="tra_loi">{item}</div>
                                                <div className="line_ngang"/>
                                            </>
                                        );
                                    } else {
                                        return (<div className="tra_loi">{item}</div>)
                                    }
                                })
                            }
                        })}
                    </div>
                </div>
            </div>
        </Container>
    );
};

export default Instruction;
