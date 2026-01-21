import { Box, Button, Dialog, DialogActions, DialogContent, Grid, IconButton, Typography } from '@mui/material';
import { grey, yellow } from '@mui/material/colors';
import React from 'react';
import { useTranslation } from "react-i18next";
import CloseButton from '../../../components/ui/CloseButton';
import LOGO from "../../../images/red-error.png";

const PopupErrorVNPTEpay = ({ data, open = true, onClose }) => {
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
                    <Box display="flex" justifyContent={"center"} width="100%" mb={"16px"} mt={"4px"}>
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
                        <Typography variant="h6" textAlign={"center"} fontWeight={600} fontSize={'0.9rem'} component="label">
                            {data?.title}
                        </Typography>
                    </Box>

                    {/* Message */}
                    <Box display="flex" flexDirection="row" justifyContent={"center"} width="100%" mb={2}>
                        <Typography variant="body2" textAlign={"center"} fontWeight={400} fontSize={11} component="label" style={{ opacity: 0.6 }}>
                            {data?.message}
                        </Typography>
                    </Box>

                    {/* Info */}
                    {data?.phone && (
                        <>
                            {data?.provider ? (
                                <Grid container spacing={1} mb={2}>
                                    <Grid item xs={6} textAlign={"center"}>
                                        <Typography variant="body1" fontWeight={600}>
                                            {data?.provider}
                                        </Typography>
                                    </Grid>
                                    <Grid item xs={6} textAlign={"center"}>
                                        <Typography variant="body1" fontWeight={600}>
                                            {data?.phone}
                                        </Typography>
                                    </Grid>
                                </Grid>
                            ) : (
                                <Grid container spacing={1} mb={2}>
                                    <Grid item xs={12} textAlign={"center"}>
                                        <Typography variant="body1" fontWeight={600}>
                                            {data?.phone}
                                        </Typography>
                                    </Grid>
                                </Grid>
                            )}

                        </>
                    )}

                    {/* Notes */}
                    {data?.notes && (
                        <Grid container mb={1} mt={1}>
                            {data?.notes?.map((note, index) => (
                                <Grid item xs={12} position={"relative"} textAlign={"center"}>
                                    <Typography variant="body2" textAlign="center" fontWeight={400} fontSize={12} component="label" style={{ opacity: 0.6 }}>
                                        {note}
                                    </Typography>
                                </Grid>
                            ))}
                        </Grid>
                    )}
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
                            }} onClick={onClose}>
                            {t("button.go_back")}
                        </Button>
                    </Box>
                </DialogActions>
            </DialogContent>
        </Dialog>
    );
};

export default PopupErrorVNPTEpay;
