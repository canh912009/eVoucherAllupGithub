import {
    Box,
    Button,
    Grid,
    Typography
} from "@mui/material";
import { yellow } from '@mui/material/colors';
import { useTranslation } from "react-i18next";
import CircleComponents from "../../components/ui/CircleComponents";
import DashedLine from "../../components/ui/DashedLine";
import LOGO from "../../images/red-error.png";
import { AQUA_EMAIL, AQUA_HOTLINE, renderMessage } from "../../utils/utils";
import { PanelActiveProductStyle } from "./styles";

const PanelActiveError = ({ errorData }) => {
    // console.log(`PanelActiveError: ${JSON.stringify(errorData)}`);

    const dots = [
        { top: '250px' },
        { position: 'right', top: '250px' },
    ];

    const { t } = useTranslation();

    const notes = [
        t('topup.error_note_support'),
        `${t('voucher.hotline')}: ${AQUA_HOTLINE}`,
        `${t('voucher.email')}: ${AQUA_EMAIL}`
    ]
    return (
        <PanelActiveProductStyle>
            <div className="container">
                <CircleComponents dots={dots} />

                <Box display="flex" position={'relative'} sx={{ backgroundColor: 'white', borderRadius: '20px' }} boxSizing={'border-box'} flexDirection="column" alignItems="flex-start" width="100%" paddingX={{ xs: 4, md: 6 }} paddingY={2}>
                    {/* Logo */}
                    <Box display="flex" textAlign={"center"} justifyContent={"center"} margin={"auto"} mt={"10px"} mb={3}>
                        <Box
                            component="img"
                            src={LOGO}
                            alt="Logo"
                            sx={{
                                display: 'flex',
                                justifyContent: 'center',
                                width: '80px',
                                height: 'auto',
                                borderRadius: '8px',
                            }}
                        />
                    </Box>

                    {/* Row Dashed Line */}
                    <Box display="flex" flexDirection="column" width="100%" mb={2}>
                        <DashedLine height="3px" dashLength="20" gapLength="10" color="#D7D5D5" />
                    </Box>

                    {/* Row Error Code */}
                    <Box display="flex" flexDirection="row" justifyContent={"center"} width="100%" mt={1} mb={2}>
                        <Typography variant="h5" textAlign={"center"} fontWeight={600} component="label">
                            Error {errorData?.errorCode}
                        </Typography>
                    </Box>

                    {/* Row Error Message */}
                    <Box display="flex" flexDirection="row" justifyContent={"center"} width="100%" mb={2}>
                        <Typography variant="body2" textAlign={"center"} fontWeight={400} component="label" style={{ opacity: 0.8 }}>
                            {t(renderMessage(errorData?.errorCode, t))}
                        </Typography>
                    </Box>

                    {/* Notes */}
                    {notes && (
                        <Grid container mb={1} mt={1}>
                            {notes?.map((note, index) => (
                                <Grid key={`note-${index}`} item xs={12} position={"relative"} textAlign={"center"}>
                                    <Typography variant="body2" textAlign="center" fontWeight={400} fontSize={12} component="label" style={{ opacity: 0.6 }}>
                                        {note}
                                    </Typography>
                                </Grid>
                            ))}
                        </Grid>
                    )}

                    {/* Row Dashed Line */}
                    <Box display="flex" flexDirection="column" width="100%" mt={2} mb={2}>
                        <DashedLine height="3px" dashLength="20" gapLength="10" color="#D7D5D5" />
                    </Box>

                    {/* Row Button Back */}
                    <Box display="flex" flexDirection="column" width="100%">
                        <Button
                            variant="contained"
                            sx={{
                                backgroundColor: yellow[600],
                                color: 'black',
                                textTransform: 'none',
                                borderRadius: '8px',
                                '&:hover': {
                                    backgroundColor: yellow[700]
                                },
                            }}
                            onClick={() => errorData?.onClose()}
                        >
                            {t("button.back")}
                        </Button>
                    </Box>
                </Box>
            </div>
        </PanelActiveProductStyle>
    );
}

export default PanelActiveError;
