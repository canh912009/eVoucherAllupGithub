import {wataneGoodClient} from './client/crud-client';

export const getGoodList = async () => {
  // console.log('getGoodList', brandCode);
  const response = await wataneGoodClient.getGoodList();
  return response?.data;
};

export const getGoodDetails = async (code: string) => {
  // console.log('getGoodList', brandCode);
  const response = await wataneGoodClient.getGoodDetails(code);
  return response?.data;
};
