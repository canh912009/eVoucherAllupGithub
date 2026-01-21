package asia.castis.web_hook.common;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum WataneErrorCode {
    COMMON_UNKNOWN("common.unknown", "System error"),
    INVALID_SIGNATURE("common.invalid_signature", "The digital signature is invalid"),
    INVALID_USERNAME_OR_CREDENTIAL("common.invalid_username_or_credential", "Username or credential is invalid");

    private final String code;
    private final String message;
}