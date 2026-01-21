import { Box, Button, Dialog, DialogActions, DialogContent, Grid, IconButton, Typography } from '@mui/material';
import { grey, yellow } from '@mui/material/colors';
import React from 'react';
import { Trans, useTranslation } from "react-i18next";
import CloseButton from '../../../components/ui/CloseButton';
import LOGO from "../../../images/icon_warning.png";

const PopupWarningVNPTEpay = ({ open = true, phoneProvider, telcoNameSelect, phone, onSubmit, onClose }) => {
    const { t } = useTranslation();

    return (
        <Dialog open={open} onClose={onClose} maxWidth="sm" fullWidth backgroundColor={grey[100]} sx={{
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
                <CloseButton onClick={onClose} />
            </IconButton>

            <DialogContent fullWidth sx={{ padding: '6px 8px' }}>
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
                                width: '60px',
                                height: 'auto',
                                borderRadius: '8px',
                            }}
                        />
                    </Box>

                    {/* Header */}
                    <Box display="flex" flexDirection="row" justifyContent={"center"} width="100%" mb={1}>
                        <Typography variant="h6" fontWeight={600} fontSize={'1rem'} component="label">
                            {t('topup.error_provider_mismatch')}
                        </Typography>
                    </Box>

                    {/* Description */}
                    <Box display="flex" flexDirection="row" justifyContent={"center"} width="100%" mb={2}>
                        <Typography variant="body2" textAlign={"center"} fontWeight={400} fontSize={11} component="label" style={{ opacity: 0.6, padding: '0 20px' }}>
                            <Trans
                                i18nKey="topup.warning_provider_mismatch"
                                values={{ provider_phone: phoneProvider?.providerNm, provider_selected: telcoNameSelect }}
                                components={{ strong: <strong /> }}
                            />
                        </Typography>
                    </Box>

                    {/* Voucher Info */}
                    <Grid container spacing={1} mb={2}>
                        <Grid item xs={6} textAlign={"center"}>
                            <Typography variant="h6" fontWeight={600}>
                                {telcoNameSelect}
                            </Typography>
                        </Grid>
                        <Grid item xs={6} textAlign={"center"}>
                            <Typography variant="h6" fontWeight={600}>
                                {phone}
                            </Typography>
                        </Grid>
                    </Grid>

                    {/* Note */}
                    <Box display="flex" flexDirection="row" justifyContent={"center"} width="100%" mb={1}>
                        <Typography variant="body2" textAlign={"center"} fontWeight={400} fontSize={12} component="label" style={{ opacity: 0.6 }}>
                            {t('topup.error_note_check_phone')}
                        </Typography>
                    </Box>
                </Box>

                <DialogActions fullWidth>
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
                                marginBottom: '10px'
                            }} onClick={onSubmit}>
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

export default PopupWarningVNPTEpay;
