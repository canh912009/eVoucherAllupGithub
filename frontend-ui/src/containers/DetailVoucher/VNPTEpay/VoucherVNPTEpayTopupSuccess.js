import {
    Box,
    Container,
    Grid,
    Typography
} from "@mui/material";
import { useState } from "react";
import { useTranslation } from "react-i18next";
import GlobalBackdrop from "../../../components/GlobalBackdrop";
import CircleComponents from "../../../components/ui/CircleComponents";
import DashedLine from "../../../components/ui/DashedLine";
import SUCCESS_IMAGE from "../../../images/popupSuccess.png";
import { ConvertNumber } from "../../../utils/utils";

/* After buy TOPUP success */
const VoucherVNPTEpayTopupSuccess = ({ dataVoucher }) => {
    // console.log("VoucherVNPTEpayTopupSuccess dataVoucher", dataVoucher);

    const dots = [
        { top: '90px' },
        { position: 'right', top: '90px' },
    ];

    const { t, i18n } = useTranslation();
    const [loading, setLoading] = useState(false);

    return (
        <Container disableGutters
            sx={{
                marginTop: '20px',
                position: 'relative',
                display: 'flex',
                flexDirection: 'column',
                justifyContent: 'center',
                alignItems: 'center',
                alignContent: 'center',
                boxSizing: 'border-box',
                borderRadius: '20px',
                // border: '1px solid red',
                // width: '100%',
                backgroundColor: 'white',
                boxShadow: '0px 4px 4px rgba(0, 0, 0, 0.25)',
            }}
        >

            <GlobalBackdrop isLoading={loading} />

            <CircleComponents dots={dots} />

            <Container disableGutters sx={{ border: '1px solid transparent' }}>

                <Box paddingX={{ xs: 2, md: 4 }} paddingY={3} disableGutters sx={{ border: '1px solid transparent' }}>

                    {/* Logo */}
                    <Box display="flex" justifyContent={"center"} width="100%" mb={"10px"}>
                        <Box
                            component="img"
                            src={SUCCESS_IMAGE}
                            alt="Logo"
                            sx={{
                                display: 'flex',
                                justifyContent: 'center',
                                width: '50px',
                                height: 'auto',
                                borderRadius: '8px',
                            }}
                        />
                    </Box>

                    <DashedLine height="2px" dashLength="10" gapLength="10" />

                    {/* Header */}
                    <Box display="flex" flexDirection="row" justifyContent={"center"} width="100%" mt={3} mb={3}>
                        <Typography variant="h4" fontWeight={600} fontSize={'1rem'} component="label">
                            {t('topup.topup_success')}
                        </Typography>
                    </Box>

                    {/* Voucher Info */}
                    <Grid container spacing={1} mb={2} pl={4} columnSpacing={{ xs: 1, sm: 2, md: 3 }}>
                        <Grid item xs={6}>
                            <Typography variant="body2" fontSize={12}>
                                {t('topup.receiver_phone_number')}
                            </Typography>
                        </Grid>
                        <Grid item xs={6}>
                            <Typography variant="body2" fontSize={12} fontWeight={500}>
                                {dataVoucher?.goods?.vnpt?.topupResult?.targetPhoneNumber}
                            </Typography>
                        </Grid>
                        <Grid item xs={6}>
                            <Typography variant="body2" fontSize={12}>
                                {t('topup.card_value')}
                            </Typography>
                        </Grid>
                        <Grid item xs={6}>
                            <Typography variant="body2" fontSize={12} fontWeight={500}>
                                {ConvertNumber(dataVoucher?.goods?.vnpt?.topupResult?.faceValue)}{' VND'}
                            </Typography>
                        </Grid>
                        <Grid item xs={6}>
                            <Typography variant="body2" fontSize={12}>
                                {t('topup.time')}
                            </Typography>
                        </Grid>
                        <Grid item xs={6}>
                            <Typography variant="body2" fontSize={12} fontWeight={500}>
                                {dataVoucher?.goods?.vnpt?.topupResult?.requestTime}
                            </Typography>
                        </Grid>
                    </Grid>

                </Box>
            </Container>
        </Container>
    )
}

export default VoucherVNPTEpayTopupSuccess;