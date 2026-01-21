import {
  Delivery,
} from "@/types";
import { API_ENDPOINTS } from "./api-endpoints";
import { crudFactory } from "./curd-factory-ev";

export const deliveryClient = {
  ...crudFactory<Delivery, any, Partial<Delivery>>(API_ENDPOINTS.DELIVERY),
};
