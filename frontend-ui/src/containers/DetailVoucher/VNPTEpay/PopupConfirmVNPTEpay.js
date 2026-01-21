import { Box, Button, Dialog, DialogActions, DialogContent, Grid, IconButton, Typography } from '@mui/material';
import { grey, yellow } from '@mui/material/colors';
import React, { useState } from 'react';
import { useTranslation } from "react-i18next";
import CloseButton from '../../../components/ui/CloseButton';
import LOGO from "../../../images/topup_confirm.png";
import { ConvertNumber } from "../../../utils/utils";
import { TOPUP_OPTIONS } from './VNPTEpayUtils';

const PopupConfirmVNPTEpay = ({ open = true, vnptProduct, typeAction, phone, value, onSubmit, onClose }) => {
    const { t } = useTranslation();
    const valueDisplay = ConvertNumber(value);
    const [isSubmitting, setIsSubmitting] = useState(false);

    const handleConfirmClick = () => {
        if (isSubmitting) return;
        setIsSubmitting(true);
        onSubmit();
    };

    return (
        <Dialog open={open} onClose={onClose} maxWidth="sm" fullWidth sx={{
            '& .MuiDialog-paper': {
                backgroundColor: 'white',
                borderRadius: 4,
            },
        }}>
            <IconButton
                aria-label="close"
                style={{ position: 'absolute', right: 4, top: 4, zIndex: 1 }}
                onClick={onClose}
            >
                <CloseButton />
            </IconButton>

            <DialogContent sx={{ padding: '6px 8px' }}>
                <Box display="flex" position={'relative'} boxSizing={'border-box'} flexDirection="column" alignItems="flex-start" width="100%" paddingX={1} paddingY={2}>
                    {/* Logo */}
                    <Box display="flex" justifyContent={"center"} width="100%" mb={"20px"}>
                        <Box
                            component="img"
                            src={LOGO}
                            alt="Logo"
                            sx={{
                                display: 'flex',
                                justifyContent: 'center',
                                width: '140px',
                                height: 'auto',
                                borderRadius: '8px',
                            }}
                        />
                    </Box>

                    {/* Header */}
                    <Box display="flex" flexDirection="row" justifyContent={"center"} width="100%" mb={1}>
                        <Typography variant="h6" fontWeight={600} fontSize={'1rem'} component="label">
                            {t('topup.confirm_header')}
                        </Typography>
                    </Box>

                    {/* Description */}
                    <Box display="flex" flexDirection="row" justifyContent={"center"} width="100%" mb={2}>
                        <Typography variant="body2" textAlign={"center"} fontWeight={400} fontSize={11} component="label" style={{ opacity: 0.6 }}>
                            {t('topup.confirm_description_1')}
                        </Typography>
                    </Box>

                    {/* Voucher Info */}
                    <Grid container spacing={1} mb={2}>
                        {typeAction === TOPUP_OPTIONS.TOPUP && (
                            <>
                                <Grid item xs={6} textAlign={"center"}>
                                    <Typography variant="h6" fontWeight={600}>
                                        {t('topup.confirm_topup_target')}
                                    </Typography>
                                </Grid>
                                <Grid item xs={6} textAlign={"center"}>
                                    <Typography variant="h6" fontWeight={600}>
                                        {phone}
                                    </Typography>
                                </Grid>
                            </>
                        )}
                        <Grid item xs={6} textAlign={"center"}>
                            <Typography variant="h6" fontWeight={600}>
                                {vnptProduct?.vnptProvider?.providerNm}
                            </Typography>
                        </Grid>
                        <Grid item xs={6} textAlign={"center"}>
                            <Typography variant="h6" fontWeight={600}>
                                {valueDisplay === '' ? '' : valueDisplay + ' VND'}
                            </Typography>
                        </Grid>
                    </Grid>

                    {/* Note */}
                    <Box display="flex" flexDirection="row" justifyContent={"center"} width="100%" mb={1}>
                        <Typography variant="body2" textAlign={"center"} fontWeight={400} fontSize={12} component="label" style={{ opacity: 0.6 }}>
                            {t('topup.confirm_note')}
                        </Typography>
                    </Box>
                </Box>

                <DialogActions>
                    <Box sx={{ display: 'block', width: '100%' }}>
                        <Button variant="contained"
                            sx={{
                                width: '100%',
                                backgroundColor: yellow[600],
                                color: 'black',
                                textTransform: 'none',
                                borderRadius: '8px',
                                '&:hover': {
                                    backgroundColor: yellow[700]
                                },
                                '&:disabled': {
                                    backgroundColor: grey[300],
                                },
                                marginBottom: '10px'
                            }} onClick={handleConfirmClick}
                            disabled={isSubmitting}>
                            {t('button.confirm')}
                        </Button>
                        <Button variant="contained" sx={{
                            width: '100%',
                            backgroundColor: grey[300],
                            color: 'black',
                            textTransform: 'none',
                            borderRadius: '8px',
                            '&:hover': {
                                backgroundColor: grey[400]
                            },
                        }} onClick={onClose}>
                            {t('button.cancel')}
                        </Button>
                    </Box>
                </DialogActions>
            </DialogContent>
        </Dialog>
    );
};

export default PopupConfirmVNPTEpay;
