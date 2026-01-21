import { BrowserRouter, Routes, Route, useLocation } from "react-router-dom";
import React, { useEffect } from "react";
import VoucherList from "./containers/VoucherList";
import ListInstruction from "./containers/ListInstruction";
import DetailVoucher from "./containers/DetailVoucher";
import Instruction from "./containers/Instruction";
import ShareVoucher from "./containers/ShareVoucher";
import StoreVoucher from "./containers/StoreVoucher";
import ResponseUseVoucher from "./containers/ReponseUseVoucher";
import ErrorPage from "./containers/ErrorPage";
import ResponseTransfer from "./containers/ReponseTransfer";
import ResponseConfirmTransfer from "./containers/ResponseConfirmTransfer";
import ErrorConfirmTransfer from "./containers/ErrorConfirmTransfer";
import StoreVoucherCancel from "./containers/StoreVoucherCancel";
import { useTranslation } from "react-i18next";
import IMAGE_VIETNAM from "./images/vietnam.png";
import IMAGE_ENGLISH from "./images/english.png";
import "./App.css";
import ResponseCancelUseVoucher from "./containers/ResponseCancelUseVoucher";
import { Paper } from "@mui/material";
import Cookies from "universal-cookie";
import ActiveVoucher from "./containers/ActiveVoucher";
import Footer from "./components/layout/Footer";

const CustomElement = ({ element }) => {
  const cookies = new Cookies();
  const { t, i18n } = useTranslation();
  if (!cookies.get("locales")) {
    cookies.set("locales", "vi");
  }
  return (
    <div>
      {element}
      <Paper
        sx={{
          width: "100vw",
          position: "fixed",
          bottom: 0,
          left: 0,
          right: 0,
          zIndex: "1000",
          height: "48px",
          borderTop: "1px solid #ccc",
        }}
        elevation={3}
      >
        <div
          style={{
            display: "flex",
            justifyContent: "center",
            alignItems: "center",
            alignContent: "center",
            backgroundColor: "white",
            marginTop: "12px",
          }}
        >
          <div> {t("voucher.chooseLanguage")}</div>
          <div
            onClick={() => {
              i18n.changeLanguage("vi");
              cookies.set("locales", "vi");
            }}
            style={{
              height: "24px",
              margin: "0 12px",
              width: "36px",
              position: "relative",
              borderRadius: "4px",
            }}
          >
            <img
              style={{ height: "24px", width: "36px", borderRadius: "4px" }}
              src={IMAGE_VIETNAM}
              alt=""
            />
            {i18n.language !== "vi" && (
              <div
                style={{
                  position: "absolute",
                  height: "24px",
                  width: "36px",
                  backgroundColor: "rgba(59, 59, 59, 0.38)",
                  top: "0",
                  left: "0",
                  borderRadius: "4px",
                }}
              ></div>
            )}
          </div>
          <div
            onClick={() => {
              i18n.changeLanguage("en");
              cookies.set("locales", "en");
            }}
            style={{
              height: "24px",
              width: "36px",
              position: "relative",
              borderRadius: "4px",
            }}
          >
            <img
              style={{ height: "24px", width: "36px", borderRadius: "4px" }}
              src={IMAGE_ENGLISH}
              alt=""
            />
            {i18n.language === "vi" && (
              <div
                style={{
                  position: "absolute",
                  height: "24px",
                  width: "36px",
                  backgroundColor: "rgba(59, 59, 59, 0.38)",
                  top: "0",
                  left: "0",
                  borderRadius: "4px",
                }}
              ></div>
            )}
          </div>
        </div>
      </Paper>
    </div>
  );
};

const ScrollToTop = () => {
  const { pathname } = useLocation();

  useEffect(() => {
    window.scrollTo(0, 0);
  }, [pathname]);

  return null;
};

function App() {
  return (
    <BrowserRouter>
      <ScrollToTop />
      <Routes>
        <Route path="/activate" element={<ActiveVoucher />} />
        <Route path="/voucher" element={<DetailVoucher />} />
        <Route
          path="/voucher-list"
          element={<CustomElement element={<VoucherList />} />}
        />
        <Route
          path="/list-instruction"
          element={<CustomElement element={<ListInstruction />} />}
        />
        <Route
          path="/instruction/:key"
          element={<CustomElement element={<Instruction />} />}
        />
        <Route
          path="/share-voucher/:otp"
          element={<CustomElement element={<ShareVoucher />} />}
        />
        <Route
          path="/store-pos/:otp"
          element={<CustomElement element={<StoreVoucher />} />}
        />
        <Route
          path="/store-pos-cancel/:otp"
          element={<CustomElement element={<StoreVoucherCancel />} />}
        />
        <Route
          path="/response-use-voucher"
          element={<CustomElement element={<ResponseUseVoucher />} />}
        />
        <Route
          path="/response-transfer"
          element={<CustomElement element={<ResponseTransfer />} />}
        />
        <Route
          path="/response-confirm-transfer"
          element={<CustomElement element={<ResponseConfirmTransfer />} />}
        />
        <Route
          path="/response-cancel-use-voucher"
          element={<CustomElement element={<ResponseCancelUseVoucher />} />}
        />
        <Route
          path="/error-confirm-transfer"
          element={<CustomElement element={<ErrorConfirmTransfer />} />}
        />
        <Route
          path="/"
          element={<CustomElement element={<ListInstruction />} />}
        />
        <Route path="*" element={<CustomElement element={<ErrorPage />} />} />
      </Routes>
      <Footer />
    </BrowserRouter>
  );
}

export default App;
