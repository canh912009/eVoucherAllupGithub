import {
  AuthResponse,
  LoginInput,
  RegisterInput,
  User,
  ForgetPasswordInput,
  VerifyForgetPasswordTokenInput,
  ResetPasswordInput,
  BaseResponse, DataAuthRes
} from '@/types';
import { API_ENDPOINTS } from './api-endpoints'
// import { HttpClient } from './http-client';
import { HttpClient as HttpClientEV } from './http-client-ev';

export const userClient = {
  me: () => {
    return HttpClientEV.get<BaseResponse<User>>(API_ENDPOINTS.ME);
  },
  login: (variables: LoginInput) => {
    return HttpClientEV.post<BaseResponse<DataAuthRes>>(API_ENDPOINTS.LOGIN_EV, variables);
  },
  logout: () => {
    return HttpClientEV.post<any>(API_ENDPOINTS.LOGOUT, {});
  },
  register: (variables: RegisterInput) => {
    // return HttpClient.post<AuthResponse>(API_ENDPOINTS.REGISTER, variables);
    return;
  },
  forgetPassword: (variables: ForgetPasswordInput) => {
    // return HttpClient.post<any>(API_ENDPOINTS.FORGET_PASSWORD, variables);
    return;
  },
  verifyForgetPasswordToken: (variables: VerifyForgetPasswordTokenInput) => {
    // return HttpClient.post<any>(
    //   API_ENDPOINTS.VERIFY_FORGET_PASSWORD_TOKEN,
    //   variables
    // );
    return;
  },
  resetPassword: (variables: ResetPasswordInput) => {
    // return HttpClient.post<any>(API_ENDPOINTS.RESET_PASSWORD, variables);
    return;
  },
};
