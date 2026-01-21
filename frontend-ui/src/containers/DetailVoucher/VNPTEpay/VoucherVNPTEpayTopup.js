import {
    Box,
    Button,
    Container,
    Typography
} from "@mui/material";
import { yellow } from '@mui/material/colors';
import { useState } from "react";
import Barcode from "react-barcode";
import { useTranslation } from "react-i18next";
import GlobalBackdrop from "../../../components/GlobalBackdrop";
import PopupError from "../../../components/PopupCustom/error";
import CircleComponents from "../../../components/ui/CircleComponents";
import DashedLine from "../../../components/ui/DashedLine";
import IconCopy from "../../../components/ui/IconCopy";
import { getImageSrc } from "../../../utils/utils";
import { topupSyntaxExpress } from "./VNPTEpayUtils";

/* After buy CARDCODE success */
const VoucherVNPTEpayTopup = ({ dataVoucher }) => {
    // console.log("VoucherVNPTEpayTopup dataVoucher", dataVoucher);

    const dots = [
        { top: '150px' },
        { position: 'right', top: '150px' },
    ];

    const topupSyntax = topupSyntaxExpress(dataVoucher);

    const { t, i18n } = useTranslation();
    const [loading, setLoading] = useState(false);
    const [errorData, setErrorData] = useState(null);

    const handleCopyClick = (text) => {
        // Create a temporary textarea element
        const textarea = document.createElement('textarea');
        textarea.value = text;
        textarea.setAttribute('readonly', '');
        textarea.style.position = 'absolute';
        textarea.style.left = '-9999px'; // Move off-screen
        document.body.appendChild(textarea);

        // Copy text to clipboard using execCommand
        textarea.select();
        document.execCommand('copy');

        // Cleanup
        document.body.removeChild(textarea);
    };

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
            {errorData &&
                <PopupError title={errorData.title} message={errorData.message} onAction={errorData.onAction} />}

            <GlobalBackdrop isLoading={loading} />

            <CircleComponents dots={dots} />

            <Container disableGutters sx={{ border: '1px solid transparent' }}>

                <Box paddingX={{ xs: 2, md: 4 }} paddingY={4} disableGutters sx={{ border: '1px solid transparent' }}>

                    {/* Logo */}
                    <Box display="flex" justifyContent={"center"} width="100%" mb={"10px"}>
                        <Box
                            component="img"
                            src={getImageSrc(dataVoucher?.goods?.brand?.imgUrl)}
                            alt="Logo"
                            sx={{
                                display: 'flex',
                                justifyContent: 'center',
                                width: '50%',
                                height: 'auto',
                                borderRadius: '8px',
                            }}
                        />
                    </Box>

                    <Box display="flex" justifyContent={"center"} alignItems={"end"} border={"1px solid transparent"} marginLeft={"30px"}>
                        <Typography variant="body1" width={"250px"} display={"flex"} position={"relative"} marginBottom={"-15px"} sx={{ color: 'grey.700' }}>
                            <Barcode value={dataVoucher?.goods?.vnpt?.cardCodeResult?.code} />
                        </Typography>
                        <Typography variant="body1" display={"flex"} sx={{ color: 'grey.700' }} onClick={() => handleCopyClick(dataVoucher?.goods?.vnpt?.cardCodeResult?.code)}>
                            <IconCopy />
                        </Typography>
                    </Box>

                    <Box display="flex" justifyContent={"center"} alignItems={"end"} border={"1px solid transparent"} marginLeft={"30px"}>
                        <Typography variant="body1" width={"250px"} display={"flex"} position={"relative"} justifyContent={"center"} sx={{ color: 'grey.700', paddingTop: '10px' }}>
                            <Typography
                                variant="body1"
                                gutterBottom
                                sx={{ marginRight: 1 }}
                            >
                                {t("topup.series")}
                            </Typography>
                            <Typography
                                variant="body1"
                                gutterBottom
                            >
                                {dataVoucher?.goods?.vnpt?.cardCodeResult?.serialNo}
                            </Typography>
                        </Typography>
                        <Typography variant="body1" disableGutters onClick={() => handleCopyClick(dataVoucher?.goods?.vnpt?.cardCodeResult?.serialNo)} sx={{ marginBottom: '8px' }}>
                            <IconCopy />
                        </Typography>
                    </Box>

                    <DashedLine height="2px" dashLength="10" gapLength="10" />

                    {/* Row Button Topup */}
                    {topupSyntax && (
                        <Box display="flex" paddingX={2} mb={2} mt={2}>
                            <Button
                                href={topupSyntax}
                                data-rel="external"
                                variant="contained"
                                sx={{
                                    width: '100%',
                                    backgroundColor: yellow[600],
                                    color: 'black',
                                    textTransform: 'none',
                                    borderRadius: '8px',
                                    '&:hover': {
                                        backgroundColor: yellow[700]
                                    },
                                }}
                            >
                                {t("button.topup_now")}
                            </Button>
                        </Box>
                    )}

                </Box>
            </Container>
        </Container>
    )
}

export default VoucherVNPTEpayTopup;