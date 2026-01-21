import React, {useEffect, useState} from 'react';
import ICON_USER from "../../images/icon_user.png";
import {
    Backdrop,
    Box,
    Button,
    FormControl,
    FormControlLabel,
    MenuItem,
    Radio,
    RadioGroup,
    TextField
} from "@mui/material";
import {LocalizationProvider} from "@mui/x-date-pickers/LocalizationProvider";
import {AdapterDayjs} from "@mui/x-date-pickers/AdapterDayjs";
import {DatePicker} from "@mui/x-date-pickers/DatePicker";
import moment from "moment/moment";
import Select from "@mui/material/Select";
import axios from "axios";
import {AiOutlineCheck, AiOutlineClose} from "react-icons/ai";

const PopupUpdateInfo = ({t, valueName, setValueName, onTransfer, onClose, otp}) => {
    const [listProvince, setListProvince] = useState([])
    const [listDivision, setListDivision] = useState([])
    const [valueProvince, setValueProvince] = useState(null)
    const [valueDivision, setValueDivision] = useState(null)
    const [errorLengthName, setErrorLengthName] = useState(false)
    const [valueBirthday, setValueBirthday] = useState(null)
    const [valueGender, setValueGender] = useState(null)

    const fromData = {
        "otp": otp,
        "name": valueName,
        "birthday": valueBirthday,
        "gender": valueGender,
        "province": valueProvince,
        "district": valueDivision
    }


    useEffect(() => {
        axios.get('https://provinces.open-api.vn/api/p/')
            .then((res) => {
                setListProvince(res.data)
            })
        axios.get('https://provinces.open-api.vn/api/d/')
            .then((res) => {
                setListDivision(res.data)
            })
    }, [])
    return (
        <Backdrop
            sx={{color: '#fff', zIndex: 10}}
            open={true}
        >
            <div className="pop_up_modal">
                <div className="row_popup">
                    <img src={ICON_USER} alt=""/>
                    <h1>{t('voucher.titleUpdateInfo')}</h1>
                    <h2>{t('voucher.descPopupUpdateInfo')}</h2>
                    <div style={{}}>
                        <div className={'label'}>{t('voucher.name')} <span style={{color: "red"}}>*</span></div>
                        <TextField
                            sx={{margin: "4px 0", width: "100%"}}
                            id="outlined-controlled"
                            label=""
                            value={valueName}
                            placeholder={"Enter your name"}
                            onChange={(event) => {
                                if (event.target.value.length < 30) {
                                    setValueName(event.target.value)
                                    setErrorLengthName(false)
                                } else {
                                    setErrorLengthName(true)
                                }
                            }}
                            size="small"
                        />
                        {errorLengthName && <div style={{
                            color: "red",
                            fontStyle: "italic"
                        }}>{"Tên không được vượt quá 30 ký tự"}</div>}
                        <div className={'label'}>{t('voucher.birthDay')}
                        </div>
                        <Box sx={{margin: "4px 0", width: "100%"}}>
                            <LocalizationProvider dateAdapter={AdapterDayjs}>
                                <DatePicker
                                    // value={ valueBirthday}
                                    onChange={(e) => {
                                        setValueBirthday(moment(e).format('YYYY-MM-DD'))
                                    }}
                                    sx={{
                                        width: "100%",
                                        "& .css-9ddj71-MuiInputBase-root-MuiOutlinedInput-root": {
                                            height: "40px"
                                        },
                                        "& .css-14s5rfu-MuiFormLabel-root-MuiInputLabel-root": {
                                            lineHeight: "unset",
                                            top: "-4px"
                                        }
                                    }}/>
                            </LocalizationProvider>
                        </Box>
                        <div className={'label'}>{t('voucher.gender')}
                        </div>
                        <div style={{
                            display: "flex",
                            alignItems: "center",
                            width: "80%",
                            alignContent: "center"
                        }}>
                            <FormControl>
                                <RadioGroup
                                    value={valueGender}
                                    onChange={(e) => {
                                        setValueGender(e.target.value)
                                    }}
                                    row
                                    aria-labelledby="demo-row-radio-buttons-group-label"
                                    name="row-radio-buttons-group"
                                >
                                    <FormControlLabel value="MEN" control={<Radio/>} label="MEN"/>
                                    <FormControlLabel value="WOMEN" control={<Radio/>} label="WOMEN"/>
                                </RadioGroup>
                            </FormControl>
                        </div>
                        <div className={'label'}>{t('voucher.province')}
                        </div>
                        <Box sx={{margin: "4px 0", width: "100%"}}>
                            <FormControl fullWidth>
                                <Select
                                    labelId="demo-simple-select-label"
                                    id="demo-simple-select"
                                    size="small"
                                    onChange={(e) => {
                                        axios.get(`https://provinces.open-api.vn/api/p/` + e.target.value + `?depth=2`)
                                            .then((res) => {
                                                setValueProvince(res.data.name)
                                                setListDivision(res.data.districts)
                                            })
                                    }}
                                >
                                    {listProvince.map((item) => {
                                        return <MenuItem value={item.code}>{item.name}</MenuItem>
                                    })}
                                </Select>
                            </FormControl>
                        </Box>
                        <div className={'label'}>{t('voucher.division')}
                        </div>
                        <Box sx={{margin: "4px 0", width: "100%"}}>
                            <FormControl fullWidth>
                                <Select
                                    labelId="demo-simple-select-label"
                                    id="demo-simple-select"
                                    size="small"
                                    onChange={(e) => {
                                        axios.get(`https://provinces.open-api.vn/api/d/` + e.target.value)
                                            .then((res) => {
                                                setValueDivision(res.data.name)
                                            })
                                    }}
                                >
                                    {listDivision.map((item) => {
                                        return <MenuItem value={item.code}>{item.name}</MenuItem>
                                    })}
                                </Select>
                            </FormControl>
                        </Box>

                    </div>
                    <div style={{
                        width: "100%",
                        margin: "auto",
                        borderTop: "0.5px dashed #e0e0e0",
                        marginTop: "12px"
                    }}></div>
                    <Button
                        onClick={()=>{
                            onTransfer(fromData)
                        }}
                        disabled={valueName === '' || valueName === null || valueName.length > 30}
                        sx={{backgroundColor: '#039855', width: "100%", margin: "20px 0"}}
                        variant='contained'><AiOutlineCheck
                        style={{marginRight: "8px"}}/>{t('voucher.buttonUpdate')}</Button>
                    <Button
                        onClick={() => {
                            onClose(false)
                        }}
                        sx={{backgroundColor: '#D1293D', width: "100%"}}
                        variant='contained'><AiOutlineClose style={{marginRight: "8px"}}/>{t('voucher.cancel')}
                    </Button>
                </div>
            </div>
        </Backdrop>
    );
};

export default PopupUpdateInfo;